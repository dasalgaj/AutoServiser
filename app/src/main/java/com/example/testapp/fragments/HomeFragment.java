package com.example.testapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.activities.HomeActivity;
import com.example.testapp.activities.HomeActivityServiser;
import com.example.testapp.activities.RezervacijaActivity;
import com.example.testapp.listeners.LoginListener;
import com.example.testapp.models.Servis;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;

    Button btnRezervacijaOpel;
    Button btnRezervacijaNissan;
    Button btnRezervacijaCitroen;
    Button btnRezervacijaRenault;

    DatabaseReference mDatabase;
    FirebaseAuth auth;

    List<Servis> servisi = new ArrayList<>();

    public LoginListener loginListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //BUTTONS
        btnRezervacijaOpel = view.findViewById(R.id.btnRezervacijaOpel);
        btnRezervacijaNissan = view.findViewById(R.id.btnRezervacijaNissan);
        btnRezervacijaCitroen = view.findViewById(R.id.btnRezervacijaCitroen);
        btnRezervacijaRenault = view.findViewById(R.id.btnRezervacijaRenault);

        //DATABASE
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Servisi");
        auth = FirebaseAuth.getInstance();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){
                    Servis servis = ds.getValue(Servis.class);

                    servisi.add(servis);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnRezervacijaOpel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rezervacijaOpel();

            }
        });

        btnRezervacijaNissan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rezervacijaNissan();

            }
        });

        btnRezervacijaCitroen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rezervacijaCitroen();

            }
        });

        btnRezervacijaRenault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rezervacijaRenault();

            }
        });

        return view;
    }


    public void rezervacijaOpel(){
        Intent intentRezervacija = new Intent(getContext(), RezervacijaActivity.class);
        Servis servis = new Servis();

        for (int i = 0; i < servisi.size(); i++){
            if (servisi.get(i).ime.equals("Opel")){

                servis.OIB = servisi.get(i).OIB;
                servis.adresa = servisi.get(i).adresa;
                servis.email = servisi.get(i).email;
                servis.ime = servisi.get(i).ime;
                servis.servisID = servisi.get(i).servisID;
                servis.telefon = servisi.get(i).telefon;

                intentRezervacija.putExtra("servis", servis);

                startActivity(intentRezervacija);
            }
        }

    }

    public void rezervacijaNissan(){
        Intent intentRezervacija = new Intent(getContext(), RezervacijaActivity.class);
        Servis servis = new Servis();

        for (int i = 0; i < servisi.size(); i++){
            if (servisi.get(i).ime.equals("Nissan")){

                servis.OIB = servisi.get(i).OIB;
                servis.adresa = servisi.get(i).adresa;
                servis.email = servisi.get(i).email;
                servis.ime = servisi.get(i).ime;
                servis.servisID = servisi.get(i).servisID;
                servis.telefon = servisi.get(i).telefon;

                intentRezervacija.putExtra("servis", servis);

                startActivity(intentRezervacija);
            }
        }

    }

    public void rezervacijaCitroen(){
        Intent intentRezervacija = new Intent(getContext(), RezervacijaActivity.class);
        Servis servis = new Servis();

        for (int i = 0; i < servisi.size(); i++){
            if (servisi.get(i).ime.equals("Citroen")){

                servis.OIB = servisi.get(i).OIB;
                servis.adresa = servisi.get(i).adresa;
                servis.email = servisi.get(i).email;
                servis.ime = servisi.get(i).ime;
                servis.servisID = servisi.get(i).servisID;
                servis.telefon = servisi.get(i).telefon;

                intentRezervacija.putExtra("servis", servis);

                startActivity(intentRezervacija);
            }
        }

    }

    public void rezervacijaRenault(){
        Intent intentRezervacija = new Intent(getContext(), RezervacijaActivity.class);
        Servis servis = new Servis();

        for (int i = 0; i < servisi.size(); i++){
            if (servisi.get(i).ime.equals("Renault")){

                servis.OIB = servisi.get(i).OIB;
                servis.adresa = servisi.get(i).adresa;
                servis.email = servisi.get(i).email;
                servis.ime = servisi.get(i).ime;
                servis.servisID = servisi.get(i).servisID;
                servis.telefon = servisi.get(i).telefon;

                intentRezervacija.putExtra("servis", servis);

                startActivity(intentRezervacija);
            }
        }

    }
}