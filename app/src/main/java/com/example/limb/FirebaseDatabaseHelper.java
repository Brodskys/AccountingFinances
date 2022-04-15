package com.example.limb;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Operations_add> operationsadds = new ArrayList<>();
    private FirebaseAuth mAuth;
    public  interface  DataStatus {
        void DataIsLoaded(List<Operations_add> operationsadds, List<String>keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();}
    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    public FirebaseDatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference().child("Users").child(user.getUid()).child("Operations"); }
    public void readOperations(final DataStatus dataStatus){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                operationsadds.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode: dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Operations_add operationsadd = keyNode.getValue(Operations_add.class);
                    operationsadds.add(operationsadd); }
                dataStatus.DataIsLoaded(operationsadds,keys); }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }}); }
    public void updateOperations(String key, Operations_add operations_add, final DataStatus dataStatus){
        mReference.child(key).setValue(operations_add)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated(); }}); }
    public void deleteOperations(String key, final DataStatus dataStatus){
        mReference.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }}); }}