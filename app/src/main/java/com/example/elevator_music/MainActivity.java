package com.example.elevator_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    EditText chatEdit;
    ImageButton chatSend;
    String all_input, emotion;
    RecyclerView.Adapter adapter;
    RecyclerView rv;
    Komoran komoran=new Komoran(DEFAULT_MODEL.FULL);
    final List<String> they=new ArrayList<>();
    ArrayList<ChatItem> chatItems = new ArrayList<>();
    String output;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatEdit = findViewById(R.id.chatEdit);
        chatSend = findViewById(R.id.chatSend);
        rv = findViewById(R.id.recyclerChat);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        adapter = new ChattingRecyclerAdapter(chatItems);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        set_they();
        chatEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //Enter키눌렀을떄 처리
                    if (!chatEdit.getText().toString().equals("")) {
                        String text = chatEdit.getText().toString();
                        all_input = all_input + text;
                        emotion = emotion_predict(all_input);
                        new Thread() {
                            public void run() {
                                String text = chatEdit.getText().toString();
                                output = post(text);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String text = chatEdit.getText().toString();
                                        chatItems.add(new ChatItem(0, text));
                                        adapter.notifyDataSetChanged();
                                        chatEdit.setText("");
                                        chatItems.add(new ChatItem(1, output));
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }.start();
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });

        chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=chatEdit.getText().toString();
                all_input=all_input+text;
                emotion=emotion_predict(all_input);
                new Thread() {
                    public void run() {
                        String text=chatEdit.getText().toString();
                        output=post(text);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String text=chatEdit.getText().toString();
                                chatItems.add(new ChatItem(0, text));
                                adapter.notifyDataSetChanged();
                                chatEdit.setText("");
                                chatItems.add(new ChatItem(1, output));
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }.start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.END);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;

        if (id == R.id.chatting) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.mRanking) {
            intent = new Intent(this, RankingActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    private void set_they() {

        BufferedReader bReader = null;
        InputStream is = getBaseContext().getResources().openRawResource(R.raw.data);
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            while (br.ready()) {
                they.add(br.readLine());
            }

            for (String str : they)
                System.out.println(str);
        } catch (IOException e) {
            System.out.println("에러");
        }
    }

    //text를 입력하여 그 입력에 맞는 감정 화남 기쁨 슬픔을 반환
    private String emotion_predict(String text) {
        float inputs[][] = new float[1][147];
        for (int i = 0; i < 147; i++) {
            inputs[0][i] = 0;
        }

        KomoranResult result = komoran.analyze(text);
        List<Token> tokenList;
        try {
            tokenList = result.getTokenList();
        } catch (Exception e) {
            return "에러";

        }

        for (Token token : tokenList) {
            if (token.getPos().equals("EC") || token.getPos().equals("ETM")) {
                System.out.println("별로");
            } else {
                System.out.println(token.getMorph());
                for (int i = 0; i < 147; i++) {
                    if (they.get(i).equals(token.getMorph()))
                        inputs[0][i] = 1f;
                }
            }

        }
        for (int i = 0; i < 147; i++) {
            System.out.println(inputs[0][i]);
        }

        float[][] output = new float[1][3];
        Interpreter tflite = getTfliteInterpreter("model.tflite");
        tflite.run(inputs, output);

        int big = 0;
        for (int i = 0; i < 3; i++) {
            System.out.println(output[0][i]);
            if (output[0][big] < output[0][i]) {

                big = i;
            }
        }
        if (big == 0) {
            text = "화남";
        } else if (big == 1) {
            text = "슬픔";
        } else {
            text = "기쁨";
        }

        return text;

    }

    // 모델 파일 인터프리터를 생성하는 공통 함수
    // loadModelFile 함수에 예외가 포함되어 있기 때문에 반드시 try, catch 블록이 필요하다.
    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(MainActivity.this, modelPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 모델을 읽어오는 함수로, 텐서플로 라이트 홈페이지에 있다.
    // MappedByteBuffer 바이트 버퍼를 Interpreter 객체에 전달하면 모델 해석을 할 수 있다.
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    //레트로핏 이용(미완성)애매함
    public String get_output(String text) {

        final String asd;
        NetworkHelper.getInstence().get_Weather_retrofit("20150910", text, "ko", "민준").enqueue(new Callback<ChatItem>() {
            @Override
            public void onResponse(Call<ChatItem> call, Response<ChatItem> response) {

                if (response.isSuccessful()) {
                    System.out.println("성공");
                    System.out.println(response.body().result);

                    try {
                        JSONObject as = new JSONObject(response.body().result.get("fulfillment").toString());
                        String output = as.get("speech").toString();
                        System.out.println(output);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println(response.errorBody());
                    System.out.println(response.raw());
                    System.out.println(response.headers());
                    System.out.println("실패");
                }
            }


            @Override
            public void onFailure(Call<ChatItem> call, Throwable t) {
                System.out.println(t.fillInStackTrace());
                System.out.println(t.toString());
                System.out.println("실패..");
            }


        });
        String send = chatEdit.getText().toString();
        return send;

        //return re;
    }


    //챗본 반응 가져온후 반환
    //httpurlconnection 이용 완성
    public String post(String text) {
        try {
            URL url = new URL("https://api.dialogflow.com/v1/query?v=20150910");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
            con.addRequestProperty("Authorization", "Bearer 957ea642f45647e98a071eaacd6b73bf"); //key값 설정

            con.setRequestMethod("POST");

            //json으로 message를 전달하고자 할 때
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoInput(true);
            con.setDoOutput(true); //POST 데이터를 OutputStream으로 넘겨 주겠다는 설정
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            text = text.replaceAll(" ", "");
            String parameters = "{lang:en,sessionId:12345,query:" + text + "}";
            System.out.println(parameters);
            wr.write(parameters);
            wr.flush();
            wr.close();

            StringBuilder sb = new StringBuilder();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //Stream을 처리해줘야 하는 귀찮음이 있음.
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                JSONObject js = new JSONObject(sb.toString());
                JSONObject ajs = new JSONObject(js.get("result").toString());
                JSONObject a = new JSONObject(ajs.get("fulfillment").toString());
                System.out.println("" + a.get("speech").toString());
                String answer = a.get("speech").toString();
                return answer;
            } else {
                System.out.println(con.getResponseMessage());
                return "통신 실패";
            }
        } catch (Exception e) {
            Log.w("오류", "post: ",e );
            return "오류";

        }
    }


}
