package org.onesocialweb.model.activity;

import java.util.Date;

import org.onesocialweb.gwt.model.GwtActivityDomWriter;
import org.onesocialweb.gwt.xml.DocumentAdapter;
import org.onesocialweb.model.acl.AclAction;
import org.onesocialweb.model.acl.AclFactory;
import org.onesocialweb.model.acl.AclRule;
import org.onesocialweb.model.acl.AclSubject;
import org.onesocialweb.model.acl.DefaultAclFactory;
import org.onesocialweb.model.atom.AtomCategory;
import org.onesocialweb.model.atom.AtomContent;
import org.onesocialweb.model.atom.AtomFactory;
import org.onesocialweb.model.atom.AtomLink;
import org.onesocialweb.model.atom.DefaultAtomFactory;
import org.onesocialweb.xml.dom.ActivityDomWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.xml.client.XMLParser;

public class ActivityDomWriterTest extends GWTTestCase {

	public void testEntryToXML() {
		AtomFactory atomFactory = new DefaultAtomFactory();
		ActivityFactory activityFactory = new DefaultActivityFactory();
		AclFactory aclFactory = new DefaultAclFactory();

		ActivityEntry entry = activityFactory.entry();
		entry.setPublished(new Date());
		entry.setTitle("My first status update");

		ActivityActor author = activityFactory.actor();
		author.setName("Laurent Eschenauer");
		author.setUri("eschnou@onesocial.me");
		entry.setActor(author);

		ActivityObject object = activityFactory.object();
		object.setTitle("My first status update");
		object.setType(ActivityObject.STATUS_UPDATE);
		entry.addObject(object);

		AtomContent content = atomFactory.content();
		content.setType("text/html");
		content.setValue("My first status update (in Html this time :-)");
		object.addContent(content);

		AtomCategory category = atomFactory.category();
		category.setLabel("Testcase");
		category.setTerm("/test");
		object.addCategory(category);

		AtomLink link = atomFactory.link();
		link.setHref("http://eschnou.com");
		link.setRel("me");
		link.setTitle("My blog");
		object.addLink(link);

		AclRule rule = aclFactory.aclRule();
		rule.addAction(aclFactory.aclAction(AclAction.ACTION_VIEW,
				AclAction.PERMISSION_GRANT));
		rule.addSubject(aclFactory.aclSubject(null, AclSubject.EVERYONE));
		entry.addAclRule(rule);

		Document document = new DocumentAdapter(XMLParser.createDocument());
		ActivityDomWriter activityDomWriter = new GwtActivityDomWriter();
		Element element = activityDomWriter.toElement(entry, document);
		assertNotNull(element);

		System.out.println(document.toString());
	}

	@Override
	public String getModuleName() {
		return "org.onesocialweb.Library";
	}

}
