package com.example.a503_25.a1017asyctaskserver;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainAcitivity2Prec extends AppCompatActivity {

    EditText ip,port,msg;
    TextView result;

    Handler handler = new Handler(){
        public void handleMessage(Message message){
            Toast.makeText(MainAcitivity2Prec.this,message.obj.toString(),Toast.LENGTH_LONG).show();
        }
    };

    class ThreadEx extends Thread{
        public void run(){
            String ipaddr = ip.getText().toString();
            String portnumber = port.getText().toString();
            String str = msg.getText().toString();

            try {
                Socket socket = new Socket(ipaddr, Integer.parseInt(portnumber));


                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                pw.println(str);
                pw.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Message message = new Message();
                message.obj = br.readLine();
                handler.sendMessage(message);

                pw.close();
                br.close();
                socket.close();



            }catch (Exception e){
                Log.e("예외:",e.getMessage());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_acitivity2_prec);

        ip = (EditText)findViewById(R.id.ip);
        port = (EditText)findViewById(R.id.port);
        msg = (EditText)findViewById(R.id.msg);
        result = (TextView)findViewById(R.id.result);


        ThreadEx ex = new ThreadEx();

        ex.start();
    }
}
