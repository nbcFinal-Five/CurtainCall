package com.nbc.curtaincall.presentation.stats

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentStatsBinding
import com.nbc.curtaincall.supabase.Supabase
import com.nbc.curtaincall.supabase.model.ProfileModel
import com.nbc.curtaincall.ui.detail_activity.er.expectation.ExpectationViewModel
import com.nbc.curtaincall.ui.detail_activity.er.review.ReviewViewModel
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round

class StatsDialogFragment(private val userInfo: UserInfo) : DialogFragment() {
	private var _binding: FragmentStatsBinding? = null
	private val binding get() = _binding!!
	private val expectationViewModel by viewModels<ExpectationViewModel>()
	private val reviewViewModel by viewModels<ReviewViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentStatsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		getUserNickname(userInfo.id)
		averageMyReviewScore()
		countMyexpectation()
		setPieChart()
	}

	@SuppressLint("SetTextI18n")
	private fun getUserNickname(userId: String) {
		lifecycleScope.launch {
			withContext(Dispatchers.IO) {
				try {
					val response = Supabase.client
						.from("profiles")
						.select {
							filter {
								eq(column = "user_id", value = userId)
							}
						}.decodeSingle<ProfileModel>()

					val nickname = response.name
					withContext(Dispatchers.Main) {
						binding.tvStaticsTitle.text = nickname + "님의 스탯"
					}
				} catch (e: RestException) {

				}
			}
		}
	}

	private fun averageMyReviewScore() {
		lifecycleScope.launch {
			withContext(Dispatchers.IO) {
				try {
					val point = Supabase.client.postgrest.rpc(
						function = "get_review_average_point",
						parameters = buildJsonObject {
							put("user_id_arg", userInfo.id)
						}
					).data.toDouble()
					withContext(Dispatchers.Main) {
						val df = DecimalFormat("#.#")
						df.roundingMode = RoundingMode.DOWN
						val roundoff = df.format(point)
						binding.tvReviewScore.setText(roundoff)
					}
				} catch (e: RestException) {

				}
			}
		}
	}

	private fun countMyexpectation() {
		lifecycleScope.launch {
			withContext(Dispatchers.IO) {
				try {
					val count = Supabase.client
						.from("expectations")
						.select {
							filter {
								// supabase에 있는 user_id 와 현재 userInfo.id 를 비교해서 같은지 테이블 확인
								eq(column = "user_id", value = userInfo.id)
							}
							count(Count.EXACT)
						}.countOrNull()!!

					withContext(Dispatchers.Main) {
						binding.tvExpectationCount.setText(count.toString())
					}
				} catch (e: RestException) {

				}
			}
		}
	}

	private fun setPieChart() {
		val genres = listOf("연극", "뮤지컬", "무용", "대중무용", "클래식", "국악", "대중음악", "서커스/마술", "복합")

		val backgroundColor = ContextCompat.getColor(requireContext(), R.color.component_color)
		// PieChart를 binding에서 가져옵니다.
		val pieChart = binding.pcGenreReview

		// PieChart에 적용할 색상을 정의합니다.
		val colorsItems = arrayListOf<Int>().apply {
			addAll(ColorTemplate.VORDIPLOM_COLORS.asList())
			addAll(ColorTemplate.JOYFUL_COLORS.asList())
			addAll(ColorTemplate.COLORFUL_COLORS.asList())
			addAll(ColorTemplate.LIBERTY_COLORS.asList())
			addAll(ColorTemplate.PASTEL_COLORS.asList())
			addAll(ColorTemplate.COLORFUL_COLORS.asList())
			add(ColorTemplate.getHoloBlue())
		}

		// PieDataSet을 생성하고 데이터 및 스타일을 설정합니다.
		val textColor = ContextCompat.getColor(requireContext(), R.color.filter_btn_text_color)

		// PieChart에 생성한 데이터를 적용합니다.
		val labelBackgroundColor = ContextCompat.getColor(requireContext(), R.color.component_color)
		val labelTextColor = ContextCompat.getColor(requireContext(), R.color.text_color)

		pieChart.apply {
			description.isEnabled = false // 설명문구 표시 여부
			isRotationEnabled = true // 차트 회전여부
			centerText = getString(R.string.stats_genre) // 가운데에 표시할 텍스트 설정
			setDrawCenterText(true)
			setCenterTextSize(16f) // 가운데 텍스트 크기 설정
			setCenterTextRadiusPercent(100f) // 원 크기를 조정합니다.
			setCenterTextColor(labelTextColor) // 텍스트 색상을 설정합니다.
			setHoleColor(labelBackgroundColor) // 원의 배경 색상을 설정합니다.
			setUsePercentValues(true) // 백분율로 표시할지 여부
			setEntryLabelColor(textColor) // 항목 라벨 색상 설정
			setBackgroundColor(backgroundColor)
		}

		CoroutineScope(Dispatchers.IO).launch {
			val deferredResults = genres.map {
				async {
					Supabase.client
						.from("reviews")
						.select {
							filter {
								eq(column = "user_id", value = userInfo.id)
								eq(column = "shcate", value = it)
							}

							count(Count.EXACT)
						}
				}
			}

			val finalResults = deferredResults.awaitAll().map { it.countOrNull() }

			val newEntries = finalResults.mapIndexed { index, value ->
				PieEntry(value?.toFloat() ?: 0F, "${genres[index]}")
			}.filter { it.value > 0 }

			val newPieDataSet = PieDataSet(newEntries, "장르별").apply {
				colors = colorsItems
				valueTextColor = textColor
				valueTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
				valueTextSize = 18f
			}

			val newPieData = PieData(newPieDataSet)

			withContext(Dispatchers.Main) {
				pieChart.apply {
					data = newPieData
					animateY(1400, Easing.EaseInOutQuad) // 애니메이션 설정
					animate() // 애니메이션 시작
				}
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}