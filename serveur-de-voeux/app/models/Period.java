package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity(name = "periods")
public class Period extends Model {

    //The opened period is the current period
    //The closed period is last period which had been ope
    private enum Status {
        OPEN,
        CLOSED;
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO) public int id;
    @Column(nullable = false) public String name;
    @Column(nullable = false) public Date starts;
    @Column(nullable = false) @Enumerated(EnumType.STRING) public Status status;
    public Date end;

    public Period(String name, String start, String end) throws ParseException {
        this.name = name;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        this.starts = format.parse(start);
        this.end = format.parse(end);
        this.status = Status.OPEN;
    }

    public static Finder<Integer, Period> find = new Finder<Integer, Period>(Integer.class,Period.class);

    public static Period create(String name, String start, String end) throws ParseException {
        Period p = new Period(name, start, end);
        p.save();
        return p;
    }

    public static Period update(int pId, String name, String start, String end) throws ParseException {
        Period p = find.ref(pId);
        if((p.status == Status.OPEN) /*&& (UnAvailability.unAvailabilityUsedPeriod(pId)||Voeux.wishesUsedPeriod(pId)||
                PersonInCharge.personInChargeUsedPeriod(pId)||Charge.chargeUsedPeriod(pId))*/) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            p.name = name;
            p.starts = format.parse(start);
            p.end = format.parse(end);
            p.save();
            return p;
        }
        return null;
    }

    public static Period findPeriod(java.sql.Date theDay)throws ParseException{
        return find.where()
                .betweenProperties("starts","end",theDay)
                .eq("status", Status.OPEN)
                .findUnique();
    }

    public static Period setClosed(int period){
        Period p = find.ref(period);
        p.status = Status.CLOSED;
        p.update();
        return p;
    }

    public static Period delete(int pId){
        /*if (UnAvailability.unAvailabilityUsedPeriod(pId)||Voeux.wishesUsedPeriod(pId)||
                PersonInCharge.personInChargeUsedPeriod(pId)||Charge.chargeUsedPeriod(pId)) {*/
        Period p = Period.find.ref(pId);
        p.delete();
        return p;
        // }
        //return null;
    }
    public static Period finById(int pId){
        return Period.find.ref(pId);
    }

    /*
    * La création d'une nouvelle période ferme automatiquement l'ancienne période
    * Ce qui implique qu'on ne peut plus créer de de voeux ni de préférence dit de
    * cpf lié à cette période
    * Il faudra le préciser à l'administrateur à chaque création d'une nouvelle période
    */

    /*
    *
    *
    * */

    public static List<Period> findOpenedPeriod() {

        return find.where()
                .eq("status", Status.OPEN)
                .findList();

    }

    public static List<Period> findClosedPeriod() {
        return find.where()
                .eq("status", Status.CLOSED)
                .findList();

    }

    public static List<Period> findAllPeriod() {
        return  find.all();
    }

    public boolean openedPeriod(){
        return status == Status.OPEN;
    }

}

