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

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.isNull;

/**
 * Utilities for searching arguments of the maxima of the function.
 */
public interface Maximizer {
    /**
     * A predicate that is always true.
     *
     * @param <T> the type
     * @return the absolute truth
     */
    static <T> Predicate<T> alwaysTrue() {
        return (o) -> true;
    }

    /**
     * A predicate that is always false.
     *
     * @param <T> the type
     * @return the absolute non-truth
     */
    static <T> Predicate<T> alwaysFalse() {
        return (o) -> false;
    }

    /**
     * Find the first argument of the maximum (argmax) of the function.
     *
     * @param it      the finite iterator
     * @param checker the predicate that evaluates the suitability of the argument
     * @param scorer  the scoring function
     * @param <V>     the argument type
     * @param <S>     the score type
     * @return a non-empty optional that contains the first found argmax, otherwise the empty one
     */
    static <V, S extends Comparable<S>> Optional<V> argmax(Iterator<V> it, Predicate<V> checker, Function<V, S> scorer) {
        V result = null;
        S score = null;

        while (it.hasNext()) {
            final V current = it.next();

            if (!checker.test(current)) continue;

            final S currentScore = scorer.apply(current);

            if (isNull(score) || (currentScore.compareTo(score) > 0)) {
                result = current;
                score = currentScore;
            }
        }

        return Optional.ofNullable(result);
    }

    /**
     * Find the first argument of the maximum (argmax) of the function.
     *
     * @param it     the finite iterator
     * @param scorer the scoring function
     * @param <V>    the argument type
     * @param <S>    the score type
     * @return a non-empty optional that contains the first found argmax, otherwise the empty one
     * @see #argmax(Iterator, Predicate, Function)
     */
    static <V, S extends Comparable<S>> Optional<V> argmax(Iterator<V> it, Function<V, S> scorer) {
        return argmax(it, alwaysTrue(), scorer);
    }

    /**
     * Find the arguments of the maxima (argmax) of the function and randomly choose any of them.
     *
     * @param it     the finite iterator
     * @param scorer the scoring function
     * @param random the random number generator
     * @param <V>    the argument type
     * @param <S>    the score type
     * @return a non-empty optional that contains the randomly chosen argmax, otherwise the empty one
     */
    static <V, S extends Comparable<S>> Optional<V> argmaxRandom(Iterator<V> it, Function<V, S> scorer, Random random) {
        final List<V> results = new LinkedList<>();
        S score = null;

        while (it.hasNext()) {
            final V current = it.next();

            final S currentScore = scorer.apply(current);

            final int compare = isNull(score) ? 1 : currentScore.compareTo(score);

            if (compare > 0) {
                results.clear();
                score = currentScore;
            }

            if (compare >= 0) {
                results.add(current);
            }
        }

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(random.nextInt(results.size())));
    }
}
