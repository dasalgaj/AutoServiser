package com.example.testapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.testapp.R;
import com.example.testapp.models.CustomTimePickerDialog;
import com.example.testapp.models.Servis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RezervacijaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView tvAutoServis;
    TextView tvDatum;
    TextView tvVrijeme;
    Button mBtnDalje;
    Button mBtnDatum;
    Button mBtnVrijeme;
    AutoCompleteTextView autoCompleteTextView;

    boolean provjera;
    String tipServisa, datumServisa, vrijemeServisa, adresaServisa, imeServisa, radnoVrijeme;
    long oibServisa;
    int servisID;

    DatabaseReference mDatabaseRezervacije = FirebaseDatabase.getInstance().getReference("Rezervacije");

    ArrayAdapter<String> adapterItems;

    String[] tipoviServisa = new String[]{"Mali servis", "Veliki servis"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervacija);

        //TEXT VIEWS
        tvAutoServis = findViewById(R.id.tvAutoServis);
        tvDatum = findViewById(R.id.tvDatum);
        tvVrijeme = findViewById(R.id.tvVrijeme);

        //BUTTONS
        mBtnDatum = findViewById(R.id.btnOdaberiDatum);
        mBtnVrijeme = findViewById(R.id.btnOdaberiVrijeme);
        mBtnDalje = findViewById(R.id.btnRezervacijaDalje);

        //AUTOCOMPLETE TEXT VIEW
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        Servis servis = getIntent().getParcelableExtra("servis");
        tvAutoServis.setText(servis.ime + " servis");

        adapterItems = new ArrayAdapter<String>(this, R.layout.dropdown_item, tipoviServisa);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });

        //DALJE NA POTVRDU REZERVACIJE
        mBtnDalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCompleteTextView.getText().toString().equals("Tip servisa") || tvDatum.getText().toString().isEmpty() || tvVrijeme.getText().toString().isEmpty()){
                    Toast.makeText(RezervacijaActivity.this, "Morate popuniti sve podatke", Toast.LENGTH_SHORT).show();
                    return;
                }

                tipServisa = autoCompleteTextView.getText().toString();
                datumServisa = tvDatum.getText().toString();
                vrijemeServisa = tvVrijeme.getText().toString();
                adresaServisa = servis.adresa;
                imeServisa = servis.ime;
                servisID = servis.servisID;
                radnoVrijeme = servis.radnoVrijeme;
                oibServisa = servis.OIB;

                String[] radnaVremena = radnoVrijeme.split("-");
                radnaVremena[0] = radnaVremena[0].trim();
                radnaVremena[1] = radnaVremena[1].trim();

                mDatabaseRezervacije.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){

                            for (DataSnapshot ds : snapshot.getChildren()){

                                if (ds.child("datum").getValue(String.class).equals(datumServisa) && ds.child("vrijeme").getValue(String.class).equals(vrijemeServisa)
                                        && ds.child("servisID").getValue(Long.class) == servisID && (ds.child("status").getValue(String.class).equals("Na cekanju") || ds.child("status").getValue(String.class).equals("U tijeku"))){

                                    Toast.makeText(RezervacijaActivity.this, "Vrijeme je zauzeto", Toast.LENGTH_SHORT).show();

                                    provjera = false;
                                    break;

                                }
                                else{

                                    provjera = true;

                                }

                            }

                            mDatabaseRezervacije.removeEventListener(this);

                            if (provjera){

                                if (checkTime(radnaVremena[0], radnaVremena[1], vrijemeServisa)){

                                    Intent i = new Intent(RezervacijaActivity.this, PotvrdaRezervacijeActivity.class);
                                    i.putExtra("tip", tipServisa);
                                    i.putExtra("datum", datumServisa);
                                    i.putExtra("vrijeme", vrijemeServisa);
                                    i.putExtra("adresa", adresaServisa);
                                    i.putExtra("imeServisa", imeServisa);
                                    i.putExtra("servisID", servisID);
                                    i.putExtra("oibServisa", oibServisa);
                                    startActivity(i);

                                }
                                else{

                                    Toast.makeText(RezervacijaActivity.this, "Izvan radnog vremena", Toast.LENGTH_SHORT).show();

                                }

                            }

                        }
                        else{

                            Intent i = new Intent(RezervacijaActivity.this, PotvrdaRezervacijeActivity.class);
                            i.putExtra("tip", tipServisa);
                            i.putExtra("datum", datumServisa);
                            i.putExtra("vrijeme", vrijemeServisa);
                            i.putExtra("adresa", adresaServisa);
                            i.putExtra("imeServisa", imeServisa);
                            i.putExtra("servisID", servisID);
                            i.putExtra("oibServisa", oibServisa);
                            startActivity(i);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        //POSTAVI DATUM
        mBtnDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //POSTAVI VRIJEME
        mBtnVrijeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(RezervacijaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvVrijeme.setText( selectedHour + ":0" + selectedMinute);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Odaberi vrijeme");
                mTimePicker.show();
            }
        });

    }

    private boolean checkTime(String r1, String r2, String r3){

        try {
            Date time1 = new SimpleDateFormat("HH").parse(r1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);


            Date time2 = new SimpleDateFormat("HH").parse(r2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            Date d = new SimpleDateFormat("HH").parse(r3);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime()) || x.equals(calendar1.getTime())) {

                return true;

            }
            else
            {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return false;
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        //MIN DATUM
        final Calendar c = Calendar.getInstance();

        int yearMIN = c.get(Calendar.YEAR);
        int monthMIN = c.get(Calendar.MONTH);
        int dayMIN = c.get(Calendar.DAY_OF_MONTH);

        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.DAY_OF_MONTH, dayMIN + 1);
        minDate.set(Calendar.MONTH, monthMIN);
        minDate.set(Calendar.YEAR, yearMIN);

        //MAX DATUM
        int yearMAX = c.get(Calendar.YEAR);
        int monthMAX = c.get(Calendar.MONTH);
        int dayMAX = c.get(Calendar.DAY_OF_MONTH);

        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.DAY_OF_MONTH, dayMAX);
        maxDate.set(Calendar.MONTH, monthMAX);
        maxDate.set(Calendar.YEAR, yearMAX + 1);

        //MIN, MAX DATUM
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        tvDatum.setText(date);
    }
}