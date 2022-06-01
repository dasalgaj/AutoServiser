package com.example.testapp.models;

public class User {

    public int servisID;
    public long rezervacijaID;
    public String type;
    public String ime;
    public String prezime;
    public String email;
    public String mobitel;
    public String password;

    public User(){

    }

    public User(int servisID, long rezervacijaID, String type, String ime, String prezime, String email, String mobitel, String password) {
        this.servisID = servisID;
        this.rezervacijaID = rezervacijaID;
        this.type = type;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.mobitel = mobitel;
        this.password = password;
    }

    //SETTER
    public void setServisID(int servisID) {
        this.servisID = servisID;
    }

    public void setRezervacijaID(long rezervacijaID) {
        this.rezervacijaID = rezervacijaID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobitel(String mobitel) {
        this.mobitel = mobitel;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //GETTER
    public int getServisID() {
        return servisID;
    }

    public long getRezervacijaID() {
        return rezervacijaID;
    }

    public String getType() {
        return type;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getEmail() {
        return email;
    }

    public String getMobitel() {
        return mobitel;
    }

    public String getPassword() {
        return password;
    }
}
