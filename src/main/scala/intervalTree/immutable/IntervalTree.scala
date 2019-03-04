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

package intervalTree.immutable

import intervalTree.Interval

class IntervalTree[T >: Null](head: Node[T], intervals: List[Interval[T]]) {

  /**
    * @return the number of intervals in the tree.
    */
  def size: Int = intervals.size

  /**
    * @return true if the tree is empty, false otherwise
    */
  def isEmpty: Boolean = intervals.isEmpty

  /**
    * @return true if the tree is not empty, false otherwise
    */
  def nonEmpty: Boolean = intervals.nonEmpty

  /**
    * Runs a point query.
    *
    * @note Builds the tree in case it is out of sync.
    *
    * @param target a target point
    * @return a sequence of associated data for all intervals
    *         containing the target point
    */
  def get(target: Long): List[T] = getIntervals(target).map(_.data)

  /**
    * Runs a point query.
    *
    * @note Builds the tree in case it is out of sync.
    *
    * @param target a target point
    * @return all intervals containing the target point
    */
  def getIntervals(target: Long): List[Interval[T]] = head.query(target)

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
  def get(start: Long, end: Long): List[T] = getIntervals(start, end).map(_.data)

  /**
    * Runs an range query.
    *
    * @note Builds the tree in case it is out of sync.
    *
    * @param start the start of the interval
    * @param end the end of the interval
    * @return all intervals containing the target point
    */
  def getIntervals(start: Long, end: Long): List[Interval[T]] =
    head.query(new Interval[T](start, end, null))

  /**
    * Adds an interval to the interval tree.
    *
    * @note The tree would be out of sync after the addition.
    *
    * @param i an interval
    * @return an IntervalTree instance
    */
  def +(i: Interval[T]): IntervalTree[T] =
    new IntervalTree[T](Node[T](i :: intervals), i :: intervals)

  /**
    * Adds a sequence of intervals to the interval tree.
    *
    * @note The tree would be out of sync after the addition.
    *
    * @param seq a sequence of intervals
    * @return an IntervalTree instance
    */
  def ++(seq: Seq[Interval[T]]): IntervalTree[T] =
    new IntervalTree[T](Node[T](seq ++ intervals), seq.toList ::: intervals)

  /**
    * Adds an interval to the interval tree.
    *
    * @param start the start of the interval
    * @param end the end of the interval
    * @param data the associated data
    * @return an IntervalTree instance
    */
  def +(start: Long, end: Long, data: T): IntervalTree[T] = {
    val interval = new Interval[T](start, end, data)
    new IntervalTree[T](Node[T](interval :: intervals), interval :: intervals)
  }
}

object IntervalTree {

  /**
    * An empty IntervalTree.
    *
    * @tparam T the type of data being stored
    * @return an IntervalTree instance
    */
  def empty[T >: Null]: IntervalTree[T] = new IntervalTree[T](Node.empty, List.empty)

  /**
    * An empty IntervalTree.
    *
    * @tparam T the type of data being stored
    * @return an IntervalTree instance
    */
  def apply[T >: Null](): IntervalTree[T] = empty

  /**
    * An IntervalTree holding the given intervals.
    *
    * @param intervals a sequence of intervals
    * @tparam T the type of data being stored
    * @return an IntervalTree instance
    */
  def apply[T >: Null](intervals: List[Interval[T]]): IntervalTree[T] =
    new IntervalTree[T](Node(intervals), intervals)
}
