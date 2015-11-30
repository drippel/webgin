package org.github.gin

import scala.collection.mutable.ListBuffer

object Hand {

  def byRank( a : Card ) : Int = {
    CardRenderer.ranks.indexOf(a.rank)
  }

  def bySuit( a : Card ) : Int = {
    CardRenderer.suits.indexOf(a.suit)
  }

  def findSets( cards : List[Card] ) : List[List[Card]] = {

    //
    val sets = new ListBuffer[List[Card]]()

    for( card <- cards ){

      val (l1,l2) = cards.partition( (c) => { card.rank.equals(c.rank) } )

      if( !l1.isEmpty && l1.size > 2 ){

        if( !sets.flatten.contains(card) ){
          sets += l1
        }
      }
    }


    sets.toList
  }

  def findRuns( cards : List[Card] ) : List[List[Card]] = {

    val sets = new ListBuffer[List[Card]]()

    var source = cards.toList

    while( !source.isEmpty ){

      val run = findRunsFromCard( source, source.head, List[Card]() )
      if( run.size > 2 ){
        sets += run
      }

      source = source.filter( (c) => { !run.contains(c) })

    }

    sets.toList
  }

  def findRunsFromCard( cards : List[Card], card : Card, run : List[Card] ) : List[Card] = {

    val accum = run ++ List(card)

    nextCard(card) match {
      case Some(n) => {
        if( cards.contains(n) ){
          findRunsFromCard( cards, n, accum )
        }
        else {
          accum
        }
      }
      case _ => {
        accum
      }
    }
  }

  def nextCard( c : Card) : Option[Card] = {

    val i = CardRenderer.ranks.indexOf(c.rank)

    CardRenderer.ranks.lift(i+1) match {
      case Some(r) => {
        Some(new Card( c.suit, r ))
      }
      case None => {
        None
      }
    }
  }

  def detectGin( cards : List[Card] ) : Boolean = {

    var hand = cards.toList

    val runs = findRuns(hand)

    val inRuns = runs.flatten

    hand = hand.filter( (c) => { !inRuns.contains(c) } )

    val sets = findSets(hand)

    val inSets = sets.flatten

    hand = hand.filter( (c) => { !inSets.contains(c) } )

    if( hand.isEmpty ){
      true
    }
    else {
      if( cards.size == 11 && hand.size == 1 ){
        true
      }
      else {
        false
      }
    }
  }


}