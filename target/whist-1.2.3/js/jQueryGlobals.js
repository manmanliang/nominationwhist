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
        writeMessage("Error: " + textStatus + " - giving up");
    }
}

$(document).ajaxSend(function(e, xhr, settings) {
	$("#ajaxProgressCount").text(settings.tryCount);
	$("#ajaxProgressAction").text(settings.progressAction);
    settings.initTimeoutRef = setTimeout("whistAjaxInitTimeoutHandler()", ajaxInitTimeout);
});

$(document).ajaxStop(function() {
	$("#ajaxProgress").css('display', 'none');
});

$(document).ajaxComplete(function(e, xhr, settings) {
    clearTimeout(settings.initTimeoutRef);
});

$.ajaxSetup({
    tryCount: 1,
    retryLimit: 4,
    error: whistAjaxErrorHandler,
    timeout: ajaxTimeout,
    dataType: 'json',
    contentType: 'application/json',
    type: 'POST'
});