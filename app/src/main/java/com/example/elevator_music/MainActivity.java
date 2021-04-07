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
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    EditText chatEdit;
    ImageButton chatSend, openDrawer;
    String all_input, name, email;
    RecyclerView.Adapter adapter;
    RecyclerView rv;
    ArrayList<ChatItem> chatItems = new ArrayList<>();
    TextView userNameTv, userEmailTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        chatEdit = findViewById(R.id.chatEdit);
        chatSend = findViewById(R.id.chatSend);
        rv = findViewById(R.id.recyclerChat);
        openDrawer = findViewById(R.id.open_drawer);

        adapter = new ChattingRecyclerAdapter(chatItems);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_header_view = navigationView.getHeaderView(0);

        userEmailTv = nav_header_view.findViewById(R.id.drawer_user_email);
        userNameTv = nav_header_view.findViewById(R.id.drawer_user_name);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        userNameTv.setText(name);
        userEmailTv.setText(email);

        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.END);
            }
        });

        chatEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //Enter키눌렀을떄 처리
                    if (!chatEdit.getText().toString().equals("")) {
                        String text = chatEdit.getText().toString();
                        all_input = all_input + text;

                        String _uri = "/chatbot/?comment=" + all_input;

                        NetworkTask networkTask = new NetworkTask("http://13.124.31.235/", _uri);
                        networkTask.execute();
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
                String text = chatEdit.getText().toString();
                all_input = all_input + text;

                String _uri = "/chatbot/?comment=" + all_input;

                NetworkTask networkTask = new NetworkTask("http://13.124.31.235/", _uri);
                networkTask.execute();
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

        if (id == R.id.setting) {
            intent = new Intent(this, SettingActivity.class);
            startDrawerIntent(intent);
        } else if (id == R.id.mRanking) {
            intent = new Intent(this, RankingActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    public class RequestHttpURLConnection {

        public String request(String _url, String _uri) {

            HttpURLConnection urlConn = null;

            // URL 뒤에 붙여서 보낼 파라미터.
            _url += _uri;

            /**
             * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
             * */
            try {
                URL url = new URL(_url);
                urlConn = (HttpURLConnection) url.openConnection();

                // [2-1]. urlConn 설정.
                urlConn.setReadTimeout(10000);
                urlConn.setConnectTimeout(15000);
                urlConn.setRequestMethod("GET"); // URL 요청에 대한 메소드 설정 : GET/POST.
                urlConn.setDoInput(true);
                urlConn.setRequestProperty("Accept-Charset", "utf-8"); // Accept-Charset 설정.
                urlConn.setRequestProperty("Context_Type", "application/json");

                // [2-2]. parameter 전달 및 데이터 읽어오기.

                // [2-3]. 연결 요청 확인.
                // 실패 시 null을 리턴하고 메서드를 종료.
                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("responseCode", "request: " + urlConn.getResponseCode() + urlConn.getResponseMessage()) ;
                    return null;
                }
                Log.e("responseCode", "request: " + urlConn.getResponseCode() + urlConn.getResponseMessage()) ;

                // [2-4]. 읽어온 결과물 리턴.
                // 요청한 URL의 출력물을 BufferedReader로 받는다.
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

                // 출력물의 라인과 그 합에 대한 변수.
                String line;
                String page = "";

                // 라인을 받아와 합친다.
                while ((line = reader.readLine()) != null) {
                    page += line;
                }
                return page;

            } catch (MalformedURLException e) { // for URL.
                e.printStackTrace();
            } catch (IOException e) { // for openConnection().
                e.printStackTrace();
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }
            return null;
        }
    }
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        String values;

        NetworkTask(String url, String comment){
            this.url = url;
            this.values = comment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
            if(result!=null){
                JsonParser parser = new JsonParser();
                Object object = parser.parse(result);
                JsonObject jsonObject = (JsonObject)object;
                String comment = jsonObject.get("chat")+"";
                String text = chatEdit.getText().toString();
                chatItems.add(new ChatItem(0, text));
                adapter.notifyDataSetChanged();
                chatEdit.setText("");
                chatItems.add(new ChatItem(1, comment));
                adapter.notifyDataSetChanged();
            }
        }
    }

    void startDrawerIntent(Intent intent){
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        startActivity(intent);
    }



}
