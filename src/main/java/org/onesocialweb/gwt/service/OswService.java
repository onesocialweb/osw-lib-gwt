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
 *  added getReplies()
 *  
 */
package org.onesocialweb.gwt.service;

import java.util.List;

import org.onesocialweb.gwt.service.RosterItem.Presence;
import org.onesocialweb.model.acl.AclFactory;
import org.onesocialweb.model.activity.ActivityEntry;
import org.onesocialweb.model.activity.ActivityFactory;
import org.onesocialweb.model.atom.AtomFactory;
import org.onesocialweb.model.relation.Relation;
import org.onesocialweb.model.relation.RelationFactory;
import org.onesocialweb.model.vcard4.Profile;
import org.onesocialweb.model.vcard4.VCard4Factory;

public interface OswService {

	public void setup(String httpBase, String hostname, String service);

	public void login(String username, String password,
			RequestCallback<Object> callback);

	public void logout(RequestCallback<Object> callback);

	public boolean isLoggedIn();

	public Stream<ActivityEntry> getInbox();

	public void getProfile(String jid, RequestCallback<Profile> callback);

	public void setProfile(Profile profile, RequestCallback<Object> callback);

	public Roster getRoster();

	public Stream<ActivityEntry> getActivities(String jid);

	public void post(ActivityEntry entry, RequestCallback<ActivityEntry> callback);
	
	public void delete(String activityId, RequestCallback<Object> callback);
	
	public void update(ActivityEntry entry, RequestCallback<ActivityEntry> callback);

	public void subscribe(String jid, RequestCallback<Object> callback);

	public void unsubscribe(String jid, RequestCallback<Object> callback);

	public void getRelations(String jid,
			RequestCallback<List<Relation>> callback);

	public void setupRelation(Relation relation,
			RequestCallback<Object> callback);

	public void updateRelation(Relation relation,
			RequestCallback<Object> callback);

	public void getSubscriptions(String jid,
			RequestCallback<List<String>> callback);

	public void getSubscribers(String jid,
			RequestCallback<List<String>> callback);

	public String startUpload(String requestId,
			UpdateCallback<UploadStatus> callback);

	public ActivityFactory getActivityFactory();

	public AtomFactory getAtomFactory();

	public AclFactory getAclFactory();

	public VCard4Factory getProfileFactory();

	public RelationFactory getRelationFactory();

	public String getUserBareJID();

	public String getUserFullJID();

	public Presence getPresence();
	
	public Stream<ActivityEntry> getReplies(ActivityEntry activity);

}