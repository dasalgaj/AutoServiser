package com.example.testapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.activities.HomeActivity;
import com.example.testapp.activities.HomeActivityServiser;
import com.example.testapp.activities.RezervacijaActivity;
import com.example.testapp.listeners.LoginListener;
import com.example.testapp.models.Rezervacija;
import com.example.testapp.models.Servis;
import com.example.testapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;

    TextView tvOpel, tvNissan, tvCitroen, tvRenault;
    Button btnRezervacijaOpel;
    Button btnRezervacijaNissan;
    Button btnRezervacijaCitroen;
    Button btnRezervacijaRenault;

    DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference mDatabaseServisi;
    DatabaseReference mDatabaseRezervacije = FirebaseDatabase.getInstance().getReference("Rezervacije");
    FirebaseUser userFirebase;
    FirebaseAuth auth;

    List<Servis> servisi = new ArrayList<>();

    String userID;

    public LoginListener loginListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mDatabaseRezervacije != null) {

            userFirebase = FirebaseAuth.getInstance().getCurrentUser();
            userID = userFirebase.getUid();

            mDatabaseUsers.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotUsers) {

                    if (snapshotUsers.exists()) {

                        User userProfile = snapshotUsers.getValue(User.class);

                        if (userProfile != null){

                            mDatabaseRezervacije.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotRezervacije) {

                                    if (snapshotRezervacije.exists()) {

                                        for (DataSnapshot dsRezervacije : snapshotRezervacije.getChildren()){

                                            if (userProfile.rezervacijaID == dsRezervacije.child("rezervacijaID").getValue(Long.class)){

                                                if (dsRezervacije.child("servisID").getValue(int.class) == 1){

                                                    tvNissan.setText("Na čekanju");
                                                    tvNissan.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                                                    btnRezervacijaNissan.setVisibility(View.INVISIBLE);

                                                }
                                                else if (dsRezervacije.child("servisID").getValue(int.class) == 2){

                                                    tvCitroen.setText("Na čekanju");
                                                    btnRezervacijaCitroen.setVisibility(View.INVISIBLE);

                                                }
                                                else if (dsRezervacije.child("servisID").getValue(int.class) == 3){

                                                    tvRenault.setText("Na čekanju");
                                                    btnRezervacijaRenault.setVisibility(View.INVISIBLE);

                                                }
                                                else if (dsRezervacije.child("servisID").getValue(int.class) == 4){

                                                    tvOpel.setText("Na čekanju");
                                                    btnRezervacijaOpel.setVisibility(View.INVISIBLE);

                                                }

                                            }

                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
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

        //TEXT VIEW
        tvOpel = view.findViewById(R.id.tvRezervacija1);
        tvNissan = view.findViewById(R.id.tvRezervacija2);
        tvCitroen = view.findViewById(R.id.tvRezervacija3);
        tvRenault = view.findViewById(R.id.tvRezervacija4);

        //DATABASE
        mDatabaseServisi = FirebaseDatabase.getInstance().getReference().child("Servisi");
        auth = FirebaseAuth.getInstance();

        mDatabaseServisi.addValueEventListener(new ValueEventListener() {
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