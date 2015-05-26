$(document).ready(function() {
    $("#gestFi").click(function(){
        //Mettre tout ce dont tu a besoin pour gérer une filière
        // 1- Menu (a- Liste des voeux, b-
        // 2- Les information relative à la filière
        $("#contentId").html("Vous pouvez gerer votre filiere");
    })

    function acceptWish(param){
        if(confirm("Etes-vous sûr de vouloir accepter ce voeu ?")){
            var $id = parseInt(param.attr("data-id"));
            $.ajax({
                type: 'PUT',
                contentType:'application/json',
                url: '/ServeurDeVoeux/voeux/filiere/',
                data: '{"id": '+$id+'}',
                dataType: 'JSON',
                timeout: 3000,
                success: function(data) {
                },
                error: function() {
                    console.log('La requête n\'a pas abouti');
                }
            });

            var $info = $("<div class='alert alert-success' role='alert'>");
            $info.html("<button type='button' class='close' data-dismiss='alert' " +
                "aria-label='Close'> <span aria-hidden='true'>&times;</span></button>" +
                "Le voeu a bien &eacutet&eacute accept&eacute");
            $info.prependTo($("#contentId"));

        }

    }

    function rejectWish(param){
        if(confirm("Etes-vous sûr de vouloir rejeté ce voeu ?")){
            var $id = parseInt(param.attr("data-id"));
            $.ajax({
                type: 'PUT',
                contentType:'application/json',
                url: '/ServeurDeVoeux/voeux/filiere/reject/',
                data: '{"id": '+$id+'}',
                dataType: 'JSON',
                timeout: 3000,
                success: function(data) {
                },
                error: function() {
                    console.log('La requête n\'a pas abouti');
                }
            });

            var $info = $("<div class='alert alert-success' role='alert'>");
            $info.html("<button type='button' class='close' data-dismiss='alert' " +
                "aria-label='Close'> <span aria-hidden='true'>&times;</span></button>" +
                "Le voeu a bien &eacutet&eacute rejet&eacute");
            $info.prependTo($("#contentId"));

        }
    }

    $(".wishesList tbody").on("click", "td.accept-control", function(){
        acceptWish($(this));
    })

    $(".wishesList tbody").on("click", "td.reject-control", function(){
        rejectWish($(this));
    })

});