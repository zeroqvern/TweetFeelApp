package com.example.tweetfeel;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tweetfeel.support.ApiClient;
import com.example.tweetfeel.support.ApiService;
import com.example.tweetfeel.support.userDataModel;

import androidx.appcompat.app.AppCompatActivity;


import java.util.regex.Pattern;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText confirmPasswordView;
    private Button signUpBtn;
    private TextView loginLink;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameView = findViewById(R.id.name);
        emailView = findViewById(R.id.newuser);
        passwordView = findViewById(R.id.password);
        confirmPasswordView = findViewById(R.id.retypePass);
        signUpBtn = findViewById(R.id.signUpBtn);
        loginLink = findViewById(R.id.loginlink);

        emailView.setError(null);
        passwordView.setError(null);
        confirmPasswordView.setError(null);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRegister();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void getRegister() {
        final String username = nameView.getText().toString();
        final String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String conpassword = confirmPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        int[] allPass = {0,0};


        if (TextUtils.isEmpty(username)) {
            nameView.setError("Required field");
            focusView = nameView;
            cancel = true;
        }
        else
        {
            allPass[0] = 1;
        }

        if (TextUtils.isEmpty(email)) {
            emailView.setError("Required field");
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError("Please enter valid email");
            focusView = emailView;
            cancel = true;
        }
        else
        {
            allPass[1] = 1;
        }

        if (TextUtils.isEmpty(password)) {
            passwordView.setError("Required field");
            focusView = passwordView;
            cancel = true;
        }
        else
        {
            if (!password.equals(conpassword)) {
                confirmPasswordView.setError("Password doesn't match");
                focusView = confirmPasswordView;
                cancel = true;
            }
            else
            {

                if (!checkAllPass(allPass))
                {

                    signingUp(username, email, password);
                }
            }
        }


        if (cancel) {
            focusView.requestFocus();
        }
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private boolean checkAllPass(int[] arr)
    {
        for(int i = 0; i < arr.length; i++) {
            if (arr[i] == 0)
            {
                return true;
            }
        }
        return false;
    }

    private void signingUp(String username, String email, String password)
    {
        Log.e("email", email);
        Log.e("pass", password);
        progressBarRun();
        signUpBtn.setVisibility(View.GONE);
        loginLink.setVisibility(View.GONE);

        Retrofit retrofit = new ApiClient().base();

        ApiService apiService = retrofit.create(ApiService.class);
        Single<userDataModel> user = apiService.register(username, email, password);
        user.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<userDataModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(userDataModel u) {
                        Log.e("Signing in", "In progress");

                        String statusCode = u.getStatus();
                        Log.e("status", statusCode);


                        if(statusCode.equals("001"))
                        {
                            Toast.makeText(RegisterActivity.this,
                                    "Successfully signed up!", Toast.LENGTH_SHORT)
                                    .show();

                            //go to activity
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                        }
                        else if(statusCode.equals("002"))
                        {
                            emailView.setError("Error");
                            passwordView.setError("Error");

                            Toast.makeText(RegisterActivity.this,
                                    "Error signing up!", Toast.LENGTH_SHORT)
                                    .show();

                            Log.e("Sign up", "failed!");

                            progressBar.setVisibility(View.GONE);
                            signUpBtn.setVisibility(View.VISIBLE);
                            loginLink.setVisibility(View.VISIBLE);
                        }
                        else if(statusCode.equals("003"))
                        {
                            Toast.makeText(RegisterActivity.this,
                                    "Email already in use!", Toast.LENGTH_SHORT)
                                    .show();
                            emailView.setError("Email already in used");


                            signUpBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            loginLink.setVisibility(View.VISIBLE);

                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this,
                                    "Error signing up!", Toast.LENGTH_SHORT)
                                    .show();

                            progressBar.setVisibility(View.GONE);
                            signUpBtn.setVisibility(View.VISIBLE);
                            loginLink.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.toString());
                        Toast.makeText(RegisterActivity.this,
                                "Error signing up!", Toast.LENGTH_SHORT)
                                .show();

                        progressBar.setVisibility(View.GONE);
                        signUpBtn.setVisibility(View.VISIBLE);
                        loginLink.setVisibility(View.VISIBLE);
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
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("exit", 1);
        startActivity(intent);
        finish();
        return;
    }
}