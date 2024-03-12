package com.nbc.shownect.ui.detail_activity.er.expectation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nbc.shownect.R
import com.nbc.shownect.databinding.FragmentExpectationBinding
import com.nbc.shownect.presentation.detail_activity.er.more.ErActivity
import com.nbc.shownect.supabase.model.PostExpectationModel
import com.nbc.shownect.ui.UserViewModel
import com.nbc.shownect.ui.auth.AuthActivity
import com.nbc.shownect.ui.detail_activity.DetailViewModel


class ExpectationFragment(
	private val mt20id: String,
	private val mt10id: String,
	private val poster: String,
) : Fragment() {
	private val detailViewModel: DetailViewModel by activityViewModels<DetailViewModel>()
	private val expectationViewModel by lazy { ViewModelProvider(this)[ExpectationViewModel::class.java] }
	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }

	private lateinit var binding: FragmentExpectationBinding

	private var isError = false

	private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		userViewModel.setUser()
	}

	private lateinit var listAdapter: ExpectationListAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentExpectationBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initHandle()
		initViewModel()
		initList()
		initData()
	}

	private fun initList() {
		listAdapter = ExpectationListAdapter()

		binding.rvExpectations.adapter = listAdapter
		binding.rvExpectations.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
	}

	private fun initData() {
		expectationViewModel.setCount(mt20id)
		expectationViewModel.setList(mt20id)
	}

	private fun initHandle() = with(binding) {
		btnExpectTrue.setOnClickListener { expectationViewModel.setIsExpect(true) }
		btnExpectFalse.setOnClickListener { expectationViewModel.setIsExpect(false) }

		etExpectation.addTextChangedListener {
			expectationViewModel.setComment(it.toString())
		}

		btnSubmit.setOnClickListener {
			hideKeyboard()
			isError = true

			val isExpect = expectationViewModel.isExpect.value
			val comment = expectationViewModel.comment.value

			expectationViewModel.setComment(comment)
			expectationViewModel.setIsExpect(isExpect)

			if (isValidComment(comment) && isExpect != null) {
				val userId = userViewModel.userInfo.value?.id

				if (userId == null) {
					val intent = Intent(requireActivity(), AuthActivity::class.java)
					launcher.launch(intent)
					return@setOnClickListener
				}

				val model = PostExpectationModel(
					userId = userId,
					mt20id = mt20id,
					poster = poster,
					isExpect = isExpect,
					comment = comment!!,
					mt10id = mt10id
				)

				expectationViewModel.createExpectation(
					model = model,
					context = requireContext(),
					errorMessage = getString(R.string.already_sakusei)
				) {
					detailViewModel.setInfo(mt20id)
				}
			}
		}

		btnMore.setOnClickListener {
			val intent = Intent(requireActivity(), ErActivity::class.java)

			intent.putExtra("mt20id", mt20id)
			intent.putExtra("mode", "expectations")

			launcher.launch(intent)
		}
	}

	private fun initViewModel() {
		expectationViewModel.isExpect.observe(viewLifecycleOwner) {
			with(binding) {
				if (isError) {
					if (it == null) {
						tvExpectWarning.visibility = View.VISIBLE
						return@observe
					} else {
						tvExpectWarning.visibility = View.INVISIBLE
					}
				}

				if (it == null) return@observe

				btnExpectTrue.setBackgroundResource(if (it) R.drawable.circle_on else R.drawable.circle_off)
				btnExpectFalse.setBackgroundResource(if (!it) R.drawable.circle_on else R.drawable.circle_off)
			}
		}

		expectationViewModel.comment.observe(viewLifecycleOwner) {
			if (isError) {
				binding.tvCommentWarning.visibility = if (isValidComment(it!!)) View.INVISIBLE else View.VISIBLE
				binding.btnSubmit.isClickable = isValidComment(it)
			}
		}

		expectationViewModel.isCreateExpectationLoading.observe(viewLifecycleOwner) {
			if (it == null) return@observe

			binding.btnSubmit.isClickable = !it
		}

		expectationViewModel.goodCount.observe(viewLifecycleOwner) {
			binding.tvTrueCount.text = it.toString()
		}
		expectationViewModel.badCount.observe(viewLifecycleOwner) {
			binding.tvFalseCount.text = it.toString()
		}
		expectationViewModel.totalCount.observe(viewLifecycleOwner) {
			binding.btnMore.text = getString(R.string.more_expectation_text) + "($it)"
		}

		expectationViewModel.comments.observe(viewLifecycleOwner) {
			if (it.isEmpty()) {
				binding.tvEmptyExpectations.visibility = View.VISIBLE
				binding.rvExpectations.visibility = View.INVISIBLE
				binding.btnMore.visibility = View.INVISIBLE
			} else {
				binding.tvEmptyExpectations.visibility = View.INVISIBLE
				binding.rvExpectations.visibility = View.VISIBLE
				binding.btnMore.visibility = View.VISIBLE

				listAdapter.submitList(it)
			}
		}
	}

	private fun isValidComment(comment: String?): Boolean {
		if (comment == null) return false
		return (comment.length >= 10) && (comment.length <= 30)
	}

	private fun hideKeyboard() {
		val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		imm.hideSoftInputFromWindow(view?.windowToken, 0)
	}
}