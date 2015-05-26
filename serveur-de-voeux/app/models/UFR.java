package models;

import play.db.ebean.Model;

import javax.persistence.*;

import play.api.db.DB;
import play.db.*;
import java.util.*;

@Entity
public class UFR extends Model {

    @Id
    private String codeufr;

    private String libelle_ufr;

    public UFR()
    {
        this.libelle_ufr = "";
        this.codeufr = "";
    }

    public UFR(String libelle_ufr, String codeufr) {
        this.libelle_ufr = libelle_ufr;
        this.codeufr = codeufr;
    }

    public String getCodeufr() {
        return codeufr;
    }

    public void setCodeufr(String codeufr) {
        this.codeufr = codeufr;
    }

    public String getLibelle_ufr() {
        return libelle_ufr;
    }

    public void setLibelle_ufr(String libelle_ufr) {
        this.libelle_ufr = libelle_ufr;
    }

    public static Finder<String,UFR> find = new Finder(
            String.class, UFR.class
    );

    /**
     * Récupére la liste des UFR
     * @return List UFR
     */
    public static List<UFR> findALL(){

        return UFR.find.orderBy("CODEUFR ASC").findList();
    }
}
