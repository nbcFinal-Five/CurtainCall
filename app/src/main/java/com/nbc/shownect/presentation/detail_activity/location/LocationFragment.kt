package com.nbc.shownect.ui.detail_activity.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.nbc.shownect.R
import com.nbc.shownect.databinding.FragmentDetailLocationBinding
import com.nbc.shownect.ui.detail_activity.DetailViewModel
import com.nbc.shownect.util.Constants


class LocationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDetailLocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by activityViewModels<DetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //클라이언트 ID 지정
        NaverMapSdk.getInstance(requireContext()).client = NaverMapSdk.NaverCloudPlatformClient(Constants.MAP_CLIENT_ID)

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.fv_hall_naver_map) as MapFragment?
            ?: MapFragment.newInstance().also {
            fm.beginTransaction().add(R.id.fv_hall_naver_map, it).commit()
        }
        mapFragment.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailLocationBinding.inflate(inflater,container,false)
        setUpObserve()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchDetailLocation()

    }

    private fun setUpObserve() {
        viewModel.locationList.observe(viewLifecycleOwner) {
            with(binding) {
                val firstShowDetail = it.first()
                tvConcertHallContents.setText(firstShowDetail.fcltynm)
                tvConcertHallLocationContents.setText(firstShowDetail.adres)
                tvHallCallNumberContents.setText(if(firstShowDetail.telno.isNullOrBlank()) "N/A" else firstShowDetail.telno)
                tvHallHomepageContents.setText(if(firstShowDetail.relateurl.isNullOrBlank()) "N/A" else firstShowDetail.relateurl)
            }
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        viewModel.locationList.observe(viewLifecycleOwner) { locations ->
            locations?.forEach { location ->
                val latitude = location.la // 위도
                val longitude = location.lo // 경도

                // 위도와 경도를 사용하여 마커를 생성합니다.
                val marker = Marker()
                marker.position = LatLng(latitude?.toDouble() ?:0.0, longitude?.toDouble() ?:0.0) // 마커의 위치 설정
                marker.map = naverMap // 마커를 지도에 추가

                // 해당 위치로 지도를 이동합니다.
                val cameraPosition = CameraPosition(
                    LatLng(latitude?.toDouble() ?:0.0, longitude?.toDouble() ?:0.0), // 지도의 중심점 위치
                    16.0 // 줌 레벨
                )
                naverMap.cameraPosition = cameraPosition

                naverMap.minZoom = 6.0
                naverMap.maxZoom = 18.0
                naverMap.extent = LatLngBounds(LatLng(32.973077, 124.270981), LatLng(38.856197,130.051725 ))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}