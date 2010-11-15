package org.onesocialweb.gwt.model;

import java.util.ArrayList;
import java.util.List;


public class GwtFormField {

	String attribute = new String();	
	List<String> values = new ArrayList<String>();	
	String label = new String();
	
	public List<String> getValues() {
		return values;
	}
	
	public String getAttribute() {
		return attribute;
	}

	public GwtFormField(String var){
		attribute=var;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void addValue(String value){
		values.add(value);
	}		
	
    
}
