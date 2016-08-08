package br.nom.abdon.domino.motor.util;

import java.util.Iterator;

public class IteratorReadOnly<E> implements Iterator<E> {

    private final Iterator<E> iterator;

    public IteratorReadOnly(final Iterator<E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public E next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new IllegalStateException("nao pode remover nada daqui");

    }
}
