package com.nbc.curtaincall.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.nbc.curtaincall.databinding.FragmentHomeBinding
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient.fetch
import com.nbc.curtaincall.fetch.repository.impl.FetchRepositoryImpl
import com.nbc.curtaincall.ui.home.adapter.GenreAdapter
import com.nbc.curtaincall.ui.home.adapter.TopRankAdapter
import com.nbc.curtaincall.ui.home.adapter.UpcomingShowAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            fetchRemoteRepository = FetchRepositoryImpl(fetch),
        )
    }
    private lateinit var beforeShowAdapter: UpcomingShowAdapter
    private lateinit var topRankAdapter: TopRankAdapter
    private val genreAdapter: GenreAdapter by lazy { GenreAdapter() }
    private var isPaging = false
    private var pagingJob: Job? = null

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
            fetchUpcomingList()
            //TOP 10 공연
            fetchTopRank()
            //장르 스피너 선택
            binding.spinnerGenre.setOnSpinnerItemSelectedListener<String> { _, _, newIndex, _ ->
                fetchGenre(newIndex)
            }
        }
    }

    //화면 초기 설정
    private fun initViews() {
        //어뎁터 초기화
        beforeShowAdapter = UpcomingShowAdapter(listOf())
        topRankAdapter = TopRankAdapter()
        initRecyclerView()
        with(viewModel) {
            showList.observe(viewLifecycleOwner) {
                beforeShowAdapter = UpcomingShowAdapter(it)
                with(binding) {
                    viewPager.adapter = beforeShowAdapter
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
                topRankAdapter.items = it.take(10)
                topRankAdapter.notifyDataSetChanged()
            }
            genre.observe(viewLifecycleOwner) {
                genreAdapter.submitList(it)
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
            //예외 처리 알림용 나중에 처리
            Toast.makeText(context, "앱 크래시", Toast.LENGTH_SHORT).show()
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
}
