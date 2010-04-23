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

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

import com.google.gwt.xml.client.CDATASection;
import com.google.gwt.xml.client.CharacterData;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;

public class NodeAdapter implements Node {

	private final com.google.gwt.xml.client.Node node;

	public static Node adapt(com.google.gwt.xml.client.Node node) {
		if (node instanceof Element) {
			return new ElementAdapter((Element) node);
		} else if (node instanceof CharacterData) {
			return new CharacterDataAdapter((CharacterData) node);
		} else if (node instanceof Text) {
			return new TextAdapter((Text) node);
		} else if (node instanceof CDATASection) {
			return new CDataSectionAdapter((CDATASection) node);
		} else if (node instanceof com.google.gwt.xml.client.Document) {
			return new DocumentAdapter(
					(com.google.gwt.xml.client.Document) node);
		} else {
			return new NodeAdapter(node);
		}
	}

	public NodeAdapter(com.google.gwt.xml.client.Node node) {
		this.node = node;
	}

	@Override
	public Node appendChild(Node newChild) throws DOMException {
		if (newChild instanceof NodeAdapter) {
			getGwtNode().appendChild(((NodeAdapter) newChild).getGwtNode());
			return newChild;
		}
		return null;
	}

	@Override
	public Node cloneNode(boolean deep) {
		return new NodeAdapter(getGwtNode().cloneNode(deep));
	}

	@Override
	public short compareDocumentPosition(Node other) throws DOMException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NamedNodeMap getAttributes() {
		return new NamedNodeMapAdapter(getGwtNode().getAttributes());
	}

	@Override
	public String getBaseURI() {
		return null;
	}

	@Override
	public NodeList getChildNodes() {
		return new NodeListAdapter(getGwtNode().getChildNodes());
	}

	@Override
	public Object getFeature(String feature, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getFirstChild() {
		return NodeAdapter.adapt(getGwtNode().getFirstChild());
	}

	@Override
	public Node getLastChild() {
		return NodeAdapter.adapt(getGwtNode().getLastChild());
	}

	@Override
	public String getLocalName() {
		return null;
	}

	@Override
	public String getNamespaceURI() {
		return getGwtNode().getNamespaceURI();
	}

	@Override
	public Node getNextSibling() {
		return NodeAdapter.adapt(getGwtNode().getNextSibling());
	}

	@Override
	public String getNodeName() {
		return getGwtNode().getNodeName();
	}

	@Override
	public short getNodeType() {
		return getGwtNode().getNodeType();
	}

	@Override
	public String getNodeValue() throws DOMException {
		return getGwtNode().getNodeValue();
	}

	@Override
	public Document getOwnerDocument() {
		return new DocumentAdapter(getGwtNode().getOwnerDocument());
	}

	@Override
	public Node getParentNode() {
		return NodeAdapter.adapt(getGwtNode().getParentNode());
	}

	@Override
	public String getPrefix() {
		return getGwtNode().getPrefix();
	}

	@Override
	public Node getPreviousSibling() {
		return NodeAdapter.adapt(getGwtNode().getPreviousSibling());
	}

	@Override
	public String getTextContent() throws DOMException {
		StringBuffer buffer = new StringBuffer();
		if (hasChildNodes()) {
			NodeList nodes = getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				buffer.append(nodes.item(i).getTextContent());
			}
		}
		return buffer.toString();
	}

	@Override
	public Object getUserData(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttributes() {
		return getGwtNode().hasAttributes();
	}

	@Override
	public boolean hasChildNodes() {
		return getGwtNode().hasChildNodes();
	}

	@Override
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		if (newChild instanceof NodeAdapter && refChild instanceof NodeAdapter) {
			return NodeAdapter.adapt(getGwtNode().insertBefore(
					((NodeAdapter) newChild).getGwtNode(),
					((NodeAdapter) refChild).getGwtNode()));
		}
		return null;
	}

	@Override
	public boolean isDefaultNamespace(String namespaceURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEqualNode(Node arg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSameNode(Node other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSupported(String feature, String version) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String lookupNamespaceURI(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String lookupPrefix(String namespaceURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void normalize() {
		// TODO Auto-generated method stub

	}

	@Override
	public Node removeChild(Node oldChild) throws DOMException {
		if (oldChild instanceof NodeAdapter) {
			return NodeAdapter.adapt(getGwtNode().removeChild(
					((NodeAdapter) oldChild).getGwtNode()));
		}
		return null;
	}

	@Override
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		if (newChild instanceof NodeAdapter && oldChild instanceof NodeAdapter) {
			return NodeAdapter.adapt(getGwtNode().replaceChild(
					((NodeAdapter) newChild).getGwtNode(),
					((NodeAdapter) oldChild).getGwtNode()));
		}
		return null;
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
		getGwtNode().setNodeValue(nodeValue);
	}

	@Override
	public void setPrefix(String prefix) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTextContent(String textContent) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return getGwtNode().toString();
	}

	public com.google.gwt.xml.client.Node getGwtNode() {
		return this.node;
	}

}
