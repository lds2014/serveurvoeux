package controllers.voeux;

import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.*;
import play.data.DynamicForm;
import static play.data.Form.form;

import play.libs.Json;
import play.mvc.*;

import scala.Int;
import views.html.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Contrôleur de gestion des voeux
 */
public class VoeuxController extends Controller {

    /**
     * Page principale des voeux
     * @return Result
     */
    public static Result index() throws ParseException {
        String showFiliere = "false"; // Non afficher

        String showUe = "false";     // Non afficher

        String showEc = "false";     // Non afficher

        String showBloc = "false";   // Non afficher

        List<UFR> ufr = UFR.findALL(); // On affiche la liste des UFR

        List<Filiere> filieres = Filiere.findALL();

        List<UE> ue = UE.all();

        List<EC> ec = EC.all();


        // 1) On vérifie que la date courante est comprise dans une période
        java.sql.Date aujourdhui = new java.sql.Date(System.currentTimeMillis());
        Period periode = Period.findPeriod(aujourdhui);

        String periodOuverte = "";
        Period nomPeriode = null;

        List<Voeux> listeVoeuxAnneeCourant  = null;

        if(periode != null)  // Il y a une période ouverte, les utilisateurs pourront saisir leurs voeux sur cette période
        {
            periodOuverte = "true";
            nomPeriode = periode;
            listeVoeuxAnneeCourant = Voeux.historique_voeux(session().get("login"), nomPeriode.id); // Récupération des voeux de la période ouverte
        }
        else // Il n'y a pas de période de saisie des voeux ouverte. Personne ne peux saisir de voeux
        {
            periodOuverte = "false";
            listeVoeuxAnneeCourant = new ArrayList<Voeux>();
            listeVoeuxAnneeCourant.add(new Voeux()); // Evite de passer une valeur null à la vue
            nomPeriode = new Period("closed", "05/11/2015", "05/11/2015"); // Date fictif - Evite de passer une valeur null à la vue

        }

        // 2) Année courante
        Calendar calendar = Calendar.getInstance();
        int annee = calendar.get( Calendar.YEAR );


        // 3) Message de confirmation d'ajout d'un voeu
        String ajout_voeu;
        if((session().get("ajout_voeu") != null) && (session().get("ajout_voeu").equals("true")))
        {
             ajout_voeu = "true";
             session().remove("ajout_voeu");
        }
        else
        {
             ajout_voeu = "false";
        }

        // Pris en compte quand on sélectionne une filière
        EC refEC = new EC();

        List<ShowAllTeacherOnWishSelected> afficheEnseignants = new ArrayList<ShowAllTeacherOnWishSelected>();
        afficheEnseignants.add(new ShowAllTeacherOnWishSelected());

        // Récupération des périodes de voeu de l'enseignant - pour l'import des voeux
        List<String> libellePeriode = Voeux.findPeriod(session().get("login"));

        if(libellePeriode.size() == 0)
        {
            libellePeriode.add("-");
        }

        List<String> verificationPeriodeOuverte = new ArrayList<String>();
        verificationPeriodeOuverte.add(0, periodOuverte);
        verificationPeriodeOuverte.add(1, nomPeriode.name);

        return ok(views.html.voeux.indexVoeux.render(filieres,ue,showUe,"",ec,"",
                  showEc,showBloc,"",ufr,showFiliere,"",session().get("firstName"),
                  session().get("lastName"), session().get("login"),ajout_voeu,listeVoeuxAnneeCourant,ec, verificationPeriodeOuverte, refEC, afficheEnseignants, libellePeriode)
        );
    }

    /**
     * On récupére l'id de l'UFR pour avoir ces filières
     * @param idufr
     * @return page principale avec liste déroulant des filières de l'UFR
     */
    public static Result getFiliere(String idufr) throws ParseException {
        String showFiliere = "true";

        String showUe = "false";

        String showEc = "false";

        String showBloc = "false";

        List<UFR> ufr = UFR.findALL();

        List<Filiere> filieres = Filiere.findInvolving(idufr);  // On sélectionne les filières de l'UFR sélectionné

        List<UE> ue = UE.all();

        List<EC> ec = EC.all();

        /** La période de saisie des voeux ce trouve bien entre les dates de début et de fin **/
        java.sql.Date aujourdhui = new java.sql.Date(System.currentTimeMillis());
        Period periode = Period.findPeriod(aujourdhui);
        String periodOuverte = "true";
        Period nomPeriode = periode;

        List<Voeux> listeVoeuxAnneeCourant = Voeux.historique_voeux(session().get("login"), nomPeriode.id); // Récupération des voeux de l'année en cours

        // Année courante
        Calendar calendar = Calendar.getInstance();
        int annee = calendar.get( Calendar.YEAR );

        // Pris en compte quand on sélectionne une filière
        EC refEC = new EC();

        List<ShowAllTeacherOnWishSelected> afficheEnseignants = new ArrayList<ShowAllTeacherOnWishSelected>();
        afficheEnseignants.add(new ShowAllTeacherOnWishSelected());

        // Récupération des périodes de voeu de l'enseignant - pour l'import des voeux
        List<String> libellePeriode = Voeux.findPeriod(session().get("login"));

        if(libellePeriode.size() == 0)
        {
            libellePeriode.add("-");
        }

        List<String> verificationPeriodeOuverte = new ArrayList<String>();
        verificationPeriodeOuverte.add(0, periodOuverte);
        verificationPeriodeOuverte.add(1, nomPeriode.name);

        return ok(views.html.voeux.indexVoeux.render(filieres,ue,showUe,"",ec,"",showEc,showBloc,"",ufr,showFiliere,
                  idufr, session().get("firstName"), session().get("lastName"), session().get("login"),"false",
                  listeVoeuxAnneeCourant,ec, verificationPeriodeOuverte, refEC, afficheEnseignants, libellePeriode));
    }

    /**
     * On récupére l'id de la filière pour avoir ces UE
     * @param id
     * @return page principale avec liste déroulante des UE de la filière
     */
    public static Result getUE(String id) throws ParseException {
        String showFiliere = "true";

        String showUe = "true";

        String showEc = "false";

        String showBloc = "false";

        List<UFR> ufr = UFR.findALL();

        Filiere recupUFR = Filiere.findFiliereById(id);
        String idufr = recupUFR.getRef_ufr();

        List<Filiere> filieres = Filiere.findALL();

        List<UE> ue = UE.findInvolving(id);

        List<EC> ec = EC.all();

        /** La période de saisie des voeux ce trouve bien entre les dates de début et de fin **/
        java.sql.Date aujourdhui = new java.sql.Date(System.currentTimeMillis());
        Period periode = Period.findPeriod(aujourdhui);
        String periodOuverte = "true";
        Period nomPeriode = periode;

        List<Voeux> listeVoeuxAnneeCourant = Voeux.historique_voeux(session().get("login"), nomPeriode.id); // Récupération des voeux de l'année en cours

        // Année courante
        Calendar calendar = Calendar.getInstance();
        int annee = calendar.get( Calendar.YEAR );

        // Pris en compte quand on sélectionne une filière
        EC refEC = new EC();

        List<ShowAllTeacherOnWishSelected> afficheEnseignants = new ArrayList<ShowAllTeacherOnWishSelected>();
        afficheEnseignants.add(new ShowAllTeacherOnWishSelected());

        // Récupération des périodes de voeu de l'enseignant - pour l'import des voeux
        List<String> libellePeriode = Voeux.findPeriod(session().get("login"));

        if(libellePeriode.size() == 0)
        {
            libellePeriode.add("-");
        }

        List<String> verificationPeriodeOuverte = new ArrayList<String>();
        verificationPeriodeOuverte.add(0, periodOuverte);
        verificationPeriodeOuverte.add(1, nomPeriode.name);

        return ok(views.html.voeux.indexVoeux.render(filieres,ue,showUe,id,ec,"",showEc,showBloc,"",ufr,
                  showFiliere,idufr,session().get("firstName"), session().get("lastName"), session().get("login"),"false",
                  listeVoeuxAnneeCourant,ec, verificationPeriodeOuverte, refEC, afficheEnseignants, libellePeriode));
    }

    /**
     * On récupére l'id de l'UE pour avoir avoir ces EC
     * @param idue
     * @return page principale avec liste déroulante des EC de l'UE
     */
    public static Result getEC(String idue) throws ParseException {
        String showFiliere = "true";

        String showUe = "true";

        String showEc = "true";

        String showBloc = "false";

        List<UFR> ufr = UFR.findALL();

        List<Filiere> filieres = Filiere.findALL(); // OK

        UE recupFilliere = UE.findUEById(idue);
        String idfiliere = recupFilliere.getRefFiliere(); // Récupération de l'id de la filière

        Filiere recupUFR = Filiere.findFiliereById(idfiliere); // Récupération de l'id de l'UFR
        String idufr = recupUFR.getRef_ufr();

        List<UE> ue = UE.findInvolving(idfiliere);

        List<EC> ec = EC.findInvolving(idue); // Les EC de l'UE
        List<EC> libelleEC = EC.all();

        /** La période de saisie des voeux ce trouve bien entre les dates de début et de fin **/
        java.sql.Date aujourdhui = new java.sql.Date(System.currentTimeMillis());
        Period periode = Period.findPeriod(aujourdhui);
        String periodOuverte = "true";
        Period nomPeriode = periode;

        List<Voeux> listeVoeuxAnneeCourant = Voeux.historique_voeux(session().get("login"), nomPeriode.id); // Récupération des voeux de l'année en cours

        // Année courante
        Calendar calendar = Calendar.getInstance();
        int annee = calendar.get( Calendar.YEAR );

        // Pris en compte quand on sélectionne une filière
        EC refEC = new EC();

        List<ShowAllTeacherOnWishSelected> afficheEnseignants = new ArrayList<ShowAllTeacherOnWishSelected>();
        afficheEnseignants.add(new ShowAllTeacherOnWishSelected());

        // Récupération des périodes de voeu de l'enseignant - pour l'import des voeux
        List<String> libellePeriode = Voeux.findPeriod(session().get("login"));

        if(libellePeriode.size() == 0)
        {
            libellePeriode.add("-");
        }

        List<String> verificationPeriodeOuverte = new ArrayList<String>();
        verificationPeriodeOuverte.add(0, periodOuverte);
        verificationPeriodeOuverte.add(1, nomPeriode.name);

        return ok(views.html.voeux.indexVoeux.render(filieres,ue,showUe,idfiliere,ec,idue,showEc,showBloc,"",ufr,
                  showFiliere,idufr,session().get("firstName"), session().get("lastName"), session().get("login"),"false",
                  listeVoeuxAnneeCourant,libelleEC, verificationPeriodeOuverte, refEC, afficheEnseignants, libellePeriode));
    }

    /**
     *
     * @param idec
     * @return
     */
    public static Result getBloc(String idec) throws ParseException {
        String showFiliere = "true";

        String showUe = "true";

        String showEc = "true";

        String showBloc = "true";

        List<UFR> ufr = UFR.findALL();

        List<Filiere> filieres = Filiere.findALL();

        EC recupIdUE = EC.findECById(idec);
        String idue = recupIdUE.getRef_UE();

        UE recupFilliere = UE.findUEById(idue);
        String idfiliere = recupFilliere.getRefFiliere(); // Récupération de l'id de la filière

        Filiere recupUFR = Filiere.findFiliereById(idfiliere); // Récupération de l'id de l'UFR
        String idufr = recupUFR.getRef_ufr();

        List<UE> ue = UE.findInvolving(idfiliere); // Les UE de la filière

        List<EC> ec = EC.findInvolving(idue); // Les EC de l'UE
        List<EC> libelleEC = EC.all();

        /** La période de saisie des voeux ce trouve bien entre les dates de début et de fin **/
        java.sql.Date aujourdhui = new java.sql.Date(System.currentTimeMillis());
        Period periode = Period.findPeriod(aujourdhui);
        String periodOuverte = "true";
        Period nomPeriode = periode;

        List<Voeux> listeVoeuxAnneeCourant = Voeux.historique_voeux(session().get("login"), nomPeriode.id); // Récupération des voeux de la période en cours

        // Année courante
        Calendar calendar = Calendar.getInstance();
        int annee = calendar.get( Calendar.YEAR );

        // Afficher qui a selectionné cette filière
        List<ShowAllTeacherOnWishSelected> afficheEnseignants = Voeux.findUserForThisWish(idec,nomPeriode.id);

        // Récupération des périodes de voeu de l'enseignant - pour l'import des voeux
        List<String> libellePeriode = Voeux.findPeriod(session().get("login"));

        if(libellePeriode.size() == 0)
        {
            libellePeriode.add("-");
        }

        List<String> verificationPeriodeOuverte = new ArrayList<String>();
        verificationPeriodeOuverte.add(0, periodOuverte);
        verificationPeriodeOuverte.add(1, nomPeriode.name);

        return ok(views.html.voeux.indexVoeux.render(filieres, ue, showUe, idfiliere, ec, idue, showEc, showBloc, idec,
                  ufr, showFiliere, idufr, session().get("firstName"), session().get("lastName"), session().get("login"), "false",
                  listeVoeuxAnneeCourant, libelleEC, verificationPeriodeOuverte, recupIdUE, afficheEnseignants, libellePeriode));
    }

    /**
     * Ajoute un voeu
     * @return Redirection vers la page indexVoeux.scala.html
     */
    public static Result add() {

        // 1 Récupérer le formulaire
        DynamicForm formulaireVoeux = form().bindFromRequest();

        String commentaire = formulaireVoeux.get("commentaire");
        String ref_ec = formulaireVoeux.get("ref_ec");
        double volume_horaire_cours = Double.parseDouble(formulaireVoeux.get("nb_h_cours"));
        int priorite_cour = Integer.parseInt(formulaireVoeux.get("prioritaire_cours"));
        int choix_cour = Integer.parseInt(formulaireVoeux.get("choixCours"));
        double volume_horaire_td = Double.parseDouble(formulaireVoeux.get("nb_h_tds"));
        int priorite_td = Integer.parseInt(formulaireVoeux.get("prioritaire_tds"));
        int choix_td = Integer.parseInt(formulaireVoeux.get("choixTD"));
        String ref_utilisateur = formulaireVoeux.get("ref_utilisateur");
        String nomPeriode = formulaireVoeux.get("nomPeriode");
        Period p = Period.find.where().eq("name", nomPeriode).findUnique();

        String validation = "ATTENTE";

        // 2 Appeler la fonction create pour création d'un voeu
        Voeux voeux = new Voeux();

        voeux.create(commentaire,ref_ec,volume_horaire_cours,priorite_cour,priorite_td,volume_horaire_td,ref_utilisateur,validation,choix_cour,choix_td,p);

        // 3 Redirection vers la vue avec un message de confirmation
        session("ajout_voeu","true");
        return redirect(routes.VoeuxController.index());
    }

    /**
     * Supprimer un voeu
     * @return
     */
    public static Result delete()
    {
        DynamicForm supprimerVoeu = form().bindFromRequest();

        String ref_ec_supp = supprimerVoeu.get("ref_ec_supp"); // Référence de l'EC
        String nomPeriode = supprimerVoeu.get("nomPeriode");
        Period p = Period.find.where().eq("name", nomPeriode).findUnique();

        String login = session().get("login"); // Login utilisateur

        Voeux voeux = new Voeux();

        voeux.deleteWishe(ref_ec_supp, login, p.id);

        return redirect(routes.VoeuxController.index());
    }

    /**
     * Historique des voeux
     * @return
     */
    public static Result historique()
    {
        List<EC> libelleEC = EC.all(); // Libellé EC

        // Récupération des périodes de voeu de l'enseignant
        List<String> periodeCourante = Voeux.findPeriod(session().get("login"));

        String periodeSelectionne = "";
        Period p = null;
        List<Voeux> listeVoeuxAnneeCourant = new ArrayList<Voeux>();

        // Récupération de la période selectionnée
        DynamicForm historiqueVoeux = form().bindFromRequest();

        if((historiqueVoeux.get("h_v_annees") != null))
        {
            periodeSelectionne = historiqueVoeux.get("h_v_annees");
            p = Period.find.where().eq("name", periodeSelectionne).findUnique();
        }
        if(p != null)
             listeVoeuxAnneeCourant = Voeux.findWishByYear(session().get("login"),p.id); // Récupération des voeux de la période choisie

        return ok(views.html.voeux.historiqueVoeux.render(session().get("firstName"), session().get("lastName"), session().get("login"),periodeCourante,periodeSelectionne,listeVoeuxAnneeCourant,libelleEC));
    }

    /**
     * Recherche un voeu à modifier
     * @return
     */
    public static Result update()
    {
        List<EC> libelleEC = EC.all(); // Libellé EC

        DynamicForm majVoeux = form().bindFromRequest();

        String ref_ec_modif = majVoeux.get("ref_ec_modif"); // EC

        Long id_voeux =  Long.parseLong(majVoeux.get("id_voeu"));

        String login = session().get("login"); // Login utilisateur

        Voeux voeuPourModifier = Voeux.findWishForUpdate(login,ref_ec_modif,id_voeux);

        return ok(views.html.voeux.modificationVoeu.render(session().get("firstName"), session().get("lastName"),voeuPourModifier,libelleEC));
    }

    /**
     * Modifier un voeu
     * @return
     */
    public static Result updateVoeu()
    {
        String login = session().get("login"); // Login utilisateur

        DynamicForm majVoeux = form().bindFromRequest();

        long id_voeu = Long.parseLong(majVoeux.get("id_voeu"));

        String commentaire = majVoeux.get("commentaire");
        String ref_ec = majVoeux.get("ref_ec");
        double vhc = Double.parseDouble(majVoeux.get("nb_h_cours"));
        int pc = Integer.parseInt(majVoeux.get("prioritaire_cours"));
        int choixC = Integer.parseInt(majVoeux.get("choixCours"));

        double vhtd = Double.parseDouble(majVoeux.get("nb_h_tds"));
        int ptd = Integer.parseInt(majVoeux.get("prioritaire_tds"));
        int choixTD = Integer.parseInt(majVoeux.get("choixTD"));
        int nomPeriode = Integer.parseInt(majVoeux.get("nomPeriode"));


        //Period p = Period.find.where().eq("name", nomPeriode).findUnique();

        Period p = Period.finById(nomPeriode); // Trouver la période par son id

        System.out.println("Tesssst !!!!!!!!! : " + p.id + " " + nomPeriode);

        String valide = "ATTENTE";

        Voeux.updateWish(id_voeu,commentaire,ref_ec,vhc,pc,ptd,vhtd,login,valide,choixC,choixTD,p); // Mise à jours du voeu


        return redirect(routes.VoeuxController.index());
    }

    public static Result importerVoeux() throws ParseException {

        DynamicForm datePeriode = form().bindFromRequest();

        // Récupérer les voeux de la période selectionnée
        List<Voeux> resultatRecuperation = Voeux.findWishByPeriodeSelected(session().get("login"), datePeriode.get("importVoeux"));

        if(resultatRecuperation != null)
        {
            // On vérifie que la date courante est comprise dans une période
            java.sql.Date aujourdhui = new java.sql.Date(System.currentTimeMillis());
            Period periode = Period.findPeriod(aujourdhui);

            // On supprimer tous les voeux saisie de cette periode actuelle
            Voeux.deleteWishSelected(session().get("login"), periode.id);


            for(Voeux voeu: resultatRecuperation)
            {
                Voeux v = new Voeux();

                v.create(voeu.getCommentaire(), voeu.getRef_ec(), voeu.getVolume_horaire_cours_assure(), voeu.getPrioritaireCours(), voeu.getPrioritaireTD(), voeu.getVolume_horaire_TD_assure(), session().get("login"), "ATTENTE", voeu.getChoixCours(), voeu.getChoixTD(), periode);
            }

        }

        return redirect(routes.VoeuxController.index());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result acceptWish(){
        JsonNode json = request().body().asJson();
        int id = json.findPath("id").intValue();
        Voeux v = Voeux.acceptAWish(id);
        return ok(Json.toJson(v));
    }


    @BodyParser.Of(BodyParser.Json.class)
    public static Result rejectWish(){
        JsonNode json = request().body().asJson();
        int id = json.findPath("id").intValue();
        Voeux v = Voeux.rejectAWish(id);
        return ok(Json.toJson(v));
    }
}


