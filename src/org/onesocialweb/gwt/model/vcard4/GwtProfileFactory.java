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
package org.onesocialweb.gwt.model.vcard4;

import org.onesocialweb.model.vcard4.BirthdayField;
import org.onesocialweb.model.vcard4.DefaultEmailField;
import org.onesocialweb.model.vcard4.DefaultFullNameField;
import org.onesocialweb.model.vcard4.DefaultGenderField;
import org.onesocialweb.model.vcard4.DefaultNameField;
import org.onesocialweb.model.vcard4.DefaultNoteField;
import org.onesocialweb.model.vcard4.DefaultPhotoField;
import org.onesocialweb.model.vcard4.DefaultProfile;
import org.onesocialweb.model.vcard4.DefaultTelField;
import org.onesocialweb.model.vcard4.DefaultTimeZoneField;
import org.onesocialweb.model.vcard4.DefaultURLField;
import org.onesocialweb.model.vcard4.EmailField;
import org.onesocialweb.model.vcard4.FullNameField;
import org.onesocialweb.model.vcard4.GenderField;
import org.onesocialweb.model.vcard4.NameField;
import org.onesocialweb.model.vcard4.NoteField;
import org.onesocialweb.model.vcard4.PhotoField;
import org.onesocialweb.model.vcard4.Profile;
import org.onesocialweb.model.vcard4.TelField;
import org.onesocialweb.model.vcard4.TimeZoneField;
import org.onesocialweb.model.vcard4.URLField;
import org.onesocialweb.model.vcard4.VCard4Factory;

public class GwtProfileFactory extends VCard4Factory {

	@Override
	public BirthdayField birthday() {
		return new GwtBirthdayField();
	}

	@Override
	public FullNameField fullname() {
		return new DefaultFullNameField();
	}

	@Override
	public GenderField gender() {
		return new DefaultGenderField();
	}

	@Override
	public NoteField note() {
		return new DefaultNoteField();
	}

	@Override
	public PhotoField photo() {
		return new DefaultPhotoField();
	}

	@Override
	public Profile profile() {
		return new DefaultProfile();
	}

	@Override
	public EmailField email() {
		return new DefaultEmailField();
	}

	@Override
	public NameField name() {
		return new DefaultNameField();
	}

	@Override
	public TelField tel() {
		return new DefaultTelField();
	}

	@Override
	public TimeZoneField timeZone() {
		return new DefaultTimeZoneField();
	}

	@Override
	public URLField url() {
		return new DefaultURLField();
	}

}
