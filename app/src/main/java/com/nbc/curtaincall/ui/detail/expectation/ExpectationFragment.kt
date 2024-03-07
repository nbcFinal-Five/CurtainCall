package com.nbc.curtaincall.ui.detail.expectation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentExpectationBinding
import com.nbc.curtaincall.supabase.Supabase
import com.nbc.curtaincall.supabase.model.PostExpectationModel
import com.nbc.curtaincall.ui.UserViewModel
import com.nbc.curtaincall.ui.auth.AuthActivity
import com.nbc.curtaincall.ui.detail.DetailViewModel
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExpectationFragment(
	private val mt20id: String,
	private val poster: String
) : Fragment() {
	private val detailViewModel by lazy { ViewModelProvider(this)[DetailViewModel::class.java] }
	private val expectationViewModel by lazy { ViewModelProvider(this)[ExpectationViewModel::class.java] }
	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }

	private lateinit var binding: FragmentExpectationBinding

	private var isError = false

	private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		userViewModel.setUser()
	}

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
	}

	private fun initHandle() = with(binding) {
		btnExpectTrue.setOnClickListener { expectationViewModel.setIsExpect(true) }
		btnExpectFalse.setOnClickListener { expectationViewModel.setIsExpect(false) }

		etExpectation.addTextChangedListener {
			expectationViewModel.setComment(it.toString())
		}

		btnSubmit.setOnClickListener {
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
					comment = comment!!
				)

				CoroutineScope(Dispatchers.IO).launch {
					try {
						Supabase.client
							.from("expectations")
							.insert<PostExpectationModel>(model)
					} catch (e: RestException) {
						Log.d("create expectation", e.error)

						if (e.error == "duplicate key value violates unique constraint \"unique_mt20id_user_id\"") {
							// TODO 중복 오류 처리 -> 토스트

						}
					}
				}
			}
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

				btnExpectTrue.setBackgroundResource(if (it) R.color.primary_color else R.color.background_color)
				btnExpectFalse.setBackgroundResource(if (!it) R.color.primary_color else R.color.background_color)
			}
		}

		expectationViewModel.comment.observe(viewLifecycleOwner) {
			if (isError) {
				binding.tvCommentWarning.visibility = if (isValidComment(it!!)) View.INVISIBLE else View.VISIBLE
				binding.btnSubmit.isClickable = isValidComment(it)
			}
		}
	}

	private fun isValidComment(comment: String?): Boolean {
		if (comment == null) return false
		return (comment.length >= 10) && (comment.length <= 30)
	}
}