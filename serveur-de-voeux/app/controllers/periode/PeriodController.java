package controllers.periode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Period;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BIGVAL on 04/04/2015.
 */
public class PeriodController extends Controller {

    public static Result index() {

        return ok(views.html.admin.period.render
                (Period.findAllPeriod(),session().get("firstName"),
                        session().get("lastName"), session().get("login")));

    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result add() {

        Period p;
        JsonNode json = request().body().asJson();

        String name = json.findPath("name").textValue();
        if(name == null)
            return badRequest("Missing parameter [name]");
        String start = json.findPath("start").textValue();
        if(start == null)
            return badRequest("Missing parameter [start]");
        String end = json.findPath("end").textValue();
        if(end == null)
            return badRequest("Missing parameter [end]");

        try {
            p = Period.create(name, start, end);

            return ok(Json.toJson(p));
        }
        catch (ParseException e) {
            return badRequest("Can't parse the date");
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result update() {

        Period p;
        JsonNode json = request().body().asJson();

        int period = json.findPath("id").intValue();

        String name = json.findPath("name").textValue();
        if(name == null)
            return badRequest("Missing parameter [name]");
        String start = json.findPath("start").textValue();
        if(start == null)
            return badRequest("Missing parameter [start]");
        String end = json.findPath("end").textValue();
        if(end == null)
            return badRequest("Missing parameter [end]");

        try {
            p = Period.update(period, name, start, end);
            return ok(Json.toJson(p));
        }
        catch (ParseException e) {
            return badRequest("Can't parse the date");
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result close() {
        JsonNode json = request().body().asJson();
        int period = json.findPath("id").intValue();
        Period p = Period.setClosed(period);
        return ok(Json.toJson(p));
    }

    public static Result getPeriod(java.sql.Date day) {
        Period p = null;
        try {
            p = Period.findPeriod(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ok(Json.toJson(p));
    }

    public static Result aPeriod(int period){
        Period p = Period.finById(period);
        ObjectNode result = Json.newObject();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        result.put("id", p.id);
        result.put("name", p.name);
        result.put("starts", formatter.format(p.starts));
        result.put("end", formatter.format(p.end));

        return ok(result);
    }


    @BodyParser.Of(BodyParser.Json.class)
    public static Result delete() {
        JsonNode json = request().body().asJson();
        int period = json.findPath("id").intValue();
        Period p = Period.delete(period);
        if(p == null){
            String pr = "can't be deleted";
            return ok(Json.toJson(pr));
        }

        return ok(Json.toJson(p));
    }
}
