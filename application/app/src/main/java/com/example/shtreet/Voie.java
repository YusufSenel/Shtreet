package com.example.shtreet;

import com.google.gson.annotations.SerializedName;

public class Voie {

    @SerializedName("NomVoie")
    private String name;

    @SerializedName("NomVoieMaj")
    private String upperCaseName;

    @SerializedName("NomVoieSimple")
    private String simpleName;

    @SerializedName("Historique")
    private String history;

    @SerializedName("TypeVoie")
    private String streetType;

    @SerializedName("VoieDescription")
    private String streetDescription;

    @SerializedName("idArrondissement")
    private int idArrondissement;

    @SerializedName("idVoie")
    private int idVoie;

    public String getName() {
        return name;
    }

    public String getUpperCaseName() {
        return upperCaseName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getHistory() {
        return history;
    }

    public String getStreetType() {
        return streetType;
    }

    public String getStreetDescription() {
        return streetDescription;
    }

    public int getIdArrondissement() {
        return idArrondissement;
    }

    public int getIdVoie() {
        return idVoie;
    }
}
