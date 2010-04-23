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
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class DocumentAdapter extends NodeAdapter implements Document {

	public DocumentAdapter(com.google.gwt.xml.client.Document document) {
		super(document);
	}

	@Override
	public Node adoptNode(Node source) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr createAttribute(String name) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr createAttributeNS(String namespaceURI, String qualifiedName)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CDATASection createCDATASection(String data) throws DOMException {
		return new CDataSectionAdapter(getGwtDocument()
				.createCDATASection(data));
	}

	@Override
	public Comment createComment(String data) {
		return new CommentAdapter(getGwtDocument().createComment(data));
	}

	@Override
	public DocumentFragment createDocumentFragment() {
		return new DocumentFragmentAdapter(getGwtDocument()
				.createDocumentFragment());
	}

	@Override
	public Element createElement(String tagName) throws DOMException {
		return new ElementAdapter(getGwtDocument().createElement(tagName));
	}

	@Override
	public Element createElementNS(String namespaceURI, String qualifiedName)
			throws DOMException {
		com.google.gwt.xml.client.Element e = getGwtDocument().createElement(
				qualifiedName);
		e.setAttribute("xmlns", namespaceURI);
		return new ElementAdapter(e);
	}

	@Override
	public EntityReference createEntityReference(String name)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessingInstruction createProcessingInstruction(String target,
			String data) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Text createTextNode(String data) {
		return new TextAdapter(getGwtDocument().createTextNode(data));
	}

	@Override
	public DocumentType getDoctype() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getDocumentElement() {
		return new ElementAdapter(getGwtDocument().getDocumentElement());
	}

	@Override
	public String getDocumentURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DOMConfiguration getDomConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getElementById(String elementId) {
		return new ElementAdapter(getGwtDocument().getElementById(elementId));
	}

	@Override
	public NodeList getElementsByTagName(String tagname) {
		return new NodeListAdapter(getGwtDocument().getElementsByTagName(
				tagname));
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		return getElementsByTagName(localName);
	}

	@Override
	public DOMImplementation getImplementation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInputEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getStrictErrorChecking() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getXmlEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getXmlStandalone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getXmlVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node importNode(Node importedNode, boolean deep) throws DOMException {
		if (importedNode instanceof NodeAdapter) {
			return new NodeAdapter(getGwtDocument().importNode(
					((NodeAdapter) importedNode).getGwtNode(), deep));
		}
		return null;
	}

	@Override
	public void normalizeDocument() {
		getGwtDocument().normalize();
	}

	@Override
	public Node renameNode(Node n, String namespaceURI, String qualifiedName)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentURI(String documentURI) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStrictErrorChecking(boolean strictErrorChecking) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setXmlVersion(String xmlVersion) throws DOMException {
		// TODO Auto-generated method stub

	}

	protected com.google.gwt.xml.client.Document getGwtDocument() {
		return (com.google.gwt.xml.client.Document) getGwtNode();
	}

}
