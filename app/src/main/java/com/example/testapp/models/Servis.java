package com.example.testapp.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Servis implements Parcelable {

    public int servisID;
    public long OIB;
    public String ime;
    public String email;
    public String adresa;
    public String telefon;

    public Servis(){

    }

    public Servis(int servisID, long OIB, String ime, String email, String adresa, String telefon) {
        this.servisID = servisID;
        this.OIB = OIB;
        this.ime = ime;
        this.email = email;
        this.adresa = adresa;
        this.telefon = telefon;
    }

    protected Servis(Parcel in) {
        servisID = in.readInt();
        OIB = in.readLong();
        ime = in.readString();
        email = in.readString();
        adresa = in.readString();
        telefon = in.readString();
    }

    public static final Creator<Servis> CREATOR = new Creator<Servis>() {
        @Override
        public Servis createFromParcel(Parcel in) {
            return new Servis(in);
        }

        @Override
        public Servis[] newArray(int size) {
            return new Servis[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(servisID);
        dest.writeLong(OIB);
        dest.writeString(ime);
        dest.writeString(email);
        dest.writeString(adresa);
        dest.writeString(telefon);
    }
}
