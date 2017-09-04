package com.example.a53125.sockettest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private String ip;
    private Socket socket;
    private EditText edit_connect;
    private EditText edit_chat;
    private Button connect;
    private Button chat;
    private TextView text;
    private BufferedReader reader;
    private BufferedWriter writer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_chat = (EditText) findViewById(R.id.edit_chat);
        edit_connect = (EditText) findViewById(R.id.edit_connect);
        connect = (Button) findViewById(R.id.connect);
        chat = (Button) findViewById(R.id.chat);
        text = (TextView) findViewById(R.id.text);


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
                System.out.println("<<<<<<<<<<<<<<<<<<<<");
                ip = edit_connect.getText().toString();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.append("我说:"+edit_chat.getText().toString()+"\n");
                send();
                edit_chat.setText("");
            }
        });

    }

    private void connect() {
        final AsyncTask<Void,String,Void> read = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    socket = new Socket("10.0.2.2",12345);
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>");
//                    Toast.makeText(MainActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                    publishProgress("@success");
                    String str;
                    while((str = reader.readLine())!= null){
                        publishProgress(str);
                    }
                } catch (IOException e) {
                    System.out.println(">");
//                    Toast.makeText(MainActivity.this,"连接失败",Toast.LENGTH_SHORT).show();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if(values[0].equals("@success")){
                    Toast.makeText(MainActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                }
                text.append("别人说:"+values[0]+"\n");
            }
        }.execute();
    }

    private void send() {
        new Thread(){
            @Override
            public void run() {
                try {
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(edit_chat.getText().toString()+"\n");
                    writer.flush();


//            System.out.println(edit_chat.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
