package com.nbc.curtaincall.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.nbc.curtaincall.data.api.RetrofitClient
import com.nbc.curtaincall.databinding.FragmentHomeBinding
import com.nbc.curtaincall.ui.home.adapter.UpcomingShowAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val kopisApiInstance = RetrofitClient.kopisApi
    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(kopisApiInstance) }
    private lateinit var beforeShowAdapter: UpcomingShowAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUpcomingList()
    }

    //화면 초기 설정
    private fun initViews() {
        viewModel.showList.observe(viewLifecycleOwner) {
            beforeShowAdapter = UpcomingShowAdapter(it)
            with(binding) {
                viewPager.adapter = beforeShowAdapter
                TabLayoutMediator(tabPosterIndicator, viewPager) { tab, position ->
                    viewPager.currentItem = tab.position
                }.attach()
            }
            lifecycleScope.launch {
                while (true) {
                    delay(3000)
                    nextPage()
                }
            }
        }
    }

    //3초 후 자동 페이징
    private fun nextPage() {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}