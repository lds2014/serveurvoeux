function confirmDelete(formulaire) {

    if(confirm("Etes-vous sur de vouloir supprimer ce voeu ?"))
    {
        document.forms[formulaire].submit();
    }
}

function confirmUpdate(formulaire) {

    if(confirm("Etes-vous sur de vouloir modifier ce voeu ?"))
    {
        document.forms[formulaire].submit();
    }
}

function historiqueVoeux()
{
    document.forms["historique_voeux"].submit();
}





