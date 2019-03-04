package intervalTree

import java.util
import java.util.Optional

import org.scalatest.prop.PropertyChecks
import org.scalatest.{FunSpec, Matchers}

/**
  * Specification test for Node.
  *
  * @see [[intervalTree.Node]]
  */
final class NodeSpecTest extends FunSpec with Matchers with PropertyChecks {

  val NO_RESULT = new util.ArrayList()

  describe("A node holding no intervals") {
    val node = new Node[Char]()

    it("should be empty") {
      node.isEmpty shouldBe true
    }

    it("should has no left or right children") {
      node.leftNode shouldBe Optional.empty
      node.rightNode shouldBe Optional.empty
    }

    it("should return no results on a query") {
      node.query(9) shouldEqual NO_RESULT
      node.query(new Interval(5, 6, 'q')) shouldEqual NO_RESULT
    }
  }

  describe("Node holding a single interval [1, 5]") {
    val intervals = new util.ArrayList[Interval[Char]]()
    intervals.add(new Interval(1, 5, 'i'))

    val node = new Node(intervals)

    it("should not be empty") {
      node.isEmpty shouldBe false
    }

    it("should has no left or right children") {
      node.leftNode shouldBe Optional.empty
      node.rightNode shouldBe Optional.empty
    }

    it("should contain all of its interval points") {
      (1 to 5).forall(!node.query(_).isEmpty) shouldBe true
    }

    it("should return 1 result on query interval [4, 8]") {
      node.query(new Interval(4, 8, 'b')).size shouldEqual 1
    }
  }

  describe("Node holding many intervals") {
    val intervals = new util.ArrayList[Interval[Char]]()
    intervals.add(new Interval(1, 5, 'i'))
    intervals.add(new Interval(6, 9, 'g'))
    intervals.add(new Interval(10, 14, 'b'))

    val node = new Node(intervals)

    it("should not be empty") {
      node.isEmpty shouldBe false
    }

    it("should has 1 left and 1 right child") {
      node.leftNode.get.size shouldEqual 1
      node.rightNode.get.size shouldEqual 1
    }

    it("should contain all of its interval points") {
      (1 to 14).forall(!node.query(_).isEmpty) shouldBe true
    }

    it("should return 2 results on query interval [4, 19]") {
      node.query(new Interval(6, 19, 'u')).size shouldEqual 2
    }
  }
}
