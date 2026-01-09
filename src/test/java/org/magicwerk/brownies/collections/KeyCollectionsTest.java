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

import org.magictest.client.Capture;
import org.magicwerk.brownies.collections.KeyCollections.KeyNCollection;
import org.magicwerk.brownies.collections.KeyCollections.KeyNList;
import org.magicwerk.brownies.collections.KeyCollections.KeyNSet;
import org.magicwerk.brownies.core.logback.LogbackTools;
import org.magicwerk.brownies.core.objects.Triad;
import org.slf4j.Logger;

/**
 * Test class of KeyList.
 *
 * @author Thomas Mauch
 */
public class KeyCollectionsTest {

	static final Logger LOG = LogbackTools.getConsoleLogger();

	static final Triad<Integer> t0 = Triad.of(0, 1, 2);
	static final Triad<Integer> t1 = Triad.of(1, 2, 3);
	static final Triad<Integer> t2 = Triad.of(2, 3, 4);

	public static void main(String[] args) {
		new KeyCollectionsTest().run();
	}

	void run() {
		testKeyList3();
		testKeyCollection3();
		testKeySet3();
	}

	// KeyList3

	static class TriadList extends KeyNList<Triad<Integer>> {
		public TriadList() {
			getBuilder()
					.withKeyMap(1, t -> ((Triad) t).getItem0())
					.withKeyMap(2, t -> ((Triad) t).getItem1())
					.withKeyMap(3, t -> ((Triad) t).getItem2())
					.build();
		}
	}

	@Capture
	public void testKeyList3() {
		{
			KeyNList<Triad<Integer>> keyList = new TriadList();
			doTestKeyList3(keyList);
		}
		{
			KeyNList.Builder<Triad<Integer>> builder = new KeyNList.Builder<>();
			builder.withKeyMap(1, t -> ((Triad) t).getItem0());
			builder.withKeyMap(2, t -> ((Triad) t).getItem1());
			builder.withKeyMap(3, t -> ((Triad) t).getItem2());
			KeyNList<Triad<Integer>> keyList = builder.build();
			doTestKeyList3(keyList);
		}
		{
			KeyNList<Triad<Integer>> keyList = KeyNList.buildList(b -> b
					.withKeyMap(1, t -> ((Triad) t).getItem0())
					.withKeyMap(2, t -> ((Triad) t).getItem1())
					.withKeyMap(3, t -> ((Triad) t).getItem2()));
			doTestKeyList3(keyList);
		}
	}

	void doTestKeyList3(KeyNList<Triad<Integer>> l0) {
		l0.add(t0);
		l0.add(t1);
		l0.add(t2);
		LOG.info("{}", l0);

		LOG.info("{}", l0.getByKey(1, 0));
		LOG.info("{}", l0.getByKey(2, 2));
		LOG.info("{}", l0.getByKey(3, 4));
	}

	// KeyCollection3

	static class TriadCollection extends KeyNCollection<Triad<Integer>> {
		public TriadCollection() {
			getBuilder()
					.withKeyMap(1, t -> ((Triad) t).getItem0())
					.withKeyMap(2, t -> ((Triad) t).getItem1())
					.withKeyMap(3, t -> ((Triad) t).getItem2())
					.build();
		}
	}

	@Capture
	public void testKeyCollection3() {
		{
			KeyNCollection<Triad<Integer>> keyCollection = new TriadCollection();
			doTestKeyCollection3(keyCollection);
		}
		{
			KeyNCollection.Builder<Triad<Integer>> builder = new KeyNCollection.Builder<>();
			builder.withKeyMap(1, t -> ((Triad) t).getItem0());
			builder.withKeyMap(2, t -> ((Triad) t).getItem1());
			builder.withKeyMap(3, t -> ((Triad) t).getItem2());
			KeyNCollection<Triad<Integer>> keyCollection = builder.build();
			doTestKeyCollection3(keyCollection);
		}
		{
			KeyNCollection<Triad<Integer>> keyCollection = KeyNCollection.buildCollection(b -> b
					.withKeyMap(1, t -> ((Triad) t).getItem0())
					.withKeyMap(2, t -> ((Triad) t).getItem1())
					.withKeyMap(3, t -> ((Triad) t).getItem2()));
			doTestKeyCollection3(keyCollection);
		}
	}

	void doTestKeyCollection3(KeyNCollection<Triad<Integer>> l0) {
		l0.add(t0);
		l0.add(t1);
		l0.add(t2);
		LOG.info("{}", l0);

		LOG.info("{}", l0.getByKey(1, 0));
		LOG.info("{}", l0.getByKey(2, 2));
		LOG.info("{}", l0.getByKey(3, 4));
	}

	// KeySet3

	static class TriadSet extends KeyNSet<Triad<Integer>> {
		public TriadSet() {
			getBuilder()
					.withKeyMap(1, t -> ((Triad) t).getItem0())
					.withKeyMap(2, t -> ((Triad) t).getItem1())
					.withKeyMap(3, t -> ((Triad) t).getItem2())
					.build();
		}
	}

	@Capture
	public void testKeySet3() {
		{
			KeyNSet<Triad<Integer>> keySet = new TriadSet();
			doTestKeyCollection3(keySet);
		}
		{
			KeyNSet.Builder<Triad<Integer>> builder = new KeyNSet.Builder<>();
			builder.withKeyMap(1, t -> ((Triad) t).getItem0());
			builder.withKeyMap(2, t -> ((Triad) t).getItem1());
			builder.withKeyMap(3, t -> ((Triad) t).getItem2());
			KeyNCollection<Triad<Integer>> keySet = builder.build();
			doTestKeyCollection3(keySet);
		}
		{
			KeyNSet<Triad<Integer>> keySet = KeyNSet.buildSet(b -> b
					.withKeyMap(1, t -> ((Triad) t).getItem0())
					.withKeyMap(2, t -> ((Triad) t).getItem1())
					.withKeyMap(3, t -> ((Triad) t).getItem2()));
			doTestKeyCollection3(keySet);
		}
	}

}
