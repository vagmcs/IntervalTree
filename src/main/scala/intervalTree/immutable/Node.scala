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

import scala.collection.SortedMap
import scala.collection.immutable.TreeSet
import scala.collection.mutable.ListBuffer

class Node[T] private (
    center: Long,
    val left: Option[Node[T]],
    val right: Option[Node[T]],
    intervals: SortedMap[Interval[T], List[Interval[T]]]) {

  /**
    * @return the number of intervals on the node
    */
  def size: Int = intervals.size

  /**
    * @return true if the node contains no data, false otherwise
    */
  def isEmpty: Boolean = intervals.isEmpty

  /**
    * Runs a point query on the node.
    *
    * @param target the query point
    * @return all intervals containing the given point
    */
  def query(target: Long): List[Interval[T]] = {
    var result = ListBuffer.empty[Interval[T]]

    intervals.takeWhile {
      case (key, list) =>
        if (key.contains(target)) list.foreach(result += _)
        key.start <= target
    }

    if (target < center && left.isDefined)
      result ++= left.get.query(target)
    if (target > center && right.isDefined)
      result ++= right.get.query(target)

    result.toList
  }

  /**
    * Runs an interval intersection query on the node.
    *
    * @param target the interval to intersect
    * @return all intervals containing time
    */
  def query(from: Long, to: Long): List[Interval[T]] = {
    val result = ListBuffer.empty[Interval[T]]

    intervals.takeWhile {
      case (key, list) =>
        if (key.intersects(from, to)) list.foreach(result += _)
        key.start <= to
    }

    if (from < center && left.isDefined)
      result ++= left.get.query(from, to)
    if (to > center && right.isDefined)
      result ++= right.get.query(from, to)

    result.toList
  }
}

object Node {

  private def medianOf(set: TreeSet[Long]): Option[Long] = {
    val mid = set.size / 2
    set.zipWithIndex.find(_._2 == mid) match {
      case None => None
      case Some((point, _)) => Some(point)
    }
  }

  /**
    * An empty node.
    *
    * @tparam T the type of data being stored
    * @return an Node instance
    */
  def empty[T]: Node[T] = new Node(0, None, None, SortedMap.empty)

  /**
    * An empty node.
    *
    * @tparam T the type of data being stored
    * @return an Node instance
    */
  def apply[T](): Node[T] = empty

  /**
    * An node holding the given intervals.
    *
    * @param intervals a sequence of intervals
    * @tparam T the type of data being stored
    * @return an Node instance
    */
  def apply[T](intervals: Seq[Interval[T]]): Node[T] = {

    var intervalsMap = SortedMap.empty[Interval[T], List[Interval[T]]]
    var endpoints = TreeSet.empty[Long]

    intervals.foreach { interval =>
      endpoints += interval.start()
      endpoints += interval.end()
    }

    val median = medianOf(endpoints).get

    var leftNodes = List.empty[Interval[T]]
    var rightNodes = List.empty[Interval[T]]

    intervals.foreach { interval =>
      if (interval.end() < median) leftNodes ::= interval
      else if (interval.start() > median) rightNodes ::= interval
      else intervalsMap = intervalsMap.updated(interval, interval :: intervalsMap.getOrElse(interval, List.empty))
    }

    if (leftNodes.nonEmpty && rightNodes.nonEmpty)
      new Node(median, Some(Node(leftNodes)), Some(Node(rightNodes)), intervalsMap)
    else if (leftNodes.nonEmpty)
      new Node(median, Some(Node(leftNodes)), None, intervalsMap)
    else if (rightNodes.nonEmpty)
      new Node(median, None, Some(Node(rightNodes)), intervalsMap)
    else
      new Node(median, None, None, intervalsMap)
  }
}
