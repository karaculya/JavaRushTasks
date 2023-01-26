package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.*;

public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {

    private Entry<String> root;

    public CustomTree() {
        root = new Entry<String>(null);
        root.newLineRootElement = true;
        root.lineNumber = 0;
    }

    public String getParent(String value) {
        setValidCollection();
        String s = null;
        for (Entry<String> entry : queue) {
            if (entry.lineNumber != 1) {
                if (entry.elementName.equals(value)) {
                    s = entry.parent.elementName;
                    break;
                }
            }
        }
        return s;
    }

    private transient ArrayList<Entry<String>> queue;

    private void setUpCollection(Entry<String> root) {
        queue = new ArrayList<>();
        Queue<Entry<String>> subQueue = new LinkedList<Entry<String>>();
        queue.add(root);
        subQueue.add(root);
        do {
            if (!subQueue.isEmpty()) {
                root = subQueue.poll();
            }
            if (root.leftChild != null) {
                queue.add(root.leftChild);
                subQueue.add(root.leftChild);
            }
            if (root.rightChild != null) {
                queue.add(root.rightChild);
                subQueue.add(root.rightChild);
            }
        } while (!subQueue.isEmpty());

    }

    private List<Entry<String>> getCollection(Entry<String> entry) {
        ArrayList<Entry<String>> list = new ArrayList<>();
        Queue<Entry<String>> subQueue = new LinkedList<Entry<String>>();
        list.add(entry);
        subQueue.add(entry);
        do {
            if (!subQueue.isEmpty()) {
                entry = subQueue.poll();
            }
            if (entry.leftChild != null) {
                list.add(entry.leftChild);
                subQueue.add(entry.leftChild);
            }
            if (entry.rightChild != null) {
                list.add(entry.rightChild);
                subQueue.add(entry.rightChild);
            }
        } while (!subQueue.isEmpty());
        return list;
    }

    private void setValidCollection() {
        setUpCollection(root);
        queue.remove(0);
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        String string = String.valueOf(o);
        setValidCollection();
        for (Entry<String> entry : queue) {
            if (entry.elementName.equals(string)) {
                return queue.indexOf(entry);
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        String string = String.valueOf(o);
        setValidCollection();
        Entry<String> element = null;
        for (Entry<String> entry : queue) {
            if (entry.elementName.equals(string)) {
                element = entry;
            }
        }
        return queue.lastIndexOf(element);
    }

    @Override
    public boolean add(String s) {
        setUpCollection(root);
        for (Entry<String> entry : queue) {
            entry.checkChildren();
            if (entry.isAvailableToAddChildren()) {
                createChild(entry, s);
                setValidCollection();
                return true;
            }
        }

        boolean reAddingChildren = false;
        for (Entry<String> entry : queue) {
            if (entry.newLineRootElement) {
                reAddingChildren = true;
            }
            if (reAddingChildren) {
                entry.availableToAddLeftChildren = true;
                entry.availableToAddRightChildren = true;
            }
        }

        return add(s);
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] toArray() {
        setValidCollection();
        int size = size();
        String[] array = new String[size];
        for (int i = 0; i < size; i++) {
            array[i] = queue.get(i).elementName;
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int size = size();
        T[] array = a.length >= size ? a : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        Iterator<String> iterator = iterator();
        for (int i = 0; i < array.length; i++) {
            if (!iterator.hasNext()) { // fewer elements than expected
                if (a != array) {
                    return Arrays.copyOf(array, i);
                }
                array[i] = null; // null-terminate
                return array;
            }
            array[i] = (T) iterator.next();
        }
        return array;
    }

    private void changeNewLineRootElement(Entry<String> entry, List<Entry<String>> delList) {
        setValidCollection();
        int size = size();
        for (int i = 0; i < size; i++) {
            Entry<String> newEntry = queue.get(i);
            if (newEntry == entry) {
                if (i < size - 1) {
                    newEntry.newLineRootElement = false;
                    for (int k = i + 1; k < size; k++) {
                        Entry<String> newRootEntry = queue.get(k);
                        if (!delList.contains(newRootEntry)) {
                            newRootEntry.newLineRootElement = true;
                            break;
                        }
                    }
                    //Если все элементы справа подлежат удалению идем вверх по дереву:
                    Entry<String> element = getUndelRoot(newEntry.parent, delList);
                    element.newLineRootElement = true;
                    break;
                } else {
                    newEntry.newLineRootElement = false;
                    Entry<String> element = getUndelRoot(newEntry.parent, delList);
                    element.newLineRootElement = true;
                    break;
                }
            }
        }
    }

    private Entry<String> getUndelRoot(Entry<String> entry, List<Entry<String>> delList) {
        for (Entry<String> element : queue) {
            if (element.lineNumber == entry.lineNumber) {
                if (!delList.contains(element)) {
                    return element;
                }
            }
        }
        if (entry.lineNumber == 0) {   // Дошли до root и возвращаем его.
            return entry;
        }
        return getUndelRoot(entry.parent, delList);
    }

    private List<Entry<String>> getNewLineRootElementsCollection(Entry<String> entry) {
        ArrayList<Entry<String>> list = new ArrayList<>();
        list.add(entry);
        if ((entry.parent != null) && (entry.parent.newLineRootElement)) {
            list.addAll(getNewLineRootElementsCollection(entry.parent));
        }
        return list;
    }

    private void createChild(Entry<String> parent, String s) {
        Entry<String> newOne = new Entry<String>(s);
        newOne.parent = parent;
        newOne.lineNumber = parent.lineNumber + 1;
        setChild(parent, newOne);
    }

    private void setChild(Entry<String> parent, Entry<String> child) {
        if (parent.availableToAddLeftChildren) {
            parent.leftChild = child;
            parent.availableToAddLeftChildren = false;
            if (parent.newLineRootElement) {
                List<Entry<String>> list = getNewLineRootElementsCollection(parent);
                for (Entry<String> entry : list) {
                    entry.newLineRootElement = false;
                }
                child.newLineRootElement = true;
            }
        } else {
            parent.rightChild = child;
            parent.availableToAddRightChildren = false;
            if (parent.newLineRootElement) {
                List<Entry<String>> list = getNewLineRootElementsCollection(parent);
                for (Entry<String> entry : list) {
                    entry.newLineRootElement = false;
                }
                child.newLineRootElement = true;
            }
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        setValidCollection();
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        setValidCollection();
        boolean modified = false;
        Iterator<String> iterator = iterator();
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean addAll(Collection<? extends String> collection) {
        boolean modified = false;
        for (String line : collection) {
            if (add(line)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public int size() {
        setValidCollection();
        return queue.size();
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof String)) {
            throw new UnsupportedOperationException();
        }
        String value = (String) o;
        setValidCollection();
        for (int i = 0; i < queue.size(); i++) {
            Entry<String> entry = queue.get(i);
            if (entry.elementName.equals(value)) {
                List<Entry<String>> list = getCollection(entry);
                for (Entry<String> stringEntry : list) {
                    if (stringEntry.newLineRootElement) {
                        changeNewLineRootElement(stringEntry, list);
                    }
                }
                if (entry.parent.leftChild == entry) {
                    entry.parent.leftChild = null;
                    setValidCollection();
                    return true;
                } else {
                    entry.parent.rightChild = null;
                    setValidCollection();
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        setValidCollection();
        for (Entry<String> entry : queue) {
            if (c.contains(entry.elementName)) {
                remove(entry.elementName);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        setValidCollection();
        return size() == 0;
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        root.newLineRootElement = true;
        root.availableToAddLeftChildren = true;
        root.availableToAddRightChildren = true;
        root.leftChild = null;
        root.rightChild = null;
        setValidCollection();
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }


    @Override
    protected CustomTree clone() throws CloneNotSupportedException {
        CustomTree customTree = (CustomTree) super.clone();
        return customTree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomTree customTree = (CustomTree) o;
        this.setValidCollection();
        customTree.setValidCollection();
        int size = size();
        for (int i = 0; i < size; i++) {
            Entry first = queue.get(i);
            Entry second = customTree.queue.get(i);
            if (!first.equals(second)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (queue != null ? queue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        setValidCollection();
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < queue.size(); i++) {
            builder.append(queue.get(i).elementName);
            if (i < queue.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public ListIterator<String> listIterator() {
        return new SolutionListIterator(0);
    }

    @Override
    public ListIterator<String> listIterator(int index) {
        checkElementIndex(index);
        return new SolutionListIterator(index);
    }

    private boolean checkIndex(int index) {
        return index >= 0 && index < size();
    }

    private void checkElementIndex(int index) {
        if (!checkIndex(index)) {
            throw new IndexOutOfBoundsException("List size: " + queue.size() + ", Index: " + index);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new SolutionIterator();
    }

    private class SolutionIterator implements Iterator<String> {
        volatile ArrayList<Entry<String>> list;
        volatile int index, lastReturned = -1;

        SolutionIterator() {
            setValidCollection();
            list = queue;
        }

        @Override
        public synchronized boolean hasNext() {
            return index != list.size();
        }

        @Override
        public synchronized String next() {
            int i = index;
            if (i >= size()) {
                throw new NoSuchElementException();
            }
            index = i + 1;
            return list.get(lastReturned = i).elementName;
        }

        @Override
        public synchronized void remove() {
            if (lastReturned < 0) {
                throw new IllegalStateException();
            }
            if (!hasNext()) {
                throw new IndexOutOfBoundsException("List size: " + list.size() + ", Index: " + index);
            }
            CustomTree.this.remove(list.get(lastReturned).elementName);
            index = lastReturned;
            lastReturned = -1;
            list = queue;
        }
    }

    private class SolutionListIterator extends SolutionIterator implements ListIterator<String> {

        SolutionListIterator(int index) {
            super();
            this.index = index;
        }

        @Override
        public synchronized boolean hasPrevious() {
            return index != 0;
        }

        @Override
        public synchronized String previous() {
            int i = index - 1;
            if (i < 0) {
                throw new NoSuchElementException("List size: " + list.size() + ", Index: " + i);
            }
            if (i >= list.size()) {
                throw new IllegalStateException();
            }
            index = i;
            return list.get(lastReturned = i).elementName;
        }

        @Override
        public synchronized int nextIndex() {
            return index;
        }

        @Override
        public synchronized int previousIndex() {
            return index - 1;
        }

        @Override
        public synchronized void set(String s) {
            if (lastReturned < 0) {
                throw new IllegalStateException();
            }
            for (Entry<String> entry : queue) {
                if (entry.equals(list.get(lastReturned))) {
                    entry.elementName = s;
                }
            }
        }

        @Override
        public synchronized void add(String s) {
            CustomTree.this.add(s);
            list = queue;
        }
    }


    private static class Entry<T> implements Serializable {

        String elementName;
        int lineNumber;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        boolean newLineRootElement;
        Entry<T> parent, leftChild, rightChild;

        private Entry(String name) {
            elementName = name;
            newLineRootElement = false;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }

        public void checkChildren() {
            if (this.leftChild != null) {
                availableToAddLeftChildren = false;
            }
            if (this.rightChild != null) {
                availableToAddRightChildren = false;
            }
        }

        public boolean isAvailableToAddChildren() {
            return this.availableToAddRightChildren || this.availableToAddLeftChildren;
        }
    }
}
