package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

        holder.mTipServisa.setText(rezervacije.getTip());
        holder.mDatumServisa.setText(rezervacije.getDatum());
        holder.mVrijemeServisa.setText("Vrijeme servisa: " + rezervacije.getVrijeme());
        holder.mImePrezime.setText("Ime i prezime: " + rezervacije.getIme() + " " + rezervacije.getPrezime());
        holder.mMobitel.setText("Mobitel: " + rezervacije.getMobitel());

        boolean isExpandable= rezervacijaArrayList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
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

        private RelativeLayout relativeLayout;
        private RelativeLayout expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTipServisa = itemView.findViewById(R.id.tvTipObavljeno);
            mDatumServisa = itemView.findViewById(R.id.tvDatumObavljeno);
            mVrijemeServisa = itemView.findViewById(R.id.tvVrijemeObavljeno);
            mImePrezime = itemView.findViewById(R.id.tvImePrezimeObavljeno);
            mMobitel = itemView.findViewById(R.id.tvMobitelObavljeno);

            relativeLayout = itemView.findViewById(R.id.relativeLayoutExpandable);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Rezervacija rezervacija = rezervacijaArrayList.get(getAdapterPosition());
                    rezervacija.setExpandable(!rezervacija.isExpandable());
                    notifyItemChanged(getAdapterPosition());

                }
            });
        }
    }
}
