function loadXMLDoc(externalFunc, url)
{
    var xmlhttp=null;
    if (window.XMLHttpRequest)
    {// code for all new browsers
        xmlhttp=new XMLHttpRequest();
    }
    else if (window.ActiveXObject)
    {// code for IE5 and IE6
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    
    if (xmlhttp!=null)
    {
        xmlhttp.onreadystatechange=externalFunc;
        xmlhttp.open("GET",url,true);
        xmlhttp.send(null);
        return xmlhttp;
    }
    else
    {
        alert("Your browser does not support XMLHTTP.");
        return null;
    }
}
