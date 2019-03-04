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

/**
 * Interval represents a time interval along some associated data.
 *
 * @param <T> the type of data being stored
 */
public class Interval<T> implements Comparable<Interval<T>> {

    private long start;
    private long end;
    private T data;

    /**
     * Creates an Interval.
     *
     * @param start a start point
     * @param end an end point
     * @param data associated data
     */
    public Interval(long start, long end, T data) throws IllegalArgumentException {
        if (start < 0 || end < 0)
            throw new IllegalArgumentException("Interval cannot be negative.");
        if (start > end)
            throw new IllegalArgumentException("Interval start should be less than end.");

        this.start = start;
        this.end = end;
        this.data = data;
    }

    /**
     * @return the start of the interval
     */
    public long start() {
        return start;
    }

    /**
     * @return the end of the interval
     */
    public long end() {
        return end;
    }

    /**
     * @return the associated data
     */
    public T data() {
        return data;
    }

    /**
     * @return the length of the interval
     */
    public long length() {
        return end - start;
    }

    /**
     * Checks if the interval contains the given point.
     *
     * @param p a point to check
     * @return true if the interval contains the given point (inclusive)
     */
    public boolean contains(long p) {
        return start <= p && p <= end;
    }

    /**
     * Checks if the interval contains another interval.
     *
     * @param i an interval
     * @return true if the interval contains all points
     *         of the given interval.
     */
    public boolean contains(Interval<?> i) {
        return start <= i.start() && i.end() <= end;
    }

    /**
     * Checks if the interval intersects (or touches) another interval.
     *
     * @param i an interval
     * @return true if the interval intersects the given interval.
     */
    public boolean intersects(Interval<?> i) {
        return start <= i.end() && end >= i.start();
    }

    public boolean intersects(long from, long to) {
        return start <= to && end >= from;
    }

    /**
     * Computes the distance to another interval.
     *
     * @param i an interval
     * @return the gap between the intervals, or 0 in case they intersect.
     */
    public long distance(Interval<?> i) {
        if (this.intersects(i)) return 0;
        else if (start < i.start()) return i.start() - end;
        else return start - i.end();
    }

    /**
     * Compares the interval to another interval.
     *
     * @param i an interval
     * @return -1 if the start point is less than the one of the given interval,
     *         or 1 if it is greater. In case starting points are the same, ending
     *         points are compared in the same way. In case they are also the same,
     *         0 is returned.
     */
    public int compareTo(Interval<T> i) {
        if (start < i.start())
            return -1;
        else if (start > i.start())
            return 1;
        else return Long.compare(end, i.end());
    }

    /**
     * Checking equality.
     *
     * @param obj an object
     * @return true if the given object is an interval having
     *         identical start, end points and data, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Interval) {
            Interval i = (Interval) obj;
            return i.start() == start && i.end() == end && i.data() == data;
        }
        else return false;
    }
}