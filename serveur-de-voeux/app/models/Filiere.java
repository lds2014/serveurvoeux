package models;


import java.util.*;

import play.db.ebean.Model;
import javax.persistence.*;

import play.api.db.DB;
import play.db.*;


@Entity
public class Filiere extends Model{

    @Id
    private String codefiliere;
    private String libelle_filiere;
    private String voie;
    private String ref_ufr;
    public @OneToOne @JoinColumn(name = "person_charge_id") PersonInCharge personInCharge;

    public Filiere()
    {
        this.codefiliere = "";
        this.libelle_filiere = "";
        this.voie = "";
        this.ref_ufr="";
    }

    public Filiere(String codefiliere, String libelle_filiere, String voie, String ref_ufr) {
        this.codefiliere = codefiliere;
        this.libelle_filiere = libelle_filiere;
        this.voie = voie;
        this.ref_ufr = ref_ufr;
    }

    public String getCodefiliere() {
        return codefiliere;
    }

    public void setCodefiliere(String codefiliere) {
        this.codefiliere = codefiliere;
    }

    public String getVoie() {
        return voie;
    }

    public void setVoie(String voie) {
        this.voie = voie;
    }

    public String getLibelle_filiere() {
        return libelle_filiere;
    }

    public void setLibelle_filiere(String libelle_filiere) {
        this.libelle_filiere = libelle_filiere;
    }

    public String getRef_ufr() {
        return ref_ufr;
    }

    public void setRef_ufr(String ref_ufr) {
        this.ref_ufr = ref_ufr;
    }

    /**
     * Interaction avec la table filière
     */
    public static Finder<String,Filiere> find = new Finder<String,Filiere>(
            String.class, Filiere.class
    );

    /**
     * Récupére la liste des filières
     * @return List Filiere
     */
    public static List<Filiere> findALL(){

        return Filiere.find.orderBy("CODEFILIERE ASC").findList();
    }

    public static List<Filiere> findInvolving(String rUFR) {
        return find.where()
                .eq("ref_ufr", rUFR)
                .orderBy("CODEFILIERE ASC")
                .findList();
    }

    public static Filiere findFiliereById(String rFiliere) {
        return find.byId(rFiliere);
    }

    public static Filiere getFiliereId(int personInChargeId){
        return find.where()
                .eq("personInCharge.id",personInChargeId)
                .findUnique();
    }
}
