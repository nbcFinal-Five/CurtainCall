package com.nbc.curtaincall.ui.home

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentHomeBinding
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient.fetch
import com.nbc.curtaincall.fetch.repository.impl.FetchRepositoryImpl
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
    private var _binding: com.nbc.curtaincall.databinding.FragmentHomeBinding? = null
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
    private val upComingShowAdapter: UpcomingShowAdapter by lazy { UpcomingShowAdapter() }
    private val topRankAdapter: TopRankAdapter by lazy { TopRankAdapter(this) }
    private val genreAdapter: GenreAdapter by lazy { GenreAdapter(this) }
    private val kidShowAdapter: KidShowAdapter by lazy { KidShowAdapter(this) }
    private var isPaging = false
    private var pagingJob: Job? = null

    private val onPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.tvPageIndicator.text = "${position + 1} / 10"
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
        with(viewModel) {
            showList.observe(viewLifecycleOwner) {
                upComingShowAdapter.submitList(it)
                with(binding) {
                    //viewpager 연결
                    viewPager.adapter = upComingShowAdapter
                    viewPager.offscreenPageLimit = 1
                    viewPager.setPageTransformer(SliderTransformer(requireContext()))

                    val itemDecoration = HorizontalMarginItemDecoration(
                        requireContext(),
                        R.dimen.viewpager_current_item_horizontal_margin
                    )
                    viewPager.addItemDecoration(itemDecoration)
                    viewPager.registerOnPageChangeCallback(onPageChangeCallback)

                    //tab 연결
                    TabLayoutMediator(tabPosterIndicator, viewPager) { tab, position ->
                        viewPager.currentItem = tab.position
                    }.attach()

                }
                if (!isPaging) startPaging()
            }
            //장르 연극 초기화
            fetchGenre(0)
        }
    }

    //옵저브 세팅
    private fun setUpObserve() {
        with(viewModel) {
            topRank.observe(viewLifecycleOwner) {
                topRankAdapter.submitList(it.take(10))
            }
            genre.observe(viewLifecycleOwner) {
                genreAdapter.submitList(it)
            }
            kidShow.observe(viewLifecycleOwner) {
                kidShowAdapter.submitList(it)
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
                if (viewPager.currentItem == 9) {
                    lifecycleScope.launch {
                        delay(3000)
                    }
                    viewPager.currentItem = 0
                } else {
                    viewPager.currentItem++
                }
            }
        }.onFailure {
            Toast.makeText(context, "Exception nextPage()", Toast.LENGTH_SHORT).show()
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

class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) :
    RecyclerView.ItemDecoration() {
    private val horizontalMarginInPx: Int =
        context.resources.getDimension(horizontalMarginInDp).toInt()

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.right = horizontalMarginInPx
        outRect.left = horizontalMarginInPx
    }
}



