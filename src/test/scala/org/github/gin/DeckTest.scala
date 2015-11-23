package org.github.gin

import org.junit.Test
import org.junit.Assert._

class DeckTest {

  @Test
  def test_standard_deck() = {

    val deck = Deck.standard52CardDeck()

    assertTrue(deck != null)
    assertTrue(deck.size == 52 )

    for( c <- deck ){
      System.out.print(CardRenderer.cardToUnicode(c))
    }


  }

}