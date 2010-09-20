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
 *  2010-08-11 Modified by Luca Faggioli Copyright 2010 Openliven S.r.l
 *  implemented getReplies()
 *
 */
package org.onesocialweb.gwt.service.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onesocialweb.gwt.model.GwtActivityDomReader;
import org.onesocialweb.gwt.model.GwtActivityDomWriter;
import org.onesocialweb.gwt.model.GwtRelationDomReader;
import org.onesocialweb.gwt.model.GwtRelationDomWriter;
import org.onesocialweb.gwt.model.GwtVCard4DomReader;
import org.onesocialweb.gwt.model.GwtVCard4DomWriter;
import org.onesocialweb.gwt.model.vcard4.GwtProfileFactory;
import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.RequestCallback;
import org.onesocialweb.gwt.service.Roster;
import org.onesocialweb.gwt.service.RosterItem;
import org.onesocialweb.gwt.service.RosterItem.Presence;
import org.onesocialweb.gwt.service.Stream;
import org.onesocialweb.gwt.service.UpdateCallback;
import org.onesocialweb.gwt.service.UploadStatus;
import org.onesocialweb.gwt.xml.DocumentAdapter;
import org.onesocialweb.gwt.xml.ElementAdapter;
import org.onesocialweb.model.acl.AclFactory;
import org.onesocialweb.model.acl.DefaultAclFactory;
import org.onesocialweb.model.activity.ActivityEntry;
import org.onesocialweb.model.activity.ActivityFactory;
import org.onesocialweb.model.activity.DefaultActivityFactory;
import org.onesocialweb.model.atom.AtomFactory;
import org.onesocialweb.model.atom.DefaultAtomFactory;
import org.onesocialweb.model.relation.DefaultRelationFactory;
import org.onesocialweb.model.relation.Relation;
import org.onesocialweb.model.relation.RelationFactory;
import org.onesocialweb.model.vcard4.Profile;
import org.onesocialweb.model.vcard4.VCard4Factory;
import org.onesocialweb.xml.dom.ActivityDomReader;
import org.onesocialweb.xml.dom.ActivityDomWriter;
import org.onesocialweb.xml.dom.RelationDomReader;
import org.onesocialweb.xml.dom.RelationDomWriter;
import org.onesocialweb.xml.dom.VCard4DomReader;
import org.onesocialweb.xml.dom.VCard4DomWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.bosh.BoshSettings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.packet.gwt.GWTPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.presence.PresenceManagerImpl;
import com.calclab.emite.im.client.roster.RosterImpl;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.xml.client.XMLParser;

public class GwtOswService implements OswService {

	private static GwtOswService instance;

	private final Session session = Suco.get(Session.class);

	private final ActivityFactory activityFactory = new DefaultActivityFactory();

	private final AclFactory aclFactory = new DefaultAclFactory();

	private final AtomFactory atomFactory = new DefaultAtomFactory();

	private final VCard4Factory profileFactory = new GwtProfileFactory();

	private final RelationFactory relationFactory = new DefaultRelationFactory();

	private final Map<String, Object> cache = new HashMap<String, Object>();

	private final GwtInbox inbox = new GwtInbox();

	private final QueryCache queryCache = new QueryCache();

	private RequestCallback<?> loginCallback;

	private RequestCallback<?> logoutCallback;

	private Map<String, UpdateCallback<UploadStatus>> uploadCallbacks = new HashMap<String, UpdateCallback<UploadStatus>>();

	private String domain;

	private Roster roster;

	private PresenceManager presenceManager;

	public static GwtOswService getInstance() {
		if (instance == null) {
			instance = new GwtOswService();
		}
		return instance;
	}

	@Override
	public AclFactory getAclFactory() {
		return aclFactory;
	}

	@Override
	public ActivityFactory getActivityFactory() {
		return activityFactory;
	}

	@Override
	public AtomFactory getAtomFactory() {
		return atomFactory;
	}

	@Override
	public VCard4Factory getProfileFactory() {
		return profileFactory;
	}

	@Override
	public RelationFactory getRelationFactory() {
		return relationFactory;
	}

	@Override
	public void setup(String httpBase, String hostname, String domain) {
		Suco.get(Connection.class).setSettings(
				new BoshSettings(httpBase, hostname));
		session.onStateChanged(new StateChangedListener());
		session.onMessage(new MessageListener());
		this.domain = domain;
	}

	@Override
	public void login(String username, String password,
			RequestCallback<Object> callback) {
		if (logoutCallback == null && loginCallback == null) {
			loginCallback = callback;
			RosterImpl rosterImpl = new RosterImpl(session);
			roster = new GwtRoster(rosterImpl);
			presenceManager = new PresenceManagerImpl(session, rosterImpl);
			session.login(XmppURI.uri(username + "@" + domain), password);
		}
	}

	@Override
	public void logout(RequestCallback<Object> callback) {
		if (logoutCallback == null && loginCallback == null) {
			logoutCallback = callback;
			session.logout();
		}
	}

	@Override
	public boolean isLoggedIn() {
		return session.isLoggedIn();
	}

	@Override
	public Stream<ActivityEntry> getInbox() {
		return inbox;
	}

	@Override
	public void getProfile(final String jid,
			final RequestCallback<Profile> callback) {
		// Hit the cache
		if (cache.containsKey("profile_" + jid)) {
			Object value = cache.get("profile_" + jid);
			if (value != null && value instanceof Profile) {
				callback.onSuccess((Profile) value);
			} else {
				callback.onFailure();
			}
			return;
		}

		// Check if ongoing query
		if (queryCache.hasQuery("profile_" + jid)) {
			queryCache.addCallback("profile_" + jid, new QueryCallback() {

				@Override
				public void onFailure() {
					callback.onFailure();
				}

				@Override
				public void onResult(Object result) {
					if (result != null && result instanceof Profile) {
						callback.onSuccess((Profile) result);
					} else {
						callback.onFailure();
					}
				}
			});
			return;
		}

		// No cache, new query
		queryCache.addQuery("profile_" + jid);
		IQ iq = new IQ(IQ.Type.get);
		iq.addQuery("http://onesocialweb.org/spec/1.0/vcard4#query");
		iq.setTo(XmppURI.jid(jid));
		session.sendIQ("osw", iq, new Listener<IPacket>() {

			public void onEvent(IPacket packet) {
				// Packet should not be an error
				if (IQ.isSuccess(packet)) {
					// Parse the packet and check if a proper query request
					final IPacket query = packet.getFirstChild("query");
					Profile profile = null;
					if (query != null && query.getChildrenCount() > 0) {
						// Parse the packet and return the profile
						IPacket item = query.getFirstChild("vcard");
						if (item instanceof GWTPacket) {
							GWTPacket i_packet = (GWTPacket) item;
							Element element = new ElementAdapter(i_packet
									.getElement());
							VCard4DomReader reader = new GwtVCard4DomReader();
							profile = reader.readProfile(element);
						}
					}

					if (profile == null) {
						profile = profileFactory.profile();
					}

					cache.put("profile_" + jid, profile);
					queryCache.success("profile_" + jid, profile);
					callback.onSuccess(profile);
					return;
				}

				// If we are here, there was an error
				cache.put("profile_" + jid, null);
				queryCache.failure("profile_" + jid);
				callback.onFailure();
			}
		});
	}

	@Override
	public void setProfile(final Profile profile,
			final RequestCallback<Object> callback) {
		VCard4DomWriter writer = new GwtVCard4DomWriter();
		Document document = new DocumentAdapter(XMLParser.createDocument());
		Element element = writer.toElement(profile, document);
		IQ iq = new IQ(IQ.Type.set);
		IPacket publish = iq.addChild("publish",
				"http://onesocialweb.org/spec/1.0/vcard4#publish");
		((Packet) publish).addChild(new GWTPacket(((ElementAdapter) element)
				.getGwtElement()));
		session.sendIQ("osw", iq, new Listener<IPacket>() {

			public void onEvent(IPacket packet) {
				// Check if no error
				if (IQ.isSuccess(packet)) {
					callback.onSuccess(null);
					cache.put("profile_" + getUserBareJID(), profile);
				} else {
					callback.onFailure();
				}
			}
		});
	}

	@Override
	public Stream<ActivityEntry> getActivities(String jid) {
		// Hit the cache
		if (cache.containsKey("activities_" + jid)) {
			Stream<ActivityEntry> activities = (GwtActivities) cache.get("activities_" + jid);
			activities.refresh(null);
			return activities;
		}

		// Create, cache, refresh and return
		GwtActivities activities = new GwtActivities(jid);
		activities.refresh(null);
		cache.put("activities_" + jid, activities);
		return activities;
	}


	@Override
	public Stream<ActivityEntry> getReplies(ActivityEntry parentActivity) {

		GwtReplies activities = new GwtReplies(parentActivity.getId(), parentActivity.getActor().getUri());		
		activities.refresh(null);
		return activities;
	}
	

	@Override
	public void getRelations(final String jid,
			final RequestCallback<List<Relation>> callback) {
		IQ iq = new IQ(IQ.Type.get);
		iq.addQuery("http://onesocialweb.org/protocol/0.1/relations#query");
		iq.setTo(XmppURI.jid(jid));
		session.sendIQ("osw", iq, new Listener<IPacket>() {

			public void onEvent(IPacket packet) {
				// Check if not an error
				if (IQ.isSuccess(packet)) {
					// Parse the packet and check if a proper query request
					final IPacket query = packet.getFirstChild("query");
					final RelationDomReader reader = new GwtRelationDomReader();
					if (query != null && query.getChildrenCount() > 0) {
						// Parse the packet and store the notifications
						List<Relation> relations = new ArrayList<Relation>();
						for (IPacket item : query.getChildren()) {
							if (item instanceof GWTPacket) {
								GWTPacket i_packet = (GWTPacket) item;
								Element element = new ElementAdapter(i_packet
										.getElement());
								Relation relation = reader.readElement(element);
								relations.add(relation);
							}
						}

						// Execute the callback
						if (callback != null) {
							callback.onSuccess(relations);
							return;
						}
					}
				}

				// If we are here, there was an error
				if (callback != null) {
					callback.onFailure();
				}
			}
		});
	}

	@Override
	public void setupRelation(final Relation relation,
			final RequestCallback<Object> callback) {
		RelationDomWriter writer = new GwtRelationDomWriter();
		Document document = new DocumentAdapter(XMLParser.createDocument());
		Element element = writer.toElement(relation, document);
		IQ iq = new IQ(IQ.Type.set);
		IPacket publish = iq.addChild("setup",
				"http://onesocialweb.org/protocol/0.1/relations#setup");
		((Packet) publish).addChild(new GWTPacket(((ElementAdapter) element)
				.getGwtElement()));
		session.sendIQ("osw", iq, new Listener<IPacket>() {

			public void onEvent(IPacket packet) {
				// Check if no error
				if (IQ.isSuccess(packet)) {
					callback.onSuccess(null);
				} else {
					callback.onFailure();
				}
			}
		});
	}

	@Override
	public void updateRelation(final Relation relation,
			final RequestCallback<Object> callback) {
		RelationDomWriter writer = new GwtRelationDomWriter();
		Document document = new DocumentAdapter(XMLParser.createDocument());
		Element element = writer.toElement(relation, document);
		IQ iq = new IQ(IQ.Type.set);
		IPacket publish = iq.addChild("update",
				"http://onesocialweb.org/protocol/0.1/relations#update");
		((Packet) publish).addChild(new GWTPacket(((ElementAdapter) element)
				.getGwtElement()));
		session.sendIQ("osw", iq, new Listener<IPacket>() {

			public void onEvent(IPacket packet) {
				// Check if no error
				if (IQ.isSuccess(packet)) {
					callback.onSuccess(null);
				} else {
					callback.onFailure();
				}
			}
		});
	}

	@Override
	public Roster getRoster() {
		return roster;
	}

	@Override
	public void post(final ActivityEntry activity,
			final RequestCallback<ActivityEntry> callback) {
		ActivityDomWriter writer = new GwtActivityDomWriter();
		Document document = new DocumentAdapter(XMLParser.createDocument());
		Element element = writer.toElement(activity, document);
		IQ iq = new IQ(IQ.Type.set);
		IPacket pubsubElement = iq.addChild("pubsub",
				"http://jabber.org/protocol/pubsub");
		IPacket publishElement = pubsubElement.addChild("publish",
				"http://jabber.org/protocol/pubsub");
		publishElement.setAttribute("node", "urn:xmpp:microblog:0");
		IPacket itemElement = publishElement.addChild("item",
				"http://jabber.org/protocol/pubsub");
		((Packet) itemElement).addChild(new GWTPacket(
				((ElementAdapter) element).getGwtElement()));
		session.sendIQ("osw", iq, new Listener<IPacket>() {

			public void onEvent(IPacket packet) {
				// Check if no error
				if (IQ.isSuccess(packet)) {
					callback.onSuccess(null);
				} else {
					callback.onFailure();
				}
			}
		});
	}
	
	@Override
	public void delete(String activityId, final RequestCallback<Object> callback){
				
		IQ iq = new IQ(IQ.Type.set);
		
		IPacket pubsubElement = iq.addChild("pubsub","http://jabber.org/protocol/pubsub");
		IPacket retractElement = pubsubElement.addChild("retract", "http://jabber.org/protocol/pubsub");
		retractElement.setAttribute("node", "urn:xmpp:microblog:0");
		IPacket itemElement = retractElement.addChild("item", "http://jabber.org/protocol/pubsub");
		itemElement.setAttribute("id", activityId);
		
		
		session.sendIQ("osw", iq, new Listener<IPacket>() {

			public void onEvent(IPacket packet) {
				// Check if no error
				if (IQ.isSuccess(packet)) {
					callback.onSuccess(null);
				} else {
					callback.onFailure();
				}
			}
		});
	}
	
	@Override
	public void update(ActivityEntry entry, final RequestCallback<ActivityEntry> callback) {
		
		ActivityDomWriter writer = new GwtActivityDomWriter();
		Document document = new DocumentAdapter(XMLParser.createDocument());
		Element element = writer.toElement(entry, document);
		IQ iq = new IQ(IQ.Type.set);
		IPacket pubsubElement = iq.addChild("pubsub",
				"http://jabber.org/protocol/pubsub");
		IPacket publishElement = pubsubElement.addChild("publish",
				"http://jabber.org/protocol/pubsub");
		publishElement.setAttribute("node", "urn:xmpp:microblog:0");
		IPacket itemElement = publishElement.addChild("item",
				"http://jabber.org/protocol/pubsub");
		((Packet) itemElement).addChild(new GWTPacket(
				((ElementAdapter) element).getGwtElement()));
		
		session.sendIQ("osw", iq, new Listener<IPacket>() {

			public void onEvent(IPacket packet) {
				// Check if no error
				if (IQ.isSuccess(packet)) {
					callback.onSuccess(null);
				} else {
					callback.onFailure();
				}
			}
		});
	}

	@Override
	public void subscribe(final String jid,
			final RequestCallback<Object> callback) {
		// If the person is not in the roster, add her
		RosterItem rosterItem = roster.getItem(jid);
		if (rosterItem == null) {
			final List<String> groups = new ArrayList<String>(1);
			roster.addItem(jid, jid, groups, null);
		}

		// Process the subscription request
		IQ iq = new IQ(IQ.Type.set);
		iq.setTo(XmppURI.jid(jid));
		IPacket pubsubPacket = iq.addChild("pubsub",
				"http://jabber.org/protocol/pubsub");
		IPacket subscribePacket = pubsubPacket.addChild("subscribe",
				"http://jabber.org/protocol/pubsub");
		subscribePacket.setAttribute("node", "urn:xmpp:microblog:0");
		subscribePacket.setAttribute("jid", getUserBareJID());
		session.sendIQ("osw", iq, new Listener<IPacket>() {
			@SuppressWarnings("unchecked")
			public void onEvent(IPacket packet) {
				// Check if no error
				if (IQ.isSuccess(packet)) {
					// This call was successfull
					callback.onSuccess(null);
					// Update the subscription cache
					if (cache.containsKey("subscriptions_" + getUserBareJID())) {
						List<String> subscriptions = (List<String>) cache
								.get("subscriptions_" + getUserBareJID());
						if (!subscriptions.contains(jid)) {
							subscriptions.add(jid);
						}
					}
					// Update the subscribers cache
					if (cache.containsKey("subscribers_" + jid)) {
						List<String> subscribers = (List<String>) cache
								.get("subscribers_" + jid);
						if (!subscribers.contains(getUserBareJID())) {
							subscribers.add(getUserBareJID());
						}
					}

				} else {
					callback.onFailure();
				}
			}
		});
	}

	@Override
	public void unsubscribe(final String jid,
			final RequestCallback<Object> callback) {
		// Effectively unsubscribe from the node
		IQ iq = new IQ(IQ.Type.set);
		iq.setTo(XmppURI.jid(jid));
		IPacket pubsubPacket = iq.addChild("pubsub",
				"http://jabber.org/protocol/pubsub");
		IPacket unsubscribePacket = pubsubPacket.addChild("unsubscribe",
				"http://jabber.org/protocol/pubsub");
		unsubscribePacket.setAttribute("node", "urn:xmpp:microblog:0");
		unsubscribePacket.setAttribute("jid", getUserBareJID());
		session.sendIQ("osw", iq, new Listener<IPacket>() {
			@SuppressWarnings("unchecked")
			public void onEvent(IPacket packet) {
				// Check if no error
				if (IQ.isSuccess(packet)) {
					callback.onSuccess(null);
					// Update the subscription cache
					if (cache.containsKey("subscriptions_" + getUserBareJID())) {
						List<String> subscriptions = (List<String>) cache
								.get("subscriptions_" + getUserBareJID());
						if (subscriptions.contains(jid)) {
							subscriptions.remove(jid);
						}
					}
					// Update the subscribers cache
					if (cache.containsKey("subscribers_" + jid)) {
						List<String> subscribers = (List<String>) cache
								.get("subscribers_" + jid);
						if (subscribers.contains(getUserBareJID())) {
							subscribers.remove(getUserBareJID());
						}
					}
				} else {
					callback.onFailure();
				}
			}
		});
	}

	@Override
	public String getUserBareJID() {
		if (session.isLoggedIn()) {
			return session.getCurrentUser().getNode() + "@"
					+ session.getCurrentUser().getHost();
		}

		return null;
	}

	@Override
	public String getUserFullJID() {
		if (session.isLoggedIn()) {
			return session.getCurrentUser().toString();
		}

		return null;
	}

	@Override
	public Presence getPresence() {
		if (presenceManager.getOwnPresence().getType() == null) {
			final Show show = presenceManager.getOwnPresence().getShow();
			if (show.equals(Show.away)) {
				return Presence.away;
			} else if (show.equals(Show.chat)) {
				return Presence.chat;
			} else if (show.equals(Show.dnd)) {
				return Presence.dnd;
			} else if (show.equals(Show.xa)) {
				return Presence.xa;
			} else {
				return Presence.available;
			}
		} else {
			return Presence.unknown;
		}
	}

	@Override
	public String startUpload(String requestId,
			UpdateCallback<UploadStatus> callback) {
		if (callback != null) {
			uploadCallbacks.put(requestId, callback);
		}
		return session.getSessionId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getSubscriptions(final String jid,
			final RequestCallback<List<String>> callback) {

		// If no callback, not point to bother doing this call
		if (callback == null) {
			return;
		}

		// Hit the cache
		if (cache.containsKey("subscriptions_" + jid)) {
			Object value = cache.get("subscriptions_" + jid);
			if (value != null) {
				callback.onSuccess((List<String>) value);
			} else {
				callback.onFailure();
			}
			return;
		}

		// Check if ongoing query
		if (queryCache.hasQuery("subscriptions_" + jid)) {
			queryCache.addCallback("subscriptions_" + jid, new QueryCallback() {

				@Override
				public void onFailure() {
					callback.onFailure();
				}

				@Override
				public void onResult(Object result) {
					if (result != null && result instanceof List) {
						callback.onSuccess((List<String>) result);
					} else {
						callback.onFailure();
					}
				}
			});
			return;
		}

		// Actually.. someone cares
		queryCache.addQuery("subscriptions_" + jid);
		IQ iq = new IQ(IQ.Type.get);
		if (!getUserBareJID().equals(jid)) {
			iq.setTo(XmppURI.jid(jid));
		}
		IPacket pubsubElement = iq.addChild("pubsub",
				"http://jabber.org/protocol/pubsub");
		IPacket itemsElement = pubsubElement.addChild("subscriptions",
				"http://jabber.org/protocol/pubsub");
		itemsElement.setAttribute("node", "urn:xmpp:microblog:0");
		session.sendIQ("osw", iq, new Listener<IPacket>() {
			public void onEvent(IPacket packet) {
				// Check if not an error
				if (IQ.isSuccess(packet)) {
					// Parse the packet and check if a proper query result
					IPacket pubsubResult = packet.getFirstChild("pubsub");
					IPacket subsResult = pubsubResult
							.getFirstChild("subscriptions");
					final List<String> subscriptions = new ArrayList<String>();
					if (subsResult != null && subsResult.getChildrenCount() > 0) {
						// Parse the packet and store the notifications
						for (IPacket item : subsResult.getChildren()) {
							String subscription = item.getAttribute("jid");
							if (subscription != null
									&& subscription.length() > 0) {
								subscriptions.add(subscription);
							}
						}
					}

					cache.put("subscriptions_" + jid, subscriptions);
					queryCache.success("subscriptions_" + jid, subscriptions);
					callback.onSuccess(subscriptions);
					return;
				}

				// If we are here, there was an error
				cache.put("subscriptions_" + jid, null);
				queryCache.failure("subscriptions_" + jid);
				callback.onFailure();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getSubscribers(final String jid,
			final RequestCallback<List<String>> callback) {

		// If no callback, not point to bother doing this call
		if (callback == null) {
			return;
		}

		// Hit the cache
		if (cache.containsKey("subscribers_" + jid)) {
			Object value = cache.get("subscribers_" + jid);
			if (value != null) {
				callback.onSuccess((List<String>) value);
			} else {
				callback.onFailure();
			}
			return;
		}

		// Check if ongoing query
		if (queryCache.hasQuery("subscribers_" + jid)) {
			queryCache.addCallback("subscribers_" + jid, new QueryCallback() {

				@Override
				public void onFailure() {
					callback.onFailure();
				}

				@Override
				public void onResult(Object result) {
					if (result != null && result instanceof List) {
						callback.onSuccess((List<String>) result);
					} else {
						callback.onFailure();
					}
				}
			});
			return;
		}

		// Actually.. someone cares
		queryCache.addQuery("subscribers_" + jid);
		IQ iq = new IQ(IQ.Type.get);
		if (!getUserBareJID().equals(jid)) {
			iq.setTo(XmppURI.jid(jid));
		}
		IPacket pubsubElement = iq.addChild("pubsub",
				"http://jabber.org/protocol/pubsub");
		IPacket itemsElement = pubsubElement.addChild("subscribers",
				"http://jabber.org/protocol/pubsub");
		itemsElement.setAttribute("node", "urn:xmpp:microblog:0");
		session.sendIQ("osw", iq, new Listener<IPacket>() {
			public void onEvent(IPacket packet) {
				// Check if not an error
				if (IQ.isSuccess(packet)) {
					// Parse the packet and check if a proper query result
					IPacket pubsubResult = packet.getFirstChild("pubsub");
					IPacket subsResult = pubsubResult
							.getFirstChild("subscribers");
					final List<String> subscribers = new ArrayList<String>();
					if (subsResult != null && subsResult.getChildrenCount() > 0) {
						// Parse the packet and store the notifications
						for (IPacket item : subsResult.getChildren()) {
							String subscriber = item.getAttribute("jid");
							if (subscriber != null && subscriber.length() > 0) {
								subscribers.add(subscriber);
							}
						}
					}

					cache.put("subscribers_" + jid, subscribers);
					queryCache.success("subscribers_" + jid, subscribers);
					callback.onSuccess(subscribers);
					return;
				}

				// If we are here, there was an error
				cache.put("subscribers_" + jid, null);
				queryCache.failure("subscribers_" + jid);
				callback.onFailure();
			}
		});
	}

	protected void onAuthorized() {
		inbox.refresh(null);
		if (loginCallback != null) {
			loginCallback.onSuccess(null);
			loginCallback = null;
		}
	}

	protected void onNotAuthorized() {
		if (loginCallback != null) {
			loginCallback.onFailure();
			loginCallback = null;
		}
		clearSession();
	}

	protected void onDisconnected() {
		if (logoutCallback != null) {
			logoutCallback.onSuccess(null);
			logoutCallback = null;
		}
		clearSession();
	}

	private void clearSession() {
		inbox.getItems().clear();
		cache.clear();
		queryCache.clear();
	}

	private class StateChangedListener implements Listener<Session.State> {

		public void onEvent(final State state) {
			Log.debug("State changed to " + state.toString());
			if (state.equals(State.authorized)) {
				onAuthorized();
			} else if (state.equals(State.notAuthorized)) {
				onNotAuthorized();
			} else if (state.equals(State.disconnected)) {
				onDisconnected();
			}
		}

	}

	private class MessageListener implements Listener<Message> {

		public void onEvent(final Message message) {
			// Check if a notification
			IPacket item = message.getFirstChild("event");
			if (item != null && item.getChildrenCount() > 0) {
				onNotificationEvent(item);
				return;
			}

			// Check if an upload event
			item = message.getFirstChild("upload");
			if (item != null && item.getChildrenCount() > 0) {
				onUploadEvent(item);
				return;
			}
		}

		private void onNotificationEvent(final IPacket event) {
			if (event instanceof GWTPacket) {
				IPacket itemsPacket = event.getFirstChild("items");
				if (itemsPacket != null
						&& itemsPacket.hasAttribute("node",
								"urn:xmpp:microblog:0")
						&& itemsPacket.getChildrenCount() > 0) {

					// Parse the packet and store the notifications
					for (IPacket item : itemsPacket.getChildren()) {
						if (item instanceof GWTPacket) {
							if (item.getName().equalsIgnoreCase("item")){
								GWTPacket i_packet = (GWTPacket) item.getFirstChild("entry");
								if (i_packet == null)
									continue;

								Element element = new ElementAdapter(i_packet.getElement());
								ActivityDomReader reader = new GwtActivityDomReader();
								ActivityEntry activity = reader.readEntry(element);
								// if the activity is already in the inbox, then it was an update...
								ActivityEntry existingActivity=inbox.getItem(activity.getId());
								if (existingActivity!=null) {								
									inbox.updateItem(activity);
									Log.debug("Updated the activity : "	+ activity.getId());
								}
								else {	
									if ((activity.getParentId()!=null) && (activity.getParentId().length()!=0)){
										// TO-DO
										Log.debug("Received a new comment : "	+ activity.getId());																		
										inbox.addCommentToItem(activity);									
									}
									else { 
										inbox.addItem(activity);
										Log.debug("Received a new activity message: "	+ activity.getId());
									}
								}
							}
							else if (item.getName().equalsIgnoreCase("retract")){
								
								String activityId = item.getAttribute("id");
								if (activityId == null)
									continue;
								
								// remove item from inbox implementation ...
								ActivityEntry entry =inbox.getItem(activityId);
								inbox.deleteItem(entry);								
								
								Log.debug("Received a new retract message: " + activityId);
							}
						}
					}
				}
			}
		}

		private void onUploadEvent(final IPacket item) {
			// Parse the upload message into an UploadStatus object
			final GwtUploadStatus uploadStatus = new GwtUploadStatus();
			if (item.hasChild("request-id")) {
				uploadStatus.setRequestId(item.getFirstChild("request-id")
						.getText());
			}
			if (item.hasChild("status")) {
				uploadStatus.setStatus(item.getFirstChild("status").getText());
			}
			if (item.hasChild("file-id")) {
				uploadStatus.setFileId(item.getFirstChild("file-id").getText());
			}
			if (item.hasChild("size")) {
				try {
					uploadStatus.setSize(Long.parseLong(item.getFirstChild(
							"size").getText()));
				} catch (NumberFormatException e) {
				}
			}
			if (item.hasChild("bytes-read")) {
				try {
					uploadStatus.setBytesRead(Long.parseLong(item
							.getFirstChild("bytes-read").getText()));
				} catch (NumberFormatException e) {
				}
			}
			if (item.hasChild("mime-type")) {
				uploadStatus.setMimeType(item.getFirstChild("mime-type")
						.getText());
			}

			// If no requestId.. then we missed something
			if (!uploadStatus.hasRequestId()) {
				return;
			}

			// Fire the upload callback attached to the request id
			UpdateCallback<UploadStatus> callback = uploadCallbacks
					.get(uploadStatus.getRequestId());
			if (callback != null) {
				callback.onUpdate(uploadStatus);
			}

			// If error or completed, remove the callback
			if (uploadStatus.hasStatus()) {
				if (uploadStatus.getStatus().equals(
						UploadStatus.Status.COMPLETED)
						|| uploadStatus.getStatus().equals(
								UploadStatus.Status.ERROR)) {
					uploadCallbacks.remove(uploadStatus.getRequestId());
				}
			}
		}
	}

	// Private constructor to enforce the singleton
	private GwtOswService() {
		//
	}
}
