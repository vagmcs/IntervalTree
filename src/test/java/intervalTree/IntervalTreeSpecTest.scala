package intervalTree

import java.util
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.prop.PropertyChecks

/**
  * Specification test for IntervalTree.
  *
  * @see [[intervalTree.IntervalTree]]
  */
final class IntervalTreeSpecTest extends FunSpec with Matchers with PropertyChecks {

  val NO_RESULT = new util.ArrayList()

  describe("A tree holding no intervals") {
    val tree = new IntervalTree[Char]()

    it("should be empty") {
      tree.isEmpty shouldBe true
    }

    it("should return no results on a query") {
      tree.get(9) shouldEqual NO_RESULT
      tree.getIntervals(9) shouldEqual NO_RESULT
      tree.get(5, 9) shouldEqual NO_RESULT
      tree.getIntervals(5, 9) shouldEqual NO_RESULT
    }
  }

  describe("Tree holding a single interval [1, 5]") {
    val intervals = new util.ArrayList[Interval[Char]]()
    intervals.add(new Interval(1, 5, 'i'))

    val tree = new IntervalTree(intervals)

    it("should not be empty") {
      tree.isEmpty shouldBe false
    }

    it("should contain all of its interval points") {
      (1 to 5).forall(!tree.get(_).isEmpty) shouldBe true
      (1 to 5).forall(!tree.getIntervals(_).isEmpty) shouldBe true
    }

    it("should return 1 result on query interval [4, 8]") {
      tree.get(4, 8).size shouldEqual 1
      tree.getIntervals(4, 8).size shouldEqual 1
    }
  }

  describe("Node holding many intervals") {
    val intervals = new util.ArrayList[Interval[Char]]()
    intervals.add(new Interval(1, 5, 'i'))
    intervals.add(new Interval(6, 9, 'g'))
    intervals.add(new Interval(10, 14, 'b'))

    val tree = new IntervalTree(intervals)

    it("should not be empty") {
      tree.isEmpty shouldBe false
    }

    it("should contain all of its interval points") {
      (1 to 14).forall(!tree.get(_).isEmpty) shouldBe true
      (1 to 14).forall(!tree.getIntervals(_).isEmpty) shouldBe true
    }

    it("should return 2 results on query interval [4, 19]") {
      tree.get(6, 19).size shouldEqual 2
      tree.getIntervals(6, 19).size shouldEqual 2
    }
  }
}
