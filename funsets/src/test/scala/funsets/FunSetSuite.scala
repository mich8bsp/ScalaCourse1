package funsets

/**
 * This class is a test suite for the methods in object FunSets.
 *
 * To run this test suite, start "sbt" then run the "test" command.
 */
class FunSetSuite extends munit.FunSuite:

  import FunSets.*

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets:
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)

  trait TestSetsMod:
    val s1 = (x: Int) => x.abs % 2 == 0
    val s2 = (x: Int) => x.abs % 5 == 0
    val s3 = (x: Int) => x.abs % 10 == 0
    val empty = (x: Int) => false
  /**
   * This test is currently disabled (by using @Ignore) because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", remove the
   * .ignore annotation.
   */
  test("singleton set one contains one") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets:
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
  }

  test("union contains all elements of each set") {
    new TestSets:
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
  }

  test("intersect contains shared elements of sets"){
    new TestSets:
      val u = union(s1,s2)
      val s = intersect(u, s1)
      assert(contains(s, 1), "Intersection 1")
      assert(!contains(s, 2), "Intersection 2")
      assert(!contains(s, 3), "Intersection 3")
  }

  test("diff contains the difference between two sets"){
    new TestSets:
      val u = union(s1,s2)
      val s = diff(u, s1)
      assert(!contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
  }

  test("filter filters the elements of the set by predicate"){
    new TestSets:
      val u = union(union(s1, s2), s3)
      val s = filter(u, _ % 2 == 1)
      assert(contains(s, 1), "Filter 1")
      assert(!contains(s, 2), "Filter 2")
      assert(contains(s, 3), "Filter 3")
  }

  test("forall checks whether all elements satisfy predicate"){
    new TestSetsMod:
      assert(forall(s1, _.abs % 2 == 0), "Forall 1.1")
      assert(!forall(s1, _.abs % 5 == 0), "Forall 1.2")
      assert(!forall(s1, _.abs % 10 == 0), "Forall 1.3")
      assert(!forall(s2, _.abs % 2 == 0), "Forall 2.1")
      assert(forall(s2, _.abs % 5 == 0), "Forall 2.2")
      assert(!forall(s2, _.abs % 10 == 0), "Forall 2.3")
      assert(forall(s3, _.abs % 2 == 0), "Forall 3.1")
      assert(forall(s3, _.abs % 5 == 0), "Forall 3.2")
      assert(forall(s3, _.abs % 10 == 0), "Forall 3.3")
      assert(forall(empty, _ => false), "Forall empty set")
  }

  test("exists checks if there is an element that satisfies predicate"){
    new TestSetsMod:
      assert(exists(s1, _.abs % 2 == 0), "Exists 1.1")
      assert(exists(s1, _.abs % 5 == 0), "Exists 1.2")
      assert(exists(s1, _.abs % 10 == 0), "Exists 1.3")
      assert(!exists(s1, _.abs % 2 != 0), "Exists 1.4")

      assert(exists(s2, _.abs % 2 == 0), "Exists 2.1")
      assert(exists(s2, _.abs % 5 == 0), "Exists 2.2")
      assert(exists(s2, _.abs % 10 == 0), "Exists 2.3")
      assert(!exists(s2, _.abs % 5 != 0), "Exists 2.4")

      assert(exists(s3, _.abs % 2 == 0), "Exists 2.1")
      assert(exists(s3, _.abs % 5 == 0), "Exists 2.2")
      assert(exists(s3, _.abs % 10 == 0), "Exists 2.3")
      assert(!exists(s3, _.abs % 2 != 0), "Exists 2.5")
      assert(!exists(s3, _.abs % 5 != 0), "Exists 2.6")
      assert(!exists(s3, _.abs % 10 != 0), "Exists 2.7")

      assert(!exists(empty, _ => true), "Exists empty set")
  }

  test("map elements of set"){
    new TestSetsMod:
      val s2Mapped = map(s2, _ * 2)
      assert(forall(s2Mapped, s3(_)), "Map 1")
  }

  import scala.concurrent.duration.*
  override val munitTimeout = 10.seconds
