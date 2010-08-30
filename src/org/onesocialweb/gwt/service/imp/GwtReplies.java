/*
 *  Copyright 2010 Openliven S.r.l
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
 *  Author: Luca Faggioli (luca.faggioli (at) openliven (dot) com)
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
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class GwtReplies extends GwtAbstractActivities implements Stream<ActivityEntry> {

	
	private final ActivityEntry activity;
	
	public GwtReplies(ActivityEntry activity) {
		this.activity = activity;
	}


	@Override
	public void refresh(final RequestCallback<List<ActivityEntry>> callback) {
		IQ iq = new IQ(IQ.Type.get);
		IPacket pubsubElement = iq.addChild("pubsub",
				"http://jabber.org/protocol/pubsub");
		IPacket itemsElement = pubsubElement.addChild("items",
				"http://jabber.org/protocol/pubsub");
		itemsElement.setAttribute("node", "http://onesocialweb.org/spec/1.0/replies");
		IPacket itemElement = itemsElement.addChild(
				"item", "http://jabber.org/protocol/pubsub");
		itemElement.setAttribute("id", activity.getId());
		iq.setTo(XmppURI.uri(activity.getActor().getUri()));
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
					helper
							.fireEvent(new ActivityEvent(Type.refreshed,
									entries));

					// Execute the callback
					if (callback != null) {
						callback.onSuccess(entries);
						return;
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

}
