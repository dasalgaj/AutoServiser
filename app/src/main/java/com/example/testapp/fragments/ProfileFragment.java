package com.example.testapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.activities.EditProfileActivity;
import com.example.testapp.activities.HomeActivity;
import com.example.testapp.listeners.LoginListener;
import com.example.testapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private View view;

    TextView mIme;
    TextView mPrezime;
    TextView mEmail;
    Button mBtnUredi;

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private String userID;

    public LoginListener loginListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        //TEXT VIEW
        mIme = view.findViewById(R.id.tvName);
        mPrezime = view.findViewById(R.id.tvSurname);
        mEmail = view.findViewById(R.id.tvEmail);

        //BUTTONS
        mBtnUredi = view.findViewById(R.id.btnEditProfile);



        mBtnUredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), EditProfileActivity.class));

            }
        });


        return view;
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

                    mIme.setText("Ime: " + firstName);
                    mPrezime.setText("Prezime: " + lastName);
                    mEmail.setText("E-mail: " + email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getUser();
    }
}