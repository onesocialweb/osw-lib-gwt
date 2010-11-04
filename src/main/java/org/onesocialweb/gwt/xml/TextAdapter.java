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
import org.w3c.dom.Text;

public class TextAdapter extends CharacterDataAdapter implements Text {

	public TextAdapter(com.google.gwt.xml.client.Text text) {
		super(text);
	}

	@Override
	public String getWholeText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isElementContentWhitespace() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Text replaceWholeText(String content) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Text splitText(int offset) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	protected com.google.gwt.xml.client.Text getGwtText() {
		return (com.google.gwt.xml.client.Text) getGwtNode();
	}

}
