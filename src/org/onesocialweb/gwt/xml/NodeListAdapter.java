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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListAdapter implements NodeList {

	private List<Node> nodes = new ArrayList<Node>();

	public NodeListAdapter(com.google.gwt.xml.client.NodeList nodeList) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			nodes.add(NodeAdapter.adapt(nodeList.item(i)));
		}
	}

	@Override
	public int getLength() {
		return nodes.size();
	}

	@Override
	public Node item(int index) {
		return nodes.get(index);
	}

}
