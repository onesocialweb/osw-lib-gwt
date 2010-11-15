package org.onesocialweb.gwt.model;


import static org.onesocialweb.xml.dom.DomHelper.appendTextNode;

import java.util.Iterator;

import org.onesocialweb.gwt.service.GwtDataForm;
import org.onesocialweb.model.vcard4.BirthdayField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GwtDataFormDomWriter {
	
	private String NS_X_DATA="jabber:x:data";

	
	public Element toElement(GwtDataForm form, Document document) {
		Element root = document.createElementNS(NS_X_DATA,"x");
		root.setAttribute("type", "submit");		
		document.appendChild(root);
		//we add the hidden field..
		
		Element hidden =root.getOwnerDocument().createElement("field");
		hidden.setAttribute("type", "hidden");
		Element value =(Element)hidden.appendChild(hidden.getOwnerDocument().createElement("value"));
		value.appendChild(value.getOwnerDocument().createTextNode("jabber:iq:register"));
		
		root.appendChild(hidden);
		//we add all other fields...
		write(form, root);
		return root;
	}
	
	private void write(GwtDataForm form, Element target){
		Iterator<GwtFormField> it=form.getFields().iterator();
		while (it.hasNext()){
			Element e = (Element) target.appendChild(target.getOwnerDocument().createElement("field"));
			GwtFormField field=it.next();
			write(field, e);	
			target.appendChild(e);
		}
	}
	
	private void write(GwtFormField field, Element target){
		target.setAttribute("label", field.getLabel());
		target.setAttribute("var", field.getAttribute());
		for (String str : field.getValues() ){
			Element value =(Element)target.appendChild(target.getOwnerDocument().createElement("value"));
			value.appendChild(value.getOwnerDocument().createTextNode(str));
			target.appendChild(value);
		}
		
	}
}
