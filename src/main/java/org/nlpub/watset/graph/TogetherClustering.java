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

import java.util.Collection;
import java.util.Collections;

import static java.util.Objects.requireNonNull;

/**
 * A trivial clustering algorithm that puts every node together in a single large cluster.
 *
 * @param <V> the type of nodes in the graph
 * @param <E> the type of edges in the graph
 */
public class TogetherClustering<V, E> implements Clustering<V> {
    private final Graph<V, E> graph;
    private Collection<Collection<V>> clusters;

    /**
     * Set up the trivial clustering algorithm.
     *
     * @param graph the graph
     */
    public TogetherClustering(Graph<V, E> graph) {
        this.graph = requireNonNull(graph);
    }

    @Override
    public void fit() {
        clusters = Collections.singletonList(graph.vertexSet());
    }

    @Override
    public Collection<Collection<V>> getClusters() {
        return requireNonNull(clusters, "call fit() first");
    }
}
