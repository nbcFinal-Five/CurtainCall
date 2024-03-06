package com.nbc.curtaincall.ui.detail.expectation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentExpectationBinding
import com.nbc.curtaincall.supabase.Supabase
import com.nbc.curtaincall.supabase.model.PostExpectationModel
import com.nbc.curtaincall.ui.UserViewModel
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
			val isExpect = expectationViewModel.isExpect.value ?: return@setOnClickListener
			val comment = expectationViewModel.comment.value ?: return@setOnClickListener

			val model = PostExpectationModel(
				userId = userViewModel.userInfo.value?.id!!,
				mt20id = mt20id,
				poster = poster,
				isExpect = isExpect,
				comment = comment
			)

			CoroutineScope(Dispatchers.IO).launch {
				try {
					Supabase.client
						.from("expectations")
						.insert<PostExpectationModel>(model)
				} catch (e: RestException) {
					Log.d("create expectation", e.error)

					if (e.error == "duplicate key value violates unique constraint \"unique_mt20id_user_id\"") {
						// TODO 중복 오류 처리

					}
				}
			}
		}
	}

	private fun initViewModel() {
		expectationViewModel.isExpect.observe(viewLifecycleOwner) {
			with(binding) {
				if (it == null) {


					return@observe
				}

				btnExpectTrue.setBackgroundResource(if (it) R.color.primary_color else R.color.background_color)
				btnExpectFalse.setBackgroundResource(if (!it) R.color.primary_color else R.color.background_color)
			}
		}
	}
}