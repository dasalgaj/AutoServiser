package com.example.testapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.adapter.RezervacijeRVAdapter;
import com.example.testapp.listeners.LoginListener;
import com.example.testapp.models.Rezervacija;
import com.example.testapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragmentServiser extends Fragment {

    private View view;

    TextView tvEmpty;
    RecyclerView mRecyclerView;
    RezervacijeRVAdapter rezervacijeAdapter;

    DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference mDatabaseRezervacije = FirebaseDatabase.getInstance().getReference("Rezervacije");
    FirebaseUser userFirebase;

    ArrayList<Rezervacija> rezervacije = new ArrayList<>();
    String userID;

    public LoginListener loginListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_serviser, container, false);

        //TEXT VIEWS
        tvEmpty = view.findViewById(R.id.tvEmpty);

        //RECYCLER VIEW
        mRecyclerView = view.findViewById(R.id.recyclerViewRezervacije);

        if (mDatabaseRezervacije != null){

            userFirebase = FirebaseAuth.getInstance().getCurrentUser();
            userID = userFirebase.getUid();

            mDatabaseUsers.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotUsers) {

                    if (snapshotUsers.exists()){

                        User userProfile = snapshotUsers.getValue(User.class);

                        if (userProfile != null){

                            mDatabaseRezervacije.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotRezervacije) {

                                    if (snapshotRezervacije.exists()){

                                        rezervacije.clear();

                                        //RV Rezervacije
                                        rezervacijeAdapter = new RezervacijeRVAdapter(rezervacije, tvEmpty, getContext());

                                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        mRecyclerView.setAdapter(rezervacijeAdapter);

                                        for (DataSnapshot dsRezervacije : snapshotRezervacije.getChildren()){

                                            if (userProfile.servisID == dsRezervacije.child("servisID").getValue(Long.class) && dsRezervacije.child("status").getValue(String.class).equals("Na cekanju")){

                                                Rezervacija rezervacija = dsRezervacije.getValue(Rezervacija.class);

                                                rezervacije.add(rezervacija);

                                                //RV Rezervacije
                                                rezervacijeAdapter = new RezervacijeRVAdapter(rezervacije, tvEmpty, getContext());

                                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                mRecyclerView.setAdapter(rezervacijeAdapter);

                                            }

                                        }

                                        if (rezervacijeAdapter.getItemCount() == 0){
                                            tvEmpty.setText("Trenutno nema rezervacija na čekanju");
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

        return view;
    }
}