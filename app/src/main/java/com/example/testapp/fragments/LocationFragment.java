package com.example.testapp.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testapp.R;
import com.example.testapp.listeners.LoginListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationFragment extends Fragment {

    public LoginListener loginListener;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng opel = new LatLng(45.8206169, 17.3845614);
            googleMap.addMarker(new MarkerOptions().position(opel).title("Opel servis").snippet("Vukovarska cesta 2"));

            LatLng nissan = new LatLng(45.8218038, 17.3934923);
            googleMap.addMarker(new MarkerOptions().position(nissan).title("Nissan servis").snippet("Vinkovačka cesta 13"));

            LatLng citroen = new LatLng(45.830199, 17.3846591);
            googleMap.addMarker(new MarkerOptions().position(citroen).title("Citroen servis").snippet("Ul. Ljudevita Gaja 13"));

            LatLng renault = new LatLng(45.8395842, 17.3606061);
            googleMap.addMarker(new MarkerOptions().position(renault).title("Renault servis").snippet("Ul. Josipa Jurja Strossmayera 188"));

            googleMap.getUiSettings().setScrollGesturesEnabled(false);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(citroen));

            googleMap.setMinZoomPreference(12.5f);
            googleMap.setMaxZoomPreference(14.0f);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}