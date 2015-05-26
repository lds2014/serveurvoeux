package controllers.admin;

import models.*;
import play.data.*;
import play.mvc.*;
import play.*;
import views.html.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;

public class Application extends Controller {

    public static Users users;

    public static Result index() throws ParseException {
        if(session().get("login") == null)
        {
            return redirect(
                    routes.Application.login()
            );
        }

        // Redirection de page selon le rôle de l'utitisateur
        if(session().get("role").equals("ADMIN"))
        {
            return ok(views.html.admin.admin.render(session().get("firstName"), session().get("lastName")));
        }


        /*** PARTIE Yoan : Affichage des infos enseignants et responsable de filière ***/

        List<EC> ec = EC.all(); // Pour afficher le libéllé de l'EC

        // On regarde si on est dans une période de voeu
        java.sql.Date aujourdhui = new java.sql.Date(System.currentTimeMillis());
        Period periode = Period.findPeriod(aujourdhui);

        String periodOuverte = "";
        Period nomPeriode = null;

        List<Voeux> listeVoeuxAnneeCourant  = null;

        CalculateHoursTeacher calculeHeuresEnseignant = null;

        if(periode != null)  // Il y a une période ouverte, les utilisateurs pourront saisir leurs voeux sur cette période
        {
            periodOuverte = "true";
            nomPeriode = periode;
            listeVoeuxAnneeCourant = Voeux.historique_voeux(session().get("login"), nomPeriode.id); // Récupération des voeux de la période ouverte

            // Bloc qui affiche les heures statutaires des enseignants
            calculeHeuresEnseignant = new CalculateHoursTeacher();

            calculeHeuresEnseignant.CalculateHours(session().get("login"), nomPeriode.id);
        }
        else // Il n'y a pas de période de saisie des voeux ouverte. Personne ne peux saisir de voeux
        {
            periodOuverte = "false";
            listeVoeuxAnneeCourant = new ArrayList<Voeux>();
            listeVoeuxAnneeCourant.add(new Voeux()); // Evite de passer une valeur null à la vue
            nomPeriode = new Period("closed", "05/11/2015", "05/11/2015"); // Date fictif - Evite de passer une valeur null à la vue
            calculeHeuresEnseignant = new CalculateHoursTeacher(); // Evite de passe une valeur null à la vue
        }


        return ok(views.html.admin.index.render(session().get("login"), session().get("firstName"), session().get("lastName"), session().get("rsp"),session().get("teacher"), periodOuverte, nomPeriode.name, listeVoeuxAnneeCourant, ec, calculeHeuresEnseignant ));
    }

    public static Result login()
    {
        String erreurLogin = "false"; // On admet un message d'erreur s'il y a erreur de login

        return ok(views.html.admin.login.render(erreurLogin));
    }

    public static Result authenticate()
    {
        String erreurLogin = "false"; // On admet un message d'erreur s'il y a erreur de login

        //Form<Login> loginForm = form(Login.class).bindFromRequest();

        Form<Login> loginForm = form(Login.class).bindFromRequest();

        if (loginForm.hasErrors())
        {
            erreurLogin = "true";

            return badRequest(views.html.admin.login.render(erreurLogin));
        }
        else
        {
            session().clear();
            session("login",users.login);
            session("email",users.email);
            session("password",users.password);
            session("role",users.role());
            session("firstName",users.firstName);
            session("lastName", users.lastName);
            session("address",users.address);
            session("phoneNumber",users.phoneNumber);

            if (Teacher.isTeacher(users.login))
                session("teacher","true");
            else
                session("teacher","false");

            if (PersonInCharge.isRspFiliere(users.login))
                session("rsp","true");
            else
                session("rsp","false");

            return redirect(
                    routes.Application.index()
            );
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
                routes.Application.index()
        );
    }

    public static class Login {

        public String login;
        public String password;

        public String validate() {
            if ((users = Users.authenticate(login, password)) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }
}
