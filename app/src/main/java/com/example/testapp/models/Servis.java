package com.example.testapp.models;

public class Servis {

    public int servisID;
    public long OIB;
    public String ime;
    public String email;
    public String adresa;
    public String telefon;

    public Servis(int servisID, long OIB, String ime, String email, String adresa, String telefon) {
        this.servisID = servisID;
        this.OIB = OIB;
        this.ime = ime;
        this.email = email;
        this.adresa = adresa;
        this.telefon = telefon;
    }
}
