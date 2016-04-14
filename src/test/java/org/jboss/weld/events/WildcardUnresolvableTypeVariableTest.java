package org.jboss.weld.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Martin Kouba
 *
 */
@RunWith(Arquillian.class)
public class WildcardUnresolvableTypeVariableTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addPackage(WildcardUnresolvableTypeVariableTest.class.getPackage());
    }

    @Inject
    BeanManager beanManager;

    @Inject
    Event<List<String>> stringEvent;

    @Inject
    Event<List<?>> wildcardEvent;

    @Inject
    Event<List<? extends Number>> wilcardBoundEvent;

    // This method should always fail for CDI 1.0 where the container cannot use the Event specified type to infer the parameterized type
    @Test
    public void testAnyListObserver() {

        List<String> stringList = new ArrayList<>();
        stringList.add("foo");

        AnyListObserver.observedList = null;
        stringEvent.fire(stringList);
        assertNotNull(AnyListObserver.observedList);
        assertEquals("foo", AnyListObserver.observedList.get(0));

        AnyListObserver.observedList = null;
        // Note that we can only work with "runtime type" = ArrayList.class and "specified type" = List<?>
        wildcardEvent.fire(stringList);
        assertNotNull(AnyListObserver.observedList);
        assertEquals("foo", AnyListObserver.observedList.get(0));

        List<Integer> intList = new ArrayList<>();
        intList.add(10);
        AnyListObserver.observedList = null;
        wildcardEvent.fire(intList);
        assertNotNull(AnyListObserver.observedList);
        assertEquals(Integer.valueOf(10), AnyListObserver.observedList.get(0));
    }

    // This method reflects TCK - FireEventTest.testTypeVariableEventTypeFails() - see also CDITCK-510
    // Should always fail on CDI 1.0
    @Test
    public <T extends Number> void testAnyListObserverBoundWildcard() {
        AnyListObserver.observedList = null;
        // Note that we can only work with "runtime type" = ArrayList.class and "specified type" = List<? extends Number>
        wilcardBoundEvent.fire(new ArrayList<T>());
        assertNotNull(AnyListObserver.observedList);
        assertTrue(AnyListObserver.observedList.isEmpty());
    }

    /*
     * This test method should always fail (11.3.11. Firing an event):
     * "If the runtime type of the event object contains a type variable, an IllegalArgumentException is thrown."
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAnyListObserverBeanManager() {
        List<String> stringList = new ArrayList<>();
        stringList.add("foo");
        AnyListObserver.observedList = null;
        // Note that we can only work with "runtime type" = ArrayList.class
        beanManager.fireEvent(stringList);
        assertNotNull(AnyListObserver.observedList);
        assertEquals("foo", AnyListObserver.observedList.get(0));
    }

}
