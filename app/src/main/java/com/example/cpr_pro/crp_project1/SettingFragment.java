package com.example.cpr_pro.crp_project1;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class SettingFragment extends Fragment {


    Button button;
    Button button2;
    Button button3;
    TextView mTextView4;

    public SettingFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        button = (Button) v.findViewById(R.id.button);
        button2 = (Button) v.findViewById(R.id.button2);
        button3 = (Button) v.findViewById(R.id.button3);
        mTextView4 = (TextView) v.findViewById(R.id.textView4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(android.R.drawable.stat_notify_error)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle("지금시각 미세먼지 : ")
                        .setContentText("예) 외출 시 마스크를 착용하세요");
                notificationBuilder.setDefaults(
                        Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                NotificationManager notificatonmanager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificatonmanager.notify(1, notificationBuilder.build());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextView4.setText("미세먼지에 관한 설명\n우리 눈에 보이지 않는 아주 작은 물질로 대기 중에 오랫동안 떠다니거나 흩날려 내려오는 직경 10㎛ 이하의 입자상 물질을 말한다. 석탄, 석유 등의 화석연료가 연소될 때 또는 제조업ㆍ자동차 매연 등의 배출가스에서 나오며, 기관지를 거쳐 폐에 흡착되어 각종 폐질환을 유발하는 대기오염물질이다.\n" +
                        " 장기간 미세먼지에 노출될 경우 면역력이 급격히 저하되어 감기, 천식, 기관지염 등의 호흡기 질환은 물론 심혈관 질환, 피부질환, 안구질환 등 각종 질병에 노출될 수 있다. 특히 직경 2.5㎛ 이하의 초미세먼지는 인체 내 기관지 및 폐 깊숙한 곳까지 침투하기 쉬워 기관지, 폐 등에 붙어 각종 질환을 유발한다.\n" +
                        "[네이버 지식백과] 미세먼지 (시사상식사전, 박문각)");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTextView4.setText("VOC에 관한 설명\n"+
                        "휘발성유기화합물로대기 중에서 질소산화물과 공존하면 햇빛의 작용으로 광화학반응을 일으켜 오존 및 팬등 광화학 산화성 물질을 생성시켜 광화학스모그를 유발하는 물질을 통틀어 일컫는 말이다. 대기오염물질이며 발암성을 지닌 독성 화학물질로서 광화학산화물의 전구물질이기도 하다. 또한 지구온난화의 원인물질이며 악취를 일으키기도 한다.\n" +
                        "[네이버지식백과] 휘발성유기화합물");

            }


        });
        return v;
    }
}