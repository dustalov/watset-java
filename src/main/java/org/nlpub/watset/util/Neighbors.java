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

package org.nlpub.watset.util;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.builder.GraphBuilder;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utilities for extracting neighborhood graphs and iterating over them.
 */
public interface Neighbors {
    /**
     * Create an iterator over the neighbors of the given node.
     *
     * @param graph the graph
     * @param node  the target node
     * @param <V>   the type of nodes in the graph
     * @param <E>   the type of edges in the graph
     * @return an iterator
     */
    static <V, E> Iterator<V> neighborIterator(Graph<V, E> graph, V node) {
        return graph.edgesOf(node).stream().
                map(e -> Graphs.getOppositeVertex(graph, e, node)).
                iterator();
    }

    /**
     * Extract the neighbors of the given node.
     *
     * @param graph the graph
     * @param node  the target node
     * @param <V>   the type of nodes in the graph
     * @param <E>   the type of edges in the graph
     * @return a set
     */
    static <V, E> Set<V> neighborSetOf(Graph<V, E> graph, V node) {
        return graph.edgesOf(node).stream().
                map(e -> Graphs.getOppositeVertex(graph, e, node)).
                collect(Collectors.toSet());
    }

    /**
     * Create an iterator over the neighbors of the given node.
     *
     * @param graph the graph
     * @param node  the target node
     * @param <V>   the type of nodes in the graph
     * @param <E>   the type of edges in the graph
     * @return a neighborhood of {@code node}
     */
    static <V, E> Graph<V, E> neighborhoodGraph(Graph<V, E> graph, V node) {
        final GraphBuilder<V, E, ? extends SimpleWeightedGraph<V, E>> builder = SimpleWeightedGraph.createBuilder(graph.getEdgeSupplier());

        final Set<V> neighborhood = neighborSetOf(graph, node);
        neighborhood.forEach(builder::addVertex);

        for (final V neighbor : neighborhood) {
            for (final E edge : graph.edgesOf(neighbor)) {
                final V source = graph.getEdgeSource(edge);
                final V target = graph.getEdgeTarget(edge);

                if (neighborhood.contains(source) && neighborhood.contains(target)) {
                    final double weight = graph.getEdgeWeight(edge);
                    builder.addEdge(source, target, edge, weight);
                }
            }
        }

        return builder.buildAsUnmodifiable();
    }
}
