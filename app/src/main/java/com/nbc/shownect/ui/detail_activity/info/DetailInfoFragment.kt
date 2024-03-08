package com.nbc.shownect.ui.detail_activity.info

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.size.Size
import coil.size.SizeResolver
import com.nbc.shownect.R
import com.nbc.shownect.databinding.FragmentDetailDetailInfoBinding
import com.nbc.shownect.supabase.Supabase
import com.nbc.shownect.ui.UserViewModel
import com.nbc.shownect.ui.auth.AuthActivity
import com.nbc.shownect.ui.detail_activity.DetailViewModel
import io.github.jan.supabase.gotrue.auth

class DetailInfoFragment : Fragment() {
	private var _binding: FragmentDetailDetailInfoBinding? = null
	private val binding get() = _binding!!
	private val viewModel: DetailViewModel by activityViewModels<DetailViewModel>()
	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }

	private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		userViewModel.setUser()

		val user = userViewModel.userInfo.value

		val info = viewModel.detailInfoList.value?.first()

		if (info != null && user != null) {
			viewModel.setInfo(info.mt20id!!)
			viewModel.setIsLike(
				mt20id = info.mt20id!!,
				userId = user.id
			)
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentDetailDetailInfoBinding.inflate(inflater, container, false)
		setUpObserve()
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.fetchDetailInfo()
	}

	private fun setUpObserve() {
		viewModel.detailInfoList.observe(viewLifecycleOwner) {
			val firstShowDetail = it?.first()
			if (firstShowDetail != null) {
				with(binding) {
					ivDetailPoster.load(firstShowDetail.poster) {
						size(resolver = SizeResolver(Size.ORIGINAL))
					}
					tvDetailShowNameSub.text = firstShowDetail.prfnm
					tvDetailGenreSub.text = firstShowDetail.genrenm
					tvDetailAgeSub.text = firstShowDetail.prfage
					tvDetailRuntimeSub.text = firstShowDetail.prfruntime
					tvDetailPriceSub.text = firstShowDetail.pcseguidance
					tvDetailShowStateSub.text = firstShowDetail.prfstate
					tvDetailPlaceSub.text = firstShowDetail.fcltynm
					tvDetailPeriodSub.text =
						"${firstShowDetail.prfpdfrom} ~ ${firstShowDetail.prfpdto}"
					tvDetailTimeSub.text = firstShowDetail.dtguidance
					tvDetailCastSub.text =
						if (firstShowDetail.prfcast.isNullOrBlank()) "미상" else firstShowDetail.prfcast
					tvDetailProductSub.text =
						if (firstShowDetail.entrpsnm.isNullOrBlank()) "미상" else firstShowDetail.entrpsnm
				}
			}
		}
		viewModel.point.observe(viewLifecycleOwner) {
			binding.rbDetailBar.rating = it.toFloat()
		}

		viewModel.totalExpectationCount.observe(viewLifecycleOwner) {
			binding.tvDetailExpectationsNum.text = "기대평 ${it}개"
		}

		viewModel.isBookmark.observe(viewLifecycleOwner) {
			val info = viewModel.detailInfoList.value?.first()

			Log.d("debug", it.toString())

			with(binding) {
				if (it) {
					ivDetailWishlist.setBackgroundResource(R.drawable.ic_heart_full_24dp)
					ivDetailWishlist.setOnClickListener {
						val user = userViewModel.userInfo.value!!

						// TODO 삭제
						viewModel.deleteBookmark(
							mt20id = info?.mt20id!!,
							userId = user.id,
						)
					}
				} else {
					ivDetailWishlist.setBackgroundResource(R.drawable.ic_heart_empty_24dp)
					ivDetailWishlist.setOnClickListener {
						val user = userViewModel.userInfo.value

						if (user == null) {
							val intent = Intent(requireActivity(), AuthActivity::class.java)
							launcher.launch(intent)
							return@setOnClickListener
						} else {
							viewModel.createBookmark(
								mt20id = info?.mt20id!!,
								poster = info?.poster!!,
								userId = user.id
							)
						}
					}
				}
			}
		}

		viewModel.detailInfoList.observe(viewLifecycleOwner) {
			val firstShowDetail = it!!.first()

			val id = firstShowDetail.mt20id
			if (id != null) {
				viewModel.setInfo(id)

				val user = userViewModel.userInfo.value

				if (user != null) {
					viewModel.setIsLike(id, user.id)
				}
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}