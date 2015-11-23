package org.github.gin

import scala.collection.mutable.ListBuffer
import scala.util.Random

class Deck {
  val cards = new ListBuffer[Card]
}

object Deck {

  val suits = List(Spades,Hearts,Diamonds,Clubs)
  val ranks = List(Ace,Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten,Jack,Queen,King)

  def standard52CardDeck() : List[Card] = {

    val cards = new ListBuffer[Card]

    for( s <- suits ){
      for( r <- ranks ){
        cards += new Card(s,r)
      }
    }

    cards.toList

  }

  def shuffle( cards : List[Card] ) : List[Card] = {

    val r = new Random()

    val src = new ListBuffer[Card]() ++= cards
    val dest = new ListBuffer[Card]()

    while( !src.isEmpty ){
      dest += src.remove(r.nextInt(src.size))
    }

    dest.toList

  }

  def shuffle( cards : List[Card], times : Int ) : List[Card] = {

    var shuffling = cards

    for( i <- 0 to times ){
      shuffling = shuffle(shuffling)
    }

    shuffling
  }

}