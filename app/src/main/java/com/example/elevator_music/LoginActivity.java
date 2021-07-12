package com.example.elevator_music;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    AccessToken accessToken;
    private static final int RC_SIGN_IN = 100;
    public static String name, userId, email;
    Button loginText;
    EditText et_id, et_password;
    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    Button btn_login;
    ImageButton googleLogin, githubLogin, fbLogin;
    private GoogleApiClient googleApiClient;
    CallbackManager callbackManager = CallbackManager.Factory.create();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }
    private void getFbUserName(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                (object, response) -> {
                    // Application code
                    try {
                        name = object.getString("name");
                        userId = auth.getUid();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();


    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "인증 실패", Toast.LENGTH_SHORT).show();
                    }else{
                        userId = auth.getUid();
                        name = auth.getCurrentUser().getDisplayName();
                        Toast.makeText(LoginActivity.this, "구글 로그인 인증 성공", Toast.LENGTH_SHORT).show();
                        LoginIntent();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("551704228898505");
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        fbLogin = findViewById(R.id.loginFb);
        googleLogin = findViewById(R.id.loginGoogle);
        et_id = findViewById(R.id.loginId);
        et_password = findViewById(R.id.loginPwd);
        btn_login = findViewById(R.id.loginEnter);
        loginText = findViewById(R.id.loginText);
        githubLogin=findViewById(R.id.loginGithub);
        userId = auth.getUid();
        if (userId!=null){
            LoginIntent();
        }
        fbLogin.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                    accessToken = loginResult.getAccessToken();
                    getFbUserName(accessToken);
                }

                @Override
                public void onCancel() {
                    Log.e("FbLogin", "onCancel: cancel" );
                }

                @Override
                public void onError(FacebookException error) {
                    Log.w("FbLogin", "onError: Error",error  );
                }
            });
        });

        githubLogin.setOnClickListener(v -> {
            final OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
            provider.addCustomParameter("login", "your-email@gmail.com");
            List<String> scopes =
                    new ArrayList<String>() {
                        {
                            add("user:email");
                            add("read:user");
                        }
                    };
            provider.setScopes(scopes);
            Task<AuthResult> pendingResultTask = auth.getPendingAuthResult();
            if (pendingResultTask != null) {
                // There's something already here! Finish the sign-in for your user.
                pendingResultTask
                        .addOnSuccessListener(
                                authResult -> {
                                    Log.e("gitLogin", "onSuccess: ");
                                    name = authResult.getAdditionalUserInfo().getUsername();
                                    userId = authResult.getUser().getUid();
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    LoginIntent();
                                })
                        .addOnFailureListener(
                                e -> {
                                });
            }
            else {
                auth
                        .startActivityForSignInWithProvider(LoginActivity.this, provider.build())
                        .addOnSuccessListener(
                                authResult -> {
                                    Log.e("gitLogin", "onSuccess: ");
                                    name = authResult.getAdditionalUserInfo().getUsername();
                                    userId = authResult.getUser().getUid();
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    LoginIntent();
                                })
                        .addOnFailureListener(
                                e -> {
                                    Log.w("gitLogin", "onFailure: ", e);
                                    auth.getCurrentUser().startActivityForLinkWithProvider(LoginActivity.this, provider.build())
                                            .addOnSuccessListener(
                                                    new OnSuccessListener<AuthResult>() {
                                                        @Override
                                                        public void onSuccess(AuthResult authResult) {
                                                            Log.e("linkWith", "onSuccess: " );
                                                            // GitHub credential is linked to the current user.
                                                            // IdP data available in
                                                            // authResult.getAdditionalUserInfo().getProfile().
                                                            // The OAuth access token can also be retrieved:
                                                            // authResult.getCredential().getAccessToken().
                                                            Toast.makeText(LoginActivity.this, "성공" ,Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                            .addOnFailureListener(
                                                    e1 -> {
                                                        // Handle failure.
                                                        Log.w("withProvider", "onFailure: ", e1);
                                                    });

                                });
            }
        });
        // Callback registration

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
            startActivity(intent);
        });
        googleLogin.setOnClickListener(v -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        btn_login.setOnClickListener(v -> {
            if (!et_password.getText().toString().equals("") || !et_id.getText().toString().equals("")) {
                auth.signInWithEmailAndPassword(et_id.getText().toString(), et_password.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.e("Login", "onComplete: Success" );
                                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                LoginIntent();
                            }
                            else{
                                Log.e("Login", "onComplete: Fail");
                                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else{
                Toast.makeText(LoginActivity.this, "빈칸이 있습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void handleFacebookAccessToken(final AccessToken token) {
        Log.e("handleFacebook", "handleFacebookAccessToken:" + token );

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("FbSignIn", "signInWithCredential:success " + auth.getCurrentUser());
                        name = auth.getCurrentUser().getDisplayName();
                        userId = auth.getUid();
                        LoginIntent();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("FbSignIn", "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                    // ...
                });
    }
    public void LoginIntent(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Log.e("tag", "LoginIntent: "+auth.getCurrentUser().getDisplayName() );
        intent.putExtra("email", auth.getCurrentUser().getEmail());
        intent.putExtra("name", auth.getCurrentUser().getDisplayName());
        intent.putExtra("userId", auth.getCurrentUser().getUid());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
