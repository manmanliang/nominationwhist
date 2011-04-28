function gameStartCall() {
	$.ajax({
        url: 'gameStart', 
        data: JSON.stringify({id:game.id, phase:-1}), 
        success: gameStartCallback,
        progressAction: "Checking for start of game"
    });
}

function updateCall(phase) {
    $.ajax({
        url: 'update',
        data: JSON.stringify({id:game.id, phase:phase}),
        success: updateCallback,
        progressAction: "Updating"
    });
}

function gameListCall() {
    $.ajax({
        url: 'games',
        success: gameListCallback,
        progressAction: "Retrieving available games"
    });
}

// What to do if the initial timeout fires
function whistAjaxInitTimeoutHandler() {
    $("#ajaxProgress").css('display', 'inline');
}

function whistAjaxErrorHandler(xhr, textStatus, exception) {
    if (textStatus == 'timeout') {
        this.tryCount++;
        if (this.tryCount <= this.retryLimit) {
            writeMessage("Request failed, trying again");
            $.ajax(this);
        } else {
            writeMessage("Network problem, giving up");
        }
    } else {
        writeMessage("Error: " + xhr.responseText + " - giving up");
    }
}

$(document).ajaxSend(function(e, xhr, settings) {
	$("#ajaxProgressCount").text(settings.tryCount);
	$("#ajaxProgressAction").text(settings.progressAction);
    this.initTimeoutRef = setTimeout("whistAjaxInitTimeoutHandler()", 500);
});

$(document).ajaxStop(function() {
    clearTimeout(this.initTimeoutRef);
	$("#ajaxProgress").css('display', 'none');
});

$.ajaxSetup({
    tryCount: 1,
    retryLimit: 4,
    error: whistAjaxErrorHandler,
    timeout: 10000,
    dataType: 'json',
    contentType: 'application/json',
    type: 'POST'
});