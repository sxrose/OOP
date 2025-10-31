package ru.nsu.sxrose1.maps;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Map from K type keys to E type elements.
 *
 * @param <K> key type.
 * @param <E> element type.
 */
public interface Map<K, E> extends Iterable<Map.Entry<K, E>> {

    /**
     * Entry of the map, contains key and element.
     *
     * @param key key.
     * @param element element.
     * @param <K> key type.
     * @param <E> element type.
     */
    record Entry<K, E>(K key, E element) {
        /**
         * String representation of map entry.
         *
         * @return string representation of map entry.
         */
        @Override
        public String toString() {
            return String.format("%s -> %s", key.toString(), element.toString());
        }
    }

    /**
     * Creates new empty map.
     *
     * @param <K> keys type.
     * @param <V> elements type.
     * @return new map.
     */
    static <K, V> Map<K, V> empty() {
        return new HashMap<>();
    }

    /**
     * Inserts new element with key.
     *
     * @param key key.
     * @param element element.
     * @return this.
     */
    Map<K, E> insert(K key, E element);

    /**
     * Finds element under the key.
     *
     * @param key key.
     * @return element if it exists, empty otherwise.
     */
    Optional<E> find(K key);

    /**
     * Delete element by key.
     *
     * @param key key.
     * @return this if key exists, empty otherwise.
     */
    Optional<Map<K, E>> delete(K key);

    /**
     * Delete element by key. Throws if key doesn't exist.
     *
     * @param key key.
     * @return this.
     * @throws NoSuchElementException if key is missing from map.
     */
    default Map<K, E> deleteExcept(K key) throws NoSuchElementException {
        return delete(key).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Map entries.
     *
     * @return set of map entries.
     */
    Set<Entry<K, E>> entries();

    /**
     * Checks if map is empty.
     *
     * @return true if map is empty.
     */
    default boolean isEmpty() {
        return entries().isEmpty();
    }

    /**
     * Functor mapping.
     *
     * @param f transformation.
     * @return new map.
     * @param <R> type of elements in result map.
     */
    default <R> Map<K, R> map(Function<? super E, ? extends R> f) {
        Map<K, R> m = empty();

        for (var e : entries()) {
            m.insert(e.key, f.apply(e.element));
        }

        return m;
    }
}
