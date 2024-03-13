package com.nbc.shownect.ui.detail_activity.er

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.nbc.shownect.R
import com.nbc.shownect.databinding.FragmentERBinding
import com.nbc.shownect.fetch.model.DbResponse
import com.nbc.shownect.ui.detail_activity.DetailViewModel
import com.nbc.shownect.ui.detail_activity.er.expectation.ExpectationFragment
import com.nbc.shownect.ui.detail_activity.er.review.ReviewFragment

class ERFragment : Fragment() {
	private val detailViewModel: DetailViewModel by activityViewModels<DetailViewModel>()
	private val erViewModel by lazy { ViewModelProvider(requireActivity())[ERViewModel::class.java] }

	private lateinit var binding: FragmentERBinding

	private var info: DbResponse? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentERBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initHandler()
		initViewModel()
	}

	private fun initHandler() = with(binding) {
		btnExpectations.setOnClickListener { erViewModel.setMode(ERViewModel.EXPECTATION) }
		btnReviews.setOnClickListener { erViewModel.setMode(ERViewModel.REVIEW) }
	}

	private fun initViewModel() {
		detailViewModel.detailInfoList.observe(viewLifecycleOwner) {
			val firstShowDetail = it!!.first()

			info = firstShowDetail


			val mt20id = firstShowDetail.mt20id
			val mt10id = firstShowDetail.mt10id
			val poster = firstShowDetail.poster

			if (mt20id != null && mt10id != null && poster != null) {
				changeFragmentByMode(
					mt20id = mt20id,
					mt10id = mt10id,
					poster = poster,
					mode = ERViewModel.EXPECTATION
				)
			}
		}

		erViewModel.mode.observe(viewLifecycleOwner) {
			if (info != null) {
				changeFragmentByMode(
					mt20id = info!!.mt20id!!,
					mt10id = info!!.mt10id!!,
					poster = info!!.poster!!,
					mode = it
				)
			}
		}
	}

	private fun changeFragmentByMode(mode: String, mt10id: String, mt20id: String, poster: String) {
		with(binding) {
			when (mode) {
				ERViewModel.REVIEW -> {
					setFragment(
						ReviewFragment(
							mt20id = mt20id,
							mt10id = mt10id,
							poster = poster
						)
					)
					btnReviews.setBackgroundResource(R.color.primary_color)
					btnReviews.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

					btnExpectations.setBackgroundResource(R.color.component_background_color)
					btnExpectations.setTextColor(ContextCompat.getColor(requireActivity(), R.color.component_color))
				}

				ERViewModel.EXPECTATION -> {
					setFragment(
						ExpectationFragment(
							mt20id = mt20id,
							mt10id = mt10id,
							poster = poster
						)
					)
					btnReviews.setBackgroundResource(R.color.component_background_color)
					btnReviews.setTextColor(ContextCompat.getColor(requireActivity(), R.color.component_color))

					btnExpectations.setBackgroundResource(R.color.primary_color)
					btnExpectations.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
				}
			}
		}
	}

	private fun setFragment(frag: Fragment) {
		this.childFragmentManager.beginTransaction()
			.replace(binding.flComment.id, frag).commit()
	}
}