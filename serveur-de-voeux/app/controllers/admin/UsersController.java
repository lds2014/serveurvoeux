package controllers.admin;

import models.Teacher;
import models.Users;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.data.Form;
import play.data.*;
import play.mvc.*;
import play.*;
import views.html.*;
import views.html.admin.infosUser;

import java.util.List;

import static play.data.Form.form;

public class UsersController extends Controller {

    public static Users user;

    /**
     * Page d'accueil admin
     * @return
     */
    public static Result create()
    {
        String message = "false"; // Message de confirmation de création d'un utilisateur

        return ok(views.html.admin.createUsers.render(session().get("firstName"), session().get("lastName"), message));
    }

    /**
     * Créer un utilisateur
     * @return
     */
    public static Result creating()
    {
        String message = "true"; // Message de confirmation de création d'un utilisateur

        /** Récupération données formulaire **/
        Form<CreateUser> usersForm = form(CreateUser.class).bindFromRequest();

        String Pwd = usersForm.get().news;

        if ( Pwd == null || usersForm.get().news.isEmpty())
        {
            Pwd = "secret" ;
        }

        /* Création d'un utilisateur */
        user = Users.create(usersForm.get().login, usersForm.get().email, Pwd, Users.role(usersForm.get().role),
                usersForm.get().firstName, usersForm.get().lastName, usersForm.get().address, usersForm.get().phoneNumber);

        Users userupdate = new Users();
        user = Users.findUsersById(usersForm.get().login); // On recherce les informations de l'utilisateur pour affichage

        userupdate.setLogin(usersForm.get().login);
        userupdate.setFirstName(usersForm.get().firstName);
        userupdate.setLastName(usersForm.get().lastName);
        userupdate.setPassword(Pwd);
        userupdate.setAddress(usersForm.get().address);
        userupdate.setEmail(usersForm.get().email);
        userupdate.setPhoneNumber(usersForm.get().phoneNumber);
        String roleu = user.role();

        /* Affectation de sa fonction */
        String afficheFonction ="";
        if(usersForm.get().teacher != null)
        {
            Teacher.create(usersForm.get().login, Teacher.job(usersForm.get().teacher), Integer.parseInt(usersForm.get().hours), Integer.parseInt(usersForm.get().extraHours));
            afficheFonction = usersForm.get().teacher; // Pour afficher sa fonction sélectionné
        }

        return ok(views.html.admin.infosUser.render(session().get("firstName"), session().get("lastName"), userupdate, roleu, message, afficheFonction));
    }

    /**
     * Page de modification utilisateur. On va selectionner l'utilisateur à mettre à jours
     * @return
     */
    public static Result update()
    {
        String message = "false"; // Message de confirmation de modification d'un utilisateur

        String roleu ="";

        List<Users> listeUtilisateur = Users.show(); // Liste des utilisateurs

        Users userupdate = new Users();

        userupdate.setLogin("none");

        return ok(views.html.admin.updateUsers.render(session().get("firstName"), session().get("lastName"), message, listeUtilisateur, userupdate, roleu, null));
    }

    /**
     * Affichage des données des utilisateurs à mettre à jours
     * @return
     */
    public static Result updateUser()
    {
        String message = "false"; // Message de confirmation de modification d'un utilisateur

        List<Users> listeUtilisateur = Users.show(); // Liste des utilisateurs

        DynamicForm iduser = form().bindFromRequest(); // Récupération de l'utilisateur à modifier

        Users userupdate = new Users();

        user = Users.findUsersById(iduser.get("idutilisateur")); // On recherce les informations de l'utilisateur

        String roleu = "";

        String profilTeacher = "";

        Teacher teacher = Teacher.showProfilTeacher(iduser.get("idutilisateur"));

        // Informations utilisateur
        userupdate.setLogin(iduser.get("idutilisateur"));

        if(!userupdate.getLogin().equals("none"))
        {
            userupdate.setEmail(user.getEmail());
            userupdate.setPassword(user.getPassword());

            roleu = user.role();

            userupdate.setFirstName(user.getFirstName());
            userupdate.setLastName(user.getLastName());
            userupdate.setAddress(user.getAddress());
            userupdate.setPhoneNumber(user.getPhoneNumber());

            /** Profil enseignant
            Teacher profil = Teacher.showProfilTeacher(user.getLogin());
            **/
            /*if(profil != null)
            {
                profilTeacher = profil.job();
            }*/
        }

        return ok(views.html.admin.updateUsers.render(session().get("firstName"), session().get("lastName"), message, listeUtilisateur, userupdate, roleu, teacher));
    }

    /**
     * Mise à jour de l'utilisateur
     * @return
     */
    public static Result updating()
    {
        /*** MARCHE PAS !! ****
            Form<User> usersForm = form(User.class).bindFromRequest();

            if (usersForm.hasErrors())
            {
                System.out.println("La mer noire");
            }

            String Pwd = usersForm.get().news ;
            if (usersForm.get().password == null || Pwd.isEmpty()){
                Pwd = user.password ;
            }
        */
        String message = "false";

        DynamicForm infoUser = form().bindFromRequest(); // Récupération de l'utilisateur à modifier

        String login = infoUser.get("login");
        String email = infoUser.get("email");
        String pwd = infoUser.get("news");
        String role = infoUser.get("role");
        String fn = infoUser.get("firstName");
        String ln = infoUser.get("lastName");
        String address = infoUser.get("address");
        String ph = infoUser.get("phoneNumber");
        String h = infoUser.get("hours");
        String eh = infoUser.get("extraHours");

        user = Users.update(login, email, pwd, role, fn, ln, address, ph); // Mise à jours

        /** Données à afficher dans la vue **/
        Users userupdate = new Users();

        userupdate.setLogin(login);
        userupdate.setFirstName(fn);
        userupdate.setLastName(ln);
        userupdate.setPassword(pwd);
        userupdate.setAddress(address);
        userupdate.setEmail(email);
        userupdate.setPhoneNumber(ph);

        String roleu = role;

        /** Modification profil enseignant **/
        Teacher.update(infoUser.get("login"),Teacher.job(infoUser.get("teacher")),Integer.parseInt(h),Integer.parseInt(eh));

        return ok(views.html.admin.infosUser.render(session().get("firstName"), session().get("lastName"), userupdate, roleu, message, infoUser.get("teacher")));
    }

    /**
     * Page de suppression utilisateur
     * @return
     */
    public static Result delete()
    {
        String message = "false";

        List<Users> listeUtilisateur = Users.show(); // Liste des utilisateurs

        return ok(views.html.admin.deleteUsers.render(session().get("firstName"), session().get("lastName"),listeUtilisateur,message));
    }

    /**
     * Supprime l'utilisateur selectionné
     * @return
     */
    public static Result deleting()
    {
        String message = "true";

        DynamicForm iduser = form().bindFromRequest(); // récupération de l'id de l'utilisateur à supprimer

        Users user = new Users();

        user.deleteUser(iduser.get("login")); // Suppression de l'utilisateur

        Teacher.deleteProfil(iduser.get("login"));

        List<Users> listeUtilisateur = Users.show(); // Liste des utilisateurs à réafficher

        return ok(views.html.admin.deleteUsers.render(session().get("firstName"), session().get("lastName"),listeUtilisateur,message));
    }

    public static Result research()
    {
        String message = "false"; // Message de confirmation de modification d'un utilisateur

        String roleu ="";

        List<Users> listeUtilisateur = Users.show(); // Liste des utilisateurs

        Users userupdate = new Users();

        userupdate.setLogin("none");


        return ok(views.html.admin.research.render(session().get("firstName"), session().get("lastName"), message, listeUtilisateur, userupdate, roleu, null));
    }

    public static Result info()
    {
        String message = "false"; // Message de confirmation de modification d'un utilisateur

        List<Users> listeUtilisateur = Users.show(); // Liste des utilisateurs

        DynamicForm iduser = form().bindFromRequest(); // Récupération de l'utilisateur à modifier

        Users userupdate = new Users();

        user = Users.findUsersById(iduser.get("idutilisateur")); // On recherce les informations de l'utilisateur

        String roleu = "";

        String profilTeacher = "";

        Teacher teacher = Teacher.showProfilTeacher(iduser.get("idutilisateur"));

        // Informations utilisateur
        userupdate.setLogin(iduser.get("idutilisateur"));

        if(!userupdate.getLogin().equals("none"))
        {
            userupdate.setEmail(user.getEmail());
            userupdate.setPassword(user.getPassword());

            roleu = user.role();

            userupdate.setFirstName(user.getFirstName());
            userupdate.setLastName(user.getLastName());
            userupdate.setAddress(user.getAddress());
            userupdate.setPhoneNumber(user.getPhoneNumber());

            /** Profil enseignant
            Teacher teacher = Teacher.showProfilTeacher(user.getLogin());
            **/
            /*if(profil != null)
            {
                profilTeacher = profil.job();
            }*/
        }

        return ok(views.html.admin.research.render(session().get("firstName"), session().get("lastName"), message, listeUtilisateur, userupdate, roleu, teacher));
    }


    public static class User{

        public String login;
        public String email;
        public String password;
        public String role;
        public String firstName;
        public String lastName;
        public String address;
        public String phoneNumber;
        public String news;
        public String conf;

       public String validate(){
            if(!password.equals(user.password))
                return "Mauvais mot de passe !";
            if(!news.equals(conf))
                return "Mauvaise confirmation du nouveau mot de passe !";
            return null;
        }
    }

    public static class CreateUser{

        public String login;
        public String email;
        public String role;
        public String firstName;
        public String lastName;
        public String address;
        public String phoneNumber;
        public String news;
        public String conf;
        public String teacher;
        public String hours;
        public String extraHours;

        public String validate(){
            lastName = lastName.toUpperCase();
            firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();

            if(!news.equals(conf))
                return "Mauvaise confirmation du nouveau mot de passe !";
            return null;
        }
    }

    public static class DeleteUser{
        public String login;

        public String validate(){
            return null;
        }
    }
}
