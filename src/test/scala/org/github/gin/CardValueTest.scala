package org.github.gin

import org.junit.Test
import org.junit.Assert._

class CardValueTest {

  @Test
  def test_card_values(): Unit = {

    val c1 = "🂮"
    val c2 = "\uD83C\uDCAE"

    try{

    Console.println(c1)
    Console.println(c2)
    val c = CardRenderer.stringToCard(c1)
    Console.println( c.suit.value +" " + c.rank.value )
    Console.println( c.suit.toString() + " " + c.rank.toString() )

    Console.println(c1.equals(c2))
    assertTrue(c1.equals(c2))
    }
    catch {
      case e : Exception => { e.printStackTrace() }
    }

  }


  @Test
  def test_list_diff_1() = {

    val c1 = new Card( Spades, Ace )
    val c2 = new Card( Hearts, Two )
    val c3 = new Card( Diamonds, Three )
    val c4 = new Card( Clubs, Four )

    val c5 = new Card( Spades, Five )
    val c6 = new Card( Hearts, Six )
    val c7 = new Card( Diamonds, Seven )
    val c8 = new Card( Clubs, Eight )

    val c9 = new Card( Spades, Nine )
    val c10 = new Card( Hearts, Ten )



    val l1 = List(c1,c2,c3,c4)
    val l2 = List(c3)

    val d1 = l1.diff(l2)

    assertTrue( "should have three cards", d1.size == 3 )
    assertTrue( "should not have c3", !d1.contains(c3) )


    val l3 = List(c1,c2,c3,c4)
    val l4 = List(c5,c6,c7,c8)

    val d2 = l3.diff(l4)

    assertTrue( "should have four cards", d2.size == 4 )

    // more complex example
    val l5 = List(c1,c2,c3,c4,c5,c6,c7,c8)
    val l6 = List(c9,c6,c2,c8,c4,c10)

    val d3 = l5.diff(l6)

    assertTrue( "should have four cards", d3.size == 4 )
  }
}