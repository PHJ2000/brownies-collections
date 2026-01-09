package org.magicwerk.brownies.collections.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.magictest.client.Capture;
import org.magictest.client.Trace;
import org.magicwerk.brownies.collections.Key1List;
import org.magicwerk.brownies.collections.Key2List;
import org.magicwerk.brownies.collections.TestHelper.Ticket;
import org.magicwerk.brownies.collections.primitive.ByteGapList;
import org.magicwerk.brownies.collections.primitive.CharGapList;
import org.magicwerk.brownies.core.exceptions.WrapperException;
import org.magicwerk.brownies.core.print.PrintTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test of class org.magicwerk.brownies.collections.helper.GapLists.
 *
 * @author Thomas Mauch
 */
public class GapListsTest {

	static final Logger LOG = LoggerFactory.getLogger(GapListsTest.class);

	public static void main(String[] args) {
		test();
	}

	static void test() {
		testAddExplained();
		//testInputStream();
		//testReader();
	}

	@Trace
	public static void testAddExplained() {
		Ticket t1 = new Ticket(1, "extId1", "text1");
		Ticket t2 = new Ticket(1, "extId2", "text2");
		Ticket t3 = new Ticket(3, "extId1", "text2");
		{
			Key1List<Ticket, Integer> list = new Key1List.Builder<Ticket, Integer>().withPrimaryKey1Map(Ticket.IdMapper).build();
			GapLists.addExplained(list, t1);
			GapLists.addExplained(list, t2);
		}
		{
			Key1List<Ticket, Integer> list = new Key1List.Builder<Ticket, Integer>().withPrimaryKey1Map(Ticket.IdMapper).withOrderByKey1(true).build();
			GapLists.addExplained(list, t1);
			GapLists.addExplained(list, t2);
		}
		{
			Key2List<Ticket, Integer, String> list = new Key2List.Builder<Ticket, Integer, String>()
					.withPrimaryKey1Map(Ticket.IdMapper).withPrimaryKey2Map(Ticket.ExtIdMapper).build();
			GapLists.addExplained(list, t1);
			GapLists.addExplained(list, t2);
			GapLists.addExplained(list, t3);
		}
		{
			Key2List<Ticket, Integer, String> list = new Key2List.Builder<Ticket, Integer, String>()
					.withPrimaryKey1Map(Ticket.IdMapper).withPrimaryKey2Map(Ticket.ExtIdMapper).withOrderByKey1(true).build();
			GapLists.addExplained(list, t1);
			GapLists.addExplained(list, t2);
			GapLists.addExplained(list, t3);
		}
	}

	@Capture
	public static void testInputStream() {
		try {
			byte[] data = { 2, 3 };
			InputStream istream = new ByteArrayInputStream(data);
			StringReader reader = new StringReader("de");
			ByteGapList list = ByteGapList.create((byte) 0, (byte) 1);
			int index = list.size();
			int len = 20;
			int read = GapLists.read(istream, list, len);
			System.out.println(PrintTools.print(list));
		} catch (IOException e) {
			throw new WrapperException(e);
		}
	}

	@Capture
	public static void testReader() {
		try {
			StringReader reader = new StringReader("de");
			CharGapList list = CharGapList.create("ab");
			int index = list.size();
			int len = 20;
			int read = GapLists.read(reader, list, len);
			System.out.println(list);
		} catch (IOException e) {
			throw new WrapperException(e);
		}
	}

}
