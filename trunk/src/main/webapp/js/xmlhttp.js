function JSONCallback() {}

JSONCallback.prototype.xmlHttp;
JSONCallback.prototype.errorElement;
JSONCallback.prototype.callback;
JSONCallback.prototype.url;

JSONCallback.prototype.init = function(url, errorElement) 
{
	if (typeof errorElement == 'undefined') {
		errorElement = "error";
	}
	
	this.errorElement = errorElement;
	this.url = url;
}

JSONCallback.prototype.xmlHttpCallback = function ()
{
	var errorDiv;
	if (this.xmlHttp.readyState == 4)
    {// 4 = "loaded"
        if (this.xmlHttp.status == 200)
        {// 200 = OK
            try {
                var JSONOutput = JSON.parse(this.xmlHttp.responseText);
            } catch (e) {
                alert("An exception occurred in the script. Error name: " + e.name 
                  + ". Error message: " + e.message + ". Response was " + this.xmlHttp.responseText); 
            }

			if (!JSONOutput.internalError)
			{
				this.callback(JSONOutput);
				return;			
			}
			else
			{
	        	errorDiv = document.getElementById(this.errorElement);
    	        errorDiv.innerHTML = errorDiv.innerHTML + "<p>" + JSONOutput.internalError + "</p>";				
			}
        }
        else
        {
        	errorDiv = document.getElementById(this.errorElement);
            errorDiv.innerHTML = errorDiv.innerHTML + "<p>" + this.xmlHttp.status + ": Data is out of data. Updating, please wait...</p>";
        }
        delete this.xmlHttp;
    }
}

JSONCallback.prototype.call = function(data)
{
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
        return this.xmlHttp;
    }
    else
    {
        alert("Your browser does not support XMLHTTP.");
        return null;
    }
}
