function toggleInstructions(currentDiv) {
    if ($("#instructionsLink").text() == "Show Instructions") {
        $("#instructionsLink").text("Hide Instructions");
        $("#" + currentDiv).hide();
        $("#instructions").show();
    } else {
        $("#instructionsLink").text("Show Instructions");
        $("#instructions").hide();
        $("#" + currentDiv).show();
    }
}

function showInstructions(selectedPane) {
    var panesArray = new Array("gameInstructions", "boardInstructions");

    for (pane in panesArray) {
        if (panesArray[pane] == selectedPane) {
            $("#" + panesArray[pane]).show();
        } else {
            $("#" + panesArray[pane]).hide();
        }
    }
}