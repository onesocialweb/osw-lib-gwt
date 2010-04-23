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

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;

public class CharacterDataAdapter extends NodeAdapter implements CharacterData {

	public CharacterDataAdapter(com.google.gwt.xml.client.CharacterData data) {
		super(data);
	}

	@Override
	public void appendData(String arg) throws DOMException {
		getGwtCharacterData().appendData(arg);
	}

	@Override
	public void deleteData(int offset, int count) throws DOMException {
		getGwtCharacterData().deleteData(offset, count);
	}

	@Override
	public String getData() throws DOMException {
		return getGwtCharacterData().getData();
	}

	@Override
	public int getLength() {
		return getGwtCharacterData().getLength();
	}

	@Override
	public void insertData(int offset, String arg) throws DOMException {
		getGwtCharacterData().insertData(offset, arg);
	}

	@Override
	public void replaceData(int offset, int count, String arg)
			throws DOMException {
		getGwtCharacterData().replaceData(offset, count, arg);
	}

	@Override
	public void setData(String data) throws DOMException {
		getGwtCharacterData().setData(data);
	}

	@Override
	public String substringData(int offset, int count) throws DOMException {
		return getGwtCharacterData().substringData(offset, count);
	}

	@Override
	public String getTextContent() {
		return getData();
	}

	protected com.google.gwt.xml.client.CharacterData getGwtCharacterData() {
		return (com.google.gwt.xml.client.CharacterData) getGwtNode();
	}

}
