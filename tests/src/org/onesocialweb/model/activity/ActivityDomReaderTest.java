package org.onesocialweb.model.activity;

import org.onesocialweb.gwt.model.GwtActivityDomReader;
import org.onesocialweb.gwt.xml.DocumentAdapter;
import org.onesocialweb.xml.dom.ActivityDomReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.xml.client.XMLParser;

public class ActivityDomReaderTest extends GWTTestCase {

	public void testLoadXml() {
		ActivityDomReader activityDomReader = new GwtActivityDomReader();
		Document document = new DocumentAdapter(XMLParser.parse(getContent()));
		ActivityEntry entry = activityDomReader.readEntry((Element) document
				.getFirstChild());

		assertNotNull(entry);
		System.out.println(entry);
	}

	private String getContent() {
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:activity=\"http://activitystrea.ms/spec/1.0/\">"
				+ "<title>Snapshot Smith uploaded a photo.</title>"
				+ "<id>http://www.facebook.com/album.php?aid=6&amp;id=499225643&amp;ref=at</id>"
				+ "<link href=\"http://www.facebook.com/album.php?aid=6&amp;id=499225643&amp;ref=at\" />"
				+ "<published>2009-04-06T21:23:00-07:00</published>"
				+ "<updated>2009-04-06T21:23:00-07:00</updated>"
				+ "<author>"
				+ "	<name>Snapshot Smith</name>"
				+ "	<uri>http://www.facebook.com/people/Snapshot-Smith/499225643</uri>"
				+ "</author>"
				+ "<category term=\"Upload Photos\" label=\"Upload Photos\" />"
				+ "<activity:verb> http://activitystrea.ms/schema/1.0/post/ </activity:verb>"
				+ "<activity:object>"
				+ "	<id>http://www.facebook.com/photo.php?pid=28&amp;id=499225643&amp;ref=at</id>"
				+ "	<thumbnail>http://photos-e.ak.fbcdn.net/photos-ak-snc1/v2692/195/117/499225643/s499225643_28_6861716.jpg</thumbnail>"
				+ "	<caption>A very attractive wall, indeed.</caption>"
				+ "	<published>2009-04-06T21:23:00-07:00</published>"
				+ "	<link rel=\"alternate\" type=\"text/html\" href=\"http://www.facebook.com/photo.php?pid=28&amp;id=499225643&amp;ref=at\" />"
				+ "	<activity:object-type>"
				+ "		http://activitystrea.ms/schema/1.0/photo/"
				+ "	</activity:object-type>"
				+ "</activity:object>"
				+ "<acl-rule xmlns=\"http://onesocialweb.org/spec/1.0/\">"
				+ "	<acl-action permission=\"http://onesocialweb.org/spec/1.0/acl/permission/grant\" >http://onesocialweb.org/spec/1.0/acl/action/view</acl-action>"
				+ "	<acl-subject type=\"http://onesocialweb.org/spec/1.0/acl/subject/everyone\" />"
				+ "</acl-rule>" + "</entry>";

		return content;
	}

	@Override
	public String getModuleName() {
		return "org.onesocialweb.Library";
	}

}
