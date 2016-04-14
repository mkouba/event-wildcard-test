package org.jboss.weld.events;

import java.util.List;

import javax.enterprise.event.Observes;

/**
 *
 * @author Martin Kouba
 *
 */
public class AnyListObserver {

    static List<?> observedList;

    void observeAllLists(@Observes List<?> anyList) {
        observedList = anyList;
    }
}
