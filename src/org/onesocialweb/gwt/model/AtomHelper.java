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
package org.onesocialweb.gwt.model;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;

public class AtomHelper {

	public static final DateTimeFormat pattern = DateTimeFormat
			.getFormat("yyy-MM-dd'T'HH:mm:ss.SSSZZZ");

	public static Date parseDate(String date) {
		try {
			if (date.endsWith("Z")) {
				date = date.substring(0, date.length() - 1);
			}
			Date result = pattern.parse(date);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public static String formatDateUTC(Date date) {
		String result = pattern.format(date, TimeZone.createTimeZone(0));
		return result + "Z";
	}
	
	public static String formatDate(Date date) {
		return pattern.format(date);
	}
}
