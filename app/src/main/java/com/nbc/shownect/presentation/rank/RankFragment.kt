package com.nbc.shownect.presentation.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.nbc.shownect.databinding.FragmentRankBinding
import com.nbc.shownect.fetch.network.retrofit.RetrofitClient.fetch
import com.nbc.shownect.fetch.repository.impl.FetchRepositoryImpl
import com.nbc.shownect.presentation.rank.adpter.RankAdapter

class RankFragment : Fragment() {
    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!!
    private val rankAdapter: RankAdapter by lazy { RankAdapter() }
    private val viewModel: RankViewModel by viewModels {
        RankViewModelFactory(
            fetchRemoteRepository = FetchRepositoryImpl(
                fetch
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankBinding.inflate(inflater, container, false)
        initRecyclerView()
        viewModel.rankList.observe(viewLifecycleOwner) {
            rankAdapter.submitList(it)
            binding.rvRank.smoothScrollToPosition(0)
        }
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchInitRank()
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

    private fun initView() {
        with(binding) {
            chipDay.isChecked = true
            chipRankAll.isChecked = true
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setUpClickListener() {
        with(binding) {
            val checkedGenre = chipGroupBottom.children.first { (it as Chip).isChecked } as? Chip
            val checkedPeriod = chipGroupTop.children.first { (it as Chip).isChecked } as? Chip
            chipDay.setOnClickListener {
                viewModel.fetchPeriod(chipDay.text.toString(), checkedGenre?.text.toString())
            }
            chipWeek.setOnClickListener {
                viewModel.fetchPeriod(chipWeek.text.toString(), checkedGenre?.text.toString())
            }
            chipMonth.setOnClickListener {
                viewModel.fetchPeriod(chipMonth.text.toString(), checkedGenre?.text.toString())
            }
            chipRankAll.setOnClickListener {
                viewModel.fetchGenre(checkedPeriod?.text.toString(), "전체")
            }
            chipRankDrama.setOnClickListener {
                viewModel.fetchGenre(checkedPeriod?.text.toString(), chipRankDrama.text.toString())
            }
            chipRankMusical.setOnClickListener {
                viewModel.fetchGenre(
                    checkedPeriod?.text.toString(),
                    chipRankMusical.text.toString()
                )
            }
            chipRankClassic.setOnClickListener {
                viewModel.fetchGenre(
                    checkedPeriod?.text.toString(),
                    chipRankClassic.text.toString()
                )
            }
            chipRankDance.setOnClickListener {
                viewModel.fetchGenre(checkedPeriod?.text.toString(), chipRankDance.text.toString())
            }
            chipRankKoreaMusic.setOnClickListener {
                viewModel.fetchGenre(
                    checkedPeriod?.text.toString(),
                    chipRankKoreaMusic.text.toString()
                )
            }
            chipRankPopularMusic.setOnClickListener {
                viewModel.fetchGenre(
                    checkedPeriod?.text.toString(),
                    chipRankPopularMusic.text.toString()
                )
            }
            chipRankMagic.setOnClickListener {
                viewModel.fetchGenre(checkedPeriod?.text.toString(), chipRankMagic.text.toString())
            }
            chipRankComplex.setOnClickListener {
                viewModel.fetchGenre(
                    checkedPeriod?.text.toString(),
                    chipRankComplex.text.toString()
                )
            }
        }
    }
}

