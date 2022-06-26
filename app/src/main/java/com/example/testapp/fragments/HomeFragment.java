package com.example.testapp.fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.activities.HomeActivity;
import com.example.testapp.activities.HomeActivityServiser;
import com.example.testapp.activities.PotvrdaRezervacijeActivity;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private View view;
    static final String CHANNEL_1_ID = "channel1";
    NotificationManagerCompat notificationManager;

    TextView tvOpel, tvNissan, tvCitroen, tvRenault, tvOpelServis, tvNissanServis, tvCitroenServis, tvRenaultServis;
    ImageView ivQRCode;
    Button btnRezervacijaOpel;
    Button btnRezervacijaNissan;
    Button btnRezervacijaCitroen;
    Button btnRezervacijaRenault;
    Button btnQRCodeOpel;
    Button btnQRCodeNissan;
    Button btnQRCodeCitroen;
    Button btnQRCodeRenault;

    DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference mDatabaseServisi;
    DatabaseReference mDatabaseRezervacije = FirebaseDatabase.getInstance().getReference("Rezervacije");
    FirebaseUser userFirebase;
    FirebaseAuth auth;

    List<Servis> servisi = new ArrayList<>();

    String userID, key;

    Context context;

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

                            mDatabaseRezervacije.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotRezervacije) {

                                    if (snapshotRezervacije.exists()) {

                                        for (DataSnapshot dsRezervacije : snapshotRezervacije.getChildren()){

                                            //KADA JE REZERVACIJA NA CEKANJU
                                            if (userProfile.rezervacijaID == dsRezervacije.child("rezervacijaID").getValue(Long.class) && dsRezervacije.child("status").getValue(String.class).equals("Na cekanju")){

                                                if (dsRezervacije.child("servisID").getValue(int.class) == 1){

                                                    tvNissan.setText("Na čekanju");
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


                                            //KADA JE REZERVACIJA POTVRDENA(U TIJEKU)
                                            if (userProfile.rezervacijaID == dsRezervacije.child("rezervacijaID").getValue(Long.class)){

                                                if ((dsRezervacije.child("servisID").getValue(int.class) == 1) && dsRezervacije.child("status").getValue(String.class).equals("U tijeku")){

                                                    tvNissan.setText("Prihvaćeno");
                                                    btnRezervacijaNissan.setVisibility(View.INVISIBLE);
                                                    btnQRCodeNissan.setVisibility(View.VISIBLE);

                                                    btnQRCodeNissan.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            MultiFormatWriter writer = new MultiFormatWriter();

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                            LayoutInflater inflater = LayoutInflater.from(getContext());
                                                            final View viewQR = inflater.inflate(R.layout.layout_qrcode, null);

                                                            //IMAGE VIEW ZA QR CODE
                                                            ivQRCode = viewQR.findViewById(R.id.ivQRCode);

                                                            try {

                                                                BitMatrix matrix = writer.encode(
                                                                        "Podaci servisa: " +  "\n" +
                                                                                "OIB servisa: " + dsRezervacije.child("oibServisa").getValue(Long.class) + "\n" +
                                                                                "Adresa servisa: " + dsRezervacije.child("adresa").getValue(String.class) + "\n" +
                                                                                "Tip servisa: " + dsRezervacije.child("tip").getValue(String.class) + "\n" +
                                                                                "Datum servisa: " + dsRezervacije.child("datum").getValue(String.class) + "\n" +
                                                                                "Vrijeme servisa: " + dsRezervacije.child("vrijeme").getValue(String.class) + "\n" +
                                                                                "\nPodaci korisnika: \n" +
                                                                                "Ime: " + dsRezervacije.child("ime").getValue(String.class) + "\n" +
                                                                                "Prezime: " + dsRezervacije.child("prezime").getValue(String.class) + "\n" +
                                                                                "Mobitel: " + dsRezervacije.child("mobitel").getValue(String.class)
                                                                        , BarcodeFormat.QR_CODE, 350, 350);

                                                                BarcodeEncoder encoder = new BarcodeEncoder();

                                                                Bitmap bitmap = encoder.createBitmap(matrix);

                                                                ivQRCode.setImageBitmap(bitmap);

                                                            } catch (WriterException e) {
                                                                e.printStackTrace();
                                                            }

                                                            builder.setView(viewQR);
                                                            builder.setCancelable(true);
                                                            builder.setTitle("QR Code");
                                                            builder.setPositiveButton("Ok",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {



                                                                        }
                                                                    });

                                                            AlertDialog dialog = builder.create();
                                                            dialog.show();

                                                        }
                                                    });

                                                }
                                                else if ((dsRezervacije.child("servisID").getValue(int.class) == 2) && dsRezervacije.child("status").getValue(String.class).equals("U tijeku")){

                                                    tvCitroen.setText("Prihvaćeno");
                                                    btnRezervacijaCitroen.setVisibility(View.INVISIBLE);
                                                    btnQRCodeCitroen.setVisibility(View.VISIBLE);

                                                    btnQRCodeCitroen.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            MultiFormatWriter writer = new MultiFormatWriter();

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                            LayoutInflater inflater = LayoutInflater.from(getContext());
                                                            final View viewQR = inflater.inflate(R.layout.layout_qrcode, null);

                                                            //IMAGE VIEW ZA QR CODE
                                                            ivQRCode = viewQR.findViewById(R.id.ivQRCode);

                                                            try {

                                                                BitMatrix matrix = writer.encode(
                                                                        "Podaci servisa: " +  "\n" +
                                                                                "OIB servisa: " + dsRezervacije.child("oibServisa").getValue(Long.class) + "\n" +
                                                                                "Adresa servisa: " + dsRezervacije.child("adresa").getValue(String.class) + "\n" +
                                                                                "Tip servisa: " + dsRezervacije.child("tip").getValue(String.class) + "\n" +
                                                                                "Datum servisa: " + dsRezervacije.child("datum").getValue(String.class) + "\n" +
                                                                                "Vrijeme servisa: " + dsRezervacije.child("vrijeme").getValue(String.class) + "\n" +
                                                                                "\nPodaci korisnika: \n" +
                                                                                "Ime: " + dsRezervacije.child("ime").getValue(String.class) + "\n" +
                                                                                "Prezime: " + dsRezervacije.child("prezime").getValue(String.class) + "\n" +
                                                                                "Mobitel: " + dsRezervacije.child("mobitel").getValue(String.class)
                                                                        , BarcodeFormat.QR_CODE, 350, 350);

                                                                BarcodeEncoder encoder = new BarcodeEncoder();

                                                                Bitmap bitmap = encoder.createBitmap(matrix);

                                                                ivQRCode.setImageBitmap(bitmap);

                                                            } catch (WriterException e) {
                                                                e.printStackTrace();
                                                            }

                                                            builder.setView(viewQR);
                                                            builder.setCancelable(true);
                                                            builder.setTitle("QR Code");
                                                            builder.setPositiveButton("Ok",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {



                                                                        }
                                                                    });

                                                            AlertDialog dialog = builder.create();
                                                            dialog.show();

                                                        }
                                                    });

                                                }
                                                else if ((dsRezervacije.child("servisID").getValue(int.class) == 3) && dsRezervacije.child("status").getValue(String.class).equals("U tijeku")){

                                                    tvRenault.setText("Prihvaćeno");
                                                    btnRezervacijaRenault.setVisibility(View.INVISIBLE);
                                                    btnQRCodeRenault.setVisibility(View.VISIBLE);

                                                    btnQRCodeRenault.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            MultiFormatWriter writer = new MultiFormatWriter();

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                            LayoutInflater inflater = LayoutInflater.from(getContext());
                                                            final View viewQR = inflater.inflate(R.layout.layout_qrcode, null);

                                                            //IMAGE VIEW ZA QR CODE
                                                            ivQRCode = viewQR.findViewById(R.id.ivQRCode);

                                                            try {

                                                                BitMatrix matrix = writer.encode(
                                                                        "Podaci servisa: " +  "\n" +
                                                                                "OIB servisa: " + dsRezervacije.child("oibServisa").getValue(Long.class) + "\n" +
                                                                                "Adresa servisa: " + dsRezervacije.child("adresa").getValue(String.class) + "\n" +
                                                                                "Tip servisa: " + dsRezervacije.child("tip").getValue(String.class) + "\n" +
                                                                                "Datum servisa: " + dsRezervacije.child("datum").getValue(String.class) + "\n" +
                                                                                "Vrijeme servisa: " + dsRezervacije.child("vrijeme").getValue(String.class) + "\n" +
                                                                                "\nPodaci korisnika: \n" +
                                                                                "Ime: " + dsRezervacije.child("ime").getValue(String.class) + "\n" +
                                                                                "Prezime: " + dsRezervacije.child("prezime").getValue(String.class) + "\n" +
                                                                                "Mobitel: " + dsRezervacije.child("mobitel").getValue(String.class)
                                                                        , BarcodeFormat.QR_CODE, 350, 350);

                                                                BarcodeEncoder encoder = new BarcodeEncoder();

                                                                Bitmap bitmap = encoder.createBitmap(matrix);

                                                                ivQRCode.setImageBitmap(bitmap);

                                                            } catch (WriterException e) {
                                                                e.printStackTrace();
                                                            }

                                                            builder.setView(viewQR);
                                                            builder.setCancelable(true);
                                                            builder.setTitle("QR Code");
                                                            builder.setPositiveButton("Ok",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {



                                                                        }
                                                                    });

                                                            AlertDialog dialog = builder.create();
                                                            dialog.show();

                                                        }
                                                    });

                                                }
                                                else if ((dsRezervacije.child("servisID").getValue(int.class) == 4) && dsRezervacije.child("status").getValue(String.class).equals("U tijeku")){

                                                    tvOpel.setText("Prihvaćeno");
                                                    btnRezervacijaOpel.setVisibility(View.INVISIBLE);
                                                    btnQRCodeOpel.setVisibility(View.VISIBLE);

                                                    btnQRCodeOpel.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            MultiFormatWriter writer = new MultiFormatWriter();

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                            LayoutInflater inflater = LayoutInflater.from(getContext());
                                                            final View viewQR = inflater.inflate(R.layout.layout_qrcode, null);

                                                            //IMAGE VIEW ZA QR CODE
                                                            ivQRCode = viewQR.findViewById(R.id.ivQRCode);

                                                            try {

                                                                BitMatrix matrix = writer.encode(
                                                                        "Podaci servisa: " +  "\n" +
                                                                                "OIB servisa: " + dsRezervacije.child("oibServisa").getValue(Long.class) + "\n" +
                                                                                "Adresa servisa: " + dsRezervacije.child("adresa").getValue(String.class) + "\n" +
                                                                                "Tip servisa: " + dsRezervacije.child("tip").getValue(String.class) + "\n" +
                                                                                "Datum servisa: " + dsRezervacije.child("datum").getValue(String.class) + "\n" +
                                                                                "Vrijeme servisa: " + dsRezervacije.child("vrijeme").getValue(String.class) + "\n" +
                                                                                "\nPodaci korisnika: \n" +
                                                                                "Ime: " + dsRezervacije.child("ime").getValue(String.class) + "\n" +
                                                                                "Prezime: " + dsRezervacije.child("prezime").getValue(String.class) + "\n" +
                                                                                "Mobitel: " + dsRezervacije.child("mobitel").getValue(String.class)
                                                                        , BarcodeFormat.QR_CODE, 350, 350);

                                                                BarcodeEncoder encoder = new BarcodeEncoder();

                                                                Bitmap bitmap = encoder.createBitmap(matrix);

                                                                ivQRCode.setImageBitmap(bitmap);

                                                            } catch (WriterException e) {
                                                                e.printStackTrace();
                                                            }

                                                            builder.setView(viewQR);
                                                            builder.setCancelable(true);
                                                            builder.setTitle("QR Code");
                                                            builder.setPositiveButton("Ok",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {



                                                                        }
                                                                    });

                                                            AlertDialog dialog = builder.create();
                                                            dialog.show();

                                                        }
                                                    });

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


        mDatabaseUsers.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotUsers) {

                User userProfile = snapshotUsers.getValue(User.class);

                if (userProfile != null){

                    mDatabaseRezervacije.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotRezervacije) {

                            //KADA JE REZERVACIJA OBAVLJENA

                            for (DataSnapshot dsRezervacije : snapshotRezervacije.getChildren()) {

                                if (userProfile.rezervacijaID == dsRezervacije.child("rezervacijaID").getValue(Long.class) && dsRezervacije.child("status").getValue(String.class).equals("ObavljenoN")) {

                                    if (dsRezervacije.child("servisID").getValue(int.class) == 1) {

                                        btnQRCodeNissan.setVisibility(View.INVISIBLE);
                                        btnRezervacijaNissan.setVisibility(View.VISIBLE);
                                        tvNissan.setText("Rezervacija");

                                        sendOnChannel1(tvNissanServis.getText().toString(), dsRezervacije.child("tip").getValue(String.class));

                                        updateStatus(dsRezervacije.getKey());

                                    } else if (dsRezervacije.child("servisID").getValue(int.class) == 2) {

                                        btnQRCodeCitroen.setVisibility(View.INVISIBLE);
                                        btnRezervacijaCitroen.setVisibility(View.VISIBLE);
                                        tvCitroen.setText("Rezervacija");

                                        sendOnChannel1(tvCitroenServis.getText().toString(), dsRezervacije.child("tip").getValue(String.class));

                                        updateStatus(dsRezervacije.getKey());

                                    } else if (dsRezervacije.child("servisID").getValue(int.class) == 3) {

                                        btnQRCodeRenault.setVisibility(View.INVISIBLE);
                                        btnRezervacijaRenault.setVisibility(View.VISIBLE);
                                        tvRenault.setText("Rezervacija");

                                        sendOnChannel1(tvRenaultServis.getText().toString(), dsRezervacije.child("tip").getValue(String.class));

                                        updateStatus(dsRezervacije.getKey());

                                    } else if (dsRezervacije.child("servisID").getValue(int.class) == 4) {

                                        btnQRCodeOpel.setVisibility(View.INVISIBLE);
                                        btnRezervacijaOpel.setVisibility(View.VISIBLE);
                                        tvOpel.setText("Rezervacija");

                                        sendOnChannel1(tvOpelServis.getText().toString(), dsRezervacije.child("tip").getValue(String.class));

                                        updateStatus(dsRezervacije.getKey());

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        btnQRCodeOpel = view.findViewById(R.id.btnQRCode1);
        btnQRCodeNissan = view.findViewById(R.id.btnQRCode2);
        btnQRCodeCitroen = view.findViewById(R.id.btnQRCode3);
        btnQRCodeRenault = view.findViewById(R.id.btnQRCode4);

        //TEXT VIEW
        tvOpel = view.findViewById(R.id.tvRezervacija1);
        tvNissan = view.findViewById(R.id.tvRezervacija2);
        tvCitroen = view.findViewById(R.id.tvRezervacija3);
        tvRenault = view.findViewById(R.id.tvRezervacija4);
        tvOpelServis = view.findViewById(R.id.tvOpelServis);
        tvNissanServis = view.findViewById(R.id.tvNissanServis);
        tvCitroenServis = view.findViewById(R.id.tvCitroenServis);
        tvRenaultServis = view.findViewById(R.id.tvRenaultServis);

        //DATABASE
        mDatabaseServisi = FirebaseDatabase.getInstance().getReference().child("Servisi");
        auth = FirebaseAuth.getInstance();

        //NOTIFICATION
        notificationManager = NotificationManagerCompat.from(getContext());

        context = getActivity();

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
                servis.radnoVrijeme = servisi.get(i).radnoVrijeme;

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
                servis.radnoVrijeme = servisi.get(i).radnoVrijeme;

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
                servis.radnoVrijeme = servisi.get(i).radnoVrijeme;

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
                servis.radnoVrijeme = servisi.get(i).radnoVrijeme;

                intentRezervacija.putExtra("servis", servis);

                startActivity(intentRezervacija);
            }
        }

    }

    public void sendOnChannel1(String imeServisa, String tipServisa){

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(imeServisa)
                .setContentText(tipServisa + " na vašem automobilu je obavljen")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);

    }

    public void updateStatus(String key){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Rezervacije").child(key);
        Map<String, Object> updates = new HashMap<String,Object>();

        updates.put("status", "Obavljeno");

        ref.updateChildren(updates);

    }
}