package com.example.testapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.listeners.LoginListener;
import com.example.testapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    private View view;

    EditText mIme;
    EditText mPrezime;
    EditText mEmail;
    EditText mLozinka;
    Button mBtnRegister;

    FirebaseAuth auth;
    DatabaseReference mRef;

    public LoginListener loginListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        //EDIT TEXT
        mIme = view.findViewById(R.id.et_nameRegister);
        mPrezime = view.findViewById(R.id.et_surnameRegister);
        mEmail = view.findViewById(R.id.et_emailRegister);
        mLozinka = view.findViewById(R.id.et_passwordRegister);

        //BUTTONS
        mBtnRegister = view.findViewById(R.id.btn_register);

        //DATABASE
        auth = FirebaseAuth.getInstance();

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();

            }
        });

        return view;
    }

    private void registerUser(){

        String ime = mIme.getText().toString();
        String prezime = mPrezime.getText().toString();
        String email = mEmail.getText().toString();
        String lozinka = mLozinka.getText().toString();

        if (ime.isEmpty()){
            mIme.setError("Ime je obavezno!");
            return;
        }

        if (prezime.isEmpty()){
            mPrezime.setError("Prezime je obavezno!");
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

        if (lozinka.isEmpty()){
            mLozinka.setError("Lozinka je obavezna!");
            return;
        }

        if (lozinka.length() < 6){
            mLozinka.setError("Minimalno 6 znakova za lozinku");
            return;
        }

        auth.createUserWithEmailAndPassword(email, lozinka)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            User oUser = new User(0, "korisnik", ime, prezime, email, lozinka);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(oUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(getContext(), "Registracija uspjesna!", Toast.LENGTH_SHORT).show();
                                        mIme.getText().clear();
                                        mPrezime.getText().clear();
                                        mEmail.getText().clear();
                                        mLozinka.getText().clear();
                                    }
                                    else{
                                        Toast.makeText(getContext(), "Registracija nije uspjela, pokusaj ponovno", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                        else{
                            Toast.makeText(getContext(), "Registracija nije uspjela", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

}