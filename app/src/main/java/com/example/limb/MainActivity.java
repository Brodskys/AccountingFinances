package com.example.limb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import java.util.Locale;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    BlurView blurView;

    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("Currency", MODE_PRIVATE);
        String value = prefs.getString("btnText", "BYN");



        if (user != null) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef = myRef.child("Users").child(user.getUid()).child("Authentication").child("balance");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value =  dataSnapshot.getValue(String.class);
                    TextView bl = (TextView) findViewById(R.id.balance_txt);
                    bl.setText(value); }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        }
        else {
            finish();
            Intent intent = new Intent(MainActivity.this,EmailPassword.class);
            startActivity(intent);
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.accounts);
        TextView b2 = (TextView) findViewById(R.id.curency_txt);
        b2.setText(value);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.accounts:
                        return true;
                    case R.id.add_operation:
                        startActivity(new Intent(getApplicationContext(), AddOperation.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.statistics:
                        startActivity(new Intent(getApplicationContext(), Menu.class));
                        overridePendingTransition(0, 0);
                        return true; }
                return false; }});
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_books);
        new FirebaseDatabaseHelper().readOperations(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Operations_add> operationsadds, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView,MainActivity.this, operationsadds,keys); }
            @Override
            public void DataIsInserted() { }
            @Override
            public void DataIsUpdated() { }
            @Override
            public void DataIsDeleted() { }});
        final EditText editText = (EditText)findViewById(R.id.balance_txt2);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, int count) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                int position = str.indexOf(".");
                if (position != -1) {
                    String subStr = str.substring(position);
                    String subStrStart = str.substring(0, position);
                    if (subStr.length() > 3 || subStrStart.length() == 0) {
                        editable.delete(editable.length() - 1, editable.length()); } } }}); }
    int raz =1;
    public void onClick(View view){
        TextView bl = (TextView) findViewById(R.id.balance_txt);
        final EditText editText = (EditText)findViewById(R.id.balance_txt2);
        String s1 = bl.getText().toString();
        String s2 = editText.getText().toString();
        if (raz==1) {
            bl.setVisibility(View.INVISIBLE);
            editText.setEnabled(true);
            editText.setFocusable(true);
            editText.setText(s1);
            editText.setVisibility(View.VISIBLE);
            editText.requestFocus();
            editText.setSelection(editText.getText().length()); }
        if(raz==2){
            bl.setVisibility(View.VISIBLE);
            bl.setText(s2);
            editText.setVisibility(View.INVISIBLE);
            raz =0;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabaseRef = database.getReference();
            mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("balance").setValue(s2);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }raz++; }



    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply(); }
    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang","");
        setLocale(language);
        String lc = getString(R.string.location);
        SharedPreferences.Editor editor = getSharedPreferences("Locale", MODE_PRIVATE).edit();
        editor.putString("local", lc);
        editor.apply(); }
    @Override
    public void onBackPressed(){
        this.finishAffinity();
    }}



