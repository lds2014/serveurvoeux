/**
 * Created by Valentino Houessou on 22/04/2015.
 */
$(document).ready(function() {

    function addPeriod(){
        var $name = $("#name").val();
        var $start = $("#start").val();
        var $end = $("#end").val();
        var $bool_1 = "";
        var $bool_2 = "";
        var $bool_3 = "";
        var $info = "";
        var $nameGet = "";
        if(($name === null)||($name === undefined)||($name === "")) {
            $("#errorName").attr("class", "form-group has-error");
            $bool_1 = "nom";
        }
        else {
            $("#errorName").attr("class", "form-group has-success");
        }
        //console.log("the name is required");
        if(($start === null)||($start === undefined)||($start === "")){
            $("#errorStart").attr("class","form-group has-error");
            $bool_2 = "d&eacutebut";
        }
        else {
            $("#errorStart").attr("class","form-group has-success");
        }
        //console.log("the start date is required");
        if(($end === null)||($end === undefined)||($end === "")){
            $("#errorEnd").attr("class","form-group has-error");
            $bool_3 = "fin";
        }
        else {
            $("#errorEnd").attr("class","form-group has-success");
        }

        if(($bool_1 === "") && ($bool_2 === "") &&($bool_3 === "")){
            $("#errorName").attr("class","form-group");
            $("#errorStart").attr("class","form-group");
            $("#errorEnd").attr("class","form-group");
            //Requête ajax pour insérer la donnee
            $.ajax({
                type: 'POST',
                contentType:'application/json',
                url: '/ServeurDeVoeux/Period/',
                data: '{"name": "'+$name+'", "start": "'+$start+'", "end": "'+$end+'"}',
                dataType: 'JSON',
                timeout: 3000,
                success: function(data) {
                    /* $.each(data, function(idx, value){
                     if(idx == "name"){
                     $nameGet = value;
                     }
                     })
                     console.log($nameGet);*/
                },
                error: function() {
                    console.log('La requête n\'a pas abouti');
                }
            });

            //preciser le nom de la periode recuperer par ajax
            var $info = $("<div class='alert alert-success' role='alert'>");
            $info.html("<button type='button' class='close' data-dismiss='alert' " +
                "aria-label='Close'> <span aria-hidden='true'>&times;</span></button>" +
                "La p&eacuteriode a bien &eacutet&eacute ajout&eacutee");
            $info.prependTo($("#contentId_2"));
            /*$("#actionForm").hide();
            location.reload();*/
        }
        else{
            var $info = $("<div class='alert alert-danger' role='alert'>");
            $info.html("<button type='button' class='close' data-dismiss='alert' " +
                "aria-label='Close'> <span aria-hidden='true'>&times;</span></button>" +
                "Erreur, donn&eacutee(s) " +$bool_1+" "+ $bool_2 +" " + $bool_3 +
                " non renseign&eacutee(s) ou mal renseign&eacutee(s) ");
            $info.prependTo($("#actionForm"));
        }
    }

    function closePeriod(param){
        if(confirm("Etes-vous sûr de vouloir clore cette période ?")){
            var $id = parseInt(param.attr("data-id"));
            console.log($id);
            $.ajax({
                type: 'PUT',
                contentType:'application/json',
                url: '/ServeurDeVoeux/Period/SetClosed/',
                data: '{"id": '+$id+'}',
                dataType: 'JSON',
                timeout: 3000,
                success: function(data) {
                },
                error: function() {
                    console.log('La requête n\'a pas abouti');
                }
            });
        }
    }

    function removePeriod(param){
        if(confirm("Etes-vous sûr de vouloir supprimer cette période ?")){
            var $id = parseInt(param.attr("data-id"));
            $.ajax({
                type: 'DELETE',
                contentType:'application/json',
                url: '/ServeurDeVoeux/Period/',
                data: '{"id": '+$id+'}',
                dataType: 'JSON',
                timeout: 3000,
                success: function(data) {
                },
                error: function() {
                    console.log('La requête n\'a pas abouti');
                }
            });
        }
    }

    function editPeriod(param){
        var $id = parseInt(param.attr("data-id"));
        $("#idp").attr("data-id",param.attr("data-id"));
        $.ajax({
            type: 'GET',
            contentType:'application/json',
            url: '/ServeurDeVoeux/Period/Info/'+$id,
            dataType: 'JSON',
            timeout: 3000,
            success: function(data) {
                $("#name_2").val(data["name"]);
                $("#start_2").val(data["starts"]);
                $("#end_2").val(data["end"]);
            },
            error: function() {
                console.log('La requête n\'a pas abouti');
            }
        });
    }

    function updatePeriod(){
        var $id = parseInt($("#idp").attr("data-id"));
        var $name = $("#name_2").val();
        var $start = $("#start_2").val();
        var $end = $("#end_2").val();
        var $bool_1 = "";
        var $bool_2 = "";
        var $bool_3 = "";
        var $info = "";
        var $nameGet = "";
        if(($name === null)||($name === undefined)||($name === "")) {
            $("#errorName_2").attr("class", "form-group has-error");
            $bool_1 = "nom";
        }
        else {
            $("#errorName_2").attr("class", "form-group has-success");
        }
        if(($start === null)||($start === undefined)||($start === "")){
            $("#errorStart_2").attr("class","form-group has-error");
            $bool_2 = "d&eacutebut";
        }
        else {
            $("#errorStart_2").attr("class","form-group has-success");
        }
        if(($end === null)||($end === undefined)||($end === "")){
            $("#errorEnd_2").attr("class","form-group has-error");
            $bool_3 = "fin";
        }
        else {
            $("#errorEnd_2").attr("class","form-group has-success");
        }

        if(($bool_1 === "") && ($bool_2 === "") &&($bool_3 === "")){
            $("#errorName_2").attr("class","form-group");
            $("#errorStart_2").attr("class","form-group");
            $("#errorEnd_2").attr("class","form-group");
            //Requête ajax pour insérer la donnee
            $.ajax({
                type: 'PUT',
                contentType:'application/json',
                url: '/ServeurDeVoeux/Period/',
                data: '{"id": '+$id+', "name": "'+$name+'", "start": "'+$start+'", "end": "'+$end+'"}',
                dataType: 'JSON',
                timeout: 3000,
                success: function(data) {
                },
                error: function() {
                    console.log('La requête n\'a pas abouti');
                }
            });

            //preciser le nom de la periode recuperer par ajax
            var $info = $("<div class='alert alert-success' role='alert'>");
            $info.html("<button type='button' class='close' data-dismiss='alert' " +
                "aria-label='Close'> <span aria-hidden='true'>&times;</span></button>" +
                "La p&eacuteriode a bien &eacutet&eacute modifi&eacutee");
            $info.prependTo($("#contentId_2"));
        }
        else{
            var $info = $("<div class='alert alert-danger' role='alert'>");
            $info.html("<button type='button' class='close' data-dismiss='alert' " +
                "aria-label='Close'> <span aria-hidden='true'>&times;</span></button>" +
                "Erreur, donn&eacutee(s) " +$bool_1+" "+ $bool_2 +" " + $bool_3 +
                " non renseign&eacutee(s) ou mal renseign&eacutee(s) ");
            $info.prependTo($("#actionForm_2"));
        }
    }

    $("#addPer").button().on( "click", function() {
        addPeriod();
    });

    $("#periodList tbody").on("click", "td.close-control", function(){
        closePeriod($(this));
    })

    $("#periodList tbody").on("click", "td.edit-control", function(){
        editPeriod($(this));
    })

    $("#periodList tbody").on("click", "td.remove-control", function(){
        removePeriod($(this));
    })

    $("#updatePer").button().on( "click", function() {
        updatePeriod();
    });

});

