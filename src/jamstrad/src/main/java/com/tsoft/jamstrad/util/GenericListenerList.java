package com.tsoft.jamstrad.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class GenericListenerList<T extends GenericListener> implements Iterable<T> {
    private GenericListenerList<T>.ListenerElement headElement;
    private GenericListenerList<T>.ListenerElement tailElement;
    private int sequenceNumberForNextElement;
    private boolean includeAdditionsWhileIterating;
    private GenericListenerList<T>.EmptyListenerIterator emptyListenerIterator;

    public GenericListenerList() {
    }

    public synchronized boolean containsListener(T listener) {
        for (GenericListenerList<T>.ListenerElement current = this.getHeadElement(); current != null; current = current.getNextElement()) {
            if (current.getListener().equals(listener)) {
                return true;
            }
        }

        return false;
    }

    public synchronized boolean addListener(T listener) {
        if (!this.containsListener(listener)) {
            GenericListenerList<T>.ListenerElement element = new GenericListenerList.ListenerElement(listener, this.sequenceNumberForNextElement++);
            if (this.isEmpty()) {
                this.setHeadElement(element);
            } else {
                this.getTailElement().setNextElement(element);
            }

            this.setTailElement(element);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean removeListener(T listener) {
        boolean removed = false;
        GenericListenerList<T>.ListenerElement previous = null;
        GenericListenerList<T>.ListenerElement current = this.getHeadElement();

        while(!removed && current != null) {
            GenericListenerList<T>.ListenerElement next = current.getNextElement();
            if (current.getListener().equals(listener)) {
                if (previous == null) {
                    this.setHeadElement(next);
                    if (next == null) {
                        this.setTailElement((GenericListenerList.ListenerElement)null);
                    }
                } else {
                    previous.setNextElement(next);
                    if (next == null) {
                        this.setTailElement(previous);
                    }
                }

                current.setRemoved(true);
                removed = true;
            } else {
                previous = current;
                current = next;
            }
        }

        return removed;
    }

    public synchronized void removeAllListeners() {
        for(GenericListenerList<T>.ListenerElement current = this.getHeadElement(); current != null; current = current.getNextElement()) {
            current.setRemoved(true);
        }

        this.setHeadElement((GenericListenerList.ListenerElement)null);
        this.setTailElement((GenericListenerList.ListenerElement)null);
    }

    public synchronized void clear() {
        this.removeAllListeners();
    }

    public Iterator<T> iterator() {
        if (this.isEmpty()) {
            if (this.emptyListenerIterator == null) {
                this.emptyListenerIterator = new GenericListenerList.EmptyListenerIterator();
            }

            return this.emptyListenerIterator;
        } else {
            return new GenericListenerList.ListenerIterator();
        }
    }

    public boolean isEmpty() {
        return this.getHeadElement() == null;
    }

    private GenericListenerList<T>.ListenerElement getHeadElement() {
        return this.headElement;
    }

    private void setHeadElement(GenericListenerList<T>.ListenerElement headElement) {
        this.headElement = headElement;
    }

    private GenericListenerList<T>.ListenerElement getTailElement() {
        return this.tailElement;
    }

    private void setTailElement(GenericListenerList<T>.ListenerElement tailElement) {
        this.tailElement = tailElement;
    }

    public boolean isIncludeAdditionsWhileIterating() {
        return this.includeAdditionsWhileIterating;
    }

    public void setIncludeAdditionsWhileIterating(boolean include) {
        this.includeAdditionsWhileIterating = include;
    }

    private class EmptyListenerIterator implements Iterator<T> {
        public EmptyListenerIterator() {
        }

        public final boolean hasNext() {
            return false;
        }

        public final T next() {
            throw new NoSuchElementException();
        }
    }

    private class ListenerElement {
        private T listener;
        private GenericListenerList<T>.ListenerElement nextElement;
        private int sequenceNumber;
        private boolean removed;

        public ListenerElement(T listener, int sequenceNumber) {
            this.listener = listener;
            this.sequenceNumber = sequenceNumber;
        }

        public GenericListenerList<T>.ListenerElement getNextElement() {
            return this.nextElement;
        }

        public void setNextElement(GenericListenerList<T>.ListenerElement nextElement) {
            this.nextElement = nextElement;
        }

        public T getListener() {
            return this.listener;
        }

        public int getSequenceNumber() {
            return this.sequenceNumber;
        }

        public boolean isRemoved() {
            return this.removed;
        }

        public void setRemoved(boolean removed) {
            this.removed = removed;
        }
    }

    private class ListenerIterator implements Iterator<T> {
        private GenericListenerList<T>.ListenerElement initialHeadElement;
        private GenericListenerList<T>.ListenerElement lastReturnedElement;
        private GenericListenerList<T>.ListenerElement nextElement;
        private boolean nextElementDefined;
        private int maximumElementSequenceNumber;

        public ListenerIterator() {
            this.setInitialHeadElement(GenericListenerList.this.getHeadElement());
            if (GenericListenerList.this.isIncludeAdditionsWhileIterating()) {
                this.setMaximumElementSequenceNumber(Integer.MAX_VALUE);
            } else {
                GenericListenerList<T>.ListenerElement tail = GenericListenerList.this.getTailElement();
                this.setMaximumElementSequenceNumber(tail != null ? tail.getSequenceNumber() : 0);
            }

        }

        public synchronized boolean hasNext() {
            return this.defineNextElement() != null;
        }

        public synchronized T next() {
            if (this.hasNext()) {
                GenericListenerList<T>.ListenerElement next = this.getNextElement();
                this.setNextElement((GenericListenerList.ListenerElement)null);
                this.setNextElementDefined(false);
                this.setLastReturnedElement(next);
                return next.getListener();
            } else {
                throw new NoSuchElementException();
            }
        }

        private GenericListenerList<T>.ListenerElement defineNextElement() {
            if (!this.isNextElementDefined()) {
                GenericListenerList<T>.ListenerElement next = null;
                if (this.getLastReturnedElement() == null) {
                    next = this.getInitialHeadElement();
                } else {
                    next = this.getLastReturnedElement().getNextElement();
                }

                while(next != null && (next.isRemoved() || !GenericListenerList.this.isIncludeAdditionsWhileIterating() && next.getSequenceNumber() > this.getMaximumElementSequenceNumber())) {
                    next = next.getNextElement();
                }

                this.setNextElement(next);
                this.setNextElementDefined(true);
            }

            return this.getNextElement();
        }

        private GenericListenerList<T>.ListenerElement getInitialHeadElement() {
            return this.initialHeadElement;
        }

        private void setInitialHeadElement(GenericListenerList<T>.ListenerElement element) {
            this.initialHeadElement = element;
        }

        private GenericListenerList<T>.ListenerElement getLastReturnedElement() {
            return this.lastReturnedElement;
        }

        private void setLastReturnedElement(GenericListenerList<T>.ListenerElement element) {
            this.lastReturnedElement = element;
        }

        private GenericListenerList<T>.ListenerElement getNextElement() {
            return this.nextElement;
        }

        private void setNextElement(GenericListenerList<T>.ListenerElement nextElement) {
            this.nextElement = nextElement;
        }

        private boolean isNextElementDefined() {
            return this.nextElementDefined;
        }

        private void setNextElementDefined(boolean nextElementDefined) {
            this.nextElementDefined = nextElementDefined;
        }

        private int getMaximumElementSequenceNumber() {
            return this.maximumElementSequenceNumber;
        }

        private void setMaximumElementSequenceNumber(int number) {
            this.maximumElementSequenceNumber = number;
        }
    }
}
