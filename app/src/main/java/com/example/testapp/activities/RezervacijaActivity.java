package com.example.testapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.example.testapp.models.Servis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RezervacijaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView tvAutoServis;
    TextView tvDatum;
    TextView tvVrijeme;
    Button mBtnDatum;
    Button mBtnVrijeme;
    AutoCompleteTextView autoCompleteTextView;

    int t1hour, t1minute;

    ArrayAdapter<String> adapterItems;

    String[] tipoviServisa = new String[]{"Mali servis", "Veliki servis", "Servis guma"};

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

        mBtnDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        mBtnVrijeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        RezervacijaActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t1hour = hourOfDay;
                                t1minute = 00;

                                String time = t1hour + ":" + t1minute;

                                SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );

                                try {
                                    Date date = f24Hours.parse(time);
                                    SimpleDateFormat f12hours = new SimpleDateFormat(
                                            "HH:mm aa"
                                    );
                                    tvVrijeme.setText(f12hours.format(date));
                                }
                                catch(ParseException e){
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                timePickerDialog.updateTime(t1hour, t1minute);

                timePickerDialog.show();
            }
        });

    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        tvDatum.setText(date);
    }
}