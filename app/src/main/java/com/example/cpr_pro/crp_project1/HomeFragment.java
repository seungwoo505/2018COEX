package com.example.cpr_pro.crp_project1;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    String nowlocation;
    boolean isChecked = false;            //스위치 체크시 true 반환
    long mNow;
    Date mDate;
    float dust = 0;
    float dust1 = 0;
    float dust2 = 0;
    float dust0 = 0;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String a;
    WebServerSender webServerSender;
    ImageView imageview = null;
    TextView settime;
    TextView locationview;
    TextView Dust_Text;
    TextView settext;
    Button checkdustBtn;
    Switch mSwitch1;
    Double latitude;
    Double longitude;
    String dust_density;


    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        imageview = (ImageView) v.findViewById(R.id.setImage);
        //bind view
        locationview = (TextView) v.findViewById(R.id.setlocation);
        settime = (TextView) v.findViewById(R.id.settime);
        Dust_Text = (TextView) v.findViewById(R.id.textView2);
        checkdustBtn = (Button) v.findViewById(R.id.checkdust);
        settext = (TextView)v.findViewById(R.id.settext);
        mSwitch1 = (Switch) v.findViewById(R.id.switch1);
        webServerSender = new WebServerSender("http://cpr1234.dothome.co.kr", "example.php", "POST");

        checkdustBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settime.setText(getTime());
                startLocationService();

                if (isChecked == true) {

                    if (dust0 >= 0 && dust0 <= 35 && dust >= 0 && dust <= 50) {
                        imageview.setImageResource(R.drawable.vertgood);
                        settext.setText("외출 등 실외활동 지장없음");
                    } else if (dust0 > 35 && dust0 <= 75 && dust > 50 && dust <= 100) {
                        imageview.setImageResource(R.drawable.good);
                        settext.setText("1. 충분한 수분섭취\n2. 몸상태에 따라 실외활동시 유의\n3. 과일, 채소 등 충분히 씻어 먹기");
                    } else if (dust0 > 75 && dust0 <= 115 && dust > 100 && dust <= 150) {
                        imageview.setImageResource(R.drawable.normal1);
                        settext.setText("1.가급적 외출을 줄이고, 외출 시에는 식약처 인증 마스크 착용\n2. 집에 돌아와서는 꺠끗이 몸을 씻고, 물이나 비타민C섭취");
                    } else if (dust0 > 115 && dust0 <= 150 && dust > 150 && dust <= 200) {
                        imageview.setImageResource(R.drawable.sobad);
                        settext.setText("1. 적절히 환기하고, 물청소등을 하며 실내 공기질 관리\n2.공기오염이 심한 곳은 피하고, 폐기물을 태우는 등 대기오염 유발은 금지 ");
                    } else if (dust0 > 150 && dust0 <= 250 && dust > 200 && dust <= 300) {
                        imageview.setImageResource(R.drawable.bad);
                        settext.setText("1. 호흡기 및 심혈관질환자,노약자는 야외활동 금지(실내생활)\n2.창문을 닫아 외부위 미세먼지 유입을 차단 ");
                    } else {
                        imageview.setImageResource(R.drawable.dddd);
                        settext.setText("1. 환자 및 노약자는 야외활동 금지\n2. 일반인도 실외활동 자제");
                    }
                } else {
                    if (dust1 <= 500 && dust2 <= 1000) {
                        imageview.setImageResource(R.drawable.vertgood);
                        settext.setText("모두 정상수치입니다.");
                    }
                    if (dust1 > 500 && dust2 <= 1000) {
                        imageview.setImageResource(R.drawable.normal1);
                        settext.setText("TVOC는 정상 수치에서 벗어났습니다.");
                    }
                    if (dust1 <= 500 && dust2 > 1000) {
                        imageview.setImageResource(R.drawable.normal1);
                        settext.setText("이산화탄소는 정상 수치에서 벗어났습니다.");
                    } else {
                        imageview.setImageResource(R.drawable.dddd);
                        settext.setText("모두 정상수치에서 벗어났습니다.");
                    }

                }

                webServerSender.add("longitude", "136.23");
                webServerSender.add("latitude", "87.015");
                webServerSender.add("dust", "1000");
                webServerSender.add("co2", "1800");
                webServerSender.add("inside", "false");
                Sendmsg sendmsg = new Sendmsg();
                sendmsg.execute();

            }
        });


        mSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Dust_Text.setText("초미세먼지 : " + dust0 + "\n미세먼지 : " + dust);
                } else if (isChecked == false) {
                    Dust_Text.setText("TVOC : " + dust1 + "\n이산화탄소 : " + dust2);
                }
            }
        });
        return v;
    }

    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                    gpsListener);

            // 네트워크를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인

        } catch(SecurityException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 리스너 클래스 정의
     */

    private class GPSListener implements LocationListener {
        /**
         * 위치 정보가 확인될 때 자동 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if(latitude!=null&&longitude!=null) {
                nowlocation = getAddress(getContext(), latitude, longitude);
                locationview.setText(nowlocation.substring(4));
            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }

    /**
     * 위도,경도로 주소구하기
     * @param lat
     * @param lng
     * @return 주소
     */
    public static String getAddress(Context mContext,double lat, double lng) {
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress  = currentLocationAddress;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return nowAddress;
    }


    private class Sendmsg extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... param) {

            try {
                webServerSender.send();
                a = webServerSender.receiveResult();
                System.out.println("request : " + a);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            return a;

        }

        protected void onPostExecute(String s) {
            json(s);


        }

        void json(String s) {
            String check;
            try {
                JSONObject json = new JSONObject(s);

                JSONArray jArr = json.getJSONArray("cpr");





                // 받아온 pRecvServerPage를 분석하는 부분

                String[] jsonName = {"number","region", "regiondust"};

                String[][] parseredData = new String[jArr.length()][jsonName.length];

                for (int i = 0; i < jArr.length(); i++) {

                    json = jArr.getJSONObject(i);

                    if(json != null) {

                        for(int j = 0; j < jsonName.length; j++) {
                            parseredData[i][j] = json.getString(jsonName[j]);
                        }
                    }

                    Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][0]);

                    Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][1]);

                    Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][2]);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            webServerSender.clearArg();
        }
    }
}
