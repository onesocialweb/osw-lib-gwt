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
package org.onesocialweb.gwt.service.imp;

import org.onesocialweb.gwt.service.RosterEvent;
import org.onesocialweb.gwt.service.RosterItem;

public class GwtRosterEvent implements RosterEvent {

	private final Type type;

	private final RosterItem item;

	public GwtRosterEvent(Type type, RosterItem item) {
		this.type = type;
		this.item = item;
	}

	@Override
	public RosterItem getItem() {
		return item;
	}

	@Override
	public Type getType() {
		return type;
	}

}
