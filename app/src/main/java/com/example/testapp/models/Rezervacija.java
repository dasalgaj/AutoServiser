package com.example.testapp.models;

public class Rezervacija {

    public long oibServisa;
    public String ime;
    public String prezime;
    public String mobitel;
    public String tip;
    public String datum;
    public String vrijeme;
    public String adresa;
    public String status;
    public int servisID;
    public long rezervacijaID;
    public boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public Rezervacija() {
    }

    public Rezervacija(long oibServisa, String ime, String prezime, String mobitel, String tip, String datum, String vrijeme, String adresa, String status,  int servisID, long rezervacijaID) {
        this.oibServisa = oibServisa;
        this.ime = ime;
        this.prezime = prezime;
        this.mobitel = mobitel;
        this.tip = tip;
        this.datum = datum;
        this.vrijeme = vrijeme;
        this.adresa = adresa;
        this.status = status;
        this.servisID = servisID;
        this.rezervacijaID = rezervacijaID;
        this.expandable = false;
    }

    //GETTER
    public long getOibServisa() {
        return oibServisa;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getMobitel() {
        return mobitel;
    }

    public String getTip() {
        return tip;
    }

    public String getDatum() {
        return datum;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getStatus() {
        return status;
    }

    public int getServisID() {
        return servisID;
    }

    public long getRezervacijaID() {
        return rezervacijaID;
    }

    //SETTER
    public void setOibServisa(long oibServisa) {
        this.oibServisa = oibServisa;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setMobitel(String mobitel) {
        this.mobitel = mobitel;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setServisID(int servisID) {
        this.servisID = servisID;
    }

    public void setRezervacijaID(long rezervacijaID) {
        this.rezervacijaID = rezervacijaID;
    }
}
