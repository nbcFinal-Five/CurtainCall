package com.nbc.curtaincall.ui.detail_activity.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentDetailLocationBinding
import com.nbc.curtaincall.ui.detail_activity.DetailViewModel
import com.nbc.curtaincall.util.Constants


class LocationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDetailLocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by activityViewModels<DetailViewModel>()
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //클라이언트 ID 지정
        NaverMapSdk.getInstance(requireContext()).client = NaverMapSdk.NaverCloudPlatformClient(Constants.MAP_CLIENT_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailLocationBinding.inflate(inflater,container,false)
        setUpObserve()
        mapView = binding.fvHallNaverMap
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchDetailLocation()
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        binding.btnMapMarkerInPlace.apply {
            setOnClickListener {
                moveToMarkerPosition()
            }
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.map_in_place))
        }
    }

    private fun setUpObserve() {
        viewModel.locationList.observe(viewLifecycleOwner) {
            with(binding) {
                val firstShowDetail = it.showList?.first()
                tvConcertHallContents.setText(firstShowDetail?.facilityName)
                tvConcertHallLocationContents.setText(firstShowDetail?.adres)
                tvHallCallNumberContents.setText(if(firstShowDetail?.telno.isNullOrBlank()) "N/A" else firstShowDetail?.telno)
                tvHallHomepageContents.setText(if(firstShowDetail?.relateurl.isNullOrBlank()) "N/A" else firstShowDetail?.relateurl)
            }
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        viewModel.locationList.observe(viewLifecycleOwner) { locations ->
            locations?.showList?.forEach { location ->
                val latitude = location.la // 위도
                val longitude = location.lo // 경도

                // 위도와 경도를 사용하여 마커를 생성
                val marker = Marker()
                marker.position = LatLng(latitude?.toDouble() ?:0.0, longitude?.toDouble() ?:0.0) // 마커의 위치 설정
                marker.map = naverMap // 마커를 지도에 추가

                // 해당 위치로 지도를 이동
                val cameraPosition = CameraPosition(
                    LatLng(latitude?.toDouble() ?:0.0, longitude?.toDouble() ?:0.0), // 지도의 중심점 위치
                    16.0 // 줌 레벨
                )
                naverMap.cameraPosition = cameraPosition

                // 지도 범위 제한 (대한민국)
                naverMap.minZoom = 6.0
                naverMap.extent = LatLngBounds(LatLng(32.973077, 124.270981), LatLng(38.856197,130.051725 ))

                // 줌인 했을 때 내부 지도 있을 경우 띄우기
                naverMap.isIndoorEnabled = true
            }
        }
    }

    private fun moveToMarkerPosition() {
        val firstLocation = viewModel.locationList.value?.showList?.firstOrNull()
        firstLocation?.let { location ->
            val latitude = location.la?.toDouble() ?: 0.0
            val longitude = location.lo?.toDouble() ?: 0.0

            val cameraPosition = CameraPosition(
                LatLng(latitude, longitude),
                16.0
            )
            mapView.getMapAsync { naverMap ->
                naverMap.moveCamera(CameraUpdate.toCameraPosition(cameraPosition))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}