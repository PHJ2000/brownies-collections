package org.magicwerk.brownies.collections.guava;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
import static com.google.common.collect.testing.features.CollectionFeature.SERIALIZABLE;
import static com.google.common.collect.testing.features.CollectionFeature.SERIALIZABLE_INCLUDING_VIEWS;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.magicwerk.brownies.collections.GapList;
import org.magicwerk.brownies.collections.IList;
import org.magicwerk.brownies.collections.Key1List;
import org.magicwerk.brownies.collections.Key2List;
import org.magicwerk.brownies.collections.KeyList;
import org.magicwerk.brownies.collections.TestHelper.Ticket;
import org.magicwerk.brownies.collections.helper.NullComparator;
import org.magicwerk.brownies.core.ArrayTools;
import org.magicwerk.brownies.core.reflect.ReflectTools;

import com.google.common.collect.testing.AbstractCollectionTestSuiteBuilder;
import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.ListTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestListGenerator;
import com.google.common.collect.testing.TestStringListGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.testers.CollectionSerializationEqualTester;
import com.google.common.collect.testing.testers.ListAddAllAtIndexTester;
import com.google.common.collect.testing.testers.ListAddAllTester;
import com.google.common.collect.testing.testers.ListAddAtIndexTester;
import com.google.common.collect.testing.testers.ListAddTester;
import com.google.common.collect.testing.testers.ListCreationTester;
import com.google.common.collect.testing.testers.ListEqualsTester;
import com.google.common.collect.testing.testers.ListGetTester;
import com.google.common.collect.testing.testers.ListHashCodeTester;
import com.google.common.collect.testing.testers.ListIndexOfTester;
import com.google.common.collect.testing.testers.ListLastIndexOfTester;
import com.google.common.collect.testing.testers.ListListIteratorTester;
import com.google.common.collect.testing.testers.ListRemoveAllTester;
import com.google.common.collect.testing.testers.ListRemoveAtIndexTester;
import com.google.common.collect.testing.testers.ListRetainAllTester;
import com.google.common.collect.testing.testers.ListSetTester;
import com.google.common.collect.testing.testers.ListSubListTester;
import com.google.common.collect.testing.testers.ListToArrayTester;
import com.google.common.testing.SerializableTester;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test KeyList with the test suite provided by Guava.
 * Run this test by executing the whole test class using JUnit.
 * This test is defined as JUnit3 test but can also be run with JUnit4.
 *
 * @author Thomas Mauch
 */
public class KeyListTestGuava extends TestCase {

	public static class MyListAddAllTester extends ListAddAllTester<Ticket> {
		@Override
		protected void expectContents(Collection<Ticket> expected) {
			IList<Ticket> ts = GapList.create(expected);
			ts.sort(new NullComparator<>(Comparator.comparing(Ticket::getId), false));
			super.expectContents(ts);
		}
	}

	public static class MyListAddTester extends ListAddTester<Ticket> {
		@Override
		protected void expectContents(Collection<Ticket> expected) {
			IList<Ticket> ts = GapList.create(expected);
			ts.sort(new NullComparator<>(Comparator.comparing(Ticket::getId), false));
			super.expectContents(ts);
		}
	}

	public static class MyListIndexOfTester extends ListIndexOfTester<Ticket> {
		@Override
		protected int getNullLocation() {
			return getNumElements() - 1;
		}
	}

	public static class MyListLastIndexOfTester extends ListLastIndexOfTester<Ticket> {
		@Override
		protected int getNullLocation() {
			return getNumElements() - 1;
		}
	}

	//	public static class MyAbstractListIndexOfTester extends AbstractListIndexOfTester<Ticket> {
	//		@Override
	//		protected int getNullLocation() {
	//			return getNumElements() - 1;
	//		}
	//	}

	public static class MyListTestSuiteBuilder<E> extends AbstractCollectionTestSuiteBuilder<MyListTestSuiteBuilder<E>, E> {

		@Override
		public Set<Method> getSuppressedTests() {
			Set<Method> ms = super.getSuppressedTests();
			ms.add((Method) ReflectTools.getAnyMethod(ListSubListTester.class, "testSubList_originalListSetAffectsSubListLargeList"));
			ms.add((Method) ReflectTools.getAnyMethod(ListSubListTester.class, "testSubList_subListSetAffectsOriginalLargeList"));
			ms.add((Method) ReflectTools.getAnyMethod(ListSubListTester.class, "testSubList_subListSetAffectsOriginal"));
			ms.add((Method) ReflectTools.getAnyMethod(ListSubListTester.class, "testSubList_originalListSetAffectsSubList"));

			ms.add((Method) ReflectTools.getAnyMethod(ListSetTester.class, "testSet"));
			ms.add((Method) ReflectTools.getAnyMethod(ListSetTester.class, "testSet_null"));
			ms.add((Method) ReflectTools.getAnyMethod(ListSetTester.class, "testSet_replacingNull"));

			ms.add((Method) ReflectTools.getAnyMethod(ListRetainAllTester.class, "testRetainAll_countIgnored"));
			return ms;
		}

		public static <E> MyListTestSuiteBuilder<E> using(TestListGenerator<E> generator) {
			return new MyListTestSuiteBuilder<E>().usingGenerator(generator);
		}

		@SuppressWarnings("rawtypes") // class literals
		@Override
		protected List<Class<? extends AbstractTester>> getTesters() {
			List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());

			testers.add(CollectionSerializationEqualTester.class);
			testers.add(ListAddAllAtIndexTester.class);
			testers.add(MyListAddAllTester.class);
			testers.add(ListAddAtIndexTester.class);
			testers.add(MyListAddTester.class);
			testers.add(ListCreationTester.class);
			testers.add(ListEqualsTester.class);
			testers.add(ListGetTester.class);
			testers.add(ListHashCodeTester.class);
			testers.add(MyListIndexOfTester.class);
			testers.add(MyListLastIndexOfTester.class);
			testers.add(ListListIteratorTester.class);
			testers.add(ListRemoveAllTester.class);
			testers.add(ListRemoveAtIndexTester.class);
			//testers.add(ListRemoveTester.class);
			//testers.add(ListReplaceAllTester.class);
			testers.add(ListRetainAllTester.class);
			testers.add(ListSetTester.class);
			testers.add(ListSubListTester.class);
			testers.add(ListToArrayTester.class);
			return testers;
		}

		/**
		 * Specifies {@link CollectionFeature#KNOWN_ORDER} for all list tests, since lists have an
		 * iteration ordering corresponding to the insertion order.
		 */
		@Override
		public TestSuite createTestSuite() {
			withFeatures(KNOWN_ORDER);
			return super.createTestSuite();
		}

		@Override
		protected List<TestSuite> createDerivedSuites(
				FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<E>, E>> parentBuilder) {
			List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));

			if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
				derivedSuites.add(
						MyListTestSuiteBuilder.using(
								new ReserializedListGenerator<E>(parentBuilder.getSubjectGenerator()))
								.named(getName() + " reserialized")
								.withFeatures(computeReserializedCollectionFeatures(parentBuilder.getFeatures()))
								.suppressing(parentBuilder.getSuppressedTests())
								.withSetUp(parentBuilder.getSetUp())
								.withTearDown(parentBuilder.getTearDown())
								.createTestSuite());
			}
			return derivedSuites;
		}

		static class ReserializedListGenerator<E> implements TestListGenerator<E> {
			final OneSizeTestContainerGenerator<Collection<E>, E> gen;

			private ReserializedListGenerator(OneSizeTestContainerGenerator<Collection<E>, E> gen) {
				this.gen = gen;
			}

			@Override
			public SampleElements<E> samples() {
				return gen.samples();
			}

			@Override
			public List<E> create(Object... elements) {
				return (List<E>) SerializableTester.reserialize(gen.create(elements));
			}

			@Override
			public E[] createArray(int length) {
				return gen.createArray(length);
			}

			@Override
			public Iterable<E> order(List<E> insertionOrder) {
				return gen.order(insertionOrder);
			}
		}

		private static Set<Feature<?>> computeReserializedCollectionFeatures(Set<Feature<?>> features) {
			Set<Feature<?>> derivedFeatures = new HashSet<>(features);
			derivedFeatures.remove(SERIALIZABLE);
			derivedFeatures.remove(SERIALIZABLE_INCLUDING_VIEWS);
			return derivedFeatures;
		}
	}

	// this method must be named suite()
	public static Test suite() {
		TestSuite test = new TestSuite("KeyList");
		test.addTest(suiteKeyList2());
		//test.addTest(suiteKeyList());
		//test.addTest(suiteKey1List());
		//test.addTest(suiteKey2List());
		return test;
	}

	static abstract class TestTicketListGenerator implements TestListGenerator<Ticket> {

		static final Ticket t1 = new Ticket(1, "extId1", "text1");
		static final Ticket t2 = new Ticket(2, "extId2", "text2");
		static final Ticket t3 = new Ticket(3, "extId3", "text3");
		static final Ticket t4 = new Ticket(4, "extId4", "text4");
		static final Ticket t5 = new Ticket(5, "extId5", "text5");

		@Override
		public SampleElements<Ticket> samples() {
			return new SampleElements<>(t1, t2, t3, t4, t5);
		}

		public abstract List<Ticket> create(Ticket[] ts);

		@Override
		public List<Ticket> create(Object... elements) {
			Ticket[] ts = ArrayTools.convert(elements, Ticket.class);
			return create(ts);
		}

		@Override
		public Ticket[] createArray(int length) {
			return new Ticket[length];
		}

		@Override
		public List<Ticket> order(List<Ticket> insertionOrder) {
			return insertionOrder;
		}
	}

	public static Test suiteKeyList2() {
		//return new ListTestSuiteBuilder<Ticket>().usingGenerator(
		return MyListTestSuiteBuilder.using(
				// This class is responsible for creating the collection
				// And providing data, which can be put into the collection
				// Here we use a abstract generator which will create strings
				// which will be put into the collection
				new TestTicketListGenerator() {
					@Override
					public List<Ticket> create(Ticket[] ts) {
						//return new KeyList.Builder<Ticket>().withContent(ts).build();
						return new Key1List.Builder<Ticket, String>().withKey1Map(Ticket::getExtId).withKey1Sort(true).withOrderByKey1(true)
								.withKey1Duplicates(true).withContent(ts).build();

					}
				})
				// The name of the test suite
				.named("KeyList Test")
				// Here we give a hit what features our collection supports
				// ListFeature.GENERAL_PURPOSE = SUPPORTS_SET + SUPPORTS_ADD_WITH_INDEX + SUPPORTS_REMOVE_WITH_INDEX + CollectionFeature.GENERAL_PURPOSE
				// CollectionFeature.GENERAL_PURPOSE = SUPPORTS_ADD + SUPPORTS_REMOVE + SUPPORTS_ITERATOR_REMOVE

				.withFeatures(
						CollectionFeature.SUPPORTS_ADD,
						CollectionFeature.SUPPORTS_REMOVE,
						CollectionFeature.SUPPORTS_ITERATOR_REMOVE,
						ListFeature.SUPPORTS_SET,
						ListFeature.SUPPORTS_REMOVE_WITH_INDEX,
						CollectionFeature.ALLOWS_NULL_VALUES,
						//CollectionFeature.SERIALIZABLE,
						//CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
						CollectionSize.ANY)
				.createTestSuite();
	}

	// this method must be named suite()
	public static Test suiteKeyList() {
		return ListTestSuiteBuilder.using(
				// This class is responsible for creating the collection
				// And providing data, which can be put into the collection
				// Here we use a abstract generator which will create strings
				// which will be put into the collection
				new TestStringListGenerator() {
					@Override
					protected List<String> create(String[] elements) {
						// Fill here your collection with the given elements
						KeyList<String> list = new KeyList.Builder<String>().withNull(true).withContent(elements).build();
						return list;
					}
				})
				// The name of the test suite
				.named("KeyList Test")
				// Here we give a hit what features our collection supports
				.withFeatures(ListFeature.GENERAL_PURPOSE,
						CollectionFeature.ALLOWS_NULL_VALUES,
						CollectionFeature.SERIALIZABLE,
						//CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
						CollectionSize.ANY)
				.createTestSuite();
	}

	// this method must be named suite()
	public static Test suiteKey1List() {
		return ListTestSuiteBuilder.using(
				// This class is responsible for creating the collection
				// And providing data, which can be put into the collection
				// Here we use a abstract generator which will create strings
				// which will be put into the collection
				new TestStringListGenerator() {
					@Override
					protected List<String> create(String[] elements) {
						// Fill here your collection with the given elements
						@SuppressWarnings("unchecked")
						Key1List<String, Integer> list = new Key1List.Builder<String, Integer>()
								.withKey1Map((Function<String, Integer> & Serializable) (s) -> s.length())
								.withContent(elements).build();
						return list;
					}
				})
				// The name of the test suite
				.named("Key1List Test")
				// Here we give a hit what features our collection supports
				.withFeatures(ListFeature.GENERAL_PURPOSE,
						CollectionFeature.ALLOWS_NULL_VALUES,
						CollectionFeature.SERIALIZABLE,
						//CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
						CollectionSize.ANY)
				.createTestSuite();
	}

	// this method must be named suite()
	public static Test suiteKey2List() {
		return ListTestSuiteBuilder.using(
				// This class is responsible for creating the collection
				// And providing data, which can be put into the collection
				// Here we use a abstract generator which will create strings
				// which will be put into the collection
				new TestStringListGenerator() {
					@Override
					protected List<String> create(String[] elements) {
						// Fill here your collection with the given elements
						@SuppressWarnings("unchecked")
						Key2List<String, Integer, Integer> list = new Key2List.Builder<String, Integer, Integer>()
								.withKey1Map((Function<String, Integer> & Serializable) (s) -> s.length())
								.withKey2Map((Function<String, Integer> & Serializable) (s) -> s.length())
								.withContent(elements).build();
						return list;
					}
				})
				// The name of the test suite
				.named("Key2List Test")
				// Here we give a hit what features our collection supports
				.withFeatures(ListFeature.GENERAL_PURPOSE,
						CollectionFeature.ALLOWS_NULL_VALUES,
						CollectionFeature.SERIALIZABLE,
						//CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
						CollectionSize.ANY)
				.createTestSuite();
	}

}
