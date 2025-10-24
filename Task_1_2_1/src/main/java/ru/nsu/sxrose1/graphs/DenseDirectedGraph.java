package ru.nsu.sxrose1.graphs;

import java.util.*;
import java.util.function.Function;

public final class DenseDirectedGraph<T> implements DirectedGraph<T> {
  private T[] verticesMap;
  private boolean[][] adjMatrix;

  DenseDirectedGraph(T[] elements) {
    verticesMap = (T[]) Arrays.stream(elements).distinct().toArray();
    adjMatrix = new boolean[verticesMap.length][verticesMap.length];
  }

  /** {@inheritDoc} */
  @Override
  public Set<T> vertices() {
    return Set.of(verticesMap);
  }

  /** {@inheritDoc} */
  @Override
  public Set<DirectedGraphEdge<T>> edges() {
    Set<DirectedGraphEdge<T>> res = new HashSet<>(Set.of());
    for (int i = 0; i < adjMatrix.length; i++) {
      for (int j = 0; j < adjMatrix.length; j++) {
        if (adjMatrix[i][j]) res.add(new DirectedGraphEdge<>(verticesMap[i], verticesMap[j]));
      }
    }

    return res;
  }

  /** {@inheritDoc} */
  @Override
  public Optional<DirectedGraph<T>> addVertex(T elem) {
    for (var e : verticesMap) {
      if (elem.equals(e)) return Optional.empty();
    }

    for (int i = 0; i < adjMatrix.length; i++)
      adjMatrix[i] = Arrays.copyOf(adjMatrix[i], adjMatrix.length + 1);
    adjMatrix = Arrays.copyOf(adjMatrix, adjMatrix.length + 1);

    return Optional.of(this);
  }

  /** {@inheritDoc} */
  @Override
  public Optional<DirectedGraph<T>> deleteVertex(T elem) {
    int i = 0;
    for (; i < verticesMap.length; i++) {
      if (verticesMap[i].equals(elem)) break;
    }

    if (i == verticesMap.length) return Optional.empty();

    return Optional.empty();
  }

  @Override
  public Optional<DirectedGraphEdge<T>> createEdge(T from, T to) {
    return Optional.empty();
  }

  @Override
  public <R> DirectedGraph<R> map(Function<? super T, ? extends R> f) {
    return null;
  }

  @Override
  public Optional<Set<T>> neighbourhood(T elem) {
    return Optional.empty();
  }

  @Override
  public Optional<ArrayList<T>> topsort() {
    return Optional.empty();
  }
}
