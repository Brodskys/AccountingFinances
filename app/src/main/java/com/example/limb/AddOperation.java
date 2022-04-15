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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
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
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddOperation extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 10;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    private List<Categories> states = new ArrayList();
    ListView countryList;
    EditText data_in;
    EditText timer_in;
    Calendar dateAndTime=Calendar.getInstance();
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    String operation;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    FirebaseListAdapter mAdapter;
    DatabaseReference reference;
    Operations_add operations_add;
    final boolean[] b1 = {false};
    final boolean[] b2 = {false};
    final boolean[] b3 = {false};
    private boolean clr1 = false;
    private boolean clr2 = false;
    private boolean clr3 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (user != null) { }
        else {
            finish();
            Intent intent = new Intent(AddOperation.this,EmailPassword.class);
            startActivity(intent);        }
        setContentView(R.layout.activity_add_operation);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Button button = (Button) findViewById(R.id.button4);
                SharedPreferences prefs = getSharedPreferences("Locale", MODE_PRIVATE);
                String value = prefs.getString("local", button.getText().toString());
                button.setText(value);            }
        }, 0, 1, TimeUnit.SECONDS);
        operation="";
        final EditText edit = (EditText) findViewById(R.id.etWidth1);
        data_in =  findViewById(R.id.date_edit);
        timer_in = findViewById(R.id.time_edit);
        setInitialDateTime();
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.add_operation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.accounts:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.add_operation:
                        return true;
                    case R.id.statistics:
                        startActivity(new Intent(getApplicationContext(), Menu.class));
                        overridePendingTransition(0, 0);
                        return true; }
                return false; }});
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Button btn = (Button) findViewById(R.id.expense);
                btn.performClick();
                String lc = getString(R.string.location);
                SharedPreferences.Editor editor = getSharedPreferences("Locale", MODE_PRIVATE).edit();
                editor.putString("local", lc);
                editor.apply();
            }
        }, SPLASH_DISPLAY_LENGHT);
        final MaterialButtonToggleGroup tGroup = findViewById(R.id.toggleGroup);
        final MaterialButton breast = tGroup.findViewById(R.id.expense);
        final MaterialButton bottle = tGroup.findViewById(R.id.income);
        final MaterialButton solids = tGroup.findViewById(R.id.transfer);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tGroup.check(v.getId());
                String s = edit.getText().toString();
                Button bt1 = (Button)findViewById(R.id.expense);
                Button bt2 = (Button)findViewById(R.id.income);
                Button bt3 = (Button)findViewById(R.id.transfer);
                switch (v.getId()) {
                    case R.id.expense:
                        edit.setTextColor(Color.parseColor("#F00C0C"));
                        edit.setText("");
                        operation = bt1.getText().toString();
                        Button button1 = (Button)findViewById(R.id.button3);
                        button1.setEnabled(true);
                        String str1 = getString(R.string.category_text);
                        String str3 = getString(R.string.transfer_text);
                        String buttonText = button1.getText().toString();
                        if (buttonText == str3) button1.setText(str1);
                        b1[0] = true;
                        b2[0] = false;
                        b3[0] = false;
                        break;
                    case R.id.income:
                        edit.setTextColor(Color.parseColor("#00C853"));
                        edit.setText("");
                        operation = bt2.getText().toString();
                        Button button2 = (Button)findViewById(R.id.button3);
                        button2.setEnabled(true);
                        String str2 = getString(R.string.category_text);
                        String str4 = getString(R.string.transfer_text);
                        String buttonText2 = button2.getText().toString();
                        if (buttonText2 == str4) button2.setText(str2);
                        b1[0] = false;
                        b2[0] = true;
                        b3[0] = false;
                        break;
                    case R.id.transfer:
                        edit.setTextColor(Color.parseColor("#FFFFFF"));
                        edit.setText("");
                        operation = bt3.getText().toString();
                        Button button3 = (Button)findViewById(R.id.button3);
                        String str5 = getString(R.string.transfer_text);
                        button3.setText(str5);
                        button3.setEnabled(false);
                        b1[0] = false;
                        b2[0] = false;
                        b3[0] = true;
                        break; } }};
        breast.setOnClickListener(onClickListener);
        bottle.setOnClickListener(onClickListener);
        solids.setOnClickListener(onClickListener);
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
                    handled = true;
                }
                return handled; }});
        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String str = edit.getText().toString();
                if (b3[0] == false) {
                    if (keyCode == KeyEvent.KEYCODE_DEL && str.length() == 1) {
                        edit.setText("");
                    } }
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
                        editable.delete(editable.length() - 1, editable.length());
                    } } }});
        findViewById(R.id.etWidth1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textLength = edit.getText().length();
                edit.setSelection(textLength, textLength);
            }});
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
                onClick_Back(null);
            }};
        countryList.setOnItemClickListener(itemListener);
        data_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(data_in);
            }});
        timer_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(timer_in);
            }});
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
    private void setInitialDateTime() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        data_in.setText(dateText);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        timer_in.setText(timeText);
    }
    private void showTimeDialog(final EditText timer_in) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                timer_in.setText(simpleDateFormat.format(calendar.getTime()));
            }};
        new TimePickerDialog(AddOperation.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
    }
    private void showDateDialog(final EditText data_in) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                data_in.setText(simpleDateFormat.format(calendar.getTime()));
            }};
        new DatePickerDialog(AddOperation.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                final EditText editText = (EditText) findViewById(R.id.etWidth1);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
            if (v instanceof EditText) {
                final EditText editText = (EditText) findViewById(R.id.edit_text);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            } }
        return super.dispatchTouchEvent(event);
    }
    public void onClick1(View v) {
        final Button button =(Button)findViewById(R.id.button);
        final String[] listItems = {"BYN","RUB","USD","EUR"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddOperation.this);
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
                    recreate();
                }
                else if (i==1){
                    Button  button =(Button)findViewById(R.id.button);
                    text =  "RUB";
                    SharedPreferences.Editor editor = getSharedPreferences("Currency", MODE_PRIVATE).edit();
                    editor.putString("btnText", text);
                    editor.commit();
                    recreate();
                }
                else if (i==2){
                    Button  button =(Button)findViewById(R.id.button);
                    text =  "USD";
                    SharedPreferences.Editor editor = getSharedPreferences("Currency", MODE_PRIVATE).edit();
                    editor.putString("btnText", text);
                    editor.commit();
                    recreate();
                }
                else if (i==3){
                    Button  button =(Button)findViewById(R.id.button);
                    text =  "EUR";
                    SharedPreferences.Editor editor = getSharedPreferences("Currency", MODE_PRIVATE).edit();
                    editor.putString("btnText", text);
                    editor.commit();
                    recreate();
                }
                dialog.dismiss(); }});
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    public void onClick2(View v) {
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.categ_rel);
        relativeLayout.setVisibility(View.VISIBLE);
        Button button = (Button)findViewById(R.id.create);
        button.setVisibility(View.GONE);
    }
    public void onClick3(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddOperation.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            startActivity(new Intent(getApplicationContext(), Map.class));
            overridePendingTransition(0, 0);
        } }
    public void onClick_Back(View v) {
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.categ_rel);
        Button button = (Button)findViewById(R.id.create);
        relativeLayout.setVisibility(View.GONE);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
    }
    boolean rz1 = false;
    boolean rz2 = false;
    boolean rz3 = false;
    public void onClick_Create(View v) throws ParseException {
        final EditText edit = (EditText) findViewById(R.id.etWidth1);
        String str = getString(R.string.category_text);
        Button button = (Button)findViewById(R.id.button3);
        String str3 = getString(R.string.notenoughdata);
        String str4= getString(R.string.thesum);
        String str5= getString(R.string.fillinthefollowinginformation);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddOperation.this);
        alertDialog.setTitle(str3);
        String text = "";
        if (edit.getText().toString().equals("")){
            text = str4;
        }
        if (button.getText() == str){
            text = str;
        }
        if (edit.getText().toString().equals("") && button.getText() == str ){
            text = str4+ "\n" +str;
        }
        alertDialog.setMessage(str5);
        alertDialog.setMessage(str5 +" " + "\n" + text);
        alertDialog.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) { }
        });
        if (edit.getText().toString().equals("") || button.getText() == str ) {
            alertDialog.show();
        }
        else {
            EditText e = (EditText)findViewById(R.id.edit_text);
            Button bb = (Button) findViewById(R.id.button4);
            Date currentDate ;
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            currentDate = dateFormat.parse(data_in.getText().toString());
            Date tm ;
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            tm = timeFormat.parse(timer_in.getText().toString());
            Float sum = Float.parseFloat(edit.getText().toString().trim());
            String categ = button.getText().toString().trim();
            String det = e.getText().toString().trim();
            String geo = bb.getText().toString().trim();
            if (b1[0]==true ) {
                final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                DatabaseReference myRef2 = database2.getReference();
                myRef2 = myRef2.child("Users").child(user.getUid()).child("Authentication").child("expense");
                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String expense = dataSnapshot.getValue(String.class);
                        expense = expense.replaceAll(",", ".");
                        Double s1 = Double.parseDouble(expense);
                        Double sum = Double.parseDouble(edit.getText().toString().trim());
                        Double summ1 = s1 + sum;
                        String ss1 = String.format("%.2f", summ1).replaceAll(",", ".");
                        if (rz1 == false) {
                            DatabaseReference mDatabaseRef2 = database2.getReference();
                            mDatabaseRef2.child("Users").child(user.getUid()).child("Authentication").child("expense").setValue(ss1);
                            rz1 = true;
                        } }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});
               b1[0]=true;
                }
            if (b2[0]==true ) {
                final FirebaseDatabase database3 = FirebaseDatabase.getInstance();
                DatabaseReference myRef3 = database3.getReference();
                myRef3 = myRef3.child("Users").child(user.getUid()).child("Authentication").child("income");
                myRef3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String income = dataSnapshot.getValue(String.class);
                        income = income.replaceAll(",", ".");
                        Double s2 = Double.parseDouble(income);
                        Double sum3 = Double.parseDouble(edit.getText().toString().trim());
                        Double summ2 = s2 + sum3;
                        String ss2 = String.format("%.2f", summ2).replaceAll(",", ".");
                        if (rz2 == false) {
                            DatabaseReference mDatabaseRef3 = database3.getReference();
                            mDatabaseRef3.child("Users").child(user.getUid()).child("Authentication").child("income").setValue(ss2);
                            rz2 = true; } }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});
              b2[0]=true;
                }
            if (b3[0]==true ) {
            final FirebaseDatabase database4 = FirebaseDatabase.getInstance();
            DatabaseReference myRef4 = database4.getReference();
            myRef4 = myRef4.child("Users").child(user.getUid()).child("Authentication").child("transfer");
            myRef4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String transfer = dataSnapshot.getValue(String.class);
                    transfer = transfer.replaceAll(",", ".");
                    Double s3 = Double.parseDouble(transfer);
                    Double sum4 = Double.parseDouble(edit.getText().toString().trim());
                    Double summ3 = s3 + sum4;
                    String ss3 = String.format("%.2f", summ3).replaceAll(",", ".");
                    if (rz3==false) {
                        DatabaseReference mDatabaseRef3 = database4.getReference();
                        mDatabaseRef3.child("Users").child(user.getUid()).child("Authentication").child("transfer").setValue(ss3);
                        rz3 = true; } }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
                b3[0]=true;
            }
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef = myRef.child("Users").child(user.getUid()).child("Authentication").child("balance");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   String value =  dataSnapshot.getValue(String.class);
                    value = value.replaceAll(",",".");
                    Double s1 = Double. parseDouble(value);
                    Double sum = Double.parseDouble(edit.getText().toString().trim());
                    Double summ1 = s1- sum;
                    String ss1 = String.format("%.2f", summ1).replaceAll(",",".");;
                    Double summ2 = s1+ sum;
                    String ss2 = String.format("%.2f", summ2).replaceAll(",",".");;
                    if (b1[0]==true ) {
                        DatabaseReference mDatabaseRef = database.getReference();
                        mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("balance").setValue(ss1);
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef1 = database.getReference();
                        myRef1 = myRef1.child("Users").child(user.getUid()).child("Authentication").child("expense");
                        myRef1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String expense =  dataSnapshot.getValue(String.class);
                                expense = expense.replaceAll(",",".");
                                Double s1 = Double. parseDouble(expense);
                                Double sum = Double.parseDouble(edit.getText().toString().trim());
                                Double summ3 = s1+ sum;
                                String ss2 = String.format("%.2f", summ3).replaceAll(",",".");;
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});
                        b1[0] = false;
                    }
                         if (b2[0]==true){
                DatabaseReference mDatabaseRef = database.getReference();
                mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("balance").setValue(ss2);
                b2[0]=false;
                         }
                    if ( b3[0]==true) {
                        DatabaseReference mDatabaseRef = database.getReference();
                        mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("balance").setValue(ss1);
                        b3[0] = false; } }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
            operations_add = new Operations_add();
            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Operations");
            operations_add.setName_operation(operation.trim());
            operations_add.setSumm((float) sum);
            operations_add.setCategoty(categ);
            operations_add.setDate(currentDate);
            operations_add.setTime(tm);
            operations_add.setDetailed(det);
            operations_add.setGeo(geo);
            reference.push().setValue(operations_add);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(0, 0);
        } }
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
        states.add(new Categories(ctg11, R.drawable.etc));
    }
}
