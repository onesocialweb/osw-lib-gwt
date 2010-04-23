/*
 *  Copyright 2010 Vodafone Group Services Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *    
 */
package org.onesocialweb.gwt.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

public class ElementAdapter extends NodeAdapter implements Element {

	public ElementAdapter(com.google.gwt.xml.client.Element element) {
		super(element);
	}

	@Override
	public String getAttribute(String name) {
		return getGwtElement().getAttribute(name);
	}

	@Override
	public String getAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr getAttributeNode(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr getAttributeNodeNS(String namespaceURI, String localName)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getElementsByTagName(String name) {
		return new NodeListAdapter(getGwtElement().getElementsByTagName(name));
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
			throws DOMException {
		return getElementsByTagName(localName);
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagName() {
		return getGwtElement().getTagName();
	}

	@Override
	public boolean hasAttribute(String name) {
		return getGwtElement().hasAttribute(name);
	}

	@Override
	public boolean hasAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		return hasAttribute(localName);
	}

	@Override
	public void removeAttribute(String name) throws DOMException {
		getGwtElement().removeAttribute(name);
	}

	@Override
	public void removeAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		removeAttribute(localName);
	}

	@Override
	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String name, String value) throws DOMException {
		getGwtElement().setAttribute(name, value);
	}

	@Override
	public void setAttributeNS(String namespaceURI, String qualifiedName,
			String value) throws DOMException {
		getGwtElement().setAttribute(qualifiedName, value);
	}

	@Override
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIdAttribute(String name, boolean isId) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIdAttributeNS(String namespaceURI, String localName,
			boolean isId) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIdAttributeNode(Attr idAttr, boolean isId)
			throws DOMException {
		// TODO Auto-generated method stub

	}

	public com.google.gwt.xml.client.Element getGwtElement() {
		return (com.google.gwt.xml.client.Element) getGwtNode();
	}

}
