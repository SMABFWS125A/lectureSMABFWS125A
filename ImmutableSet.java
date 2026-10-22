package code;

import java.util.*;

public class ImmutableSet<T> implements Iterable<T> {

    private class ImmutableSetIterator implements Iterator<T> {

        private Node current;
        private ImmutableSet<T> used;

        private ImmutableSetIterator(final Node root) {
            this.current = root;
            this.used = new ImmutableSet<T>();
            this.forward();
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public T next() {
            final T result = this.current.value;
            this.current = this.current.next;
            this.forward();
            return result;
        }

        private void forward() {
            while (
                this.current != null
                && (!this.current.added || this.used.contains(this.current.value)
            )) {
                this.used = this.used.add(this.current.value);
                this.current = this.current.next;
            }
        }

    }

    private class Node {

        public final boolean added;

        public final Node next;

        public final T value;

        public Node(final T value, final Node next, final boolean added) {
            this.value = value;
            this.next = next;
            this.added = added;
        }

    }

    public static void main(final String[] args) {
        final ImmutableSet<Integer> set =
            new ImmutableSet<Integer>()
            .add(5)
            .add(7)
            .add(13)
            .remove(5)
            .add(18)
            .add(5);
        for (final Integer i : set) {
            System.out.println(i);
        }
    }

    private final Node root;

    public ImmutableSet() {
        this.root = null;
    }

    private ImmutableSet(final Node root) {
        this.root = root;
    }

    public ImmutableSet<T> add(final T element) {
        return new ImmutableSet<T>(
            new Node(element, this.root, true)
        );
    }

    public boolean contains(final T element) {
//        Iterator<T> iterator = this.iterator();
//        while (iterator.hasNext()) {
//            T value = iterator.next();
//            if (element.equals(value)) {
//                return true;
//            }
//        }
        for (final T value : this) {
            if (element.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return !this.iterator().hasNext();
    }

    @Override
    public Iterator<T> iterator() {
        return new ImmutableSetIterator(this.root);
    }

    public ImmutableSet<T> remove(final T element) {
        return new ImmutableSet<T>(
            new Node(element, this.root, false)
        );
    }

}
