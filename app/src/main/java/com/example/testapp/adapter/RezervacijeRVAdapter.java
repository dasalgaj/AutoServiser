package com.example.testapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.FCMSend;
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

public class RezervacijeRVAdapter extends RecyclerView.Adapter<RezervacijeRVAdapter.ViewHolder> {

    private ArrayList<Rezervacija> rezervacijaArrayList;
    private Context context;
    private TextView tvEmpty;

    DatabaseReference mDatabaseRezervacije = FirebaseDatabase.getInstance().getReference("Rezervacije");

    String key;

    public RezervacijeRVAdapter(ArrayList<Rezervacija> rezervacijaArrayList, TextView tvEmpty, Context context) {
        this.rezervacijaArrayList = rezervacijaArrayList;
        this.tvEmpty = tvEmpty;
        this.context = context;
    }

    @NonNull
    @Override
    public RezervacijeRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rezervacije_rv_item, parent,
                false);

        return new RezervacijeRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RezervacijeRVAdapter.ViewHolder holder, int position) {
        Rezervacija rezervacije = rezervacijaArrayList.get(position);

        holder.mTipServisa.setText("Tip servisa: " + rezervacije.getTip());
        holder.mDatumServisa.setText("Datum servisa: " + rezervacije.getDatum());
        holder.mVrijemeServisa.setText("Vrijeme servisa: " + rezervacije.getVrijeme());
        holder.mImePrezime.setText("Ime i prezime: " + rezervacije.getIme() + " " + rezervacije.getPrezime());
        holder.mMobitel.setText("Mobitel: " + rezervacije.getMobitel());

        holder.mBtnServiserPotvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabaseRezervacije.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshotRezervacije) {

                        if (snapshotRezervacije.exists()){

                            for (DataSnapshot dsRezervacije : snapshotRezervacije.getChildren()){

                                if (rezervacije.getRezervacijaID() == dsRezervacije.child("rezervacijaID").getValue(Long.class)
                                        && dsRezervacije.child("status").getValue(String.class).equals("Na cekanju")
                                        && dsRezervacije.child("servisID").getValue(int.class) == rezervacije.servisID){

                                    key = dsRezervacije.getKey();

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Rezervacije").child(key);
                                    Map<String, Object> updates = new HashMap<String,Object>();

                                    updates.put("status", "U tijeku");

                                    ref.updateChildren(updates);

                                }

                            }

                            if (getItemCount() == 0){
                                tvEmpty.setText("Trenutno nema rezervacija na čekanju");
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                String title = "Auto Servis";
                String message = "Vaš servis je prihvaćen";
                FCMSend.pushNotification(
                        context,
                        "eNEwvCzyQ2a1Fh1b1SiS-k:APA91bGPgl9wufItlnTux7b59acKFN31ZbyoEL7x_ujYyVstIW-_ehVWz_r1xelzo3v6psOdlZWV8xuguzsFQMwdgwcmppvjbIW-3HV_e6feYh_XRJOlDIOU86YGUyqHi5rxTfwd5BYu",
                        title,
                        message
                );

            }
        });
    }

    @Override
    public int getItemCount() {
        return rezervacijaArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTipServisa;
        private TextView mDatumServisa;
        private TextView mVrijemeServisa;
        private TextView mImePrezime;
        private TextView mMobitel;
        private Button mBtnServiserPotvrdi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTipServisa = itemView.findViewById(R.id.tvTipServiser);
            mDatumServisa = itemView.findViewById(R.id.tvDatumServiser);
            mVrijemeServisa = itemView.findViewById(R.id.tvVrijemeServiser);
            mImePrezime = itemView.findViewById(R.id.tvImePrezimeServiser);
            mMobitel = itemView.findViewById(R.id.tvMobitelServiser);
            mBtnServiserPotvrdi = itemView.findViewById(R.id.btnServiserPotvrdi);
        }
    }
}
