


function showHtml() {  
  var renderer = (window["renderer"]);
  if (renderer == null) {
    return "Error:Renderer is null.";
  }
  
  var dhoMain = window["dho"];
  if(renderer == null){
	  return "Error:dhoMain is null";
  }
  
  var html = renderer.getHtmlToShow();
  return dhoMain.showHtml(html); 
}