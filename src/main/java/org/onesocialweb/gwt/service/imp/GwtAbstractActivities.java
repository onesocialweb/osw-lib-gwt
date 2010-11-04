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

public abstract class GwtAbstractActivities implements Stream<ActivityEntry> {

	protected final ObservableHelper<StreamEvent<ActivityEntry>> helper = new ObservableHelper<StreamEvent<ActivityEntry>>();

	protected final Session session = Suco.get(Session.class);


	protected final List<ActivityEntry> entries = new ArrayList<ActivityEntry>();

	protected boolean isReady = false;


	@Override
	public List<ActivityEntry> getItems() {
		return entries;
	}


	public abstract void refresh(final RequestCallback<List<ActivityEntry>> callback);

	@Override
	public void registerEventHandler(Observer<StreamEvent<ActivityEntry>> handler) {
		helper.registerEventHandler(handler);
	}

	@Override
	public void unregisterEventHandler(Observer<StreamEvent<ActivityEntry>> handler) {
		helper.unregisterEventHandler(handler);
	}

	@Override
	public boolean isReady() {
		return isReady;
	}

	public class ActivityEvent extends StreamEvent<ActivityEntry> {

		public ActivityEvent(Type type, List<ActivityEntry> items) {
			super(type, items);
		}

	}

}
