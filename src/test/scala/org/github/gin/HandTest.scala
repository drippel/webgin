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
}