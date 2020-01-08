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
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/**
  * Specification test for Node.
  *
  * @see [[intervalTree.immutable.Node]]
  */
final class NodeSpecTest extends FunSpec with Matchers with ScalaCheckPropertyChecks {

  describe("A node holding no intervals") {
    val node = Node.empty[Char]

    it("should be empty") {
      node.isEmpty shouldBe true
    }

    it("should has no left or right children") {
      node.left shouldBe None
      node.right shouldBe None
    }

    it("should return no results on a query") {
      node.query(9) shouldEqual List.empty
      node.query(5, 6) shouldEqual List.empty
    }
  }

  describe("Node holding a single interval [1, 5]") {
    val intervals = Seq(new Interval(1, 5, 'i'))
    val node = Node(intervals)

    it("should not be empty") {
      node.isEmpty shouldBe false
    }

    it("should has no left or right children") {
      node.left shouldBe None
      node.right shouldBe None
    }

    it("should contain all of its interval points") {
      (1 to 5).forall(node.query(_).nonEmpty) shouldBe true
    }

    it("should return 1 result on query interval [4, 8]") {
      node.query(4, 8).size shouldEqual 1
    }
  }

  describe("Node holding many intervals") {
    val intervals = Seq(
      new Interval(1, 5, 'i'),
      new Interval(6, 9, 'g'),
      new Interval(10, 14, 'b')
    )

    val node = Node(intervals)

    it("should not be empty") {
      node.isEmpty shouldBe false
    }

    it("should has 1 left and 1 right child") {
      node.left.get.size shouldEqual 1
      node.right.get.size shouldEqual 1
    }

    it("should contain all of its interval points") {
      (1 to 14).forall(node.query(_).nonEmpty) shouldBe true
    }

    it("should return 2 results on query interval [4, 19]") {
      node.query(6, 19).size shouldEqual 2
    }
  }
}
