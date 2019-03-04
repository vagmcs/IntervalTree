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

import java.util.ArrayList;
import java.util.List;

/**
 * Interval Tree is essentially a map of intervals to data. It
 * can be queried for all data associated to a particular
 * interval or point.
 *
 * @param <T> the type of the data being stored
 */
public class IntervalTree<T> {

    private Node<T> head;
    private List<Interval<T>> intervals;
    private boolean inSync;

    /**
     * Create an empty interval tree.
     */
    public IntervalTree() {
        this.head = new Node<>();
        this.intervals = new ArrayList<>();
        this.inSync = true;
    }

    /**
     * Create an interval tree from a given list of intervals.
     *
     * @param intervals a list of intervals
     */
    public IntervalTree(List<Interval<T>> intervals) {
        this.head = new Node<>(intervals);
        this.intervals = new ArrayList<>();
        this.intervals.addAll(intervals);
        this.inSync = true;
    }

    /**
     * @return the number of intervals in the tree.
     */
    public int size() {
        return intervals.size();
    }

    /**
     * @return true if the tree contains no data, false otherwise
     */
    public boolean isEmpty() { return intervals.size() == 0; }

    /**
     * Runs a point query. Builds the tree in case it is out of sync.
     *
     * @param target a target point
     * @return the associated data for all intervals containing the target point
     */
    public List<T> get(long target) {
        List<Interval<T>> intervals = getIntervals(target);
        List<T> result = new ArrayList<>();
        for (Interval<T> interval : intervals)
            result.add(interval.data());
        return result;
    }

    /**
     * Runs a point query. Builds the tree in case it is out of sync.
     *
     * @param target the target point
     * @return all intervals containing the target point
     */
    public List<Interval<T>> getIntervals(long target) {
        build();
        return head.query(target);
    }

    /**
     * Runs an interval query. Builds the tree in case it is out of sync.
     *
     * @param start the start of the interval
     * @param end the end of the interval
     * @return the associated data for all intervals that intersect target
     */
    public List<T> get(long start, long end) {
        List<Interval<T>> intervals = getIntervals(start, end);
        List<T> result = new ArrayList<>();
        for (Interval<T> interval : intervals)
            result.add(interval.data());
        return result;
    }

    /**
     * Runs an interval query. Builds the tree in case it is out of sync.
     *
     * @param start the start of the interval
     * @param end the end of the interval
     * @return all intervals that intersect the target
     */
    public List<Interval<T>> getIntervals(long start, long end) {
        build();
        return head.query(new Interval<>(start, end, null));
    }

    /**
     * Add an interval to the interval tree.
     *
     * @implNote The tree would be out of sync after the addition.
     *
     * @param i an interval
     */
    public void add(Interval<T> i) {
        intervals.add(i);
        inSync = false;
    }

    /**
     * Add a list of intervals to the interval tree.
     *
     * @implNote The tree would be out of sync after the additions.
     *
     * @param list a list of intervals.
     */
    public void addAll(List<Interval<T>> list) {
        for (Interval<T> i : list)
            add(i);
    }

    /**
     * Add an interval to the tree.
     *
     * @implNote The tree would be out of sync after the addition.
     *
     * @param start the start of the interval
     * @param end the end of the interval
     * @param data the associated data
     */
    public void add(long start, long end, T data) {
        intervals.add(new Interval<>(start, end, data));
        inSync = false;
    }

    /**
     * Build the interval tree.
     */
    private void build() {
        if(!inSync) {
            head = new Node<>(intervals);
            inSync = true;
        }
    }
}