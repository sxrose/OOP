package ru.nsu.sxrose1.maps;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public interface Map<K, E> {
  record Entry<K, E>(K key, E element) {}

  /**
   * Creates new empty map.
   *
   * @return new map.
   * @param <K> keys type.
   * @param <V> elements type.
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
   * @return set of map entries.
   */
  Set<Entry<K, E>> entries();

  /**
   * Functor mapping.
   *
   * @param f transformation.
   * @return new map.
   * @param <R> type of elements in result map.
   */
  default <R> Map<K, R> map(Function<E, R> f) {
    Map<K, R> m = empty();

    for (var e : entries()) {
      m.insert(e.key, f.apply(e.element));
    }

    return m;
  }

  /**
   * Monad join of maps.
   *
   * @param maps maps map to join.
   * @return new map with keys and elements from maps.
   */
  static <K, E> Map<K, E> join(Map<K, Map<K, E>> maps) {
    Map<K, E> m = empty();

    for (var n : maps.entries()) {
      for (var i : n.element.entries()) {
        m.insert(i.key, i.element);
      }
    }

    return m;
  }

  /**
   * Monadic bind.
   *
   * @param f transformation.
   * @return new map.
   * @param <R> type of elements in result map.
   */
  default <R> Map<K, R> flatMap(Function<E, Map<K, R>> f) {
    return join(this.map(f));
  }
}
