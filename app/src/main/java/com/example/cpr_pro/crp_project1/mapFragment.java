package com.example.cpr_pro.crp_project1;

import android.content.Context;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;



public class mapFragment extends Fragment implements  OnMapStateChangeListener, OnCalloutOverlayListener{
    View view;

    Location location;
    double lat; // 위도
    double lon; // 경도


    public LocationManager locationManager;
    protected Context mContext;
    LinearLayout MapContainer;

    // 오버레이의 리소스를 제공하기 위한 객체
    NMapViewerResourceProvider mMapViewerResourceProvider = null;
    NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener =null;
    NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener; //말풍선 오버레이 객체 생성 시 호출되는 콜백 인터페이스를 정의한다.



    private NMapContext mMapContext;
    NMapView mapView;
    NMapView.OnMapStateChangeListener onMapViewStateChangeListener;
    NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener;
    NMapController mMapController;

    private static final String CLIENT_ID = "WzK2y6cnp3gzdhaV8_8l";// 애플리케이션 클라이언트 아이디 값
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gmaps, container, false);


        Button buttonGPS =(Button)view.findViewById(R.id.buttonGPS);
        buttonGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationService();

            }
        });

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext = new NMapContext(super.getActivity());
        mMapContext.onCreate();

        this.mContext = getContext();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NMapView mapView = (NMapView)getView().findViewById(R.id.mapView);
        mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mMapContext.setupMapView(mapView);

        mMapController = mapView.getMapController();
        mapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        mapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
        mapView.setClickable(true);
        NMapOverlayManager mOverlayManager;
        mMapViewerResourceProvider = new NMapViewerResourceProvider(getActivity());
        mOverlayManager = new NMapOverlayManager(getActivity(), mapView,mMapViewerResourceProvider);



        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener); //말풍선 오버레이 객체 생성 시 호출되는 콜백 인터페이스를 설정한다.

        int markerId = NMapPOIflagType.PIN;
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider); //전체 POI 아이템의 개수와 NMapResourceProvider를 상속받은 클래스를 인자로 전달한다.
        poiData.beginPOIdata(2); //POI 아이템 추가를 시작한다.

        NMapPOIitem item1 = poiData.addPOIitem(126.872772, 37.546848, "KB국민은행 염창역 지점 앞", markerId, 0); //POI아이템 설정
        item1.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW); //마커 선택 시 표시되는 말풍선의 오른쪽 아이콘을 설정한다.
        item1.hasRightAccessory(); //마커 선택 시 표시되는 말풍선의 오른쪽 아이콘 설정 여부를 반환한다.
        item1.setRightButton(true); //마커 선택 시 표시되는 말풍선의 오른쪽 버튼을 설정한다.
        item1.showRightButton(); //마커 선택 시 표시되는 말풍선의 오른쪽 버튼 설정 여부를 반환한다.
        int a=3;
        NMapPOIitem item2 = poiData.addPOIitem(126.914925, 37.528728, "국회의원회관"+a, markerId, 0);
        item2.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
        item2.hasRightAccessory();
        item2.setRightButton(true);
        item2.showRightButton();

        poiData.addPOIitem(127, 37, "KB국민은행 염창역 지점 앞", markerId, 0); //경도위도 좌표 입력해주면, 그 좌표가 표시됨
        poiData.addPOIitem(126.914925, 37.528728, "국회의원회관", markerId, 0); //경도위도 좌표 입력해주면, 그 좌표가 표시됨
        poiData.endPOIdata(); //POI 아이템 추가를 종료한다.
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null); //POI 데이터를 인자로 전달하여 NMapPOIdataOverlay 객체를 생성한다.
        poiDataOverlay.showAllPOIdata(0); //POI 데이터가 모두 화면에 표시되도록 지도 축척 레벨 및 지도 중심을 변경한다. zoomLevel이 0이 아니면 지정한 지도 축척 레벨에서 지도 중심만 변경한다.
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener); //POI 아이템의 선택 상태 변경 시 호출되는 콜백 인터페이스를 설정한다.

    }

    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }
    @Override
    public void onStop() {
        mMapContext.onStop();
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
    }
    public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
        if (errorInfo == null) { // success

            mMapController.setMapCenter(new NGeoPoint(127,37 ), 11);//맵의 위치


        } else { // fail
        }
    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    private void startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        // 위치 정보를 받을 리스너 생성
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        try {
            // GPS를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    (LocationListener) gpsListener);

            // 네트워크를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                Double lat = lastLocation.getLatitude();
                Double lon = lastLocation.getLongitude();
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay nMapOverlay, NMapOverlayItem nMapOverlayItem, Rect rect) {
        return null;
    }

    /**
     * 리스너 클래스 정의
     */

    private class GPSListener implements LocationListener {
        /**
         * 위치 정보가 확인될 때 자동 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            if(lat!=0&&lon!=0) {
                mMapController.setMapCenter(new NGeoPoint(lon, lat), 11);
            }
            String msg = "Latitude : "+ lat + "\nLongitude:"+ lon;
            Log.i("GPSListener", msg);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
