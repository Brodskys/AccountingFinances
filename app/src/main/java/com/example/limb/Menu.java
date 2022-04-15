package com.example.limb;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Locale;
public class Menu extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_menu);
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        if (user != null) { }
        else {
            finish();
            Intent intent = new Intent(Menu.this,EmailPassword.class);
            startActivity(intent);        }
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.statistics);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.accounts:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.add_operation:
                        startActivity(new Intent(getApplicationContext(),AddOperation.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.statistics:
                        return true; }
                return false; }}); }
    public void onClick1(View v) {
        showChangeLanguageDialog();
    }
    private void showChangeLanguageDialog() {
        final String[] listItems = {"Русский","English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Menu.this);
        String s = getString(R.string.language_text);
        mBuilder.setTitle(s);
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i==0){
                    setLocale("ru");
                    startActivity(new Intent(getApplicationContext(),Menu.class));
                    overridePendingTransition(0,0);
                    recreate(); }
                else if (i==1){
                    setLocale("en");
                    startActivity(new Intent(getApplicationContext(),Menu.class));
                    overridePendingTransition(0,0);
                    recreate(); }
                dialog.dismiss(); }});
        AlertDialog mDialog = mBuilder.create();
        mDialog.show(); }
    public void onClick2(View v) {
        final Button button =(Button)findViewById(R.id.button);
        final String[] listItems = {"BYN","RUB","USD","EUR"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Menu.this);
        String s = getString(R.string.currency_text);
        mBuilder.setTitle(s);
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String text =  "";
                if (i==0){
                    Button  button =(Button)findViewById(R.id.button);
                    text =  "BYN";
                    SharedPreferences.Editor editor = getSharedPreferences("Currency", MODE_PRIVATE).edit();
                    editor.putString("btnText", text);
                    editor.commit();
                    recreate(); }
                else if (i==1){
                    Button  button =(Button)findViewById(R.id.button);
                    text =  "RUB";
                    SharedPreferences.Editor editor = getSharedPreferences("Currency", MODE_PRIVATE).edit();
                    editor.putString("btnText", text);
                    editor.commit();
                    recreate(); }
                else if (i==2){
                    Button  button =(Button)findViewById(R.id.button);
                    text =  "USD";
                    SharedPreferences.Editor editor = getSharedPreferences("Currency", MODE_PRIVATE).edit();
                    editor.putString("btnText", text);
                    editor.commit();
                    recreate(); }
                else if (i==3){
                    Button  button =(Button)findViewById(R.id.button);
                    text =  "EUR";
                    SharedPreferences.Editor editor = getSharedPreferences("Currency", MODE_PRIVATE).edit();
                    editor.putString("btnText", text);
                    editor.commit();
                    recreate(); }
                dialog.dismiss(); }});
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();    }
    public void onClick3(View v) {
        String ctg1 = getString(R.string.abouttheprogram);
        String ctg2 = getString(R.string.abouttheprogram2);
        String ctg3 = getString(R.string.abouttheprogram3);
        String ctg4 = getString(R.string.abouttheprogram4);
        String ctg5 = getString(R.string.abouttheprogram5);
        String ctg6 = getString(R.string.abouttheprogram6);
        AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
        builder.setTitle(ctg1);
        builder.setIcon(R.drawable.report);
        builder.setMessage(ctg2 + "\n" + ctg3 + "\n" + ctg4+ "\n" + "\n" +ctg5+ "\n" +ctg6);
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); }});
            builder.show(); }
    public void onClick5(View v) {
        startActivity(new Intent(getApplicationContext(),Help.class));
        overridePendingTransition(0,0); }
    public void onClick6(View v) {
        String ctg1 = getString(R.string.areyousure);
        String ctg2 = getString(R.string.yes);
        String ctg3 = getString(R.string.no);
        AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
        builder.setMessage(ctg1).setCancelable(true).setPositiveButton(ctg2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference mDatabaseRef = database.getReference();
                                mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("balance").setValue("0.00");
                                mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("expense").setValue("0.00");
                                mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("income").setValue("0.00");
                                mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("transfer").setValue("0.00");
                                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Operations");
                                mDatabaseRef.removeValue();
                            }}).setNeutralButton(ctg3, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel(); }});
        builder.show(); }
    public void onClick4(View v) {
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database1.getReference();
        myRef1 = myRef1.child("Users").child(user.getUid()).child("Authentication").child("expense");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String expense = dataSnapshot.getValue(String.class);
                SharedPreferences.Editor editor = getSharedPreferences("expense", MODE_PRIVATE).edit();
                editor.putString("expense", expense);
                editor.apply(); }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
        FirebaseUser user2 = mAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database2.getReference();
        myRef2 = myRef2.child("Users").child(user2.getUid()).child("Authentication").child("income");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String income = dataSnapshot.getValue(String.class);
                SharedPreferences.Editor editor = getSharedPreferences("income", MODE_PRIVATE).edit();
                editor.putString("income", income);
                editor.apply(); }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
        FirebaseUser user3 = mAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        DatabaseReference myRef3 = database3.getReference();
        myRef3 = myRef3.child("Users").child(user3.getUid()).child("Authentication").child("transfer");
        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String transfer = dataSnapshot.getValue(String.class);
                SharedPreferences.Editor editor = getSharedPreferences("transfer", MODE_PRIVATE).edit();
                editor.putString("transfer", transfer);
                editor.apply(); }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
        recreate();
        startActivity(new Intent(getApplicationContext(),Statistics.class));
        overridePendingTransition(0,0); }
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
    public void onClick_profile(View v) {
        Intent intent = new Intent(this,Profile.class);
        this.startActivity(intent); }
}
