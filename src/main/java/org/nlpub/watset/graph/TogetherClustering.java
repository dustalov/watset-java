/*
 * Copyright 2018 Dmitry Ustalov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.nlpub.watset.graph;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ClusteringAlgorithm;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * A trivial clustering algorithm that puts every node together in a single large cluster.
 *
 * @param <V> the type of nodes in the graph
 * @param <E> the type of edges in the graph
 */
public class TogetherClustering<V, E> implements ClusteringAlgorithm<V> {
    /**
     * Builder for {@link TogetherClustering}.
     *
     * @param <V> the type of nodes in the graph
     * @param <E> the type of edges in the graph
     */
    @SuppressWarnings({"unused", "UnusedReturnValue"})
    public static class Builder<V, E> implements ClusteringBuilder<V, E, TogetherClustering<V, E>> {
        @Override
        public TogetherClustering<V, E> build(Graph<V, E> graph) {
            return new TogetherClustering<>(graph);
        }

        @Override
        public Function<Graph<V, E>, ClusteringAlgorithm<V>> provider() {
            return TogetherClustering.provider();
        }
    }

    /**
     * A factory function that sets up the algorithm for the given graph.
     *
     * @param <V> the type of nodes in the graph
     * @param <E> the type of edges in the graph
     * @return a factory function that sets up the algorithm for the given graph
     */
    public static <V, E> Function<Graph<V, E>, ClusteringAlgorithm<V>> provider() {
        return TogetherClustering::new;
    }

    private final Graph<V, E> graph;
    private List<Set<V>> clusters;

    /**
     * Set up the trivial clustering algorithm that puts every node together in a single large cluster.
     *
     * @param graph the graph
     */
    public TogetherClustering(Graph<V, E> graph) {
        this.graph = requireNonNull(graph);
    }

    @Override
    public Clustering<V> getClustering() {
        clusters = Collections.singletonList(graph.vertexSet());
        return new ClusteringImpl<>(clusters);
    }
}
