package com.example.shtreet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VoieAPI {
    String BASE_URL = "https://adrien-le-corre.emi.u-bordeaux.fr/flask/";
    @GET("voie")
    Call<List<Voie>> getVoies();
    @GET("voie/{NomVoie}")
    Call<Voie> getStreetWithName(@Path("NomVoie") String name);
}
