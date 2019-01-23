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

public interface Neighbors {
    static <V, E> Iterator<V> neighborIterator(Graph<V, E> graph, V node) {
        return graph.edgesOf(node).stream().
                map(e -> Graphs.getOppositeVertex(graph, e, node)).
                iterator();
    }

    static <V, E> Set<V> neighborSetOf(Graph<V, E> graph, V node) {
        return graph.edgesOf(node).stream().
                map(e -> Graphs.getOppositeVertex(graph, e, node)).
                collect(Collectors.toSet());
    }

    static <V, E> Graph<V, E> neighborhoodGraph(Graph<V, E> graph, V node) {
        final GraphBuilder<V, E, ? extends SimpleWeightedGraph<V, E>> builder = SimpleWeightedGraph.createBuilder(graph.getEdgeSupplier());

        final Set<V> neighborhood = neighborSetOf(graph, node);

        for (final V neighbor : neighborhood) {
            builder.addVertex(neighbor);

            for (final E edge : graph.edgesOf(neighbor)) {
                final V opposite = Graphs.getOppositeVertex(graph, edge, neighbor);

                if (neighborhood.contains(opposite)) {
                    builder.addVertex(opposite);
                    builder.addEdge(neighbor, opposite, edge, graph.getEdgeWeight(edge));
                }
            }
        }

        return builder.buildAsUnmodifiable();
    }
}
