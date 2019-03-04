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

package intervalTree

import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{ FunSpec, Matchers }

/**
  * Specification test for Interval.
  *
  * @see [[intervalTree.Interval]]
  */
final class IntervalSpecTest extends FunSpec with Matchers with PropertyChecks {

  type Data = Option[_]

  val LIMIT: Long = 10000000

  describe("Interval [1, 5] having no data") {
    val i = new Interval(1, 5, None)

    it("should start at 1") {
      i.start() shouldEqual 1
    }

    it("should end at 5") {
      i.end() shouldEqual 5
    }

    it("should have no data") {
      i.data() shouldEqual None
    }

    it("should have length 4") {
      i.length() shouldEqual 4
    }

    it("should equal to itself") {
      i shouldEqual i
    }

    it("should have 0 distance to itself") {
      i.distance(i) shouldEqual 0
    }

    it("should have distance 10 to [15, 40]") {
      i.distance(new Interval(15, 40, 0.2)) shouldEqual 10
    }

    it("should contain point '3'") {
      i.contains(3) shouldBe true
    }

    it("should not contain point '7'") {
      i.contains(7) shouldBe false
    }

    it("should contain [2, 4]") {
      i.contains(new Interval(2, 4, None)) shouldBe true
    }

    it("should not contain [3, 7]") {
      i.contains(new Interval(3, 7, None)) shouldBe false
    }

    it("should intersect [3, 7]") {
      i.intersects(new Interval(3, 7, None)) shouldBe true
    }
  }

  describe("Interval [5, 1]") {

    it("should throw an 'IllegalArgumentException'") {
      assertThrows[IllegalArgumentException] {
        new Interval(5, 1, None)
      }
    }
  }

  describe("Interval [-1, 8]") {

    it("should throw an 'IllegalArgumentException'") {
      assertThrows[IllegalArgumentException] {
        new Interval(-1, 8, None)
      }
    }
  }

  describe("Interval [9, -1]") {

    it("should throw an 'IllegalArgumentException'") {
      assertThrows[IllegalArgumentException] {
        new Interval(9, -1, None)
      }
    }
  }

  describe("An interval") {

    it("should contain every point from 'start' to 'end'.") {

      val generator = for {
        x <- Gen.choose(0, LIMIT - 1)
        y <- Gen.choose(x + 1, LIMIT)
      } yield (x, y)

      forAll(generator) {
        case (start: Long, end: Long) =>

          whenever(start < end) {
            val i = new Interval(start, end, None)
            i.start() to i.end() foreach { p =>
              i.contains(p) shouldBe true
            }
          }
      }
    }

    it("should have sub-intervals") {

      val generator = for {
        a <- Gen.choose(0, LIMIT - 1)
        b <- Gen.choose(a + 1, LIMIT)
        c <- Gen.choose(a, b - 1)
        d <- Gen.choose(c + 1, b)
      } yield (a, b, c, d)

      forAll(generator) {
        case (start: Long, end: Long, p: Long, q: Long) =>

          whenever(start < end && start <= p && p < q && q <= end) {
            val a = new Interval(start, end, None)
            val b = new Interval(p, q, None)
            a.contains(b) shouldBe true
          }
      }
    }

    it("should intersect other intervals") {

      val generator = for {
        a <- Gen.choose(0, LIMIT - 1)
        b <- Gen.choose(a + 1, LIMIT)
        c <- Gen.choose(0, b - 1)
        d <- if (c < a) Gen.choose(a, LIMIT) else Gen.choose(c + 1, LIMIT)
      } yield (a, b, c, d)

      forAll(generator) {
        case (start: Long, end: Long, p: Long, q: Long) =>

          whenever(start >= 0 && p >= 0 && start < end && p < q && p <= end) {
            val a = new Interval(start, end, None)
            val b = new Interval(p, q, None)
            a.intersects(b) shouldBe true
          }
      }
    }
  }
}
