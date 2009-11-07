function toggleInstructions(currentDiv) {
    var instructionsLink = document.getElementById("instructionsLink");
    
    if (instructionsLink.innerHTML == "Show Instructions") {
        instructionsLink.innerHTML = "Hide Instructions";
        document.getElementById(currentDiv).style.display = "none";
        document.getElementById("instructions").style.display = "";
    } else {
        instructionsLink.innerHTML = "Show Instructions";
        document.getElementById("instructions").style.display = "none";
        document.getElementById(currentDiv).style.display = "";
    }
}

function showInstructions(selectedPane) {
    var panesArray = new Array("gameInstructions", "boardInstructions");

    for (pane in panesArray) {
        if (panesArray[pane] == selectedPane) {
            document.getElementById(panesArray[pane]).style.display = "";
        } else {
            document.getElementById(panesArray[pane]).style.display = "none";
        }
    }
}