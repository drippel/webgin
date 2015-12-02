package org.github.gin

import scala.collection.mutable.ListBuffer
import org.junit.Test
import org.junit.Assert._

class HandTest {

  @Test
  def test_find_sets_1() {

    val cards = new ListBuffer[Card]()

    cards += new Card( Spades, Two )
    cards += new Card( Hearts, Three )
    cards += new Card( Clubs, Five )
    cards += new Card( Clubs, Three )
    cards += new Card( Diamonds, Ten )
    cards += new Card( Diamonds, Jack )
    cards += new Card( Clubs, Nine )
    cards += new Card( Spades, Six )
    cards += new Card( Hearts, Ace )
    cards += new Card( Spades, Three )

    val sets = Hand.findSets(cards.toList)

    assertTrue( "should have one set", !sets.isEmpty )
    assertTrue( "should have one set", sets.size == 1 )

    // should be a set of threes
    val set = sets.head

    assertTrue( "should be 3 cards", set.size == 3 )
    assertTrue( "should contain 3 of heart", set.contains(new Card(Hearts,Three)))


  }

  @Test
  def test_find_sets_2() {

    val cards = new ListBuffer[Card]()

    cards += new Card( Spades, Two )
    cards += new Card( Hearts, Three )
    cards += new Card( Clubs, Five )
    cards += new Card( Clubs, Three )
    cards += new Card( Diamonds, Five )
    cards += new Card( Diamonds, Jack )
    cards += new Card( Clubs, Nine )
    cards += new Card( Spades, Six )
    cards += new Card( Hearts, Five )
    cards += new Card( Spades, Three )

    val sets = Hand.findSets(cards.toList)

    assertTrue( "should have two sets", !sets.isEmpty )
    assertTrue( "should have two sets", sets.size == 2 )

    // should be a set of threes and fives
    val set = sets.flatten

    assertTrue( "should contain 3 of hearts", set.contains(new Card(Hearts,Three)))
    assertTrue( "should contain 5 of diamonds", set.contains(new Card(Diamonds,Five)))
    assertTrue( "should not contain 2 of spades", !set.contains(new Card(Spades,Two)))


  }


  @Test
  def test_next_card() = {

    val c1 = new Card( Spades, Ace )
    val n1 = Hand.nextCard(c1)

    assertTrue( "should be a card", n1.isDefined )
    assertTrue( "should be 2 of spades", n1.get.equals(new Card(Spades,Two)))

    val c2 = new Card( Hearts, Eight )
    val n2 = Hand.nextCard(c2)

    assertTrue( "should be a card", n2.isDefined )
    assertTrue( "should be 9 of hearts", n2.get.equals(new Card(Hearts,Nine)))


    val c3 = new Card( Diamonds, King )
    val n3 = Hand.nextCard(c3)

    assertTrue( "should not be a card", n3.isEmpty )

  }

  @Test
  def test_find_runs_from_card_1() = {

    val c1 = new Card(Spades,Ace)
    val c2 = new Card(Hearts,Ace)
    val c3 = new Card(Diamonds,Ten)
    val c4 = new Card(Spades,Two)
    val c5 = new Card(Clubs,Jack)
    val c6 = new Card(Clubs,Nine)
    val c7 = new Card(Spades,Three)

    val cards = List(c1,c2,c3,c4,c5,c6,c7)

    val run = Hand.findRunsFromCard(cards, c1, List[Card]() )

    assertTrue( "should return a non empty list", !run.isEmpty )
    assertTrue( "should contains three cards", run.size == 3 )
    assertTrue( "should contains c1", run.contains(c1) )
    assertTrue( "should contains c4", run.contains(c4) )
    assertTrue( "should contains c7", run.contains(c7) )

    val run2 = Hand.findRunsFromCard(cards, c3, List[Card]() )

    assertTrue( "should return a non empty list", !run2.isEmpty )
    assertTrue( "should contains one card", run2.size == 1 )
    assertTrue( "should contains c1", run2.contains(c3) )

  }

  @Test
  def test_find_runs_1() = {

    val c1 = new Card(Spades,Ace)
    val c2 = new Card(Hearts,Ace)
    val c3 = new Card(Diamonds,Ten)
    val c4 = new Card(Spades,Two)
    val c5 = new Card(Clubs,Jack)
    val c6 = new Card(Clubs,Nine)
    val c7 = new Card(Spades,Three)
    val c8 = new Card(Clubs,Two)
    val c9 = new Card(Hearts,King)
    val c10 = new Card(Spades,Four)
    val c11 = new Card(Diamonds,Six)

    val cards = List(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11)

    val runs = Hand.findRuns(cards)

    assertTrue( "list should not be empty", !runs.isEmpty )
    assertTrue( "list should have one run", runs.size == 1 )

    val run = runs(0)

    assertTrue( "run should be 4 long", run.size == 4 )
    assertTrue( "run should contain ace of spades", run.contains(c1))
    assertTrue( "run should contain two of spades", run.contains(c4))
    assertTrue( "run should contain three of spades", run.contains(c7))
    assertTrue( "run should contain four of spades", run.contains(c10))

  }

  @Test
  def test_detect_gin_1() = {

    val c1 = new Card(Spades,Ace)
    val c2 = new Card(Spades,Two)
    val c3 = new Card(Spades,Three)
    val c4 = new Card(Spades,Four)

    val c5 = new Card(Clubs,Eight)
    val c6 = new Card(Hearts,Eight)
    val c7 = new Card(Diamonds,Eight)

    val c8 = new Card(Clubs,Ten)
    val c9 = new Card(Clubs,Jack)
    val c10 = new Card(Clubs,Queen)

    val c11 = new Card(Hearts,Five)

    val cards = List(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11)

    assertTrue( "should be gin", Hand.detectGin(cards))


  }

  @Test
  def test_detect_gin_2() = {

    val c1 = new Card(Spades,Ace)
    val c2 = new Card(Spades,Two)
    val c3 = new Card(Spades,Three)
    val c4 = new Card(Spades,Four)

    val c5 = new Card(Clubs,Eight)
    val c6 = new Card(Hearts,Eight)
    val c7 = new Card(Diamonds,Eight)

    val c8 = new Card(Clubs,Ten)
    val c9 = new Card(Clubs,Jack)
    val c10 = new Card(Clubs,Queen)
    val c11 = new Card(Clubs,King)

    val cards = List(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11)

    assertTrue( "should be gin", Hand.detectGin(cards))

  }

  @Test
  def test_detect_gin_3() = {

    val c1 = new Card(Spades,Ace)
    val c2 = new Card(Hearts,Ace)
    val c3 = new Card(Clubs,Ace)
    val c4 = new Card(Diamonds,Ace)

    val c5 = new Card(Clubs,Eight)
    val c6 = new Card(Hearts,Eight)
    val c7 = new Card(Diamonds,Eight)

    val c8 = new Card(Clubs,Ten)
    val c9 = new Card(Hearts,Ten)
    val c10 = new Card(Diamonds,Ten)

    val c11 = new Card(Clubs,King)

    val cards = List(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11)

    assertTrue( "should be gin", Hand.detectGin(cards))

  }

  @Test
  def test_detect_gin_4() = {

    val c1 = new Card(Spades,Ace)
    val c2 = new Card(Hearts,Ace)
    val c3 = new Card(Clubs,Ace)
    val c4 = new Card(Diamonds,Ace)

    val c5 = new Card(Clubs,Eight)
    val c6 = new Card(Hearts,Eight)
    val c7 = new Card(Diamonds,Eight)

    val c8 = new Card(Clubs,Ten)
    val c9 = new Card(Hearts,Ten)

    val c10 = new Card(Diamonds,Nine)
    val c11 = new Card(Clubs,King)

    val cards = List(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11)

    assertFalse( "should not be gin", Hand.detectGin(cards))

  }

  @Test
  def test_detect_gin_5() = {

    val c1 = new Card(Spades,Ace)
    val c2 = new Card(Hearts,Ace)
    val c3 = new Card(Clubs,Ace)
    val c4 = new Card(Diamonds,Ace)

    val c5 = new Card(Clubs,Eight)
    val c6 = new Card(Hearts,Eight)
    val c7 = new Card(Diamonds,Seven)

    val c8 = new Card(Clubs,Ten)
    val c9 = new Card(Hearts,Ten)

    val c10 = new Card(Diamonds,Nine)
    val c11 = new Card(Clubs,King)

    val cards = List(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11)

    assertFalse( "should not be gin", Hand.detectGin(cards))

  }

  @Test
  def test_detect_gin_6() = {

    val c1 = new Card(Hearts,Two)
    val c2 = new Card(Spades,Two)
    val c3 = new Card(Clubs,Two)

    val c4 = new Card(Diamonds,Three)
    val c5 = new Card(Diamonds,Four)
    val c6 = new Card(Diamonds,Five)

    val c7 = new Card(Spades,Seven)
    val c8 = new Card(Spades,Eight)
    val c9 = new Card(Spades,Nine)
    val c10 = new Card(Spades,Ten)

    val c11 = new Card(Hearts,King)


    val cards = List(c1,c2,c3,c4,c5,c6,c8,c9,c10,c11,c7)

    assertTrue( "should be gin", Hand.detectGin(cards))

  }
}