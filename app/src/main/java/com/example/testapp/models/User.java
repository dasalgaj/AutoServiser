package com.example.testapp.models;

public class User {

    public int servisID;
    public String type;
    public String ime;
    public String prezime;
    public String email;
    public String password;

    public User(){

    }

    public User(int servisID, String type, String ime, String prezime, String email, String password) {
        this.servisID = servisID;
        this.type = type;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.password = password;
    }

    //SETTER
    public void setServisID(int servisID) {
        this.servisID = servisID;
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

    public void setPassword(String password) {
        this.password = password;
    }

    //GETTER
    public int getServisID() {
        return servisID;
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

    public String getPassword() {
        return password;
    }
}
