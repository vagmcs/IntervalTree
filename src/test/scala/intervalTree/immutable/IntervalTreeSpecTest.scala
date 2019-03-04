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
import org.scalatest.{ FunSpec, Matchers }
import org.scalatest.prop.PropertyChecks

/**
  * Specification test for IntervalTree.
  *
  * @see [[intervalTree.immutable.IntervalTree]]
  */
final class IntervalTreeSpecTest extends FunSpec with Matchers with PropertyChecks {

  describe("A tree holding no intervals") {
    val tree = IntervalTree.empty[Char]

    it("should be empty") {
      tree.isEmpty shouldBe true
    }

    it("should return no results on a query") {
      tree.get(9) shouldEqual List.empty
      tree.getIntervals(9) shouldEqual List.empty
      tree.get(5, 9) shouldEqual List.empty
      tree.getIntervals(5, 9) shouldEqual List.empty
    }
  }

  describe("Tree holding a single interval [1, 5]") {
    val intervals = List(new Interval(1, 5, 'i'))
    val tree = IntervalTree(intervals)

    it("should not be empty") {
      tree.isEmpty shouldBe false
    }

    it("should contain all of its interval points") {
      (1 to 5).forall(tree.get(_).nonEmpty) shouldBe true
      (1 to 5).forall(tree.getIntervals(_).nonEmpty) shouldBe true
    }

    it("should return 1 result on query interval [4, 8]") {
      tree.get(4, 8).size shouldEqual 1
      tree.getIntervals(4, 8).size shouldEqual 1
    }
  }

  describe("Node holding many intervals") {
    val intervals = List(
      new Interval(1, 5, 'i'),
      new Interval(6, 9, 'g'),
      new Interval(10, 14, 'b')
    )

    val tree = IntervalTree(intervals)

    it("should not be empty") {
      tree.isEmpty shouldBe false
    }

    it("should contain all of its interval points") {
      (1 to 14).forall(tree.get(_).nonEmpty) shouldBe true
      (1 to 14).forall(tree.getIntervals(_).nonEmpty) shouldBe true
    }

    it("should return 2 results on query interval [4, 19]") {
      tree.get(6, 19).size shouldEqual 2
      tree.getIntervals(6, 19).size shouldEqual 2
    }
  }
}

