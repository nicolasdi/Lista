package ed.estructuras.lineales;

import ed.estructuras.ColeccionAbstracta;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Clase que implementa la interfaz {@code List<E>}
 * Características de esta lista :
 * - Acepta elementos nulos
 * - Acepta elementos repetidos
 * @author nicolasdi
 */
/*
 * Notas sobre la implementación:
 * El control del tamaño de la estructura está en suprimeNodo, add(i,e)
 * No se documentaron los métodos públicos porque al implementar
 * la interfaz se hereda la documentación de ésta.

 * Organización del código :
 * |Clase principal
 * |
 * |`--Métodos públicos y privados en orden alfabético
 * |`--Clases internas
 */
public class ListaDoblementeLigada<E> extends ColeccionAbstracta<E> implements List<E> {
    //int tam;
    /* Apuntador al primer elemento de la lista */
    private Nodo cabeza;
    /* Apuntador al último elemento de la lista */
    private Nodo rabo;

    /**
     * Crea una Lista vacía
     */
    public ListaDoblementeLigada() {
        this.cabeza = null;
        this.rabo = null;
        this.tam = 0;
    }

    public boolean add(E e) {
        this.add(this.size(), e);
        return true;
    }

    public void add(int i, E e) {
        if(i < 0 || this.size() < i) throw new IndexOutOfBoundsException();

        //Se desea agregar en el índice 0 y la lista es vacía
        if(this.isEmpty() && i == 0) {
            Nodo agregado = new Nodo(null, e, null);
            this.cabeza = agregado;
            this.rabo = agregado;
            this.tam++;
            return;
        }

        //se desea agregar al inicio y la lista no es vacía
        if(i == 0) {
            Nodo nuevo = new Nodo(e, this.cabeza);
            this.cabeza = nuevo;
            this.tam++;
            return;
        }

        //se desea agregar al *final* de la lista
        if(i == this.size()) {
            Nodo nuevo = new Nodo(this.rabo, e);
            this.rabo = nuevo;
            this.tam++;
            return;
        }

        //Observación: Dadas las verificaciones anteriores de los
        //if's, para este punto se asegura que su antecesor y sucesor
        //son distintos de null e i-1 esté dentro de un rango válido
        //para la lista
        Nodo antecesor = this.irIndice(i - 1);
        Nodo sucesor = antecesor.getSiguiente();
        Nodo nuevo = new Nodo(antecesor,e, sucesor);
        this.tam++;
        return;
    }

    //Duda, creo que este método es ineficiente, pero la
    //implementación para hacerlo eficiente la veía perrix XD
    public boolean addAll(int i, Collection<? extends E> c) {
        if(c == null) throw new NullPointerException();
        if(c == this) throw new IllegalArgumentException();
        boolean coleccionModificada = false;
        if(i < 0 || this.size() < i) throw new IndexOutOfBoundsException();

        if(this.isEmpty() || this.size() == 1) {
            for(E elemento: c) {
                this.add(elemento);
                coleccionModificada = true;
            }
            return coleccionModificada;
        }

        int insertaEn = i;
        for(E elemento : c) {
            this.add(insertaEn, elemento);
            coleccionModificada = true;
            insertaEn++;
        }

        return coleccionModificada;
    }

    public E get(int i) {
        return this.irIndice(i).get();
    }

    private Nodo getCabeza() {
        return this.cabeza;
    }

    private Nodo getRabo() {
        return this.rabo;
    }

    public int indexOf(Object o) {
        ListIterator<E> buscador = new IteradorLista();
        while(buscador.hasNext()) {
            E elemento = buscador.next();
            if(elemento == null && o == null) {
                return buscador.previousIndex();
            }

            if(elemento == null) {
                continue;
            }

            if(elemento.equals(o)) {
                return buscador.previousIndex();
            }
        }

        return -1;
    }

    /* Método auxiliar para quedar parados en el Nodo de la lista con
     * indice i */
    private Nodo irIndice(int i) {
        if(i < 0 || this.size() <= i) throw new IndexOutOfBoundsException();
        int j = 0;
        Nodo transeunte = this.getCabeza();
        while(j < i) {
            transeunte = transeunte.getSiguiente();
            j++;
        }
        return transeunte;
    }

    public Iterator<E> iterator() {
        return new IteradorLista();
    }

    public int lastIndexOf(Object o) {
        if(this.isEmpty()) return -1;

        ListIterator<E> buscador = this.listIterator(this.size() - 1);
        while(buscador.hasPrevious()) {
            E elemento = buscador.previous();
            if(elemento == null && o == null) {
                return buscador.nextIndex();
            }

            if(elemento == null) {
                continue;
            }

            if(elemento.equals(o)) {
                return buscador.nextIndex();
            }
        }

        return -1;
    }

    public ListIterator<E> listIterator() {
        return new IteradorLista();
    }

    public ListIterator<E> listIterator(int i) {
        if(i < 0 || this.size() < i) throw new IndexOutOfBoundsException();
        return new IteradorLista(i);
    }

    public E remove(int i) {
        Nodo porEliminar = this.irIndice(i);
        this.suprimeNodo(porEliminar);
        return porEliminar.get();
    }

    public E set(int i, E e) {
        Nodo porModificar = this.irIndice(i);
        E eliminado = porModificar.get();
        porModificar.setElemento(e);
        return eliminado;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    /*
     * Se recibe un nodo y dependiendo de su posición en la lista se
     * elimina de ésta.
     */
    private void suprimeNodo(Nodo porEliminar) {
        this.tam--;
        //Lista que sólo contiene un elemento
        if(porEliminar.getAnterior() == null
            && porEliminar.getSiguiente() == null) {
            this.cabeza = null;
            this.rabo = null;
            return;
        }

        //Se desea eliminar a la cabeza
        if(porEliminar.getAnterior() == null) {
            this.cabeza = porEliminar.getSiguiente();
            this.cabeza.anterior = null;
            return;
        }

        //Se desea eliminar al rabo
        if(porEliminar.getSiguiente() == null) {
            this.rabo = porEliminar.getAnterior();
            this.rabo.siguiente = null;
            return;
        }

        //Se desea eliminar a un nodo tal que su antecesor y sucesor
        //son distintos de null
        porEliminar.enlazaNodos(porEliminar.getAnterior(), porEliminar.getSiguiente());
    }

    /*
     * Clase que funciona como bloque constructor para la clase
     * principal y guarda los elementos almacenados en la estructura.
     */
    private class Nodo {
        /* Con respecto a este nodo; nodo anterior en la lista. */
        private Nodo anterior;
        /* Con respecto a este nodo; nodo siguiente en la  lista. */
        private Nodo siguiente;
        /* Elemento que guarda este nodo. */
        private E elemento;

        /**
         * Crea y enlaza un nodo con su antecesor y sucesor.
         */
        public Nodo(Nodo anterior, E elemento, Nodo siguiente) {
            this.elemento = elemento;
            this.anterior = anterior;
            this.siguiente = siguiente;
            //si es el  único  elemento en la lista
            if(anterior == null && siguiente == null) return;
            anterior.siguiente = this;
            siguiente.anterior = this;
        }

        /**
         * Crea un nodo y lo prepara para ser asignado como rabo de la
         * lista.
         */
        public Nodo(Nodo rabo, E elemento) {
            this.elemento = elemento;
            this.anterior = rabo;
            this.siguiente = null;
            rabo.siguiente = this;
        }

        /**
         * Crea un nuevo nodo y lo prepara para ser asignado como
         * cabeza de la lista.
         */
        public Nodo(E elemento, Nodo cabeza) {
            this.elemento = elemento;
            this.anterior = null;
            this.siguiente = cabeza;
            cabeza.anterior = this;
        }

        /* Se enlazan los nodos recibidos de tal forma que tengan
         * posiciones consecutivas en la lista */
        private void enlazaNodos(Nodo antecesor, Nodo sucesor) {
            antecesor.siguiente = sucesor;
            sucesor.anterior = antecesor;
        }

        /** Obtiene al elemento que guarda el nodo. */
        public E get() {
            return this.elemento;
        }

        /**
         * Devuelve al nodo anterior de *este* nodo.
         * @return nodo anterior a este nodo.
         */
        public Nodo getAnterior() {
            return this.anterior;
        }

        /**
         * Devuelve al nodo siguiente de *este* nodo.
         * @return nodo siguiente a este nodo.
         */
        public Nodo getSiguiente() {
            return this.siguiente;
        }

        /** Reasigna al elemento que guarda el nodo. */
        public void setElemento(E e) {
            this.elemento = e;
        }
    }

    /*
     * Observaciones sobre la implementación: cuando se crea un objeto
     * de esta clase, por omisión se encuentra al inicio de la lista.
     */
    private class IteradorLista implements ListIterator<E> {
        /* Indicador para conocer la posición del iterador dentro de
         * la lista. */
        int contador;
        /* Indica el elemento anterior del iterador. */
        private Nodo espalda;
        /* Indica el elemento siguiente del iterador. */
        private Nodo frente;
        /* Bandera para indicar si ha sido usado desde
         * su instanciación. */
        private boolean inicio;
        /* Bandera para indicar si es posible eliminar un elemento de
         * la estructura utilizando al iterador. */
        private boolean removible;
        /* Almacén del último elemento que fue llamado por next o
         * previous. */
        private Nodo ultimoLlamado;

        /**
         * Crea un iterador; se encuentra al inicio de la lista.
         */
        public IteradorLista() {
            this.inicio = true;
        }

        /**
         * Crea un iterador; se encuentra en la posición i de la lista.
         * @throws IndexOufOfBoundsException si se encuentra fuera
         * del rango permitido de la lista.
         */
        public IteradorLista(int i) {

            if(i == ListaDoblementeLigada.this.size()) {
                this.mueveFinal();
                contador = i;
                return;
            }

            this.frente = ListaDoblementeLigada.this.irIndice(i);
            this.espalda = this.frente.getAnterior();
            this.contador = i;
        }

        public void add(E e) {
            if(!this.inicio && contador == 0) this.inicia();
            ListaDoblementeLigada.this.add(this.nextIndex(),e);
            this.espalda = this.frente.getAnterior();
            this.contador++;
        }

        /* Se mueve al iterador al final de la lista. */
        private void mueveFinal() {
            this.frente = null;
            this.espalda = ListaDoblementeLigada.this.getRabo();
            this.inicio = false;
        }

        public boolean hasNext() {
            if(this.inicio) {
                this.inicia();
            }

            this.removible = false;
            return this.frente != null;
        }

        public boolean hasPrevious() {
            if(this.inicio) {
                this.inicia();
            }

            this.removible = false;
            return this.espalda != null;
        }

        /*
         * Se posicionan al objeto al inicio de la lista.
         */
        private void inicia() {
            this.frente = ListaDoblementeLigada.this.getCabeza();
            this.espalda = null;
            this.contador = 0;
            this.inicio = false;
        }

        public E next() {
            if(!this.hasNext()) throw new NoSuchElementException();

            ultimoLlamado = this.frente;
            E aux = this.frente.get();
            this.espalda = this.frente;
            this.frente = this.frente.getSiguiente();
            this.removible = true;
            contador++;
            return aux;
        }

        public int nextIndex() {
            return contador;
        }

        public E previous() {
            if(!this.hasPrevious()) throw new NoSuchElementException();

            ultimoLlamado = this.espalda;
            E aux = this.espalda.get();
            this.frente = this.espalda;
            this.espalda = this.espalda.getAnterior();
            this.removible = true;
            contador--;
            return aux;
        }

        public int previousIndex() {
            return contador - 1;
        }

        public void remove() {
            if(!removible) throw new IllegalStateException("No ha sido llamado next o previous");
            //Según yo, no es necesaria la siguiente verificación
            //se está uno antes de la cabeza o uno despues del rabo
            if(ultimoLlamado == null) return;
            ListaDoblementeLigada.this.suprimeNodo(ultimoLlamado);
        }

        public void set(E e) {
            if(!removible) throw new IllegalStateException("No ha sido llamado next o previous");
            ultimoLlamado.setElemento(e);
        }
    }
}
