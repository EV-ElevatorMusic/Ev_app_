package com.example.elevator_music;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_music.Retrofit.Data;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    EditText chatEdit;
    ImageButton chatSend;
    ImageButton openDrawer;
    ImageButton logoutBtn;
    String all_input, name, email;
    RecyclerView.Adapter adapter;
    RecyclerView rv;
    ArrayList<ChatItem> chatItems = new ArrayList<>();
    TextView userNameTv;
    TextView userEmailTv;
    static ArrayList<Data> likedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        chatEdit = findViewById(R.id.chatEdit);
        chatSend = findViewById(R.id.chatSend);
        rv = findViewById(R.id.recyclerChat);
        openDrawer = findViewById(R.id.open_drawer);

        adapter = new ChattingRecyclerAdapter(chatItems, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_header_view = navigationView.getHeaderView(0);

        TextView logoutTv = nav_header_view.findViewById(R.id.log_out_tv);
        userEmailTv = nav_header_view.findViewById(R.id.drawer_user_email);
        userNameTv = nav_header_view.findViewById(R.id.drawer_user_name);
        logoutBtn = nav_header_view.findViewById(R.id.log_out_btn);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("likedList", "");
        if (json.equals("")){
            likedList = new ArrayList<>();
        }else{
            Type type = new TypeToken<ArrayList<Data>>() {
            }.getType();
            likedList = gson.fromJson(json, type);
        }


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        userNameTv.setText(name);
        userEmailTv.setText(email);

        logoutBtn.setOnClickListener(view -> {
            LoginActivity.auth.signOut();
            Intent logoutIntent = new Intent(getApplicationContext(), EnterActivity.class);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logoutIntent);
        });


        openDrawer.setOnClickListener(v -> drawer.openDrawer(Gravity.END));

        chatEdit.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (!chatEdit.getText().toString().equals("")) {
                    String text = chatEdit.getText().toString();
                    chatItems.add(new ChatItem(0, text));
                    chatEdit.setText("");
                    all_input += text;

                    String _uri = "/chatbot/?comment=" + all_input;

                    NetworkTask networkTask = new NetworkTask("http://34.64.94.120/", _uri);
                    networkTask.execute();
                    return true;
                }
                return true;
            }
            return false;
        });

        chatSend.setOnClickListener(v -> {
            String text = chatEdit.getText().toString();
            if (text.equals("")) Toast.makeText(this, "대화를 입력해주세요", Toast.LENGTH_SHORT).show();

            else{
                all_input = all_input + text;
                String _uri = "/chatbot/?comment=" + all_input;

                chatItems.add(new ChatItem(0, text));
                adapter.notifyDataSetChanged();
                chatEdit.setText("");

                NetworkTask networkTask = new NetworkTask("http://34.64.94.120/", _uri);
                networkTask.execute();
                chatSend.setEnabled(false);
                new Handler().postDelayed(() -> chatSend.setEnabled(true), 500);
            }

        });


        logoutTv.setOnClickListener(v -> {
            LoginActivity.auth.signOut();
            Intent logoutIntent = new Intent(getApplicationContext(), EnterActivity.class);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logoutIntent);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;


        if (id == R.id.mRanking) {
            intent = new Intent(this, RankingActivity.class);
        }
        else{
            intent = new Intent(this, LikedMusicActivity.class);
        }
        startActivity(intent);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    public class RequestHttpURLConnection {

        public String request(String _url, String _uri) {

            HttpURLConnection urlConn = null;

            _url += _uri;

            try {
                URL url = new URL(_url);
                urlConn = (HttpURLConnection) url.openConnection();

                urlConn.setReadTimeout(10000);
                urlConn.setConnectTimeout(15000);
                urlConn.setRequestMethod("GET");
                urlConn.setDoInput(true);
                urlConn.setRequestProperty("Connection", "close");


                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("responseCode", "request: " + urlConn.getResponseCode() + urlConn.getResponseMessage()) ;
                    return null;
                }
                Log.e("responseCode", "request: " + urlConn.getResponseCode() + urlConn.getResponseMessage()) ;

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

                String line;
                String page = "";

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
                String responsetype = jsonObject.get("responsetype") + "";
                responsetype = responsetype.replaceAll("\"", "");
                if (responsetype.equals("music")){
                    jsonObject = (JsonObject) jsonObject.get("music");

                    String artist_name = jsonObject.get("artist_name")+"";
                    artist_name = artist_name.replaceAll("\"", "");

                    String cover_img = jsonObject.get("cover_img")+"";
                    cover_img = cover_img.replaceAll("\"", "");

                    String music_name = jsonObject.get("music_name")+"";
                    music_name = music_name.replaceAll("\"", "");

                    String preview_url = jsonObject.get("preview_url")+"";
                    preview_url = preview_url.replaceAll("\"", "");

                    chatItems.add(new ChatItem(1, "이런 노래는 어떠신가요?"));
                    chatItems.add(new ChatItem(2, artist_name, cover_img, music_name, preview_url));
                }else{
                    String comment = jsonObject.get("chat")+"";
                    comment = comment.replaceAll("\"", "");
                    Log.e("tag", "onPostExecute: "+ jsonObject.get("responsetype") );
                    chatItems.add(new ChatItem(1, comment));

                }
                adapter.notifyItemInserted(chatItems.size());
            }
        }
    }

    void startDrawerIntent(Intent intent){
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        startActivity(intent);
    }



}
