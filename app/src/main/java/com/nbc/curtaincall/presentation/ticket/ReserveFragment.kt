package com.nbc.curtaincall.presentation.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbc.curtaincall.databinding.ReserveBottomsheetDialogBinding
import com.nbc.curtaincall.presentation.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReserveFragment : BottomSheetDialogFragment() {
    private var _binding: ReserveBottomsheetDialogBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel by activityViewModels<TicketViewModel>()
    private val adapter by lazy { ListAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ReserveBottomsheetDialogBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() = with(binding) { recyclerView.adapter = adapter }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submitList(sharedViewModel.reserveInfoList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}