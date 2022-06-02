package com.example.testapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.fragments.HomeFragment;
import com.example.testapp.models.Rezervacija;
import com.example.testapp.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.Value;

public class PotvrdaRezervacijeActivity extends AppCompatActivity implements OnMapReadyCallback{

    TextView tvPotvrdaTip;
    TextView tvPotvrdaDatum;
    TextView tvPotvrdaVrijeme;
    TextView tvPotvrdaAdresa;
    Button mBtnRezervacijaPotvrdi;

    String imeServisa, adresa, userID;
    long rezervacijaID;

    Rezervacija rezervacija;

    FirebaseUser user;
    DatabaseReference mDatabaseRezervacije;
    DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potvrda_rezervacije);

        //TEXT VIEW
        tvPotvrdaTip = findViewById(R.id.tvPotvrdaTip);
        tvPotvrdaDatum = findViewById(R.id.tvPotvrdaDatum);
        tvPotvrdaVrijeme = findViewById(R.id.tvPotvrdaVrijeme);
        tvPotvrdaAdresa = findViewById(R.id.tvPotvrdaAdresa);

        //BUTTONS
        mBtnRezervacijaPotvrdi = findViewById(R.id.btnRezervacijaPotvrdi);

        //DATABASE
        mDatabaseRezervacije = FirebaseDatabase.getInstance().getReference().child("Rezervacije");

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.potvrdaMap);
        mapFragment.getMapAsync(this);

        String tip = getIntent().getStringExtra("tip");
        String datum = getIntent().getStringExtra("datum");
        String vrijeme = getIntent().getStringExtra("vrijeme");
        adresa = getIntent().getStringExtra("adresa");
        imeServisa = getIntent().getStringExtra("imeServisa");
        int servisID = getIntent().getIntExtra("servisID", 0);

        tvPotvrdaTip.setText("Tip servisa: " + tip);
        tvPotvrdaDatum.setText("Datum servisa: " + datum);
        tvPotvrdaVrijeme.setText("Vrijeme servisa: " + vrijeme);
        tvPotvrdaAdresa.setText("Adresa servisa: " + adresa);

        ValueEventListener stateValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    rezervacijaID = userProfile.rezervacijaID;

                    rezervacija = new Rezervacija(userProfile.ime, userProfile.prezime, userProfile.mobitel, tip, datum, vrijeme, adresa, "Na cekanju", servisID, rezervacijaID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };



        mBtnRezervacijaPotvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PotvrdaRezervacijeActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Potvrda rezervacije");
                builder.setMessage("Da li ste sigurni?");
                builder.setPositiveButton("Potvrdi",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mDatabaseUsers.child(userID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        User userProfile = snapshot.getValue(User.class);

                                        if (userProfile != null){
                                            rezervacijaID = userProfile.rezervacijaID;

                                            Rezervacija rezervacija = new Rezervacija(userProfile.ime, userProfile.prezime, userProfile.mobitel, tip, datum, vrijeme, adresa, "Na cekanju", servisID, rezervacijaID);
                                            mDatabaseRezervacije.push().setValue(rezervacija);
                                            Toast.makeText(PotvrdaRezervacijeActivity.this, "Uspješno rezervirano", Toast.LENGTH_SHORT).show();

                                            mDatabaseUsers.removeEventListener(this);

                                            Intent i = new Intent(PotvrdaRezervacijeActivity.this, HomeActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (imeServisa.equals("Opel")){
            LatLng opel = new LatLng(45.8206169, 17.3845614);
            googleMap.addMarker(new MarkerOptions().position(opel).title("Opel servis").snippet(adresa));

            googleMap.getUiSettings().setScrollGesturesEnabled(false);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(opel));

            googleMap.setMinZoomPreference(13.5f);
            googleMap.setMaxZoomPreference(16.0f);
        }
        else if (imeServisa.equals("Nissan")){
            LatLng nissan = new LatLng(45.8218038, 17.3934923);
            googleMap.addMarker(new MarkerOptions().position(nissan).title("Nissan servis").snippet(adresa));

            googleMap.getUiSettings().setScrollGesturesEnabled(false);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(nissan));

            googleMap.setMinZoomPreference(13.5f);
            googleMap.setMaxZoomPreference(16.0f);
        }
        else if (imeServisa.equals("Citroen")){
            LatLng citroen = new LatLng(45.830199, 17.3846591);
            googleMap.addMarker(new MarkerOptions().position(citroen).title("Citroen servis").snippet(adresa));

            googleMap.getUiSettings().setScrollGesturesEnabled(false);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(citroen));

            googleMap.setMinZoomPreference(13.5f);
            googleMap.setMaxZoomPreference(16.0f);
        }
        else if (imeServisa.equals("Renault")){
            LatLng renault = new LatLng(45.8395842, 17.3606061);
            googleMap.addMarker(new MarkerOptions().position(renault).title("Renault servis").snippet(adresa));

            googleMap.getUiSettings().setScrollGesturesEnabled(false);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(renault));

            googleMap.setMinZoomPreference(13.5f);
            googleMap.setMaxZoomPreference(16.0f);
        }
    }
}