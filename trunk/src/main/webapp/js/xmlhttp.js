function xmlHttpRestart(data) {
	this.xmlHttp.abort();
    this.timeoutCount++;
    
    // Report this to the user
    if (this.timeoutCount > 3) {
        writeMessage("There is a problem connecting to the server please check your network");
    } else {
        writeMessage("Game table is out of date, updating...");
    }
    
	this.call(data);
}

function JSONCallback() {}

JSONCallback.prototype.xmlHttp;
JSONCallback.prototype.timer;
JSONCallback.prototype.errorElement;
JSONCallback.prototype.callback;
JSONCallback.prototype.timeout;
JSONCallback.prototype.timeoutCount;
JSONCallback.prototype.url;

JSONCallback.prototype.init = function(url, errorElement, timeout) {
	if (typeof errorElement == 'undefined') {
		errorElement = "error";
	}
	if (typeof timeout == 'undefined') {
		timeout = 5000;
	}
	
	this.url = url;
	this.timeout = timeout;
	this.errorElement = errorElement;
    this.timeoutCount = 0;
}

JSONCallback.prototype.xmlHttpCallback = function () {
	if (this.xmlHttp.readyState == 4) {
		// 4 = "loaded"
        
        if (this.xmlHttp.status != 0) {
            // Not aborting so see what happened
            
            // Clear the timer information
            if (this.timer) {
                clearTimeout(this.timer);
            }
            this.timeoutCount = 0;
		
            if (this.xmlHttp.status == 200) {
                // 200 = OK
                try {
                    var JSONOutput = JSON.parse(this.xmlHttp.responseText);
                } catch (e) {
                    alert("An exception occurred in the script. Error name: " + e.name 
                    + ". Error message: " + e.message + ". Response was " + this.xmlHttp.responseText); 
                }

                if (!JSONOutput.internalError) {
                    this.callback(JSONOutput);
                } else {
                    writeMessage(JSONOutput.internalError);
                }
            } else {
                writeMessage("Sorry there has been an error, please report this error code: " + this.xmlHttp.status);
            }
        }
        
        delete this.xmlHttp;
    }
}

JSONCallback.prototype.call = function(data) {
	// Store a copy of this classes "this" so we can get it in the callback
	// We'll use this when we make an anonymous function for the stateChange handler
	var classInstanceThis = this;

	var method = "POST";
	
	if (typeof data == 'undefined') {
		method = "GET";
		data = null;
	}
		
    if (window.XMLHttpRequest)
    {// code for all new browsers
        this.xmlHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject)
    {// code for IE5 and IE6
        this.xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    
    if (this.xmlHttp != null)
    {
        this.xmlHttp.onreadystatechange = function () {classInstanceThis.xmlHttpCallback()};
        this.xmlHttp.open(method, this.url, true);
        this.xmlHttp.send(data);
        this.timer = setTimeout(function() {xmlHttpRestart.call(classInstanceThis, data);}, this.timeout);
    }
    else
    {
        alert("Your browser does not support XMLHTTP.");
    }
}