package com.example.limb;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class OperationDetails extends AppCompatActivity {
        private final int SPLASH_DISPLAY_LENGHT = 10;
        public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
        private List<Categories> states = new ArrayList();
        ListView countryList;
        EditText data_in;
        EditText timer_in;
        private FirebaseAuth mAuth;
        String operation;
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        private TextView Mopertion;
        private EditText Msumm;
        private Button Mcategory;
        private EditText Mdate;
        private EditText Mtime;
        private EditText Mdetails;
        private Button Mgeo;
        private Button MUpdate_btn;
        private ImageView MDelete_btn;
    private String key;
    private String name_operation;
    private String summ;
    private String category;
    private String date;
    private String time;
    private String details;
    private String geo;
    private int raz1 = 1;
    private int raz2 = 1;
    private int raz3 = 1;
    private int rz1 = 1;
    private int rz2 = 1;
    private int rz3 = 1;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_operation_details);
            key = getIntent().getStringExtra("key");
            name_operation = getIntent().getStringExtra("Name_operation");
            summ = getIntent().getStringExtra("Summ");
            category = getIntent().getStringExtra("Categoty");
            date = getIntent().getStringExtra("Date");
            time = getIntent().getStringExtra("Time");
            details = getIntent().getStringExtra("Detailed");
            geo = getIntent().getStringExtra("Geo");
            Mopertion = (TextView)findViewById(R.id.textView10);
            Mopertion.setText(name_operation);
            Msumm = (EditText)findViewById(R.id.etWidth1);
            Msumm.setText(summ);
            Mcategory =(Button)findViewById(R.id.button3);
            Mcategory.setText(category);
            Mdate = (EditText)findViewById(R.id.date_edit);
            Mdate.setText(date);
            Mtime = (EditText)findViewById(R.id.time_edit);
            Mtime.setText(time);
            Mdetails = (EditText)findViewById(R.id.edit_text);
            Mdetails.setText(details);
            Mgeo =(Button)findViewById(R.id.button4);
            Mgeo.setText(geo);
            MUpdate_btn =(Button)findViewById(R.id.update_btn);
            MDelete_btn =(ImageView)findViewById(R.id.delete_btn);
            MUpdate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Operations_add operations_add = new Operations_add();
                    Date currentDate ;
                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    try {
                        currentDate = dateFormat.parse(Mdate.getText().toString());
                        operations_add.setDate(currentDate);
                    } catch (ParseException e) {
                        e.printStackTrace(); }
                    Date tm ;
                    DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    try {
                        tm = timeFormat.parse(timer_in.getText().toString());
                        operations_add.setTime(tm);
                    } catch (ParseException e) {
                        e.printStackTrace(); }
                    operations_add.setName_operation(Mopertion.getText().toString());
                    operations_add.setSumm(Float.parseFloat (Msumm.getText().toString()));
                    operations_add.setCategoty(Mcategory.getText().toString());
                    operations_add.setDetailed(Mdetails.getText().toString());
                    operations_add.setGeo(Mgeo.getText().toString());
                    new FirebaseDatabaseHelper().updateOperations(key, operations_add, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Operations_add> operationsadds, List<String> keys) { }
                        @Override
                        public void DataIsInserted() { }
                        @Override
                        public void DataIsUpdated() {
                            onBackPressed(); }
                        @Override
                        public void DataIsDeleted() { }});}});
            MDelete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FirebaseDatabaseHelper().deleteOperations(key, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Operations_add> operationsadds, List<String> keys) { }
                        @Override
                        public void DataIsInserted() { }
                        @Override
                        public void DataIsUpdated() { }
                        @Override
                        public void DataIsDeleted() {
                            TextView txt = (TextView)findViewById(R.id.textView10);
                            final String str = txt.getText().toString();
                            String st1 = getString(R.string.expense_text);
                            String st2 = getString(R.string.income_text);
                            String st3 = getString(R.string.transfer_text);
                            EditText txt2 = (EditText)findViewById(R.id.etWidth1);
                            final String str2 = txt2.getText().toString();
                                if (str.equals(st1)){
                                final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                                DatabaseReference myRef2 = database2.getReference();
                                myRef2 = myRef2.child("Users").child(user.getUid()).child("Authentication").child("expense");
                                myRef2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String expense = dataSnapshot.getValue(String.class);
                                        expense = expense.replaceAll(",", ".");
                                        Double s1 = Double.parseDouble(expense);
                                        Double sum = Double.parseDouble(str2.trim());
                                        Double summ1 = s1 - sum;
                                        String ss1 = String.format("%.2f", summ1).replaceAll(",", ".");
                                        while (raz1==1){
                                            DatabaseReference mDatabaseRef2 = database2.getReference();
                                            mDatabaseRef2.child("Users").child(user.getUid()).child("Authentication").child("expense").setValue(ss1);
                                            raz1++; } }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }});
                                    final FirebaseDatabase database3 = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef3 = database3.getReference();
                                    myRef3 = myRef3.child("Users").child(user.getUid()).child("Authentication").child("balance");
                                    myRef3.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String balance = dataSnapshot.getValue(String.class);
                                            balance = balance.replaceAll(",", ".");
                                            Double s2 = Double.parseDouble(balance);
                                            Double sum2 = Double.parseDouble(str2.trim());
                                            Double summ1 = s2 + sum2;
                                            String ss2 = String.format("%.2f", summ1).replaceAll(",", ".");
                                            while (rz1==1){
                                                DatabaseReference mDatabaseRef4 = database3.getReference();
                                                mDatabaseRef4.child("Users").child(user.getUid()).child("Authentication").child("balance").setValue(ss2);
                                                rz1++; } }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }}); }
                            if (str.equals(st2)){
                                final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                                DatabaseReference myRef2 = database2.getReference();
                                myRef2 = myRef2.child("Users").child(user.getUid()).child("Authentication").child("income");
                                myRef2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String expense = dataSnapshot.getValue(String.class);
                                        expense = expense.replaceAll(",", ".");
                                        Double s1 = Double.parseDouble(expense);
                                        Double sum = Double.parseDouble(str2.trim());
                                        Double summ1 = s1 - sum;
                                        String ss1 = String.format("%.2f", summ1).replaceAll(",", ".");
                                        while (raz2==1){
                                            DatabaseReference mDatabaseRef2 = database2.getReference();
                                            mDatabaseRef2.child("Users").child(user.getUid()).child("Authentication").child("income").setValue(ss1);
                                            raz2++; } }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }});
                                final FirebaseDatabase database3 = FirebaseDatabase.getInstance();
                                DatabaseReference myRef3 = database3.getReference();
                                myRef3 = myRef3.child("Users").child(user.getUid()).child("Authentication").child("balance");
                                myRef3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String balance = dataSnapshot.getValue(String.class);
                                        balance = balance.replaceAll(",", ".");
                                        Double s2 = Double.parseDouble(balance);
                                        Double sum2 = Double.parseDouble(str2.trim());
                                        Double summ1 = s2 - sum2;
                                        String ss2 = String.format("%.2f", summ1).replaceAll(",", ".");
                                        while (rz2==1){
                                            DatabaseReference mDatabaseRef4 = database3.getReference();
                                            mDatabaseRef4.child("Users").child(user.getUid()).child("Authentication").child("balance").setValue(ss2);
                                            rz2++; } }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }}); }
                            if (str.equals(st3)){
                                final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                                DatabaseReference myRef2 = database2.getReference();
                                myRef2 = myRef2.child("Users").child(user.getUid()).child("Authentication").child("transfer");
                                myRef2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String expense = dataSnapshot.getValue(String.class);
                                        expense = expense.replaceAll(",", ".");
                                        Double s1 = Double.parseDouble(expense);
                                        Double sum = Double.parseDouble(str2.trim());
                                        Double summ1 = s1 - sum;
                                        String ss1 = String.format("%.2f", summ1).replaceAll(",", ".");
                                        while (raz3==1){
                                            DatabaseReference mDatabaseRef2 = database2.getReference();
                                            mDatabaseRef2.child("Users").child(user.getUid()).child("Authentication").child("transfer").setValue(ss1);
                                            raz3++; } }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }});
                                final FirebaseDatabase database3 = FirebaseDatabase.getInstance();
                                DatabaseReference myRef3 = database3.getReference();
                                myRef3 = myRef3.child("Users").child(user.getUid()).child("Authentication").child("balance");
                                myRef3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String balance = dataSnapshot.getValue(String.class);
                                        balance = balance.replaceAll(",", ".");
                                        Double s2 = Double.parseDouble(balance);
                                        Double sum2 = Double.parseDouble(str2.trim());
                                        Double summ1 = s2 + sum2;
                                        String ss2 = String.format("%.2f", summ1).replaceAll(",", ".");
                                        while (rz3==1){
                                            DatabaseReference mDatabaseRef4 = database3.getReference();
                                            mDatabaseRef4.child("Users").child(user.getUid()).child("Authentication").child("balance").setValue(ss2);
                                            rz3++; } }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }}); }
                            finish(); }}); }});
            operation="";
            final EditText edit = (EditText) findViewById(R.id.etWidth1);
            data_in =  findViewById(R.id.date_edit);
            timer_in = findViewById(R.id.time_edit);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String lc = getString(R.string.location);
                    SharedPreferences.Editor editor = getSharedPreferences("Locale", MODE_PRIVATE).edit();
                    editor.putString("local", lc);
                    editor.apply();
                }
            }, SPLASH_DISPLAY_LENGHT);
            Button button = (Button) findViewById(R.id.button);
            SharedPreferences prefs = getSharedPreferences("Currency", MODE_PRIVATE);
            String value = prefs.getString("btnText", "BYN");
            button.setText(value);
            edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == KeyEvent.KEYCODE_ENTER) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
                        handled = true; }
                    return handled; }});
            edit.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    String str = edit.getText().toString();
                    String str2 = Mopertion.getText().toString();
                    String ctg1 = getString(R.string.transfer_text);
                    if (str2 != ctg1) {
                        if (keyCode == KeyEvent.KEYCODE_DEL && str.length() == 1) {
                            edit.setText(""); } }
                    return false; }});
            edit.addTextChangedListener(new TextWatcher() {
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
                            editable.delete(editable.length() - 1, editable.length()); } } }});
            findViewById(R.id.etWidth1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int textLength = edit.getText().length();
                    edit.setSelection(textLength, textLength);
                    edit.setSelection(textLength, textLength); }});
            setInitialData();
            countryList = (ListView) findViewById(R.id.countriesList);
            CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this, R.layout.list_item, states);
            countryList.setAdapter(categoriesAdapter);
            AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Categories selectedState = (Categories) parent.getItemAtPosition(position);
                    Button button = (Button)findViewById(R.id.button3);
                    button.setText(selectedState.getName());
                    onClick_Back(null); }};
            countryList.setOnItemClickListener(itemListener);
            data_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateDialog(data_in);
                }
            });
            timer_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimeDialog(timer_in);
                }
            });
            timer_in.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int inType = timer_in.getInputType();
                    timer_in.setInputType(InputType.TYPE_NULL);
                    timer_in.onTouchEvent(event);
                    timer_in.setInputType(inType);
                    return true; }});
            data_in.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int inType = data_in.getInputType();
                    data_in.setInputType(InputType.TYPE_NULL);
                    data_in.onTouchEvent(event);
                    data_in.setInputType(inType);
                    return true; }}); }
        private void showTimeDialog(final EditText timer_in) {
            final Calendar calendar = Calendar.getInstance();
            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                    calendar.set(Calendar.MINUTE,minute);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    timer_in.setText(simpleDateFormat.format(calendar.getTime())); }};
            new TimePickerDialog(com.example.limb.OperationDetails.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show(); }
        private void showDateDialog(final EditText data_in) {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    data_in.setText(simpleDateFormat.format(calendar.getTime())); }};
            new DatePickerDialog(com.example.limb.OperationDetails.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show(); }
        @Override
        public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (v instanceof EditText) {
                    final EditText editText = (EditText) findViewById(R.id.etWidth1);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); }
                if (v instanceof EditText) {
                    final EditText editText = (EditText) findViewById(R.id.edit_text);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); } }
            return super.dispatchTouchEvent(event); }
        public void onClick1(View v) {
            final Button button =(Button)findViewById(R.id.button);
            final String[] listItems = {"BYN","RUB","USD","EUR"};
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(com.example.limb.OperationDetails.this);
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
            mDialog.show(); }
        public void onClick2(View v) {
            RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.categ_rel);
            relativeLayout.setVisibility(View.VISIBLE);
            Button button = (Button)findViewById(R.id.update_btn);
            button.setVisibility(View.GONE); }
        public void onClick3(View v) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(com.example.limb.OperationDetails.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION); }
            else {
                startActivity(new Intent(getApplicationContext(), Map.class));
                overridePendingTransition(0, 0); } }
        public void onClick_Back(View v) {
            RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.categ_rel);
            Button button = (Button)findViewById(R.id.update_btn);
            relativeLayout.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE); }
    public void onClick_Back2(View v) {
        onBackPressed(); }
        private void setInitialData() {
            String ctg1 = getString(R.string.f_and_d);
            String ctg2 = getString(R.string.purchases);
            String ctg3 = getString(R.string.housing);
            String ctg4 = getString(R.string.transport);
            String ctg5 = getString(R.string.vehicle);
            String ctg6 = getString(R.string.l_and_e);
            String ctg7 = getString(R.string.cpc);
            String ctg8 = getString(R.string.financial_expenses);
            String ctg9 = getString(R.string.investment);
            String ctg10 = getString(R.string.income_text);
            String ctg11 = getString(R.string.etc);
            states.add(new Categories(ctg1, R.drawable.food));
            states.add(new Categories(ctg2, R.drawable.purchases));
            states.add(new Categories(ctg3, R.drawable.housing));
            states.add(new Categories(ctg4, R.drawable.public_transport));
            states.add(new Categories(ctg5, R.drawable.car));
            states.add(new Categories(ctg6, R.drawable.entertainment));
            states.add(new Categories(ctg7, R.drawable.pc));
            states.add(new Categories(ctg8, R.drawable.finance));
            states.add(new Categories(ctg9, R.drawable.investment));
            states.add(new Categories(ctg10, R.drawable.income));
            states.add(new Categories(ctg11, R.drawable.etc)); }}
