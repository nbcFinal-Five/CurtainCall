package com.nbc.curtaincall.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.nbc.curtaincall.R
import coil.size.Size
import coil.size.SizeResolver
import com.nbc.curtaincall.databinding.ActivityDetailBinding
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient
import com.nbc.curtaincall.ui.UserViewModel
import com.nbc.curtaincall.ui.detail.expectation.ExpectationFragment
import com.nbc.curtaincall.ui.detail.review.ReviewFragment
import com.nbc.curtaincall.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
	private lateinit var binding: ActivityDetailBinding

	private val detailViewModel by lazy { ViewModelProvider(this)[DetailViewModel::class.java] }
	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }

	private var mt20id: String? = null
	private var poster: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDetailBinding.inflate(layoutInflater)
		setContentView(binding.root)

		ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		val id = intent.getStringExtra(Constants.SHOW_ID)
		if (id != null) {
			fetchShowDetail(id)
		}

		initHandler()
		initViewModel()
	}

	private fun fetchShowDetail(id: String) {
		lifecycleScope.launch {
			runCatching {
				withContext(Dispatchers.IO) {
					RetrofitClient.fetch.fetchShowDetail(path = id)
				}
			}.onSuccess { showDetail ->
				showDetail.showList?.firstOrNull()?.let { firstShowDetail ->
					mt20id = firstShowDetail.mt20id!!
					poster = firstShowDetail.poster!!

					detailViewModel.setExpectationCount(firstShowDetail.mt20id!!)

					setFragment(
						ExpectationFragment(
							mt20id = firstShowDetail.mt20id!!,
							poster = firstShowDetail.poster!!
						) {
							detailViewModel.setExpectationCount(mt20id!!)
						}
					)

					with(binding) {
						ivDetailPoster.load(firstShowDetail.poster)
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
						ivDetailIntroPoster.load(firstShowDetail.styurls?.styurlList?.get(0))
						Log.d("TAG", "fetchShowDetail: ${firstShowDetail}")
					}
				}
			}.onFailure {
				Toast.makeText(applicationContext, "통신 에러: ${it.message}", Toast.LENGTH_SHORT)
					.show()
			}
		}
	}

	private fun initHandler() = with(binding) {
		btnExpectations.setOnClickListener { detailViewModel.setMode(DetailViewModel.EXPECTATION) }
		btnReviews.setOnClickListener { detailViewModel.setMode(DetailViewModel.REVIEW) }
	}

	private fun initViewModel() {
		detailViewModel.mode.observe(this) {
			if (mt20id != null && poster != null) {
				with(binding) {
					when (it) {
						DetailViewModel.REVIEW -> {
							setFragment(
								ReviewFragment(
									mt20id = mt20id!!,
									poster = poster!!
								)
							)
							btnReviews.setBackgroundResource(R.color.primary_color)
							btnReviews.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.white))

							btnExpectations.setBackgroundResource(R.color.component_background_color)
							btnExpectations.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.component_color))
						}

						DetailViewModel.EXPECTATION -> {
							setFragment(
								ExpectationFragment(
									mt20id = mt20id!!,
									poster = poster!!
								) {
									detailViewModel.setExpectationCount(mt20id!!)
								}
							)
							btnReviews.setBackgroundResource(R.color.component_background_color)
							btnReviews.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.component_color))

							btnExpectations.setBackgroundResource(R.color.primary_color)
							btnExpectations.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.white))
						}
					}
				}
			}
		}

		detailViewModel.totalExpectationCount.observe(this) {
			binding.tvDetailExpectationsNum.text = "기대평 $it 개"
		}
	}

	private fun setFragment(frag: Fragment) {
		supportFragmentManager.commit {
			replace(R.id.fl_comment, frag)
			setReorderingAllowed(true)
		}
	}

}
