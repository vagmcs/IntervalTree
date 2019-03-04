/*
 * Copyright (C) 2018  Evangelos Michelioudakis
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package intervalTree;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.*;
import java.util.Map.Entry;

/**
 * Node represents an interval node and all its contained intervals.
 *
 * @param <T> the type of the data being stored
 */
public class Node<T> {

    private long center;
    private Optional<Node<T>> leftNode;
    private Optional<Node<T>> rightNode;
    private SortedMap<Interval<T>, List<Interval<T>>> intervals;

    /**
     * Creates an empty node.
     */
    public Node() {
        center = 0;
        leftNode = Optional.empty();
        rightNode = Optional.empty();
        intervals = new TreeMap<>();
    }

    /**
     * Creates a node given a list of intervals.
     *
     * @param list a list of intervals
     */
    public Node(List<Interval<T>> list) {

        intervals = new TreeMap<>();
        SortedSet<Long> endpoints = new TreeSet<>();

        for (Interval<T> interval : list) {
            endpoints.add(interval.start());
            endpoints.add(interval.end());
        }

        long median = getMedian(endpoints).orElse(0L);
        center = median;

        List<Interval<T>> left = new ArrayList<>();
        List<Interval<T>> right = new ArrayList<>();

        for (Interval<T> interval : list) {
            if (interval.end() < median)
                left.add(interval);
            else if (interval.start() > median)
                right.add(interval);
            else {
                List<Interval<T>> posting = intervals.computeIfAbsent(interval, i -> new ArrayList<>());
                posting.add(interval);
            }
        }

        if (left.size() > 0)
            leftNode = Optional.of(new Node<>(left));
        else
            leftNode = Optional.empty();

        if (right.size() > 0)
            rightNode = Optional.of(new Node<>(right));
        else
            rightNode = Optional.empty();
    }

    /**
     * @return the number of intervals on the node
     */
    public int size() { return intervals.size(); }

    /**
     * @return true if the node contains no data, false otherwise
     */
    public boolean isEmpty() {
        return intervals.size() == 0;
    }

    /**
     * @return the left children of the node
     */
    public Optional<Node<T>> leftNode() {
        return leftNode;
    }

    /**
     * @return the right children of the node
     */
    public Optional<Node<T>> rightNode() {
        return rightNode;
    }

    /**
     * Runs a point query on the node.
     *
     * @param target the query point
     * @return all intervals containing the given point
     */
    public List<Interval<T>> query(long target) {
        List<Interval<T>> result = new ArrayList<>();

        for (Entry<Interval<T>, List<Interval<T>>> entry : intervals.entrySet()) {
            if (entry.getKey().contains(target))
                result.addAll(entry.getValue());
            else if (entry.getKey().start() > target)
                break;
        }

        if (target < center && leftNode.isPresent())
            result.addAll(leftNode.get().query(target));

        else if (target > center && rightNode.isPresent())
            result.addAll(rightNode.get().query(target));

        return result;
    }

    /**
     * Runs an interval intersection query on the node.
     *
     * @param target the interval to intersect
     * @return all intervals containing time
     */
    public List<Interval<T>> query(Interval<T> target) {
        List<Interval<T>> result = new ArrayList<>();

        for (Entry<Interval<T>, List<Interval<T>>> entry : intervals.entrySet()) {
            if (entry.getKey().intersects(target))
                result.addAll(entry.getValue());
            else if (entry.getKey().start() > target.end())
                break;
        }

        if (target.start() < center && leftNode.isPresent())
            result.addAll(leftNode.get().query(target));

        if (target.end() > center && rightNode.isPresent())
            result.addAll(rightNode.get().query(target));

        return result;
    }

    /**
     * Compute the median.
     *
     * @param set a set of points
     * @return the median of the set (not interpolated)
     */
    private Optional<Long> getMedian(SortedSet<Long> set) {
        int i = 0;
        int middle = set.size() / 2;
        for (Long point : set) {
            if (i == middle)
                return Optional.of(point);
            i++;
        }
        return Optional.empty();
    }
}
