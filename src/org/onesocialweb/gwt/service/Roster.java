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
package org.onesocialweb.gwt.service;

import java.util.List;
import java.util.Set;

import org.onesocialweb.gwt.util.Observable;

public interface Roster extends Observable<RosterEvent> {

	public Set<String> getGroups();

	public List<RosterItem> getItems();

	public RosterItem getItem(String jid);

	public void addItem(String jid, String nickname, List<String> groups,
			RequestCallback<RosterItem> callback);

	public void removeItem(String jid, RequestCallback<RosterItem> callback);

}
