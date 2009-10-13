var JSONCallCount = 0;

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

JSONCallback.prototype.xmlHttpCallback = function (xmlHttp)
{
	if (xmlHttp.readyState == 4)
    {// 4 = "loaded"
        if (xmlHttp.status == 200)
        {// 200 = OK
            try {
                var JSONOutput = JSON.parse(xmlHttp.responseText);
            } catch (e) {
                alert("An exception occurred in the script. Error name: " + e.name 
                  + ". Error message: " + e.message + ". Response was " + this.xmlHttp.responseText); 
            }

			this.callback(JSONOutput);
        }
        else
        {
            document.getElementById(this.errorElement).innerHTML = "<p>" + xmlHttp.status + ": Data is out of data. Updating, please wait...</p>";
        }
        
        JSONCallCount--;
    }
}

JSONCallback.prototype.call = function(data)
{
	var method = "POST";
	
	if (typeof data == 'undefined') {
		method = "GET";
		data = null;
	}
		
	JSONCallCount++;

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
        this.xmlHttp.onreadystatechange = this.xmlHttpCallback(this.xmlHttp);
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
