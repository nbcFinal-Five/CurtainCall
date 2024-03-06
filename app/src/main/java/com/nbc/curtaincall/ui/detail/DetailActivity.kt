package com.nbc.curtaincall.ui.detail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.nbc.curtaincall.databinding.ActivityDetailBinding
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient
import com.nbc.curtaincall.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

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
    }

    private fun fetchShowDetail(id: String) {
        lifecycleScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    RetrofitClient.fetch.fetchShowDetail(path = id)
                }
            }.onSuccess { showDetail ->
                showDetail.showList?.firstOrNull()?.let { firstShowDetail ->
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
}
