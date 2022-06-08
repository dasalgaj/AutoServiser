package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.Rezervacija;

import java.util.ArrayList;

public class ObavljenoRVAdapter extends RecyclerView.Adapter<ObavljenoRVAdapter.ViewHolder>{

    private ArrayList<Rezervacija> rezervacijaArrayList;
    private Context context;

    public ObavljenoRVAdapter(ArrayList<Rezervacija> rezervacijaArrayList, Context context) {
        this.rezervacijaArrayList = rezervacijaArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ObavljenoRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obavljeno_rv_item, parent,
                false);

        return new ObavljenoRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObavljenoRVAdapter.ViewHolder holder, int position) {
        Rezervacija rezervacije = rezervacijaArrayList.get(position);

        holder.mTipServisa.setText("Tip servisa: " + rezervacije.getTip());
        holder.mDatumServisa.setText("Datum servisa: " + rezervacije.getDatum());
        holder.mVrijemeServisa.setText("Vrijeme servisa: " + rezervacije.getVrijeme());
        holder.mImePrezime.setText("Ime i prezime: " + rezervacije.getIme() + " " + rezervacije.getPrezime());
        holder.mMobitel.setText("Mobitel: " + rezervacije.getMobitel());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTipServisa = itemView.findViewById(R.id.tvTipObavljeno);
            mDatumServisa = itemView.findViewById(R.id.tvDatumObavljeno);
            mVrijemeServisa = itemView.findViewById(R.id.tvVrijemeObavljeno);
            mImePrezime = itemView.findViewById(R.id.tvImePrezimeObavljeno);
            mMobitel = itemView.findViewById(R.id.tvMobitelObavljeno);
        }
    }
}
