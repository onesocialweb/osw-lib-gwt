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
package org.onesocialweb.gwt.util;

/**
 * Based on this post on stack overflow
 * http://stackoverflow.com/questions/439298
 * /best-way-to-encode-text-data-for-xml-in-java
 * 
 * @author http://stackoverflow.com/users/53897/thorbjorn-ravn-andersen
 * 
 */
public class HtmlHelper {

	/**
	 * Returns the string where all non-ascii and <, &, > are encoded as numeric
	 * entities. I.e. "&lt;A &amp; B &gt;" .... (insert result here). The result
	 * is safe to include anywhere in a text field in an XML-string. If there
	 * was no characters to protect, the original string is returned.
	 * 
	 * @param originalString
	 *            original string which may contain characters either reserved
	 *            in XML or with different representation in different encodings
	 *            (like 8859-1 and UFT-8)
	 * @return
	 */
	public static String encode(String originalString) {
		if (originalString == null) {
			return null;
		}
		boolean anyCharactersProtected = false;

		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < originalString.length(); i++) {
			char ch = originalString.charAt(i);

			// TODO This is a dirty hack and could be improved
			if (ch == '&') {
				if (i<originalString.length()-1) {
					char next = originalString.charAt(i+1);
					if (next == '#') {
						stringBuffer.append(ch);
						continue;
					}
				}
			}
			
			if (ch == '<' || ch == '>' || ch == '&') {
				stringBuffer.append("&#" + (int) ch + ";");
				anyCharactersProtected = true;
			} else {
				stringBuffer.append(ch);
			}
		}
		if (anyCharactersProtected == false) {
			return originalString;
		}

		return stringBuffer.toString();
	}
}