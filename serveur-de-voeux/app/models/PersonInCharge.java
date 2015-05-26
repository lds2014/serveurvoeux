package models;

import play.db.ebean.Model;

import javax.persistence.*;

@Entity(name = "persons_charges")
public class PersonInCharge extends Model {
    private enum WorkloadAssignation {
        EXTRA_TIME,
        NORMAL_TIME;
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO) public int id;
    public @ManyToOne(optional = false) @JoinColumn(name = "teacher_id") Teacher teacher;
    public @ManyToOne(optional = false) @JoinColumn(name = "charge_id") Charge charge;
    public @ManyToOne(optional = false) @JoinColumn(name = "period_id") Period period;
    public @Column(nullable = false) @Enumerated(EnumType.STRING) WorkloadAssignation assignation;

    public static Finder<Integer, PersonInCharge> find = new Finder<Integer, PersonInCharge>(
            Integer.class, PersonInCharge.class
    );


    public static boolean personInChargeUsedPeriod(int period) {
        return find.where()
                .eq("period.id", period)
                .findRowCount() > 0;
    }

    public static boolean isRspFiliere(String login){
        return find.where()
                .eq("teacher.user_id",login.toLowerCase())
                .eq("charge.id",1)
                .findRowCount() > 0;

    }

    public static PersonInCharge getPersonInCharge(String login){
        return find.where()
                .eq("teacher.user_id",login.toLowerCase())
                .eq("charge.id",1)
                .findUnique();
    }
}
