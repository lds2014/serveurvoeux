package models;


public class ShowAllTeacherOnWishSelected {

    public String nom;
    public String prenom;
    public Double heures_cours;
    public Double heures_td;

    public ShowAllTeacherOnWishSelected() {
        this.nom = "";
        this.prenom = "";
        this.heures_cours = 0.0;
        this.heures_td = 0.0;
    }

    public ShowAllTeacherOnWishSelected(String nom, String prenom, Double heures_cours, Double heures_td) {
        this.nom = nom;
        this.prenom = prenom;
        this.heures_cours = heures_cours;
        this.heures_td = heures_td;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Double getHeures_cours() {
        return heures_cours;
    }

    public Double getHeures_td() {
        return heures_td;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setHeures_cours(Double heures_cours) {
        this.heures_cours = heures_cours;
    }

    public void setHeures_td(Double heures_td) {
        this.heures_td = heures_td;
    }
}
