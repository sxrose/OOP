package ru.nsu.sxrose1.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import ru.nsu.sxrose1.utils.PrimesUtils;

public class HashMap<K, E> implements Map<K, E> {
  static final int MIN_CAPACITY = 31;
  static final double MIN_LOAD_FACTOR = 0.4;

  private enum SlotTag {
    OCCUPIED,
    EMPTY,
    DELETED
  }

  private record Slot(SlotTag tag, Object key, Object element) {}

  private Slot[] data;
  private int load = 0;
  private int deleted = 0;

  private int capacity() {
    return data.length;
  }

  private double loadFactor() {
    return ((double) load - deleted) / capacity();
  }

  private double effectiveLoadFactor() {
    return ((double) load) / capacity();
  }

  private int index(Object key) {
    int code = key.hashCode();
    return (Integer.signum(code) * code) % capacity();
  }

  private int probe(int ind) {
    while (true) {
      if (Objects.requireNonNull(data[ind].tag) == SlotTag.EMPTY) {
        return ind;
      }
      ind = nextProbe(ind);
    }
  }

  private int nextProbe(int ind) {
    return (ind + 1) % capacity();
  }

  private Optional<Integer> findInd(Object key) {
    assert (Objects.nonNull(key));
    int ind = index(key);
    while (true) {
      switch (data[ind].tag) {
        case EMPTY -> {
          return Optional.empty();
        }
        case OCCUPIED -> {
          if (Objects.requireNonNull(data[ind].key).equals(key)) return Optional.of(ind);
        }
      }

      ind = nextProbe(ind);
    }
  }

  private static int updatedCapacity(int newCap) {
    return Optional.of(newCap)
        .filter(PrimesUtils::exceed1000Prime)
        .flatMap(PrimesUtils::getNextPrimeUnder1000)
        .orElse(newCap);
  }

  private Map<K, E> insertNoResize(Object key, Object element) {
    assert (load < capacity());
    load += 1;
    data[probe(index(key))] = new Slot(SlotTag.OCCUPIED, key, element);
    return this;
  }

  private void rehash(int newCap) {
    Slot[] oldData = data;
    data = new Slot[newCap];
    for (int i = 0; i < newCap; i++) data[i] = new Slot(SlotTag.EMPTY, null, null);
    load = 0;
    deleted = 0;

    for (var s : oldData) {
      if (s.tag == SlotTag.OCCUPIED)
        insertNoResize(Objects.requireNonNull(s.key), Objects.requireNonNull(s.element));
    }
  }

  private Optional<Map<K, E>> deleteNoResize(Object key) {
    return findInd(key)
        .map(
            (ind) -> {
              data[ind] = new Slot(SlotTag.DELETED, null, null);
              deleted += 1;
              return this;
            });
  }

  /** Creates empty HashMap. */
  HashMap() {
    this(MIN_CAPACITY);
  }

  /**
   * Creates empty HashMap with provided initial capacity.
   *
   * @param capacity initial capacity.
   */
  HashMap(int capacity) {
    data = new Slot[capacity];
    for (int i = 0; i < capacity; i++) data[i] = new Slot(SlotTag.EMPTY, null, null);
  }

  /** {@inheritDoc} */
  @Override
  public Map<K, E> insert(K key, E element) {
    if (effectiveLoadFactor() >= 1.0) rehash(updatedCapacity(2 * capacity() + 1));
    return insertNoResize(key, element);
  }

  /** {@inheritDoc} */
  @Override
  public Optional<E> find(K key) {
    // we know that we put only correct slots
    @SuppressWarnings("unchecked")
    var res = findInd(key).map((ind) -> (E) Objects.requireNonNull(data[ind].element));
    return res;
  }

  /** {@inheritDoc} */
  @Override
  public Optional<Map<K, E>> delete(K key) {
    if (capacity() > MIN_CAPACITY && loadFactor() <= MIN_LOAD_FACTOR)
      rehash(updatedCapacity(Integer.max(MIN_CAPACITY, (int) (capacity() * MIN_LOAD_FACTOR))));
    return deleteNoResize(key);
  }

  /** {@inheritDoc} */
  @Override
  public Set<Entry<K, E>> entries() {
    return Arrays.stream(data)
        .filter((s) -> s.tag == SlotTag.OCCUPIED)
        .map(
            (s) -> {
              @SuppressWarnings("unchecked")
              K key = (K) Objects.requireNonNull(s.key);
              @SuppressWarnings("unchecked")
              E elem = (E) Objects.requireNonNull(s.element);

              return new Map.Entry<>(key, elem);
            })
        .collect(Collectors.toSet());
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hashCode(entries());
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object other) {
    return other instanceof Map && ((Map<?, ?>) other).entries().equals(this.entries());
  }
}
