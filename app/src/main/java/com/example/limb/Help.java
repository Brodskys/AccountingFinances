package com.example.limb;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
public class Help extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help); }
    public void onClick_Back(View v) {
        onBackPressed();
    }}
