package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity(name = "unavailabilities")
public class UnAvailability extends Model {
    private enum Frequency {
        ONCE,
        DAILY,
        WEEKLY,
        EVERY_TWO_WEEKS,
        MONTHLY,
    }

    private enum TimeSlot {
        SLOT1("SLOT1", "08:30", "10:30"),
        SLOT2("SLOT2", "10:45", "12:45"),
        SLOT3("SLOT3", "13:30", "15:30"),
        SLOT4("SLOT4", "15:45", "17:45");

        private final String slotid;
        private final String startTime;
        private final String endTime;

        TimeSlot(String slotid, String startTime, String endTime) {

            this.slotid = slotid;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }


    @Id @GeneratedValue(strategy=GenerationType.AUTO) public int id;
    @Column(nullable = false) @Enumerated(EnumType.STRING) public TimeSlot timeSlot;
    @Column(nullable = false) @Enumerated(EnumType.STRING) public Frequency frequency;
    @Column(nullable = false) public Date initialDate;


    @ManyToOne(optional = false) @JoinColumn(name = "teacher_id") Teacher teacher;
    @ManyToOne(optional = false) @JoinColumn(name = "period_id") Period period;

    public static Finder<Integer, UnAvailability> find = new Finder<Integer, UnAvailability>(
            Integer.class, UnAvailability.class
    );

    public UnAvailability(String timeSlot, String frequency, String initialDate, String teacher) throws ParseException {
        switch (timeSlot){
            case "SLOT1": this.timeSlot = TimeSlot.SLOT1;
                          break;
            case "SLOT2": this.timeSlot = TimeSlot.SLOT2;
                          break;
            case "SLOT3": this.timeSlot = TimeSlot.SLOT3;
                          break;
            case "SLOT4": this.timeSlot = TimeSlot.SLOT4;
                          break;
            default     : break;
        }

        switch (frequency){
            case "ONCE"           : this.frequency = Frequency.ONCE;
                                    break;
            case "DAILY"          : this.frequency = Frequency.DAILY;
                                    break;
            case "WEEKLY"         : this.frequency = Frequency.WEEKLY;
                                    break;
            case "EVERY_TWO_WEEKS": this.frequency = Frequency.EVERY_TWO_WEEKS;
                                    break;
            case "MONTHLY"        : this.frequency = Frequency.MONTHLY;
                                    break;
            default               : break;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.initialDate = format.parse(initialDate);
        //this.teacher = Teacher.find_1.ref(teacher);
        this.period = Period.findPeriod(new java.sql.Date((new Date()).getTime()));
    }

    public static UnAvailability create (String timeSlot, String frequency, String initialDate, String teacher) throws ParseException {
        UnAvailability un = new  UnAvailability(timeSlot, frequency, initialDate, teacher);
        un.save();
        return un;
    }

    public static void delete (int u){
        UnAvailability.find.ref(u).delete();
    }

    public static UnAvailability updateUnavailability(int u, String frequency, String timeSlot, String initialDate) throws ParseException {
        UnAvailability un = find.ref(u);
        if(un.period.openedPeriod()){
            switch (timeSlot){
                case "SLOT1": un.timeSlot = TimeSlot.SLOT1;
                    break;
                case "SLOT2": un.timeSlot = TimeSlot.SLOT2;
                    break;
                case "SLOT3": un.timeSlot = TimeSlot.SLOT3;
                    break;
                case "SLOT4": un.timeSlot = TimeSlot.SLOT4;
                    break;
                default     : break;
            }

            switch (frequency){
                case "ONCE"           : un.frequency = Frequency.ONCE;
                    break;
                case "DAILY"          : un.frequency = Frequency.DAILY;
                    break;
                case "WEEKLY"         : un.frequency = Frequency.WEEKLY;
                    break;
                case "EVERY_TWO_WEEKS": un.frequency = Frequency.EVERY_TWO_WEEKS;
                    break;
                case "MONTHLY"        : un.frequency = Frequency.MONTHLY;
                    break;
                default               : break;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            un.initialDate = format.parse(initialDate);
            un.save();
            return un;
        }
        else {
            return null;
        }
    }

    public static List<UnAvailability> TeacherUnAvailability(String teacher){
        return UnAvailability.find.where()
                .eq("teacher.users.login", teacher)
                .findList();
    }

    public static List<UnAvailability> TeacherUnAvailabilityHistorical(int teacher, int period){
        return UnAvailability.find.where()
                .eq("teacher.id", teacher)
                .eq("period.id", period)
                .findList();
    }

    //add this function in wishes and Charge and PersonInCharge
    //it will be used to know if a period can be update or delete
    public static boolean unAvailabilityUsedPeriod(int period) {
        return find.where()
                .eq("period.id", period)
                .findRowCount() > 0;
    }

}
