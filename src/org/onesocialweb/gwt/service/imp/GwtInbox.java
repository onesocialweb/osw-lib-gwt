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
import java.util.List;

import org.onesocialweb.gwt.model.GwtActivityDomReader;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.Stream;
import org.onesocialweb.gwt.service.StreamEvent;
import org.onesocialweb.gwt.service.StreamEvent.Type;
import org.onesocialweb.gwt.util.ObservableHelper;
import org.onesocialweb.gwt.util.Observer;
import org.onesocialweb.gwt.xml.ElementAdapter;
import org.onesocialweb.model.activity.ActivityEntry;
import org.onesocialweb.xml.dom.ActivityDomReader;
import org.w3c.dom.Element;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.gwt.GWTPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class GwtInbox implements Stream<ActivityEntry> {

	private final ObservableHelper<StreamEvent<ActivityEntry>> helper = new ObservableHelper<StreamEvent<ActivityEntry>>();

	private final Session session = Suco.get(Session.class);

	private final List<ActivityEntry> entries = new ArrayList<ActivityEntry>();

	private boolean isReady = false;

	@Override
	public void refresh(final RequestCallback<List<ActivityEntry>> callback) {
		IQ iq = new IQ(IQ.Type.get);
		IPacket pubsubElement = iq.addChild("pubsub",
				"http://jabber.org/protocol/pubsub");
		IPacket itemsElement = pubsubElement.addChild("items",
				"http://jabber.org/protocol/pubsub");
		itemsElement.setAttribute("node",
				"http://onesocialweb.org/spec/1.0/inbox");

		session.sendIQ("osw", iq, new Listener<IPacket>() {

			public void onEvent(IPacket packet) {
				// Check if not an error
				if (IQ.isSuccess(packet)) {
					// IQ Succes means we can clear the existing entries
					entries.clear();
					// Parse the packet and check if a proper query result
					IPacket pubsubResult = packet.getFirstChild("pubsub");
					IPacket itemsResult = pubsubResult.getFirstChild("items");
					if (itemsResult != null
							&& itemsResult.getChildrenCount() > 0) {
						// Parse the packet and store the notifications
						for (IPacket item : itemsResult.getChildren()) {
							if (item instanceof GWTPacket) {
								GWTPacket i_packet = (GWTPacket) item
										.getFirstChild("entry");
								if (i_packet == null)
									continue;

								Element element = new ElementAdapter(i_packet
										.getElement());
								ActivityDomReader reader = new GwtActivityDomReader();
								ActivityEntry activity = reader
										.readEntry(element);
								entries.add(activity);
							}
						}
					}

					// Set the ready flag to true
					isReady = true;

					// Fire the event to the observer
					helper.fireEvent(new InboxEvent(Type.refreshed, entries));

					// Execute the callback
					if (callback != null) {
						callback.onSuccess(entries);
					}

					// Done
					return;
				}

				// If we are here, there was an error
				if (callback != null) {
					callback.onFailure();
				}
			}
		});
	}

	@Override
	public List<ActivityEntry> getItems() {
		return entries;
	}

	@Override
	public void registerEventHandler(
			Observer<StreamEvent<ActivityEntry>> handler) {
		helper.registerEventHandler(handler);
	}

	@Override
	public void unregisterEventHandler(
			Observer<StreamEvent<ActivityEntry>> handler) {
		helper.unregisterEventHandler(handler);
	}

	@Override
	public boolean isReady() {
		return isReady;
	}

	public void addItem(ActivityEntry item) {
		entries.add(0, item);
		List<ActivityEntry> items = new ArrayList<ActivityEntry>();
		items.add(item);
		helper.fireEvent(new InboxEvent(Type.added, items));
	}
	
	public void updateItem(ActivityEntry item) {
		ActivityEntry previousActivity = getItem(item.getId());
		int index= entries.indexOf(previousActivity);
		
		entries.remove(index);
		entries.add(index, item);
		
		List<ActivityEntry> items = new ArrayList<ActivityEntry>();
		items.add(item);
		helper.fireEvent(new InboxEvent(Type.refreshed, items));
	}
		
	
	public void deleteItem(ActivityEntry item){
		entries.remove(item);		
		List<ActivityEntry> items = new ArrayList<ActivityEntry>();
		items.add(item);
		helper.fireEvent(new InboxEvent(Type.removed, items));
		
	}
	
	public ActivityEntry getItem(String activityId){
		for (ActivityEntry entry : entries) {
			if (entry.getId().equals(activityId)) {
				return entry;
			}
		}
		return null;
	}

	private class InboxEvent extends StreamEvent<ActivityEntry> {


		public InboxEvent(Type type, List<ActivityEntry> items) {
			super(type, items);
		}
				
				
	}
	
	

}
