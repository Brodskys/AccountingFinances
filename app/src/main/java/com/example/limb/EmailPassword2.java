package com.example.limb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class EmailPassword2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private EditText mEmailText;
    private EditText mPasswordText;
    BlurView blurView;
    private static final String TAG = "myLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_email_password2);
        blurView =(BlurView)findViewById(R.id.blurView2);


    }
    final Handler handler = new Handler();
    final int delay = 7000;
    boolean period = false;



    public void Registration(View v) {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mEmailText = findViewById(R.id.email_text);
        mPasswordText = findViewById(R.id.password_text);
        final String email = mEmailText.getText().toString();
        final String password = mPasswordText.getText().toString();
        final String ctg2 = getString(R.string.registrationfailed);
        final String ctg3 = getString(R.string.pass2_text);
        final String ctg4 = getString(R.string.email2_text);
        final EditText til1 = findViewById(R.id.password_text);
        final EditText til2 = findViewById(R.id.email_text);


        if(mCurrentUser != null){
            if(!email.isEmpty() || !password.isEmpty()) {


                mAuth.fetchSignInMethodsForEmail(til2.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().size() == 0) {

                if (til1.getText().toString().length() >= 6) {


                AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                mCurrentUser.linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference mDatabaseRef = database.getReference();
                                            mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("login").setValue(email);
                                            mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("password").setValue(password);
                                            user.sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) { }
                                                        }
                                                    });
                                            blurBackgroud();
                                            EditText tt = findViewById(R.id.email_text);
                                            TextView ttt = findViewById(R.id.em_txt);
                                            String s = tt.getText().toString();
                                            ttt.setText(s);
                                            blurView.setVisibility(View.VISIBLE);
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    if (period == false) {
                                                        blurView.setVisibility(View.GONE);

                                                        period = true;

                                                        FirebaseAuth.getInstance().signOut();
                                                        startActivity(new Intent(getApplicationContext(), EmailPassword.class));
                                                        overridePendingTransition(0, 0);
                                                    }

                                                    handler.postDelayed(this, delay);
                                                }

                                            }, delay);


                                        } else {
                                            String ctg1 = getString(R.string.error);
                                            Toast.makeText(EmailPassword2.this, ctg1, Toast.LENGTH_LONG).show();
                                        }
                    }
                }); }
                else
                    Toast.makeText(EmailPassword2.this, ctg3, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(EmailPassword2.this, ctg4, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });


            }
        }
    }


        private void blurBackgroud() {
                        float radius = 10f;

                        View decorView = getWindow().getDecorView();
                        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);

                        Drawable windowBackground = decorView.getBackground();

                        blurView.setupWith(rootView)
                                .setFrameClearDrawable(windowBackground)
                                .setBlurAlgorithm(new RenderScriptBlur(this))
                                .setBlurRadius(radius)
                                .setHasFixedTransformationMatrix(true);
                    }

                    private void setLocale(String lang) {
                        Locale locale = new Locale(lang);
                        Locale.setDefault(locale);
                        Configuration configuration = new Configuration();
                        configuration.locale = locale;
                        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
                        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
                        editor.putString("My_Lang", lang);
                        editor.apply();
                    }

                    public void loadLocale() {
                        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                        String language = preferences.getString("My_Lang", "");
                        setLocale(language);
                        String lc = getString(R.string.location);
                        SharedPreferences.Editor editor = getSharedPreferences("Locale", MODE_PRIVATE).edit();
                        editor.putString("local", lc);
                        editor.apply();
                    }

                    public void onClick_Back(View view) {
                        onBackPressed();
                    }
                }