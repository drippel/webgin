package org.github.gin

import scala.collection.mutable.ListBuffer

object Hand {

  def byRank( a : Card ) : Int = {
    CardRenderer.ranks.indexOf(a.rank)
  }

  def bySuit( a : Card ) : Int = {
    CardRenderer.suits.indexOf(a.suit)
  }


  def findPairs( cards : List[Card] ) : List[List[Card]] = {

    val sets = new ListBuffer[List[Card]]()

    for( card <- cards ){

      val (l1,l2) = cards.partition( (c) => { card.rank.equals(c.rank) } )

      if( !l1.isEmpty && l1.size > 1 ){

        if( !sets.flatten.contains(card) ){
          sets += l1
        }
      }
    }


    sets.toList
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

  def findRuns = findRelated(2)(_)

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

  def neighboringCard( dir : Int )( c : Card) : Option[Card] = {

    val i = CardRenderer.ranks.indexOf(c.rank)

    CardRenderer.ranks.lift(i+dir) match {
      case Some(r) => {
        Some(new Card( c.suit, r ))
      }
      case None => {
        None
      }
    }
  }

  def nextCard = neighboringCard(1)(_)

  def prevCard = neighboringCard(-1)(_)

  def detectGin( cards : List[Card] ) : Boolean = {

    var hand = sortCards(cards)

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

  def sortCards( cards : List[Card] ) : List[Card] = {
    cards.toList.sortBy(bySuit).sortBy(byRank)
  }

  def findDeadwood( cards : List[Card] ) : List[Card] = {

    val sorted = sortCards(cards)

    val runs = findRuns(sorted)
    val inRuns = runs.flatten
    var notInRuns = sorted.filter( (c) => { !inRuns.contains(c)} )
    val sets = findSets(notInRuns)
    val inSets = sets.flatten
    val remainder = notInRuns.filter( (c) => { !inSets.contains(c)} )

    remainder
  }

  def countDeadwood( cards : List[Card] ) : Int = {
    findDeadwood(cards).foldRight(0)( (c,i) => { i + Deck.cardValue(c) } )
  }

  def findRelated( limit : Int )(cards: List[Card]) : List[List[Card]] = {

    val sets = new ListBuffer[List[Card]]()

    var source = cards.toList

    while( !source.isEmpty ){

      val run = findRunsFromCard( source, source.head, List[Card]() )
      if( run.size > limit ){
        sets += run
      }

      source = source.filter( (c) => { !run.contains(c) })

    }

    sets.toList

  }
  // the assumption here is that the incoming list has already been
  // filtered down to deadwood
  def findNeighbors = findRelated(1)(_)

  def extendsRun( run : List[Card], card : Card ) : Boolean = {

    var found = prevCard(run.head) match {
      case Some(c) => { card.equals(c)}
      case _ => { false }
    }

    if( !found ){
      found = nextCard(run.last) match {
        case Some(c) => { card.equals(c)}
        case _ => { false }
      }
    }

    found
  }

}