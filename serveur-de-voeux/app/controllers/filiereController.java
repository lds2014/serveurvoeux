package controllers;

import models.EC;
import models.Filiere;
import models.PersonInCharge;
import models.UE;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;


public class filiereController extends Controller {


    public static Result getManque(String login) {
        PersonInCharge p = PersonInCharge.getPersonInCharge(login);
        Filiere f = Filiere.getFiliereId(p.id);

        return ok(views.html.teacher.rspFiliere.rspFilieresManques.render(f ,session().get("login"), session().get("firstName"), session().get("lastName"), session().get("rsp")));
    }


    public static Result InfoFiliere(String login) {
        PersonInCharge p = PersonInCharge.getPersonInCharge(login);
        Filiere f = Filiere.getFiliereId(p.id);

        return ok(views.html.teacher.rspFiliere.rspInfoFilieres.render(f ,session().get("login"), session().get("firstName"), session().get("lastName"), session().get("rsp")));
    }


    public static Result valider(String login) {
        PersonInCharge p = PersonInCharge.getPersonInCharge(login);
        Filiere f = Filiere.getFiliereId(p.id);

        return ok(views.html.teacher.rspFiliere.rspValidationVoeux.render(f ,session().get("login"), session().get("firstName"), session().get("lastName"), session().get("rsp")));
    }
}
