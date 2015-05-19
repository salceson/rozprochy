package pl.edu.agh.ki.dsrg.sr.bankmanagement.locators;

import Ice.*;
import Ice.Object;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Michał Ciołczyk
 */
public abstract class EvictorBase implements ServantLocator {
    protected final Map<Identity, EvictorEntry> entries = new HashMap<>();
    protected final LinkedList<Identity> queue = new LinkedList<>();
    protected int size;

    protected EvictorBase(int size) {
        this.size = size;
    }

    @Override
    public synchronized Ice.Object locate(Current current, LocalObjectHolder cookie) throws UserException {
        EvictorEntry entry;

        if (entries.containsKey(current.id)) {
            entry = entries.get(current.id);
            entry.queuePosition.remove();
        } else {
            entry = new EvictorEntry();
            LocalObjectHolder cookieHolder = new LocalObjectHolder();
            entry.servant = add(current, cookieHolder);
            entry.cookie = cookieHolder.value;
            entry.useCount = 0;
            entries.put(current.id, entry);
        }

        entry.useCount++;
        queue.addFirst(current.id);
        entry.queuePosition = queue.iterator();
        entry.queuePosition.next();

        cookie.value = entry;

        return entry.servant;
    }


    @Override
    public synchronized void deactivate(String s) {
        size = 0;
        evictServants();
    }

    @Override
    public void finished(Current current, Object object, java.lang.Object cookie) throws UserException {
        EvictorEntry entry = (EvictorEntry) cookie;
        entry.useCount--;
        evictServants();
    }

    private void evictServants() {
        Iterator<Identity> iterator = queue.descendingIterator();
        int excessEntries = entries.size() - size;
        for (int i = 0; i < excessEntries && entries.size() > size; ++i) {
            Identity identity = iterator.next();
            EvictorEntry entry = entries.get(identity);

            evict(entry.servant, entry.cookie);

            entry.queuePosition.remove();
            entries.remove(identity);
        }
    }

    protected abstract Object add(Current current, LocalObjectHolder cookieHolder);

    protected abstract void evict(Object servant, java.lang.Object cookie);

    protected static class EvictorEntry {
        Object servant;
        java.lang.Object cookie;
        Iterator<Identity> queuePosition;
        int useCount;
    }
}
