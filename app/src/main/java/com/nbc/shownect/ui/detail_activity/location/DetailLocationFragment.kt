package com.nbc.shownect.ui.detail_activity.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.naver.maps.map.NaverMapSdk
import com.nbc.shownect.R
import com.nbc.shownect.databinding.FragmentDetailLocationBinding
import com.nbc.shownect.databinding.FragmentSearchBinding
import com.nbc.shownect.util.Constants


class DetailLocationFragment : Fragment() {

    private var _binding: FragmentDetailLocationBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //클라이언트 ID 지정
        NaverMapSdk.getInstance(requireContext()).client = NaverMapSdk.NaverCloudPlatformGovClient(Constants.MAP_CLIENT_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailLocationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}