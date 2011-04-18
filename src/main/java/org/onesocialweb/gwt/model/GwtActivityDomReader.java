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

import org.onesocialweb.model.activity.ActivityFactory;
import org.onesocialweb.model.activity.DefaultActivityFactory;
import org.onesocialweb.model.atom.AtomFactory;
import org.onesocialweb.model.atom.DefaultAtomFactory;
import org.onesocialweb.xml.dom.AclDomReader;
import org.onesocialweb.xml.dom.ActivityDomReader;
import org.onesocialweb.xml.dom.AtomDomReader;

public class GwtActivityDomReader extends ActivityDomReader {

	@Override
	protected AclDomReader getAclDomReader() {
		return new GwtAclDomReader();
	}

	@Override
	protected ActivityFactory getActivityFactory() {
		return new DefaultActivityFactory();
	}
	
	@Override
	protected AtomFactory getAtomFactory() {
		return new DefaultAtomFactory();
	}

	@Override
	protected AtomDomReader getAtomDomReader() {
		return new GwtAtomDomReader();
	}

	@Override
	protected Date parseDate(String atomDate) {
		return AtomHelper.parseDate(atomDate);
	}

}
