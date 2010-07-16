package org.calgb.test.performance.html;

import java.util.HashMap;
import java.util.Vector;


public class HtmlElement {
	public HashMap<String, String> attributes;
	
	HtmlElement(HashMap<String,String> attr){
		this.attributes = attr;
	}
	HashMap<String, String> getAttributes(){
		return attributes;
	}
	
	public String getAttribute(String attr){
		return attributes.get(attr);
	}
	public void setAttribute(String attr, String value){
		 attributes.put(attr, value);
	}
	
}
