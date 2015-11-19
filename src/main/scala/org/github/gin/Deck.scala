package org.github.gin

import scala.collection.mutable.ListBuffer

class Deck {
  val cards = new ListBuffer[Card]
}

object Deck {

  def createStandardDeck() : Deck = {

    val deck = new Deck()

    for( i <- 0 to 3 ){
      for( j <- 0 to 12 ){
        deck.cards += new Card(i,j)
      }
    }

    deck

  }

}