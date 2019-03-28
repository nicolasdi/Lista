package ed.estructuras.lineales;

import ed.estructuras.ColeccionAbstracta;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Clase que implementa la clase List<E>
 */
public class ListaDoblementeLigada<E> extends ColeccionAbstracta<E> implements List<E> {

	public boolean add(E e) {
		return false;
	}

	public void add(int index, E element) {
		return;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return false;
	}

	public E get(int index) {
		return null;
	}

	public int indexOf(Object o) {
		return 0;
	}

	public Iterator<E> iterator() {
		return null;
	}

	public int lastIndexOf(Object o) {
		return -1;
	}

	public ListIterator<E> listIterator() {
		return null;
	}

	public ListIterator<E> listIterator(int index) {
		return null;
	}

	public E remove(int index) {
		return null;
	}

	/*
	public default void replaceAll(UnaryOperator<E> operator) {
		return;
		}*/

	public E set(int index, E element) {
		return null;
	}

	/*
	public void sort(Comparator<? super E> c) {
		return;
		}*/

	//aquí iba Spliterator pero se me hace que no va a ser necesario,
	//por eso evité poner la firma
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return null;
	}





}
