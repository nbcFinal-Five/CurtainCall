package com.nbc.shownect.ui.detail_activity.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.nbc.shownect.databinding.FramgentDetailIntroImageBinding
import com.nbc.shownect.ui.detail_activity.DetailViewModel


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
            val firstShowDetail = it!!.first()
//            binding.ivDetailIntroPoster.load(firstShowDetail.styurls?.styurlList?.get(0)) {
//                size(resolver = SizeResolver(Size.ORIGINAL))
//            }
            Glide.with(requireContext()).load(firstShowDetail.styurls?.styurlList?.get(0)).override(Target.SIZE_ORIGINAL).into(binding.ivDetailIntroPoster)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}