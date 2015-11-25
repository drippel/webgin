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
}