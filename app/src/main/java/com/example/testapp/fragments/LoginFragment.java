package com.example.testapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.activities.HomeActivity;
import com.example.testapp.listeners.LoginListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginFragment extends Fragment {

    private View view;

    EditText mEmail;
    EditText mLozinka;
    Button mBtnLogin;

    DatabaseReference mRef;
    FirebaseAuth auth;

    public LoginListener loginListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        //EDIT TEXT
        mEmail = view.findViewById(R.id.et_emailLogin);
        mLozinka = view.findViewById(R.id.et_passwordLogin);

        //BUTTON
        mBtnLogin = view.findViewById(R.id.btn_login);

        //DATABASE
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        auth = FirebaseAuth.getInstance();

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin();

            }
        });

        return view;
    }

    private void userLogin(){
        String email = mEmail.getText().toString();
        String lozinka = mLozinka.getText().toString();

        if (email.isEmpty()){
            mEmail.setError("Email popunite!");
            return;
        }

        if (lozinka.isEmpty()){
            mLozinka.setError("Lozinku popunite");
            return;
        }

        auth.signInWithEmailAndPassword(email, lozinka)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            startActivity(new Intent(getContext(), HomeActivity.class));

                        }
                        else{
                            Toast.makeText(getContext(), "Prijava neuspjesna, pokusajte ponovno", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}