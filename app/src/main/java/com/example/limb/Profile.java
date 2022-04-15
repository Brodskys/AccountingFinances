package com.example.limb;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {
    private DatabaseReference mRef;
    private EditText  email, email2;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = (EditText) findViewById(R.id.eml_ed);
        email2 = (EditText) findViewById(R.id.eml_ed2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


            String em = firebaseUser.getEmail();
            if (em == null || em.equals("")){
                Button b1 = (Button)findViewById(R.id.update_email);
                Button b2 = (Button)findViewById(R.id.update_pass);
                Button b3 = (Button)findViewById(R.id.exit_btn);
                Button b4 = (Button)findViewById(R.id.exit_reg);
                EditText t1 = (EditText)findViewById(R.id.pass1);

                b1.setEnabled(false);
                b2.setEnabled(false);
                b3.setVisibility(View.GONE);
                b4.setVisibility(View.VISIBLE);
                t1.setEnabled(false);
                em="example@email.com";
            }
            email.setText(em);
            email2.setText(em);


    }
    public void updatepass(View v) {
        EditText p1 = (EditText) findViewById(R.id.pass1);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String ctg2 = getString(R.string.error);
        final String ctg3 = getString(R.string.passwordchangedsuccessfully);
        try {
    final String newPassword = p1.getText().toString();
    user.updatePassword(newPassword)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Profile.this, ctg3, Toast.LENGTH_SHORT).show();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabaseRef = database.getReference();
                        mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("password").setValue(newPassword);
                        startActivity(new Intent(getApplicationContext(), Menu.class));
                        overridePendingTransition(0, 0);
                    } else {
                        Toast.makeText(Profile.this, ctg2, Toast.LENGTH_SHORT).show(); } }});
} catch (Throwable e) {
    e.printStackTrace(); } }
    int raz = 0;
    public void updateemail(View v) {
        EditText p1 = (EditText) findViewById(R.id.eml_ed);
        p1.setEnabled(true);
        p1.setFocusable(true);
        p1.requestFocus();
        p1.setSelection(p1.getText().length());
       if (raz == 1) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            String st1 = firebaseUser.getEmail();
            if (st1.equals(p1.getText().toString()) || p1.getText().toString().equals("") || p1.getText().toString().contains(" ")) { }
            else {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String ctg1 = getString(R.string.mailwaschangedsuccessfully);
                final String ctg2 = getString(R.string.error);
                final String newPassword = p1.getText().toString();
               EditText em = (EditText) findViewById(R.id.eml_ed);


                String ctg11 = getString(R.string.areyousure);
                String ctg111 = em.getText().toString();
                String ctg22 = getString(R.string.yes);
                String ctg33 = getString(R.string.no);


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Profile.this);
                alertDialog.setTitle(ctg11);
                alertDialog.setMessage(ctg111);
                alertDialog .setCancelable(true)
                        .setPositiveButton(ctg22, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                user.updateEmail(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Profile.this, ctg1, Toast.LENGTH_SHORT).show();
                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference mDatabaseRef = database.getReference();
                                                    mDatabaseRef.child("Users").child(user.getUid()).child("Authentication").child("login").setValue(newPassword);
                                                    startActivity(new Intent(getApplicationContext(), Menu.class));
                                                    overridePendingTransition(0, 0);
                                                    user.sendEmailVerification()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) { }
                                                                }
                                                            });
                                                    FirebaseAuth.getInstance().signOut();
                                                    Intent intent = new Intent(Profile.this, EmailPassword.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(Profile.this, ctg2, Toast.LENGTH_SHORT).show();
                                                }
                                            }});

                            }})
                        .setNeutralButton(ctg33, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel(); }});
                alertDialog.show();




            }
           raz = 0;
       }raz++; }





    public void delete_bt(View v) {
        String ctg1 = getString(R.string.areyousure);
        String ctg2 = getString(R.string.yes);
        String ctg3 = getString(R.string.no);
        final String ctg4 = getString(R.string.accountdeleted);
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setMessage(ctg1)
                .setCancelable(true)
                .setPositiveButton(ctg2,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Profile.this, ctg4, Toast.LENGTH_SHORT).show(); } }});
                                FirebaseAuth.getInstance().signOut();
                                mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                                mRef.removeValue();
                                startActivity(new Intent(getApplicationContext(), EmailPassword.class));
                                overridePendingTransition(0, 0); }})
                .setNeutralButton(ctg3,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel(); }});
        builder.show(); }
    public void exit_bt(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, EmailPassword.class);
        startActivity(intent);
    }

    public void exit_registration(View v) {
        Intent intent = new Intent(this, EmailPassword2.class);
        startActivity(intent);
    }

    public void onClick_Back(View v) {
        onBackPressed();
    }}
