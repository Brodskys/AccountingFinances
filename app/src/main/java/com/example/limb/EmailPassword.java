package com.example.limb;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Locale;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class EmailPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextInputLayout ETemail;
    private TextInputLayout ETpassword;


    DatabaseReference reference;
    Operations_add operationsadd;
    String email ;
    String password ;
    BlurView blurView;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_email_password);


        ETemail = (TextInputLayout) findViewById(R.id.et_email);
        ETpassword = (TextInputLayout) findViewById(R.id.et_password);
        TextView mEditInit = (TextView) findViewById(R.id.textView11);

        mEditInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themeAndLogo();
            }
        });
        TextView mEditInit2 = (TextView) findViewById(R.id.textView5);
        mEditInit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repass();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (!isConnected()) {
                    Intent intent = new Intent(EmailPassword.this,SplashScreen.class);
                    startActivity(intent);
                }



                if (user != null ) {
                    finish();
                    Intent intent = new Intent(EmailPassword.this,MainActivity.class);
                    startActivity(intent);
                } else {
                } }};
        FirebaseUser user = mAuth.getCurrentUser();


        try {


  if (user != null && user.isEmailVerified()|| user.isAnonymous() ) {
      finish();
      startActivity(new Intent(getApplicationContext(), MainActivity.class));
      overridePendingTransition(0, 0);
  }
        } catch (Throwable e) {
            e.printStackTrace(); }
    }


    public void repass() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        ETemail = (TextInputLayout) findViewById(R.id.et_email);
        try {
            auth.sendPasswordResetEmail(ETemail.getEditText().getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                String ctg1 = getString(R.string.passwordsendtoyouremail);
                                Toast.makeText(EmailPassword.this, ctg1, Toast.LENGTH_LONG).show();
                            } else {
                                String ctg1 = getString(R.string.error);
                                Toast.makeText(EmailPassword.this, ctg1, Toast.LENGTH_LONG).show();
                            } }});
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public void themeAndLogo() {
        email = ETemail.getEditText().getText().toString();
         password = ETpassword.getEditText().getText().toString();
        boolean b1= false;
        boolean b2 =false;
        final TextInputLayout til = (TextInputLayout) findViewById(R.id.et_email);
        TextInputLayout tiet = (TextInputLayout) findViewById(R.id.et_password);
        til.setErrorTextColor(ColorStateList.valueOf(Color.parseColor("#F00C0C")));
        til.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.parseColor("#F00C0C")));
        tiet.setErrorTextColor(ColorStateList.valueOf(Color.parseColor("#F00C0C")));
        tiet.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.parseColor("#F00C0C")));
        String ctg1 = getString(R.string.enteryouremailaddress);
        String ctg2 = getString(R.string.enterthepassword);
        if(til.getEditText().getText().toString().isEmpty() || til.getEditText().getText().toString().contains(" ")){
            til.setError(ctg1);
            b1= true;
        }else{
            til.setError(null);
            b1= false;
        }
        if(tiet.getEditText().getText().toString().isEmpty() || tiet.getEditText().getText().toString().contains(" ")){
            tiet.setError(ctg2);
            b2= true;
        }else{
            tiet.setError(null);
            b2= false;
        }
        if (b1 ==false && b2 ==false) {
            final String ctg33 = getString(R.string.pass2_text);
            final String ctg44 = getString(R.string.email2_text);
            final TextInputLayout tiet2 = (TextInputLayout) findViewById(R.id.et_password);
            final TextInputLayout til2 = (TextInputLayout) findViewById(R.id.et_email);


            mAuth.fetchSignInMethodsForEmail(til2.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if (task.getResult().getSignInMethods().size() == 0) {

                        if (tiet2.getEditText().getText().toString().length() >= 6) {


                            regist();



                        } else
                            Toast.makeText(EmailPassword.this, ctg33, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(EmailPassword.this, ctg44, Toast.LENGTH_SHORT).show();

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

    public void regist() {
        final String ctg22 = getString(R.string.registrationfailed);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    operationsadd = new Operations_add();
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Authentication");
                    operationsadd.setLogin(email.trim());
                    operationsadd.setPassword(password.trim());
                    reference.setValue(operationsadd);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mDatabaseRef = database.getReference();
                    mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("balance").setValue("0.00");
                    mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("expense").setValue("0.00");
                    mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("income").setValue("0.00");
                    mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("transfer").setValue("0.00");

                    TextView tt = findViewById(R.id.em_txt);
                    tt.setText(email);

                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    }
                                }
                            });
                    Verification();

                } else
                    Toast.makeText(EmailPassword.this, ctg22, Toast.LENGTH_SHORT).show();
            }});
    }


int rz=0;

    public void signin(View v)
    {
        rz++;
        final String email = ETemail.getEditText().getText().toString();
        final String password = ETpassword.getEditText().getText().toString();
        boolean b1= false;
        boolean b2 =false;
        String ctg1 = getString(R.string.enteryouremailaddress);
        String ctg2 = getString(R.string.enterthepassword);
        TextInputLayout til = (TextInputLayout) findViewById(R.id.et_email);
        TextInputLayout tiet = (TextInputLayout) findViewById(R.id.et_password);
        til.setErrorTextColor(ColorStateList.valueOf(Color.parseColor("#F00C0C")));
        til.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.parseColor("#F00C0C")));
        tiet.setErrorTextColor(ColorStateList.valueOf(Color.parseColor("#F00C0C")));
        tiet.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.parseColor("#F00C0C")));
        if(til.getEditText().getText().toString().isEmpty() || til.getEditText().getText().toString().contains(" ")){
            til.setError(ctg1);
            b1= true;
        }else{
            til.setError(null);
            b1= false;
        }
        if(tiet.getEditText().getText().toString().isEmpty() || tiet.getEditText().getText().toString().contains(" ")){
            tiet.setError(ctg2);
            b2= true;
        }else{
            tiet.setError(null);
            b2= false;
        }
        if (b1 ==false && b2 ==false) {
            final String ctg4 = getString(R.string.authorizationfailed);



            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                            if (user != null ) {
                                if (user.isEmailVerified()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference mDatabaseRef = database.getReference();
                                    mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("login").setValue(email);
                                    mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("password").setValue(password);
                                    Intent mainIntent = new Intent(EmailPassword.this, MainActivity.class);
                                    EmailPassword.this.startActivity(mainIntent);
                                    EmailPassword.this.finish();
                                } else {
                                    Toast.makeText(EmailPassword.this, "Подтвердите почту", Toast.LENGTH_SHORT).show();
                                    recreate();
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                        }
                                                    }
                                                });


                                }
                            }
                            else {
                                recreate();

                                FirebaseAuth.getInstance().getCurrentUser().reload()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            }
                                        });
                            }


                        } else {
                        final TextInputLayout til2 = (TextInputLayout) findViewById(R.id.et_email);
                        final String ctg22 = getString(R.string.user_text);

                        mAuth.fetchSignInMethodsForEmail(til2.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.getResult().getSignInMethods().size() == 0) {
                                    Toast.makeText(EmailPassword.this, ctg22, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EmailPassword.this, ctg4, Toast.LENGTH_SHORT).show();
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
            }); }

    }



    public void Anonymous(View v) {
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                final String ctg = getString(R.string.anon_text);
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Authentication");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mDatabaseRef = database.getReference();
                    mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("balance").setValue("0.00");
                    mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("expense").setValue("0.00");
                    mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("income").setValue("0.00");
                    mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("transfer").setValue("0.00");
                    Intent mainIntent = new Intent(EmailPassword.this, MainActivity.class);
                    EmailPassword.this.startActivity(mainIntent);
                    EmailPassword.this.finish();
                } else {
                    Toast.makeText(EmailPassword.this, ctg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    final Handler handler = new Handler();
    final int delay = 7000;
    boolean raz = false;


    public void Verification() {
        blurView =(BlurView)findViewById(R.id.blurView);
        final TextInputLayout t1 = (TextInputLayout) findViewById(R.id.et_email);
        final TextInputLayout t2 = (TextInputLayout) findViewById(R.id.et_password);
        final Button t3 = (Button) findViewById(R.id.btn_sign_in);
        final TextView t4 = (TextView)  findViewById(R.id.textView5);
        final TextView t5 = (TextView)  findViewById(R.id.textView17);
        final TextView t6 = (TextView)  findViewById(R.id.textView11);
        TextView tt = findViewById(R.id.em_txt);
        String sss = tt.getText().toString();
        if(sss.equals(""))
            try {
            tt.setText(user.getEmail());
            } catch (Throwable e) {
            e.printStackTrace();
            }
       t1.setEnabled(false);
        t2.setEnabled(false);
        t3.setEnabled(false);
        t4.setEnabled(false);
        t5.setEnabled(false);
        t6.setEnabled(false);
        blurView.setVisibility(View.VISIBLE);
        blurBackgroud();

        handler.postDelayed(new Runnable(){
            public void run() {

                if (raz == false) {
                    try {
                        blurView.setVisibility(View.GONE);
                        t1.setEnabled(true);
                            t2.setEnabled(true);
                            t3.setEnabled(true);
                            t4.setEnabled(true);
                            t5.setEnabled(true);
                            t6.setEnabled(true);
                            recreate();
                            raz = true;


                    } catch (Throwable e) {
                        e.printStackTrace();

                    }
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);
    }


    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }
    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang","");
        setLocale(language);
        String lc = getString(R.string.location);
        SharedPreferences.Editor editor = getSharedPreferences("Locale", MODE_PRIVATE).edit();
        editor.putString("local", lc);
        editor.apply();
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) { }
        return connected;
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

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }



}
