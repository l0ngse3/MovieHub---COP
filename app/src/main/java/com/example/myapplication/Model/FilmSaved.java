package com.example.myapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilmSaved {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("idFilm")
    @Expose
    private String idFilm;

    public FilmSaved() {
    }

    public FilmSaved(String idFilm, String username) {
        this.username = username;
        this.idFilm = idFilm;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getIdFilm() {
        return idFilm;
    }
    public void setIdFilm(String idFilm) {
        this.idFilm = idFilm;
    }

}
