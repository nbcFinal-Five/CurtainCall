package com.nbc.curtaincall.ui.detail_activity.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
import com.nbc.curtaincall.databinding.FramgentDetailIntroImageBinding
import com.nbc.curtaincall.ui.detail_activity.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroImageFragment : Fragment() {
    private var _binding: FramgentDetailIntroImageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by activityViewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FramgentDetailIntroImageBinding.inflate(inflater, container, false)
        viewModel.detailInfoList.observe(viewLifecycleOwner) {
            it.first().styUrl?.styUrlList?.forEach {
                    url ->
                binding.linearLayout.addView(createPhotoView(url))
            }
        }
        return binding.root
    }

    //소개 이미지가 몇개가 들어오는지 알 수 없어서 동적으로 PhotoView를 추가해줌
    private fun createPhotoView(url: String): PhotoView {
        return PhotoView(requireContext()).apply {
            Glide.with(this).load(url).override(Target.SIZE_ORIGINAL).into(this)
            adjustViewBounds = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}