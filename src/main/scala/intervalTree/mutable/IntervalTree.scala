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

package intervalTree.mutable

import collection.JavaConversions._
import intervalTree.{ Interval, IntervalTree => JTree }

/**
  * Interval Tree is essentially a map of intervals to data. It
  * can be queried for all data associated to a particular
  * interval or point.
  *
  * @tparam T the type of data being stored
  */
class IntervalTree[T] private (underlying: JTree[T]) {

  /**
    * @return the number of intervals in the tree.
    */
  def size: Int = underlying.size

  /**
    * @return true if the tree is empty, false otherwise
    */
  def isEmpty: Boolean = underlying.isEmpty

  /**
    * @return true if the tree is not empty, false otherwise
    */
  def nonEmpty: Boolean = !isEmpty

  /**
    * Runs a point query.
    *
    * @note Builds the tree in case it is out of sync.
    *
    * @param target a target point
    * @return a sequence of associated data for all intervals
    *         containing the target point
    */
  def get(target: Long): Seq[T] = underlying.get(target)

  /**
    * Runs a point query.
    *
    * @note Builds the tree in case it is out of sync.
    *
    * @param target a target point
    * @return all intervals containing the target point
    */
  def getIntervals(target: Long): Seq[Interval[T]] = underlying.getIntervals(target)

  /**
    * Runs an range query.
    *
    * @note Builds the tree in case it is out of sync.
    *
    * @param start the start of the interval
    * @param end the end of the interval
    * @return a sequence of associated data for all intervals
    *         containing the target point
    */
  def get(start: Long, end: Long): Seq[T] = underlying.get(start, end)

  /**
    * Runs an range query.
    *
    * @note Builds the tree in case it is out of sync.
    *
    * @param start the start of the interval
    * @param end the end of the interval
    * @return all intervals containing the target point
    */
  def getIntervals(start: Long, end: Long): Seq[Interval[T]] = underlying.getIntervals(start, end)

  /**
    * Adds an interval to the interval tree.
    *
    * @note The tree would be out of sync after the addition.
    *
    * @param i an interval
    */
  def +=(i: Interval[T]): Unit = underlying.add(i)

  /**
    * Adds a sequence of intervals to the interval tree.
    *
    * @note The tree would be out of sync after the addition.
    *
    * @param seq a sequence of intervals
    */
  def ++=(seq: Seq[Interval[T]]): Unit = underlying.addAll(seq)

  /**
    * Adds an interval to the interval tree.
    *
    * @note The tree would be out of sync after the addition.
    *
    * @param start the start of the interval
    * @param end the end of the interval
    * @param data the associated data
    */
  def +=(start: Long, end: Long, data: T): Unit = underlying.add(start, end, data)
}

object IntervalTree {

  /**
    * An empty IntervalTree.
    *
    * @tparam T the type of data being stored
    * @return an IntervalTree instance
    */
  def empty[T]: IntervalTree[T] = new IntervalTree[T](new JTree())

  /**
    * An empty IntervalTree.
    *
    * @tparam T the type of data being stored
    * @return an IntervalTree instance
    */
  def apply[T](): IntervalTree[T] = empty

  /**
    * An IntervalTree holding the given intervals.
    *
    * @param intervals a sequence of intervals
    * @tparam T the type of data being stored
    * @return an IntervalTree instance
    */
  def apply[T](intervals: Seq[Interval[T]]): IntervalTree[T] =
    new IntervalTree[T](new JTree[T](intervals))
}
