package com.example.tweetfeel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tweetfeel.support.ApiClient;
import com.example.tweetfeel.support.ApiService;
import com.example.tweetfeel.support.userDataModel;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class LoginActivity extends AppCompatActivity {


    private EditText emailView;
    private EditText passwordView;
    private Button signInBtn;
    private Button signUpBtn;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        int exit = getIntent().getIntExtra("exit", -1);
        Log.e("exit", exit +" ");
        if(exit == 1)
        {
            finish();
        }
        else
        {
            SharedPreferences pref = getApplicationContext()
                    .getSharedPreferences("userDataModel", 0);
            SharedPreferences.Editor editor = pref.edit();


            if(pref.contains("username"))
            {
                Toast.makeText(getApplicationContext(), pref.getString("username",null),
                        Toast.LENGTH_SHORT).show();
                //sign user in
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
            else
            {
                Toast.makeText(getApplicationContext(), pref.getString("username",null),
                        Toast.LENGTH_SHORT).show();
            }


            setContentView(R.layout.activity_login);

            emailView = findViewById(R.id.username);
            passwordView = findViewById(R.id.password);
            signInBtn = findViewById(R.id.signinbtn);
            signUpBtn = findViewById(R.id.signUpBtn);
            emailView.setError(null);
            passwordView.setError(null);


            signInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = emailView.getText().toString();
                    String pass = passwordView.getText().toString();

                    Toast.makeText( LoginActivity.this,
                            email + " " + pass, Toast.LENGTH_SHORT).show();
                    attemptLogin();

                }
            });

            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });

        }



    }
    private void attemptLogin() {
        final String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            emailView.setError("Required field");
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError("Please enter valid email");
            focusView = emailView;
            cancel = true;
        }


        if(!TextUtils.isEmpty(password))
        {
          isPasswordValid(email, password);
        }
        else
        {
            passwordView.setError("Required field");
        }



        if (cancel) {
            focusView.requestFocus();
        }

    }

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }


    private void isPasswordValid(String email, String password)
    {
        progressBarRun();
        signInBtn.setVisibility(View.GONE);
        signUpBtn.setVisibility(View.GONE);

        Retrofit retrofit = new ApiClient().base();

        ApiService apiService = retrofit.create(ApiService.class);
        Single<userDataModel> user = apiService.login(email,password);
        user.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<userDataModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(userDataModel u) {
                        Log.e("Logging in", "Retrieving user data");

                        String statusCode = u.getStatus();

                        if(statusCode.equals("001"))
                        {
                            Log.e("id", u.getId()+"");
                            Log.e("username", u.getUsername()+"");
                            Log.e("email", u.getEmail()+"");
                            Log.e("keywords", u.getKeywords()+"");

                            Toast.makeText(LoginActivity.this,
                                "Login success", Toast.LENGTH_SHORT)
                                .show();

                            //store user data in sharedPreference
                            SharedPreferences pref = getSharedPreferences("userDataModel", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("id", u.getId());
                            editor.putString("username", u.getUsername());
                            editor.putString("email", u.getEmail());

                            Set<String> set = new HashSet<>(u.getKeywords());
                            editor.putStringSet("keywords",set);
                            editor.putInt("logout", 0);
                            editor.commit();

                            //go to activity
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }
                        else
                        {
                            emailView.setError("Error");
                            passwordView.setError(" Error");

                            Toast.makeText(LoginActivity.this,
                                    "Login failed!", Toast.LENGTH_SHORT)
                                    .show();

                            Log.e("Login", "failed!");

                            progressBar.setVisibility(View.GONE);
                            signInBtn.setVisibility(View.VISIBLE);
                            signUpBtn.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.toString());
                        Toast.makeText(LoginActivity.this,
                                "Login failed", Toast.LENGTH_SHORT)
                                .show();

                        progressBar.setVisibility(View.GONE);
                        signInBtn.setVisibility(View.VISIBLE);
                    }
                });

    }


    private void progressBarRun() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, 30000);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
