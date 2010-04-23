package org.onesocialweb.gwt.service.imp;

import org.onesocialweb.gwt.service.OswService;
import org.onesocialweb.gwt.service.OswServiceFactory;
import org.onesocialweb.gwt.service.RequestCallback;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtOswServiceTest extends GWTTestCase {

	public void testLogin() {
		OswService service = OswServiceFactory.getService();
		service.setup("/OswClient/proxy", "localhost", "xmpp.loc");
		service.login("alice", "password", new RequestCallback<Object>() {

			@Override
			public void onFailure() {
				fail("Fail callback has been triggered");
			}

			@Override
			public void onSuccess(Object result) {
				finishTest();
			}

		});

		delayTestFinish(1000);

	}

	@Override
	public String getModuleName() {
		return "org.onesocialweb.OswCore";
	}
}
