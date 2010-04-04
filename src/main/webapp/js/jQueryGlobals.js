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

$.ajaxSetup({
    tryCount: 0,
    retryLimit: 3,
    error: whistAjaxErrorHandler,
    timeout: 10000,
    dataType: 'json'
});