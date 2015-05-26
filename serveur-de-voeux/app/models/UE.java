package models;

import java.util.*;

import play.db.ebean.Model;
import javax.persistence.*;

import play.api.db.DB;
import play.db.*;

@Entity
public class UE extends Model {

    @Id
    public String code_UE;

    public String libelle_UE;

    public int semestre;

    @ManyToOne
    public String ref_Filiere;

    /**
     * Constructeur par defaut
     */
   /* public UE()
    {
        this.CODEUE = "";
        this.libelle_UE = "";
        this.semestre = 0;
        this.refFiliere= null;
    }*/

    public UE(String codeUE, String libelle_UE, int semestre, String refFiliere) {
        this.code_UE = codeUE;
        this.libelle_UE = libelle_UE;
        this.semestre = semestre;
        this.ref_Filiere=refFiliere;
    }

    public String getCodeUE() {
        return code_UE;
    }

    public void setCodeUE(String codeUE) {
        this.code_UE = codeUE;
    }

    public String getLibelle_UE() {
        return libelle_UE;
    }

    public void setLibelle_UE(String libelle_UE) {
        this.libelle_UE = libelle_UE;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getRefFiliere() {
        return ref_Filiere;
    }

    public void setRefFiliere(String refFiliere) {
        this.ref_Filiere = refFiliere;
    }

    public static List<UE> all() {
        return find.all();
    }

    /*public static void delete(String codeUE) {
        find.ref(codeUE).delete();
    }*/

    /*public static UE create(String codeUE, String libelle_UE, int semestre,String refFi) {
        UE ue = new UE(codeUE,libelle_UE,semestre,Filiere.find.ref(refFi));
        ue.save();
        return ue;
    }*/

    public static List<UE> findInvolving(String rFiliere) {
        return find.where()
                .eq("ref_Filiere", rFiliere)
                .findList();
    }

    public static UE findUEById(String rUe) {
        return find.byId(rUe);
    }

    public static List<UE> findALL(){

        return UE.find.orderBy("CODEUE ASC").findList();
    }

    public static Finder<String,UE> find = new Finder(
            String.class, UE.class
    );
}
