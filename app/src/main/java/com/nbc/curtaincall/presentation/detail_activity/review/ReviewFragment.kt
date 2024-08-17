package com.nbc.curtaincall.presentation.detail_activity.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.nbc.curtaincall.databinding.FragmentERBinding

class ReviewFragment : Fragment() {
    private var _binding: FragmentERBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentERBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnExpectations.setOnClickListener {
                layoutExpectation.isVisible = true
                layoutReview.isVisible = false
            }
            btnReviews.setOnClickListener {
                layoutExpectation.isVisible = false
                layoutReview.isVisible = true
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}