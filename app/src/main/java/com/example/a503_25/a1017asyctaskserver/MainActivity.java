package com.example.a503_25.a1017asyctaskserver;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

class AsyncTaskEx extends AsyncTask<Integer, Integer,Integer>{

    //MainAtivity의 데이터를 저장하기 위한 변수
    ProgressBar progressBar;
    TextView textView;
    int value;

    //생성자 - MainActivity에서 데이터를 넘겨받기 위해 생성 // 밖에 클래스를 만들면 이렇게 바깥에는 생성자를 생성해줘서
    // 해주면 된다.
    public AsyncTaskEx(ProgressBar progressBar, TextView textView, int value){
        this.progressBar = progressBar;
        this.textView = textView;
        this.value = value;
        Log.e("생성자","인스턴스 생성됨" );
    }

    @Override
    //인스턴스가 생성되면 가장 먼저 호출되는 메소드
    //메인 스레드에서 수행 : 화면을 갱신하는 코드를 작성해도 됩니다.
    protected void onPreExecute() {
        Log.e("초기화메소드","처음실행");
        super.onPreExecute();
        //UI 초기화를 여기서 해준다.
        value = 0;
        progressBar.setProgress(value);
        Log.e("초기화메소드","마지막 실행");
    }

    @Override
    //비동기적인 작업을 처리하는 메소드
    // 매개변수는 클래스를 생성할 때 적용한 제너릭 두번째 자료형과 일치해야 합니다.( 두번째 자료형과 매개변수 자료형이 같아야함)
    //리턴 타입은 세번째 자료형과 일치해야 합니다.
    //메인 스레드에서 동작하지 않음
    //UI 갱신하는 코드는 작성할 수 없음 ( 여기서는 프로그래스바랑 텍스트뷰는 올수 없음)
    protected Integer doInBackground(Integer... integers) {
        while (isCancelled() == false){
            value = value + 1;
            if(value >= 100){
                break;
            }else {
                //onProgressUpdate 호출
                publishProgress(value);
            }
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                Log.e("문제발생 :",e.getMessage());
            }
        }
        return value;
    }


    @Override
    //doInBackground에서 publishProgress를 호출하면 자동으로 호출되는 메소드
    // 메인스레드에서 실행
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(value);
        textView.setText(String.format("value 현재 값 = %d",value));
    }


    @Override
    //인스턴스가 cancelled를 호출했을 때 호출되는 메소드
    //메인스레드에서 실행
    protected void onCancelled(Integer integer) {
        super.onCancelled(integer);
        textView.setText("스레드 중지");
    }


    @Override
    // doInBackground가 작업을 종료했을 때 호출되는 메소드
    //메인스레드에서 실행
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        //value = 0;
        //progressBar.setProgress(value);
        textView.setText("스레드 완료");

    }




}


public class MainActivity extends AppCompatActivity {
    //프로그래스 바와 텍스트 뷰 변수 선언
    ProgressBar progressBar;
    TextView textView;
    //프로그래스 바의 값을 표시할 정수 변수 선언
    int value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = (Button)findViewById(R.id.start);
        Button end = (Button)findViewById(R.id.end);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        textView = (TextView)findViewById(R.id.textView);

        View.OnClickListener listener = new View.OnClickListener() {
            AsyncTaskEx task = null;
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.start:
                        task = new AsyncTaskEx(progressBar,textView,value);
                        task.execute(100);
                        break;
                    case R.id.end:
                        task.cancel(true);
                        break;
                }
            }
        };
        start.setOnClickListener(listener);
        end.setOnClickListener(listener);
    }
}
