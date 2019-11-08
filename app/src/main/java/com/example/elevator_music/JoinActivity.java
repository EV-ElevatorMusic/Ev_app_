package com.example.elevator_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {
    FirebaseUser currentUser;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    EditText et_id, et_password, et_pwdCheck, et_name;
    TextView signUp;
    String p ="[\\w~\\-.]+@[\\w~\\-]+(\\.[\\w~\\-]+)+";
    final String TAG = "signUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        et_id = findViewById(R.id.joinId);
        et_password = findViewById(R.id.joinPwd);
        et_pwdCheck = findViewById(R.id.joinCheck);
        signUp = findViewById(R.id.joinEnter);
        et_name = findViewById(R.id.joinName);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: Clicked");
                if (et_password.getText().toString().equals(et_pwdCheck.getText().toString())) {
                    Log.e(TAG, "passCheck Success");
                    if (Pattern.matches(p, et_id.getText().toString()) && !et_password.getText().toString().equals("")) {
                        Log.e(TAG, "match Success" );
                        String signUp_Id = et_id.getText().toString();
                        String signUp_Password = et_password.getText().toString();
                        auth.createUserWithEmailAndPassword(signUp_Id, signUp_Password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Log.e("SignUp", "Success");
                                            currentUser = auth.getCurrentUser();
                                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(et_name.getText().toString()).build();
                                            currentUser.updateProfile(request);
                                        } else {
                                            Log.e("SignUp", "Failure", task.getException());
                                            Toast.makeText(JoinActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {
                        Log.e(TAG, "matchFailed"+Pattern.matches(p, et_id.getText().toString())+!et_password.getText().toString().equals(""));
                        Toast.makeText(JoinActivity.this, "이메일 형식이 다르거나 빈칸이 있습니다", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.e(TAG, "passCheck Fail");
                    Toast.makeText(JoinActivity.this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                    //Todo 비밀번호, 비밀번호 확인 다를시
                }
            }
        });
    }
}
