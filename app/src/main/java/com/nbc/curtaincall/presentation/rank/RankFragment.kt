package com.nbc.curtaincall.presentation.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentRankBinding
import com.nbc.curtaincall.presentation.rank.adpter.RankAdapter
import com.nbc.curtaincall.ui.home.adapter.PosterClickListener
import com.nbc.curtaincall.ui.main.MainViewModel
import com.nbc.curtaincall.ui.ticket.TicketDialogFragment

class RankFragment : Fragment(), PosterClickListener {
    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!!
    private val rankAdapter: RankAdapter by lazy { RankAdapter(this) }
    private val viewModel: RankViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankBinding.inflate(inflater, container, false)
        initRecyclerView()
        setUpObserve()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListener()

    }

    private fun initRecyclerView() {
        with(binding) {
            rvRank.apply {
                adapter = rankAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun setUpObserve() {
        //초기 아이템 설정
        with(viewModel) {
            rankInitList.observe(viewLifecycleOwner) {
                rankAdapter.submitList(it) {
                    binding.rvRank.smoothScrollToPosition(0)
                }
            }
            //로딩 처리
            isRankLoading.observe(viewLifecycleOwner) {
                with(binding) {
                    skeletonRankLoading.isVisible = it
                    rvRank.isVisible = !it
                }
            }
            //칩 선택시 아이템 리스트 변경
            rankList.observe(viewLifecycleOwner) {
                rankAdapter.submitList(it)
                binding.rvRank.smoothScrollToPosition(0)
            }
            //초기 화면 한 번만 초기화 되도록 설정
            initState.observe(viewLifecycleOwner) {
                if (!it) {
                    with(binding) {
                        chipDay.isChecked = true
                        chipRankAll.isChecked = true
                    }
                    fetchInitRank()
                    initState(true)
                }
            }
        }
    }

    private fun setUpClickListener() {
        with(binding) {
            //기간별 칩
            chipDay.setOnClickListener {
                val checkedGenre =
                    chipGroupGenre.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(chipDay.text.toString(), checkedGenre?.text.toString())
            }
            chipWeek.setOnClickListener {
                val checkedGenre =
                    chipGroupGenre.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(chipWeek.text.toString(), checkedGenre?.text.toString())
            }
            chipMonth.setOnClickListener {
                val checkedGenre =
                    chipGroupGenre.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(chipMonth.text.toString(), checkedGenre?.text.toString())
            }

            //장르별 칩
            chipRankAll.setOnClickListener {
                val checkedPeriod =
                    chipGroupPeriod.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(checkedPeriod?.text.toString(), "전체")

            }
            chipRankDrama.setOnClickListener {
                val checkedPeriod =
                    chipGroupPeriod.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(checkedPeriod?.text.toString(), chipRankDrama.text.toString())
            }
            chipRankMusical.setOnClickListener {
                val checkedPeriod =
                    chipGroupPeriod.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(
                    checkedPeriod?.text.toString(),
                    chipRankMusical.text.toString()
                )
            }
            chipRankClassic.setOnClickListener {
                val checkedPeriod =
                    chipGroupPeriod.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(
                    checkedPeriod?.text.toString(),
                    chipRankClassic.text.toString()
                )
            }
            chipRankDance.setOnClickListener {
                val checkedPeriod =
                    chipGroupPeriod.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(checkedPeriod?.text.toString(), chipRankDance.text.toString())
            }
            chipRankKoreaMusic.setOnClickListener {
                val checkedPeriod =
                    chipGroupPeriod.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(
                    checkedPeriod?.text.toString(),
                    chipRankKoreaMusic.text.toString()
                )
            }
            chipRankPopularMusic.setOnClickListener {
                val checkedPeriod =
                    chipGroupPeriod.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(
                    checkedPeriod?.text.toString(),
                    chipRankPopularMusic.text.toString()
                )
            }
            chipRankMagic.setOnClickListener {
                val checkedPeriod =
                    chipGroupPeriod.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(checkedPeriod?.text.toString(), chipRankMagic.text.toString())
            }
            chipRankComplex.setOnClickListener {
                val checkedPeriod =
                    chipGroupPeriod.children.firstOrNull { (it as Chip).isChecked } as? Chip
                viewModel.fetchRank(
                    checkedPeriod?.text.toString(),
                    chipRankComplex.text.toString()
                )
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    //포스터 클릭 리스너
    override fun posterClicked(id: String) {
        val ticketDialog = TicketDialogFragment()
        sharedViewModel.sharedShowId(id) //해당 공연의 id를 MainViewModel로 보내줌
        ticketDialog.setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.RoundCornerBottomSheetDialogTheme
        )
        ticketDialog.show(childFragmentManager, ticketDialog.tag)
    }
}

