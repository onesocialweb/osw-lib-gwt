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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.Roster;
import org.onesocialweb.gwt.service.RosterEvent;
import org.onesocialweb.gwt.service.RosterItem;
import org.onesocialweb.gwt.util.ObservableHelper;
import org.onesocialweb.gwt.util.Observer;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;

public class GwtRoster implements Roster {

	private final com.calclab.emite.im.client.roster.Roster roster;

	private final List<RosterItem> items = new ArrayList<RosterItem>();

	private final ObservableHelper<RosterEvent> observableHelper = new ObservableHelper<RosterEvent>();

	private final HashMap<String, RequestCallback<RosterItem>> itemCallbacks = new HashMap<String, RequestCallback<RosterItem>>();

	public GwtRoster(com.calclab.emite.im.client.roster.Roster roster) {
		this.roster = roster;
		this.roster.onRosterRetrieved(new RosterRefreshedListener());
		this.roster.onItemAdded(new ItemAddedListener());
		this.roster.onItemChanged(new ItemChangedListener());
		this.roster.onItemRemoved(new ItemRemovedListener());
	}

	@Override
	public Set<String> getGroups() {
		return roster.getGroups();
	}

	@Override
	public List<RosterItem> getItems() {
		return items;
	}

	@Override
	public RosterItem getItem(String jid) {
		for (RosterItem item : items) {
			if (item.getJid().equals(jid)) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void addItem(String jid, String nickname, List<String> groups,
			RequestCallback<RosterItem> callback) {
		// Keep a reference to the callback
		if (callback != null) {
			itemCallbacks.put(jid, callback);
		}

		// Convert the list to an array
		String[] groupsArray = new String[groups.size()];
		for (int i = 0; i < groups.size(); i++) {
			groupsArray[i] = groups.get(i);
		}

		// Request the item to be added
		roster.addItem(XmppURI.jid(jid), nickname, groupsArray);
	}

	@Override
	public void removeItem(String jid, RequestCallback<RosterItem> callback) {
		// Keep a reference to the callback
		if (callback != null) {
			itemCallbacks.put(jid, callback);
		}

		// Request the item to be removed
		roster.removeItem(XmppURI.jid(jid));
	}

	@Override
	public void registerEventHandler(Observer<RosterEvent> handler) {
		observableHelper.registerEventHandler(handler);
	}

	@Override
	public void unregisterEventHandler(Observer<RosterEvent> handler) {
		observableHelper.unregisterEventHandler(handler);
	}

	public void update(RosterItem item, RequestCallback<RosterItem> callback) {
		// Keep a reference to the callback
		if (callback != null) {
			itemCallbacks.put(item.getJid(), callback);
		}

		// Convert the list to an array
		String[] groupsArray = new String[item.getGroups().size()];
		for (int i = 0; i < item.getGroups().size(); i++) {
			groupsArray[i] = item.getGroups().get(i);
		}

		// Request the item to be updated
		roster.updateItem(XmppURI.jid(item.getJid()), item.getNickname(),
				groupsArray);
	}

	private class RosterRefreshedListener implements
			Listener<Collection<com.calclab.emite.im.client.roster.RosterItem>> {

		@Override
		public void onEvent(
				Collection<com.calclab.emite.im.client.roster.RosterItem> parameter) {
			items.clear();
			for (com.calclab.emite.im.client.roster.RosterItem rosterItem : parameter) {
				items.add(new GwtRosterItem(rosterItem));
			}
			observableHelper.fireEvent(new GwtRosterEvent(
					RosterEvent.Type.refreshed, null));
		}

	}

	private class ItemAddedListener implements
			Listener<com.calclab.emite.im.client.roster.RosterItem> {

		@Override
		public void onEvent(com.calclab.emite.im.client.roster.RosterItem item) {
			// Add the item to the internal collection
			GwtRosterItem gwtItem = new GwtRosterItem(item);
			items.add(gwtItem);

			// If we requested this and have a callback, fire it now
			RequestCallback<RosterItem> callback = itemCallbacks.get(gwtItem
					.getJid());
			if (callback != null) {
				callback.onSuccess(gwtItem);
				itemCallbacks.remove(gwtItem.getJid());
			}

			// Fire the global roster event
			observableHelper.fireEvent(new GwtRosterEvent(
					RosterEvent.Type.added, gwtItem));
		}

	}

	private class ItemChangedListener implements
			Listener<com.calclab.emite.im.client.roster.RosterItem> {

		@Override
		public void onEvent(
				com.calclab.emite.im.client.roster.RosterItem changed) {
			for (Iterator<RosterItem> i = items.iterator(); i.hasNext();) {
				RosterItem item = i.next();
				if (item.getJid().equals(changed.getJID().toString())) {
					// Update the item (this will trigger item listeners
					((GwtRosterItem) item).setItem(changed);

					// If we requested this and have a callback, fire it now
					RequestCallback<RosterItem> callback = itemCallbacks
							.get(item.getJid());
					if (callback != null) {
						callback.onSuccess(item);
						itemCallbacks.remove(item.getJid());
					}

					// Fire the global roster event
					observableHelper.fireEvent(new GwtRosterEvent(
							RosterEvent.Type.changed, item));
				}
			}
		}

	}

	private class ItemRemovedListener implements
			Listener<com.calclab.emite.im.client.roster.RosterItem> {

		@Override
		public void onEvent(
				com.calclab.emite.im.client.roster.RosterItem removed) {
			// Delete the item from the local roster
			for (Iterator<RosterItem> i = items.iterator(); i.hasNext();) {
				RosterItem item = i.next();
				if (item.getJid().equals(removed.getJID().toString())) {
					// Remove from the roster
					i.remove();

					// If we requested this and have a callback, fire it now
					RequestCallback<RosterItem> callback = itemCallbacks
							.get(removed.getJID().toString());
					if (callback != null) {
						callback.onSuccess(item);
						itemCallbacks.remove(item.getJid());
					}

					// Trigger roster observers
					observableHelper.fireEvent(new GwtRosterEvent(
							RosterEvent.Type.removed, item));
					break;
				}
			}
		}

	}

}
