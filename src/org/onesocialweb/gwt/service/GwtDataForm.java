package org.onesocialweb.gwt.service;

import java.util.ArrayList;
import java.util.List;

import org.onesocialweb.gwt.model.GwtFormField;

public class GwtDataForm {
	

	List<GwtFormField> fields = new ArrayList<GwtFormField>();
	
	public List<GwtFormField> getFields() {
		return fields;
	}

	public void addField(GwtFormField field){
		fields.add(field);
	}
}
