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

    // group by suit

    val groups = cards.groupBy( (c) => { c.suit } )

    for( group <- groups if group._2.size > 2 ){

      // sort by rank
      val sorted = group._2.sortBy( byRank )

    }

    sets.toList
  }


}