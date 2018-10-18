package com.example.a503_25.a1017asyctaskserver;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

public class LooperPrec extends AppCompatActivity {
    ArrayList<String> oddDatas;
    ArrayList<String> evenDatas;

    ArrayAdapter<String> oddAdapter;
    ArrayAdapter<String> evenAdapter;

    OneThread oneThread;
    TwoThread twoThread;


    Handler handler; // 이 핸들러에 하면 Looper을 안만들어도 되지만 밑에처럼 스레드를
    //만들고 하면 루퍼를 만들어야 한다.




    class OneThread extends Thread{
        //스레드 내부에 핸들러 만들기
        Handler onehandler;
        public void run(){
            //스레드 내부에서 핸들러를 만들 때는 Looper 이용
            Looper.prepare();
            onehandler = new Handler(){
                @Override
                public void handleMessage(Message message){
                    //1초대기 Thread.sleep이랑 똑같은데 앞에꺼는 try-catch를 사용해야하지만 밑에꺼는 안해도된다.
                    //밑에꺼는 안드로이드에서 제공하는 거다.
                    SystemClock.sleep(1000);
                    //메시지의 전송 내용 가져오기
                    final int data = message.arg1;
                    //메시지로 구분
                    if(message.what == 0 ){
                        //다른 작업이 없을 때 처리
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //데이터를 추가하고 리스트 뷰 다시 출력
                                evenDatas.add("even:"+data);
                                evenAdapter.notifyDataSetChanged();
                            }
                        });
                    }else{
                        //다른 작업이 없을 때 처리
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //데이터를 추가하고 리스트 뷰 다시 출력
                                oddDatas.add("odd:"+data);
                                oddAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            };
            //루퍼를 반복
            Looper.loop();
        }

    }
    //랜덤한 정수를 생성해서 onThread에게 메시지를 전송하는 스레드 클래스
    class TwoThread extends Thread{
        public void run(){
            Random r = new Random();
            for(int i = 0 ;i <10; i= i+1){
                SystemClock.sleep(100);
                int data = r.nextInt(20);
                Message message = new Message();
                if(data % 2 == 0){
                    message.what= 0;
                }else {
                    message.what = 1;
                }
                message.arg1 = data;
                //다른 스레드의 핸들러를 이용한것이다.
                oneThread.onehandler.sendMessage(message);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //생성한 루퍼 종료
        oneThread.onehandler.getLooper().quit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper_prec);

        oddDatas = new ArrayList<>();
        evenDatas = new ArrayList<>();

        oddAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,oddDatas);
        evenAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,evenDatas);

        ListView oddlistView = (ListView)findViewById(R.id.oddListView);
        ListView evneListView = (ListView)findViewById(R.id.evenListView);

        oddlistView.setAdapter(oddAdapter);
        evneListView.setAdapter(evenAdapter);

        handler = new Handler();

        oneThread = new OneThread();
        oneThread.start();

        twoThread = new TwoThread();
        twoThread.start();


    }
}
