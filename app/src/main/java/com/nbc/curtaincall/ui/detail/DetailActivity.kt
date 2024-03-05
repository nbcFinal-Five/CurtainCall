package com.nbc.curtaincall.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.ActivityDetailBinding
import com.nbc.curtaincall.fetch.model.DbsResponse
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var showDetail : DbsResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val id = intent.getStringExtra("fromTicketId")
        lifecycleScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    showDetail = id?.let { RetrofitClient.fetch.fetchShowDetail(path = it) }!!
                }
            }.onFailure {
                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
            }

            with(binding) {
                ivDetailPoster.load(showDetail.showList[0].poster)
                tvDetailShowNameSub.text = showDetail.showList[0].prfnm
                tvDetailGenreSub.text = showDetail.showList[0].genrenm
                tvDetailAgeSub.text = showDetail.showList[0].prfage
                tvDetailRuntimeSub.text = showDetail.showList[0].prfruntime
                tvDetailPriceSub.text = showDetail.showList[0].pcseguidance
                tvDetailShowStateSub.text = showDetail.showList[0].prfstate
                tvDetailPlaceSub.text = showDetail.showList[0].fcltynm
                tvDetailPeriodSub.text = "${showDetail.showList[0].prfpdfrom} ~ ${showDetail.showList[0].prfpdto}"
                tvDetailTimeSub.text = showDetail.showList[0].prfruntime
                tvDetailCastSub.text =
                    if (showDetail.showList[0].prfcast.isNullOrEmpty()) "미상" else showDetail.showList[0].prfcast
                tvDetailProductSub.text =
                    if (showDetail.showList[0].entrpsnm.isNullOrEmpty()) "미상" else showDetail.showList[0].entrpsnm
                //Log.i("TAG", "onCreate: ${showDetail.showList[0].styurls}")
            }
        }
    }
}
