package com.example.testapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.fragments.ProfileFragment;
import com.example.testapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    EditText mIme;
    EditText mPrezime;
    EditText mEmail;
    Button mBtnSpremiPodatke;

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private String userID;

    String ime;
    String prezime;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //EDIT TEXT
        mIme = findViewById(R.id.etIme);
        mPrezime = findViewById(R.id.etPrezime);
        mEmail = findViewById(R.id.etEmail);

        //BUTTONS
        mBtnSpremiPodatke = findViewById(R.id.btnSpremiProfil);

        getUser();

        mBtnSpremiPodatke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateUser();

            }
        });
    }

    private void getUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        mDatabase.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    String firstName = userProfile.ime;
                    String lastName = userProfile.prezime;
                    String email = userProfile.email;

                    mIme.setText(firstName);
                    mPrezime.setText(lastName);
                    mEmail.setText(email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser(){

        ime = mIme.getText().toString();
        prezime = mPrezime.getText().toString();
        email = mEmail.getText().toString();

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        HashMap updatedUser = new HashMap();
        updatedUser.put("ime", ime);
        updatedUser.put("prezime", prezime);
        updatedUser.put("email", email);

        if (ime.isEmpty()){
            mIme.setError("Ne moze biti prazno");
            return;
        }

        if (prezime.isEmpty()){
            mPrezime.setError("Ne moze biti prazno");
            return;
        }

        if (email.isEmpty()){
            mEmail.setError("Email je obavezan!");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Unesite valjani email");
            return;
        }

        //PROMJENA IMENA, PREZIMENA i E-MAIL
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.child(userID).updateChildren(updatedUser).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "Uspješno izmjenjeno", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditProfileActivity.this, "Pogreška", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //PROMJENA E-MAIL
        user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Nije promijenjeno", Toast.LENGTH_SHORT).show();
            }
        });

    }
}