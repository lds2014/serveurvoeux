package models;

import java.util.*;

import play.db.ebean.Model;
import javax.persistence.*;

import play.api.db.DB;
import play.db.*;

@Entity
public class EC extends Model {

    @Id
    public String code_Ec;

    public String libelle_ec;

    public double ects;

    public int heures_cours;

    public int heures_tds;

    public int heures_projet;

    public int heures_exam;

    public String ref_UE;

    public EC()
    {
        this.code_Ec = "";
        this.libelle_ec = "";
        this.ects = 3;
        this.heures_cours = 0;
        this.heures_tds = 0;
        this.heures_projet = 0;
        this.heures_exam = 0;
        this.ref_UE = "";
    }

    public EC(String codeEc, String libelle_ec, double ects, int heures_cours, int heures_tds,int heures_projet, int heures_exam, String refUE) {
        this.code_Ec = codeEc;
        this.libelle_ec = libelle_ec;
        this.ects = ects;
        this.heures_cours = heures_cours;
        this.heures_tds=heures_tds;
        this.heures_projet = heures_projet;
        this.heures_exam = heures_exam;
        this.ref_UE = refUE;
    }

    public String getCode_Ec() {
        return code_Ec;
    }

    public void setCode_Ec(String code_Ec) {
        this.code_Ec = code_Ec;
    }

    public String getLibelle_ec() {
        return libelle_ec;
    }

    public void setLibelle_ec(String libelle_ec) {
        this.libelle_ec = libelle_ec;
    }

    public double getEcts() {
        return ects;
    }

    public void setEcts(double ects) {
        this.ects = ects;
    }

    public int getHeures_cours() {
        return heures_cours;
    }

    public void setHeures_cours(int heures_cours) {
        this.heures_cours = heures_cours;
    }

    public int getHeures_tds() {return heures_tds;}

    public void setHeures_tds(int heures_tds) {this.heures_tds = heures_tds;}

    public int getHeures_projet() {
        return heures_projet;
    }

    public void setHeures_projet(int heures_projet) {
        this.heures_projet = heures_projet;
    }

    public int getHeures_exam() {
        return heures_exam;
    }

    public void setHeures_exam(int heures_exam) {
        this.heures_exam = heures_exam;
    }

    public String getRef_UE() { return ref_UE; }

    public void setRef_UE(String ref_UE) {
        this.ref_UE = ref_UE;
    }

    public static Finder<String,EC> find = new Finder(
            String.class, EC.class
    );

    public static List<EC> all(){
        return find.all();
    }

    public static EC create(String codeEc, String libelle_ec, double ects, int heures_cours,int heures_tds, int heures_projet, int heures_exam, String ref_UE) {
        EC ec = new EC(codeEc,libelle_ec,ects,heures_cours,heures_tds,heures_projet,heures_exam,ref_UE);
        ec.save();
        return ec;
    }

    public static List<EC> findInvolving(String rUE) {
        return find.where()
                .eq("ref_UE",rUE)
                .findList();
    }

    public static EC findECById(String rEC) {
        return find.byId(rEC);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EC)) return false;
        if (!super.equals(o)) return false;

        EC ec = (EC) o;

        if (Double.compare(ec.ects, ects) != 0) return false;
        if (heures_cours != ec.heures_cours) return false;
        if(heures_tds!=ec.heures_tds) return false;
        if (heures_exam != ec.heures_exam) return false;
        if (heures_projet != ec.heures_projet) return false;
        if (!code_Ec.equals(ec.code_Ec)) return false;
        if (!libelle_ec.equals(ec.libelle_ec)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + code_Ec.hashCode();
        result = 31 * result + libelle_ec.hashCode();
        temp = Double.doubleToLongBits(ects);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + heures_cours;
        result = 31 * result + heures_tds;
        result = 31 * result + heures_projet;
        result = 31 * result + heures_exam;

        return result;
    }

    public static double getManquesCours(String ec){
        EC myEc = find.where().eq("code_Ec",ec).findUnique();
        return Voeux.getWishHoursCours(ec) - myEc.getHeures_cours();
    }

    public static double getManquesTD(String ec){
        EC myEc = find.where().eq("code_Ec",ec).findUnique();
        return Voeux.getWishHoursTd(ec) - myEc.getHeures_tds();
    }

    public static String getLibelleByRef(String ref){
        EC e=find.where().eq("code_Ec",ref).findUnique();
        return e.getLibelle_ec();
    }
}