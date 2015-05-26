package models;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity()
@Table(name = "teachers", uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
public class Teacher extends Model {

    private enum Job {
        NONE,
        PROFESSOR,
        DOCTOR,
        TEMPORARY_TEACHER,
        ASSISTANT_TEACHER,
        EXTERNAL_TEACHER;
    }

    @SequenceGenerator(name = "trigger_teachers", sequenceName = "s_inc_teachers")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trigger_teachers")
    @Column(name="id")
    @Id
    public long id;

    public String user_id;

    @Column(nullable = false) @Enumerated(EnumType.STRING)
    public Job job;

    public Integer hours;

    public Integer extraHours;


    public Teacher(String user_id, Job job, Integer hours, Integer extraHours)
    {
        this.user_id = user_id;
        this.job = job;
        this.hours = hours;
        this.extraHours = extraHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Teacher teacher = (Teacher) o;

        if (id != teacher.id) return false;
        if (extraHours != null ? !extraHours.equals(teacher.extraHours) : teacher.extraHours != null) return false;
        if (hours != null ? !hours.equals(teacher.hours) : teacher.hours != null) return false;
        if (job != teacher.job) return false;
        if (user_id != null ? !user_id.equals(teacher.user_id) : teacher.user_id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (user_id != null ? user_id.hashCode() : 0);
        result = 31 * result + (job != null ? job.hashCode() : 0);
        result = 31 * result + (hours != null ? hours.hashCode() : 0);
        result = 31 * result + (extraHours != null ? extraHours.hashCode() : 0);
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getExtraHours() {
        return extraHours;
    }

    public void setExtraHours(Integer extraHours) {
        this.extraHours = extraHours;
    }

    public static Finder<Long, Teacher> find = new Finder<Long, Teacher>(
            Long.class, Teacher.class
    );

    /**
     * Liste des enseignants ayant une charge pour fonction
     * @return
     */
    public static List<Teacher> show()
    {
        return find.where("JOB != 'NONE'").order("USER_ID ASC").findList();
    }

    /**
     * Profil d'un enseignant
     * @param user_id
     * @return
     */
    public static Teacher showProfilTeacher(String user_id)
    {
        Teacher teacher = find.where().eq("user_id",user_id).findUnique();

        return teacher;
    }

    /**
     * Ajouter un profil enseignant
     * @param users
     * @param job
     * @param hours
     * @param extraHours
     * @return
     */
    public static Teacher create(String users,Job job,Integer hours, Integer extraHours)
    {
        Teacher teacher = new Teacher(users,job, hours, extraHours);
        Ebean.save(teacher);

        return teacher;
    }

    /**
     * Modifier le profil enseignant
     * @param user_id
     * @param job
     * @param hours
     * @param extraHours
     * @return
     */
    public static Teacher update(String user_id, Job job, Integer hours, Integer extraHours)
    {
        Teacher teacher = find.where().eq("user_id",user_id).findUnique();

        teacher.setUser_id(user_id);
        teacher.setJob(job);
        teacher.setHours(hours);
        teacher.setExtraHours(extraHours);
        teacher.save();

        return teacher;
    }

    /**
     * Suppression du profil de l'utilisateur
     * @param user_id
     */
    public static void deleteProfil(String user_id)
    {
        Teacher teacher = find.where().eq("user_id",user_id).findUnique();

        Ebean.delete(teacher);
    }

    public String job()
    {
        if(this.job == Job.DOCTOR)
            return "DOCTOR";
        if(this.job == Job.ASSISTANT_TEACHER)
            return "ASSISTANT_TEACHER";
        if(this.job == Job.TEMPORARY_TEACHER)
            return "TEMPORARY_TEACHER";
        if(this.job == Job.EXTERNAL_TEACHER)
            return "EXTERNAL_TEACHER";
        if(this.job == Job.PROFESSOR)
            return "PROFESSOR";
        return "NONE";
    }

    public static Job job(String job)
    {
        if(job.equals("DOCTOR"))
            return Job.DOCTOR;
        if(job.equals("PROFESSOR"))
            return Job.PROFESSOR;
        if(job.equals("TEMPORARY_TEACHER"))
            return Job.TEMPORARY_TEACHER;
        if(job.equals("ASSISTANT_TEACHER"))
            return Job.ASSISTANT_TEACHER;
        if(job.equals("EXTERNAL_TEACHER"))
            return Job.EXTERNAL_TEACHER;
        return Job.NONE;
    }

    /**
     * Liste des profiles
     * @return
     */
    public static List<String> listJob()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("NONE");
        list.add("PROFESSOR");
        list.add("DOCTOR");
        list.add("TEMPORARY_TEACHER");
        list.add("ASSISTANT_TEACHER");
        list.add("EXTERNAL_TEACHER");
        return list;
    }

    public static boolean isTeacher(String login){
        return find.where()
                .eq("user_id",login.toLowerCase())
                .findRowCount() > 0;
    }

    public static Finder<String, Teacher> find_1 = new Finder<String, Teacher>(
            String.class, Teacher.class
    );
}
