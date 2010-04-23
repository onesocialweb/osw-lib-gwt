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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NamedNodeMapAdapter implements NamedNodeMap {

	private final com.google.gwt.xml.client.NamedNodeMap map;

	public NamedNodeMapAdapter(com.google.gwt.xml.client.NamedNodeMap map) {
		this.map = map;
	}

	@Override
	public int getLength() {
		return map.getLength();
	}

	@Override
	public Node getNamedItem(String name) {
		return new NodeAdapter(map.getNamedItem(name));
	}

	@Override
	public Node item(int index) {
		return new NodeAdapter(map.item(index));
	}

	@Override
	public Node getNamedItemNS(String namespaceURI, String localName)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node removeNamedItem(String name) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node removeNamedItemNS(String namespaceURI, String localName)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node setNamedItem(Node arg) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node setNamedItemNS(Node arg) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

}
