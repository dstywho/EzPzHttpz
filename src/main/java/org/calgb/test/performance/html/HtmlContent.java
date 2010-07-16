package org.calgb.test.performance.html;

import java.util.HashMap;
import java.util.Vector;



public class HtmlContent {
	private HtmlElement element;
	private String innerHtml;
	
	HtmlContent(String html){
		
	}
	HtmlContent(HtmlElement htmlElement, String innerHtml){
		this.element =htmlElement;
		this.innerHtml = innerHtml;
	}
	
	public HtmlElement getElement(){
		return element;
	}
	
	HashMap<String, String> getAttributes(RegexMatch match, String tagName) {
		HashMap<String, String> attrPairs = new HashMap<String, String>();
		String tagDeclaration = match.find("<" + tagName + ".*?>").get(0)
				.getText();
		tagDeclaration = tagDeclaration.replaceAll("(<form)|(\\/*>$)|\\\"", "");
		String[] tagPairs = tagDeclaration.split("\\s");
		for (String tagPair : tagPairs) {
			if (tagPair.contains("=")) {
				int eqlIdx = tagPair.indexOf("=");
				attrPairs.put(tagPair.substring(0, eqlIdx), tagPair
						.substring(eqlIdx + 1));
			}
		}
		return attrPairs;
	}
	
	public HtmlElement find(String tagName, String attribute, String value){
			Vector<RegexMatch> matches = new RegexMatch(innerHtml
					.replaceAll("\\n|\\r", "")).find("<"+tagName+"[^>]+"+attribute +"="+ "\""+ value+"\""+".*?\\/>");
			
			for(RegexMatch match :matches){
				HashMap<String, String> attributes = getAttributes(match, tagName);
				return new HtmlElement(attributes);
			}
			return null;
	}
}
