package org.magicwerk.brownies.collections;

import org.magicwerk.brownies.core.CheckTools;
import org.magicwerk.brownies.core.logback.LogbackTools;
import org.magicwerk.brownies.core.reflect.ReflectTools;

import ch.qos.logback.classic.Logger;

/**
 * Test for DEBUG_CHECK enabled.
 * This program is executed as part of {@link RunDebugTest}.
 *
 * @author Thomas Mauch
 */
public class RunDebugTestCheck {

	static final Logger LOG = LogbackTools.getConsoleLogger();

	public static void main(String[] args) {
		new RunDebugTestCheck().run();
	}

	void run() {
		boolean gapListDebugMode = false;
		try {
			testGapList();
		} catch (AssertionError e) {
			gapListDebugMode = true;
		}
		LOG.info("Checking GapList for debugMode: {}", gapListDebugMode);

		boolean bigListDebugMode = false;
		try {
			testBigList();
		} catch (AssertionError e) {
			bigListDebugMode = true;
		}
		LOG.info("Checking BigList for debugMode: {}", bigListDebugMode);

		CheckTools.check(gapListDebugMode && bigListDebugMode, "Debug mode not enabled - fail");
	}

	/**
	 * This test will fail if GapList contains DEBUG_CHECK = true and assertions are enabled.
	 */
	void testGapList() {
		try {
			GapList<String> list = GapList.create();
			list.add("b");
			ReflectTools.setFieldValue(list, GapList.class.getDeclaredField("size"), 2);
			//ReflectTools.setAnyBeanValue(list, "size", 2);
			list.add("c");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This test will fail if BigList contains DEBUG_CHECK = true and assertions are enabled.
	 */
	void testBigList() {
		try {
			BigList<String> list = BigList.create();
			list.add("b");
			//ReflectTools.setAnyBeanValue(list, "rootNode", null);
			ReflectTools.setFieldValue(list, BigList.class.getDeclaredField("rootNode"), null);
			list.add("c");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
