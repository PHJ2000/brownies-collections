/*
 * Copyright 2013 by Thomas Mauch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */
package org.magicwerk.brownies.collections;

import java.util.List;

import org.magictest.client.Trace;
import org.magicwerk.brownies.core.logback.LogbackTools;
import org.slf4j.Logger;

/**
 * Test of class {@link ICollectionTools}.
 *
 * @author Thomas Mauch
 */
public class ICollectionToolsTest {

	static final Logger LOG = LogbackTools.getConsoleLogger();

	public static void main(String[] args) {
		new ICollectionToolsTest().test();
	}

	void test() {
		testGetIf();
	}

	@Trace
	public void testGetIf() {
		List<Integer> list = GapListTest.getSortedGapList(5);
		ICollectionTools.getIf(list, i -> i == 4);
	}
}
