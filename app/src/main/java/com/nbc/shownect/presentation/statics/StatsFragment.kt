package com.nbc.shownect.presentation.statics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import com.nbc.shownect.R
import com.nbc.shownect.databinding.FragmentStatsBinding
import com.nbc.shownect.ui.detail_activity.er.expectation.ExpectationViewModel
import com.nbc.shownect.ui.detail_activity.er.review.ReviewViewModel

class StatsFragment : Fragment() {
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
        _binding = FragmentStatsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        averageMyReviewScore()
        countMyexpectation()
        setPieChart()
        setBarChart()
    }

    private fun averageMyReviewScore() {

    }

    private fun countMyexpectation() {

    }

    private fun setBarChart(){
        val backgroundColor = ContextCompat.getColor(requireContext(), R.color.component_color)
        // 먼저 바 차트를 초기화합니다.
        val barChart = binding.bcDateReview

        // 바 차트를 더 잘 표시하기 위해 설정을 변경할 수 있습니다.
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.description.isEnabled = false

        // 바 차트의 데이터를 설정합니다.
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 10f))
        entries.add(BarEntry(1f, 20f))
        entries.add(BarEntry(2f, 30f))
        entries.add(BarEntry(3f, 40f))
        entries.add(BarEntry(4f, 50f))

        // 데이터 세트를 만듭니다.
        val dataSet = BarDataSet(entries, "Label")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

        // 바 차트에 데이터를 설정합니다.
        val data = BarData(dataSet)
        barChart.data = data

        // 바 차트를 스타일링합니다.
        barChart.apply {
            setFitBars(true)
            animateY(1400)
            invalidate()
            setBackgroundColor(backgroundColor)
        }
    }

    private fun setPieChart() {
        val backgroundColor = ContextCompat.getColor(requireContext(), R.color.component_color)
        // PieChart를 binding에서 가져옵니다.
        val pieChart = binding.pcGenreReview

        // PieChart에 표시할 데이터를 설정합니다.
        val entries = arrayListOf(
            PieEntry(14f, "Apple"),
            PieEntry(22f, "Orange"),
            PieEntry(7f, "Mango"),
            PieEntry(31f, "RedOrange"),
            PieEntry(26f, "Other")
        )

        // PieChart에 적용할 색상을 정의합니다.
        val colorsItems = arrayListOf<Int>().apply {
            addAll(ColorTemplate.VORDIPLOM_COLORS.asList())
            addAll(ColorTemplate.JOYFUL_COLORS.asList())
            addAll(ColorTemplate.COLORFUL_COLORS.asList())
            addAll(ColorTemplate.LIBERTY_COLORS.asList())
            addAll(ColorTemplate.PASTEL_COLORS.asList())
            add(ColorTemplate.getHoloBlue())
        }

        // PieDataSet을 생성하고 데이터 및 스타일을 설정합니다.
        val pieDataSet = PieDataSet(entries, "").apply {
            colors = colorsItems
            valueTextColor = Color.BLACK
            valueTextSize = 18f
        }

        // PieData를 생성하고 데이터를 설정합니다.
        val pieData = PieData(pieDataSet)



        // PieChart에 생성한 데이터를 적용합니다.
        pieChart.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            centerText = "진단 결과" // 가운데에 표시할 텍스트 설정
            setUsePercentValues(true) // 백분율로 표시할지 여부
            setCenterTextSize(20f) // 가운데 텍스트 크기 설정
            setEntryLabelColor(Color.BLACK) // 항목 라벨 색상 설정
            setBackgroundColor(backgroundColor)
            animateY(1400, Easing.EaseInOutQuad) // 애니메이션 설정
            animate() // 애니메이션 시작
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}