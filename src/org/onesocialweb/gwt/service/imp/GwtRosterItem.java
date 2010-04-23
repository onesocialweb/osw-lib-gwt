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

import java.util.List;

import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.RosterEvent;
import org.onesocialweb.gwt.service.RosterItem;
import org.onesocialweb.gwt.util.ObservableHelper;
import org.onesocialweb.gwt.util.Observer;

import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;

public class GwtRosterItem implements RosterItem {

	private final ObservableHelper<RosterEvent> observableHelper = new ObservableHelper<RosterEvent>();

	private com.calclab.emite.im.client.roster.RosterItem item;

	public GwtRosterItem(com.calclab.emite.im.client.roster.RosterItem item) {
		this.item = item;
	}

	@Override
	public String getJid() {
		return item.getJID().toString();
	}

	@Override
	public Presence getPresence() {
		final Show show = item.getShow();
		if (show.equals(Show.away)) {
			return Presence.away;
		} else if (show.equals(Show.chat)) {
			return Presence.chat;
		} else if (show.equals(Show.dnd)) {
			return Presence.dnd;
		} else if (show.equals(Show.xa)) {
			return Presence.xa;
		} else {
			if (item.isAvailable()) {
				return Presence.available;
			} else {
				return Presence.unknown;
			}
		}
	}

	@Override
	public String getNickname() {
		return item.getName();
	}

	@Override
	public void setNickname(String nickname) {
		item.setName(nickname);
	}

	@Override
	public List<String> getGroups() {
		return item.getGroups();
	}

	@Override
	public void setGroups(List<String> groups) {
		item.setGroups(groups);
	}

	@Override
	public void save(RequestCallback<RosterItem> callback) {
		GwtRoster roster = (GwtRoster) GwtOswService.getInstance().getRoster();
		roster.update(this, callback);
	}

	@Override
	public void registerEventHandler(Observer<RosterEvent> handler) {
		observableHelper.registerEventHandler(handler);
	}

	@Override
	public void unregisterEventHandler(Observer<RosterEvent> handler) {
		observableHelper.unregisterEventHandler(handler);
	}

	public void setItem(com.calclab.emite.im.client.roster.RosterItem item) {
		this.item = item;
		observableHelper.fireEvent(new GwtRosterEvent(RosterEvent.Type.changed,
				this));
	}

}
