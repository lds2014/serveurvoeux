package models;

import javax.persistence.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;
import oracle.sql.TIMESTAMP;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import play.db.ebean.Model;
import play.api.db.DB;
import play.db.*;



@Entity
public class Voeux extends Model {

    @SequenceGenerator(name = "trigger_voeux", sequenceName = "s_inc_voeux")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trigger_voeux")
    @Column(name="id_voeu")
    @Id
    private long id_voeu;

    private String ref_utilisateurs;

    private String commentaire;

    private String ref_ec;

    private Double volume_horaire_cours_assure;

    private int prioritaire_Cours; // Un enseignant est prioritaire 4 années consécutives sur un même cours ou TD
    private int prioritaire_TD;

    private Double volume_horaire_TD_assure;

    private java.sql.Date date_d;

    private int annee;

    private String valide;

    private int choix_Cours; // Indique si le cours ou TD est en 1er choix ou 2nd choix
    private int choix_TD;

    @ManyToOne(optional = false) @JoinColumn(name = "period_id") public Period period;

    /**
     * Constructeur par défaut
     */
    public Voeux()
    {
        this.date_d = new java.sql.Date((new Date()).getTime());
        Calendar calendar = Calendar.getInstance();
        this.annee = calendar.get( Calendar.YEAR );
    }

    public Voeux(String commentaire, String ref_ec,
                 Double volume_horaire_cours_assure, int prioritaireCours,int prioritaireTD,
                 Double volume_horaire_TD_assure,String ref_utilisateurs,
                 String validation,int choixC, int choixTd) {

        this.commentaire = commentaire;
        this.ref_ec = ref_ec;
        this.volume_horaire_cours_assure = volume_horaire_cours_assure;
        this.prioritaire_Cours = prioritaireCours;
        this.prioritaire_TD = prioritaireTD;
        this.volume_horaire_TD_assure = volume_horaire_TD_assure;
        this.valide = validation;
        this.ref_utilisateurs = ref_utilisateurs;
        this.date_d = new java.sql.Date((new Date()).getTime());
        Calendar calendar = Calendar.getInstance();
        this.annee = calendar.get( Calendar.YEAR );
        this.choix_Cours = choixC;
        this.choix_TD = choixTd;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public long getId_voeu() {
        return id_voeu;
    }

    public void setId_voeu(long id_voeu) {
        this.id_voeu = id_voeu;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getRef_ec() {
        return ref_ec;
    }

    public void setRef_ec(String ref_ec) {
        this.ref_ec = ref_ec;
    }

    public Double getVolume_horaire_cours_assure() {
        return volume_horaire_cours_assure;
    }

    public void setVolume_horaire_cours_assure(Double volume_horaire_cours_assure) {
        this.volume_horaire_cours_assure = volume_horaire_cours_assure;
    }

    public int getPrioritaireCours() {
        return prioritaire_Cours;
    }

    public void setPrioritaireCours(int priorite) {
        this.prioritaire_Cours = priorite;
    }

    public int getPrioritaireTD() {
        return prioritaire_TD;
    }

    public void setPrioritaireTD(int prioritaireTD) {
        this.prioritaire_TD = prioritaireTD;
    }

    public Double getVolume_horaire_TD_assure() {
        return volume_horaire_TD_assure;
    }

    public void setVolume_horaire_TD_assure(Double volume_horaire_TD_assure) {
        this.volume_horaire_TD_assure = volume_horaire_TD_assure;
    }

    public java.sql.Date getDate() {
        return date_d;
    }

    public void setDate(java.sql.Date date) {
        this.date_d = date;
    }

    public int getYear() {
        return annee;
    }

    public void setYear(int year) {
        this.annee = year;
    }

    public String getRef_utilisateurs() {
        return ref_utilisateurs;
    }

    public void setRef_utilisateurs(String ref_utilisateurs) {
        this.ref_utilisateurs = ref_utilisateurs;
    }

    public String getValide() {
        return valide;
    }

    public void setValide(String valide) {
        this.valide = valide;
    }

    public int getChoixCours() {
        return choix_Cours;
    }

    public void setChoixCours(int choixCours) {
        this.choix_Cours = choixCours;
    }

    public int getChoixTD() {
        return choix_TD;
    }

    public void setChoixTD(int choixTD) {
        this.choix_TD = choixTD;
    }

    /**
     * Finder permet la relation de la classe Voeux avec la table Voeux situé dans la base
     */
    public static Finder<Long,Voeux> find = new Finder<Long,Voeux>(
            Long.class, Voeux.class
    );

    /**
     * Création d'un voeux
     * @param commentaire
     * @param ref_ec
     * @param volume_horaire_cours_assure
     * @param prioritaireCours
     * @param prioritaireTD
     * @param volume_horaire_TD_assure
     * @param ref_utilisateurs
     * @param validation
     * @param choixC
     * @param choixTd
     * @return
     */
    public Voeux create(String commentaire, String ref_ec,
                        Double volume_horaire_cours_assure, int prioritaireCours,int prioritaireTD,
                        Double volume_horaire_TD_assure,String ref_utilisateurs,
                        String validation,int choixC, int choixTd, Period nomPeriode) {

        Voeux voeux = new Voeux();

        voeux.setCommentaire(commentaire);
        voeux.setRef_ec(ref_ec);
        voeux.setVolume_horaire_cours_assure(volume_horaire_cours_assure);
        voeux.setPrioritaireCours(prioritaireCours);
        voeux.setPrioritaireTD(prioritaireTD);
        voeux.setVolume_horaire_TD_assure(volume_horaire_TD_assure);
        voeux.setRef_utilisateurs(ref_utilisateurs);
        voeux.setValide(validation);
        voeux.setChoixCours(choixC);
        voeux.setChoixTD(choixTd);
        voeux.setPeriod(nomPeriode);

        voeux.save(); // Insertion dans la Base

        return voeux;
    }

    /**
     * Récupére les listes des voeux selon la période
     * @param ref_utilisateurs
     * @return
     */
    public static List<Voeux> historique_voeux(String ref_utilisateurs, int periodeOuverte)
    {
        return find.where().eq("period.id",periodeOuverte)
                           .eq("ref_utilisateurs",ref_utilisateurs)
                           .findList();
    }

    /**
     * Liste des voeux
     * @return La liste des voeux
     */
    public static List<Voeux> finAll()
    {
        return find.all();
    }

    /**
     * Supprimer un voeu
     * @param ref_ec_delete
     * @param ref_utilisateurs
     * @param nomPeriode
     */
    public static void deleteWishe(String ref_ec_delete, String ref_utilisateurs, int nomPeriode)
    {
        Voeux voeux = find.where().eq("ref_ec",ref_ec_delete)
                                  .eq("ref_utilisateurs",ref_utilisateurs)
                                  .eq("period.id",nomPeriode)
                                  .findUnique();
        Ebean.delete(voeux);
    }

    /**
     * Afficher les périodes de saisie des voeux d'un enseignant
     * @param ref_utilisateurs
     * @return
     */
    public static List<String> findPeriod(String ref_utilisateurs)
    {
        String sql = "SELECT p.name FROM Voeux v inner join Period p " +
                     "on v.period_id = p.id WHERE v.ref_utilisateurs like :ref GROUP BY p.name, p.id ORDER BY p.name DESC";

        List<SqlRow> requete = Ebean.createSqlQuery(sql)
                                    .setParameter("ref",ref_utilisateurs)
                                    .findList();

        List<String> voeux = new ArrayList<String>();

        for(SqlRow periode : requete)
        {
            voeux.add(periode.getString("name"));
        }

        return voeux;
    }


    /**
     * Liste des voeux de l'utilisateur par période
     * @param ref_utilisateurs
     * @param nomPeriode
     * @return
     */
    public static List<Voeux> findWishByYear(String ref_utilisateurs, int nomPeriode)
    {
        return find.where().eq("period.id",nomPeriode)
                           .eq("ref_utilisateurs",ref_utilisateurs)
                           .findList();
    }

    /**
     * Trouver tous les enseignants qui ont selectionnés ce voeu pour une période donnée
     * @param ref_ecs
     * @return
     */
    public static List<ShowAllTeacherOnWishSelected> findUserForThisWish(String ref_ecs, int period)
    {
        String sql = "SELECT FIRST_NAME, LAST_NAME, VOLUME_HORAIRE_COURS_ASSURE, VOLUME_HORAIRE_TD_ASSURE " +
                     "FROM Voeux INNER JOIN USERS ON ref_utilisateurs = login " +
                     "WHERE ref_ec like :refec AND Period_id = :periodeid";

        List<SqlRow> requete = Ebean.createSqlQuery(sql)
                                    .setParameter("refec",ref_ecs)
                                    .setParameter("periodeid",period)
                                    .findList();

        List<ShowAllTeacherOnWishSelected> enseignantSurEC = new ArrayList<ShowAllTeacherOnWishSelected>();

        for(SqlRow resultat : requete)
        {
            ShowAllTeacherOnWishSelected nouveau = new ShowAllTeacherOnWishSelected();

            nouveau.setNom(resultat.getString("LAST_NAME"));
            nouveau.setPrenom(resultat.getString("FIRST_NAME"));
            nouveau.setHeures_cours(resultat.getDouble("VOLUME_HORAIRE_COURS_ASSURE"));
            nouveau.setHeures_td(resultat.getDouble("VOLUME_HORAIRE_TD_ASSURE"));

            enseignantSurEC.add(nouveau);
        }

        return enseignantSurEC;
    }

    /**
     * Récupere un voeu pour le modifier
     * @param ref_utilisateurs
     * @param ref_ec
     * @param id_voeu
     * @return
     */
    public static Voeux findWishForUpdate(String ref_utilisateurs, String ref_ec, Long id_voeu)
    {
        return find.where().eq("id_voeu",id_voeu)
                           .eq("ref_Utilisateurs",ref_utilisateurs)
                           .eq("ref_ec",ref_ec)
                           .findUnique();
    }

    /**
     * Mise à jours du voeu
     * @param id_voeu
     * @param commentaire
     * @param ref_ec
     * @param volume_horaire_cours_assure
     * @param prioritaireCours
     * @param prioritaireTD
     * @param volume_horaire_TD_assure
     * @param ref_utilisateurs
     * @param validation
     * @param choixC
     * @param choixTd
     */
    public static void updateWish(long id_voeu, String commentaire, String ref_ec,
                                  Double volume_horaire_cours_assure, int prioritaireCours,int prioritaireTD,
                                  Double volume_horaire_TD_assure,String ref_utilisateurs,
                                  String validation,int choixC, int choixTd, Period nomPeriode)
    {
        Voeux updateWish = find.where().eq("id_voeu",id_voeu).findUnique();

        updateWish.setDate(new java.sql.Date((new Date()).getTime()));
        Calendar calendar = Calendar.getInstance();
        int annee = calendar.get( Calendar.YEAR );
        updateWish.setYear(annee);
        updateWish.setCommentaire(commentaire);
        updateWish.setRef_ec(ref_ec);
        updateWish.setVolume_horaire_cours_assure(volume_horaire_cours_assure);
        updateWish.setPrioritaireCours(prioritaireCours);
        updateWish.setPrioritaireTD(prioritaireTD);
        updateWish.setVolume_horaire_TD_assure(volume_horaire_TD_assure);
        updateWish.setRef_utilisateurs(ref_utilisateurs);
        updateWish.setValide(validation);
        updateWish.setChoixCours(choixC);
        updateWish.setChoixTD(choixTd);
        updateWish.setPeriod(nomPeriode);

        updateWish.save();
    }

    public static double getWishHoursCours(String refEc){
        List<Voeux> EcWish =find.where()
                .eq("ref_ec",refEc)
                .eq("valide","VALIDE")
                .findList();
        double hours = 0;
        for(Voeux v : EcWish){
            hours += v.getVolume_horaire_cours_assure();
        }
        return hours;
    }

    public static double getWishHoursTd(String refEc){
        List<Voeux> EcWish =  find.where()
                .eq("ref_ec",refEc)
                .eq("valide","VALIDE")
                .findList();
        double hours = 0;
        for(Voeux v : EcWish){
            hours += v.getVolume_horaire_TD_assure();
        }
        return hours;
    }

    public static List<Voeux> getListeVoeuxEc(String CodeEc) throws ParseException {
        return find.where()
                .eq("ref_ec",CodeEc)
                .eq("valide","ATTENTE")
                .eq("period",Period.findPeriod(new java.sql.Date((new java.util.Date()).getTime())))
                .findList();
    }

    /**
     * Trouver les voeux d'un enseignant en selectionnant une période de voeux
     * @param ref_utilisateurs
     * @param nomPeriode
     * @return
     */
    public static List<Voeux> findWishByPeriodeSelected(String ref_utilisateurs, String nomPeriode)
    {

        return find.where().eq("period.name",nomPeriode)
                           .eq("ref_utilisateurs",ref_utilisateurs)
                           .eq("valide","VALIDE")
                           .findList();
    }

    public static void deleteWishSelected(String ref_utilisateurs, int nomPeriode)
    {
        List<Voeux> voeux = find.where()
                                .eq("ref_Utilisateurs",ref_utilisateurs)
                                .eq("period.id",nomPeriode)
                                .eq("valide","ATTENTE")
                                .findList();

        Ebean.delete(voeux);
    }

    public static Voeux acceptAWish(long id){
        Voeux v = Voeux.find.ref(id);
        v.setValide("VALIDE");
        v.update();
        return v;
    }

    public static Voeux rejectAWish(long id){
        Voeux v = Voeux.find.ref(id);
        v.setValide("REFUSE");
        v.update();
        return v;
    }
}