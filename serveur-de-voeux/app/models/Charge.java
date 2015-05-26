package models;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity(name = "charges")
public class Charge extends Model {
    @Id @GeneratedValue(strategy= GenerationType.AUTO)public int id;
    @Column(nullable = false) public String name;
    public int workload;
    @ManyToOne(optional = false) @JoinColumn(name = "period_id") Period period;

    public static Finder<Integer, Charge> find = new Finder<Integer, Charge>(
            Integer.class, Charge.class
    );

    public static boolean chargeUsedPeriod(int period) {
        return find.where()
                .eq("period.id", period)
                .findRowCount() > 0;
    }

    public Charge(String libelle,int heure){
        this.name=libelle;
        this.workload=heure;
    }
    public Charge(){
        this.name="";
        this.workload=0;
    }

    public String getName() {
        return name;
    }

    public int getWorkload() {
        return workload;
    }

    public static Charge create(String libelle, int heure){
        Charge charge= new Charge(libelle,heure);
        Ebean.save(charge);

        return charge;
    }

    public static Charge update(String libelle, int heure){
        Charge charge= find.where()
                .eq("NAME",libelle)
                .findUnique();
        charge.setName(libelle);
        charge.setWorkload(heure);
        charge.save();
        return charge;
    }

    public static void deleteCharge(String libelle){
        Charge charge=find.where()
                .eq("NAME",libelle)
                .findUnique();
        Ebean.delete(charge);
    }

    public static Charge findCharge(int idcharge)
    {
        return find.byId(idcharge);
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

}
