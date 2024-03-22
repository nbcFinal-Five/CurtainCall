package com.nbc.curtaincall.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentHomeBinding
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient.fetch
import com.nbc.curtaincall.fetch.repository.impl.FetchRepositoryImpl
import com.nbc.curtaincall.presentation.home.HorizontalMarginItemDecoration
import com.nbc.curtaincall.ui.home.adapter.GenreAdapter
import com.nbc.curtaincall.ui.home.adapter.KidShowAdapter
import com.nbc.curtaincall.ui.home.adapter.PosterClickListener
import com.nbc.curtaincall.ui.home.adapter.TopRankAdapter
import com.nbc.curtaincall.ui.home.adapter.UpcomingShowAdapter
import com.nbc.curtaincall.ui.main.MainViewModel
import com.nbc.curtaincall.ui.main.MainViewModelFactory
import com.nbc.curtaincall.ui.ticket.TicketDialogFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), PosterClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            fetchRemoteRepository = FetchRepositoryImpl(fetch),
        )
    }
    private val sharedViewModel: MainViewModel by activityViewModels<MainViewModel> {
        MainViewModelFactory(
            fetchRemoteRepository = FetchRepositoryImpl(
                fetch
            )
        )
    }
    private val upComingShowAdapter: UpcomingShowAdapter by lazy { UpcomingShowAdapter(this) }
    private val topRankAdapter: TopRankAdapter by lazy { TopRankAdapter(this) }
    private val genreAdapter: GenreAdapter by lazy { GenreAdapter(this) }
    private val kidShowAdapter: KidShowAdapter by lazy { KidShowAdapter(this) }
    private var isPaging = false
    private var pagingJob: Job? = null
    private var isPositionInit = false
    private val onPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        //페이지가 선택될 때 마다 호출
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.tvPageIndicator.text =
                "${(position % upComingShowAdapter.currentList.size) + 1} / ${upComingShowAdapter.currentList.size}"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initViews()
        setUpObserve()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            //공연 예정작
            fetchUpcoming()
            //TOP 10 공연
            fetchTopRank()
            //장르 스피너 선택
            binding.spinnerHomeGenre.setOnSpinnerItemSelectedListener<String> { _, _, newIndex, _ ->
                fetchGenre(newIndex)
            }
            //어린이 관람 가능 공연 목록
            fetchKidShow()
        }
    }

    //화면 초기 설정
    private fun initViews() {
        //어뎁터 초기화
        upComingShowAdapter
        topRankAdapter
        kidShowAdapter
        initRecyclerView()

        //viewpager 연결
        with(binding.viewPager) {
            adapter = upComingShowAdapter
            //ViewPager PageTransformer 세팅
            offscreenPageLimit = 1
            setPageTransformer(SliderTransformer(requireContext()))
            val itemDecoration = HorizontalMarginItemDecoration(
                requireContext(),
                R.dimen.viewpager_current_item_horizontal_margin
            )
            addItemDecoration(itemDecoration)

            //PageChangeCallback
            registerOnPageChangeCallback(onPageChangeCallback)

        }
        //장르 연극 초기화
        viewModel.fetchGenre(0)
    }

    //옵저브 세팅
    private fun setUpObserve() {
        with(viewModel) {
            showList.observe(viewLifecycleOwner) {
                upComingShowAdapter.submitList(it)
                //포지션 초기화 
                if (!isPositionInit) positionInit()
                //페이징 초기화
                if (!isPaging) startPaging()
            }
            topRank.observe(viewLifecycleOwner) {
                topRankAdapter.submitList(it?.take(10))
            }
            genre.observe(viewLifecycleOwner) {
                genreAdapter.submitList(it)
            }
            kidShow.observe(viewLifecycleOwner) {
                kidShowAdapter.submitList(it)
            }
            with(binding) {
                //로딩 화면 처리
                isLoadingGenre.observe(viewLifecycleOwner) { state ->
                    skeletonGenreLoading.isVisible = state
                }
                isLoadingRecommend.observe(viewLifecycleOwner) { state ->
                    skeletonTopRankLoading.isVisible = state
                }
                isLoadingKid.observe(viewLifecycleOwner) { state ->
                    skeletonKidLoading.isVisible = state
                }
                isServerErrorViewPager.observe(viewLifecycleOwner) { state ->
                    ivViewpagerError.isVisible = state
                    tvViewpagerError.isVisible = state
                }
                isServerErrorTopRank.observe(viewLifecycleOwner) { state ->
                    ivTopRankError.isVisible = state
                    tvTopRankError.isVisible = state
                    rvHomeTopRank.isVisible = !state
                }
                isServerErrorGenre.observe(viewLifecycleOwner) { state ->
                    ivGenreError.isVisible = state
                    tvGenreError.isVisible = state
                    rvHomeGenre.isVisible = !state
                }
                isServerErrorKid.observe(viewLifecycleOwner) { state ->
                    ivKidError.isVisible = state
                    tvKidError.isVisible = state
                    rvHomeKidShow.isVisible = !state
                }
            }
        }
    }

    //리사이클러뷰 초기화
    private fun initRecyclerView() {
        with(binding) {
            //HOT 추천 리사이클러뷰
            rvHomeTopRank.apply {
                adapter = topRankAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            //장르별 리사이클러뷰
            rvHomeGenre.apply {
                adapter = genreAdapter
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            //어린이 공연 리사이클러뷰
            rvHomeKidShow.apply {
                adapter = kidShowAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    //3초 후 자동 페이징
    private fun nextPage() {
        runCatching {
            with(binding) {
                if (viewPager.currentItem == upComingShowAdapter.itemCount - 1) {
                    lifecycleScope.launch {
                        delay(3000)
                    }
                    viewPager.currentItem =
                        (upComingShowAdapter.itemCount / 2) - (upComingShowAdapter.currentList.size / 2)
                } else {
                    viewPager.currentItem++
                }
            }
        }
    }

    //페이징 스타트 함수
    private fun startPaging() {
        isPaging = true
        pagingJob = lifecycleScope.launch {
            while (true) {
                delay(3000)
                nextPage()
            }
        }
    }

    //포지션을 중간위치에 맞추기 위한 코드 / 추가된 리스트의 크기가 짝수/홀수 일때 처리 (인디케이터)
    private fun positionInit() {
        binding.viewPager.currentItem =
            if (upComingShowAdapter.itemCount % 2 == 0) (upComingShowAdapter.itemCount / 2)
            else (upComingShowAdapter.itemCount / 2) - (upComingShowAdapter.currentList.size / 2)
        isPositionInit = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isPaging = false
        pagingJob?.cancel()
    }

    //포스터 클릭 시 티켓
    override fun posterClicked(id: String) {
        val ticketDialog = TicketDialogFragment()
        sharedViewModel.sharedShowId(id) //해당 공연의 id를 MainViewModel로 보내줌
        ticketDialog.setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.RoundCornerBottomSheetDialogTheme
        )
        ticketDialog.show(childFragmentManager, ticketDialog.tag)
    }
}





