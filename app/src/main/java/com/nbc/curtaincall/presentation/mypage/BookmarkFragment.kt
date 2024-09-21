package com.nbc.curtaincall.presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nbc.curtaincall.databinding.FragmentMyPageBookmarkBinding
import com.nbc.curtaincall.presentation.TicketViewModel

class BookmarkFragment : Fragment() {
    private var _binding: FragmentMyPageBookmarkBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel by activityViewModels<TicketViewModel>()
    private val adapter by lazy {
        BookmarkAdapter {
            sharedViewModel.posterClick(it, childFragmentManager)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBookmarkBinding.inflate(inflater, container, false)
        initView()
        setUpObserve()
        return binding.root
    }

    private fun initView() {
        with(binding) {
            recyclerView.adapter = adapter
        }
    }

    private fun setUpObserve() {
        with(sharedViewModel) {
            myPageInterestList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}