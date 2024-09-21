package com.nbc.curtaincall.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentHomeBinding
import com.nbc.curtaincall.presentation.home.HorizontalMarginItemDecoration
import com.nbc.curtaincall.ui.home.adapter.GenreAdapter
import com.nbc.curtaincall.ui.home.adapter.KidShowAdapter
import com.nbc.curtaincall.ui.home.adapter.TopRankAdapter
import com.nbc.curtaincall.ui.home.adapter.UpcomingShowAdapter
import com.nbc.curtaincall.presentation.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val sharedViewModel: TicketViewModel by activityViewModels<TicketViewModel>()
    private val upComingShowAdapter: UpcomingShowAdapter by lazy {
        UpcomingShowAdapter {
            sharedViewModel.posterClick(it,childFragmentManager)
        }
    }
    private val topRankAdapter: TopRankAdapter by lazy {
        TopRankAdapter {
            sharedViewModel.posterClick(it, childFragmentManager)
        }
    }
    private val genreAdapter: GenreAdapter by lazy {
        GenreAdapter {
            sharedViewModel.posterClick(
                it,
                childFragmentManager
            )
        }
    }
    private val kidShowAdapter: KidShowAdapter by lazy {
        KidShowAdapter {
            sharedViewModel.posterClick(
                it,
                childFragmentManager
            )
        }
    }
    private var isPaging = false
    private var pagingJob: Job? = null
    private var isPositionInit = false
    private val onPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        //페이지가 선택될 때 마다 호출
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.tvPageIndicator.text =
                    //"${(position % topRankAdapter.currentList.size) + 1} / ${topRankAdapter.currentList.size}"

                "${(position % topRankAdapter.currentList.size) + 1} / ${topRankAdapter.currentList.size}"
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
            //장르 스피너 선택
            binding.spinnerHomeGenre.setOnSpinnerItemSelectedListener<String> { _, _, newIndex, _ ->
                fetchGenre(newIndex)
            }
        }
    }

    //화면 초기 설정
    private fun initViews() {
        initRecyclerView()

        //viewpager 연결
        with(binding.viewPager) {
            binding.viewPager.adapter = topRankAdapter
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
    }

    //옵저브 세팅
    private fun setUpObserve() {
        with(viewModel) {

            topRank.observe(viewLifecycleOwner) {
                topRankAdapter.submitList(it?.take(10))
                //포지션 초기화
                if (!isPositionInit) positionInit()
                //페이징 초기화
                if (!isPaging) startPaging()
            }
            genre.observe(viewLifecycleOwner) {
                genreAdapter.submitList(it)
            }
            showList.observe(viewLifecycleOwner) {
                upComingShowAdapter.submitList(it)
            }
            kidShow.observe(viewLifecycleOwner) {
                kidShowAdapter.submitList(it)
            }
            with(binding) {
                //로딩 화면 처리
                isLoadingGenre.observe(viewLifecycleOwner) { state ->
                    skeletonGenreLoading.isVisible = state
                }
                isLoadingUpcomingShow.observe(viewLifecycleOwner) { state ->
                    skeletonUpcomingShowLoading.isVisible = state
                }
                isLoadingKid.observe(viewLifecycleOwner) { state ->
                    skeletonKidLoading.isVisible = state
                }

                isServerErrorViewPager.observe(viewLifecycleOwner) { state ->
                    ivViewpagerError.isVisible = state
                    tvViewpagerError.isVisible = state
                }
                isServerErrorGenre.observe(viewLifecycleOwner) { state ->
                    ivGenreError.isVisible = state
                    tvGenreError.isVisible = state
                    rvHomeGenre.isVisible = !state
                }
                isServerErrorUpcomingShow.observe(viewLifecycleOwner) { state ->
                    ivUpcomingShowError.isVisible = state
                    tvUpcomingShowError.isVisible = state
                    rvHomeUpcomingShow.isVisible = !state
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
            //장르별 공연
            rvHomeGenre.adapter = genreAdapter
            //공연 예정
            rvHomeUpcomingShow.adapter = upComingShowAdapter
            //어린이 공연
            rvHomeKidShow.adapter = kidShowAdapter
        }
    }

    //3초 후 자동 페이징
    private fun nextPage() {
        runCatching {
            with(binding) {
                if (viewPager.currentItem == topRankAdapter.itemCount - 1) {
                    lifecycleScope.launch {
                        delay(3000)
                    }
                    viewPager.currentItem =
                        (topRankAdapter.itemCount / 2) - (topRankAdapter.currentList.size / 2)
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
            if (topRankAdapter.itemCount % 2 == 0) (topRankAdapter.itemCount / 2)
            else (topRankAdapter.itemCount / 2) - (topRankAdapter.currentList.size / 2)
        topRankAdapter.itemCount / 2
        isPositionInit = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.spinnerHomeGenre.dismiss()
        _binding = null
        isPaging = false
        pagingJob?.cancel()
    }

    //포스터 클릭 시 티켓
//    fun posterClick(id: String?) {
//        val ticketDialog = TicketDialogFragment()
//        id?.let {
//            sharedViewModel.sharedShowId(id) //해당 공연의 id를 MainViewModel로 보내줌
//            ticketDialog.setStyle(
//                DialogFragment.STYLE_NORMAL,
//                R.style.RoundCornerBottomSheetDialogTheme
//            )
//            ticketDialog.show(childFragmentManager, ticketDialog.tag)
//        }
//    }
}





