package org.github.gin.player

import scala.util.Random
import org.github.gin.GinGame
import org.github.gin.Card
import org.github.gin.Hand

abstract class Player {

  def stock( game : GinGame ) : Boolean

  def discard( game : GinGame ) : Card

}

class RandomPlayer extends Player {

  val random = new Random()

  def stock( game : GinGame ): Boolean = {
    random.nextBoolean()
  }

  def discard(game: GinGame ): Card = {
    game.computerHand(random.nextInt(game.computerHand.size))
  }
}

class SetMaker extends Player {

  def stock( game : GinGame ): Boolean = {

    // does the current discard make a set?
    game.discard.headOption match {
      case Some(c) => {
        !makesSet( game, c )
      }
      case _ => {
        true
      }
    }
  }

  def makesSet( game : GinGame, card : Card ) : Boolean = {

    val set = game.computerHand.filter( (c) => { c.rank.equals(card.rank) } )

    if( set.size >= 2 ){
      true
    }
    else {
      false
    }

  }

  def discard(game: GinGame): Card = {

    // find sets
    val sets = Hand.findSets(Hand.sortCards(game.computerHand))

    val deadwood = game.computerHand.diff( sets.flatten )

    if( deadwood.size > 0 ){

      // filter out pairs
      val pairs = Hand.findPairs(deadwood)

      val deaddead = deadwood.diff( pairs.flatten )

      if( !deaddead.isEmpty ){
        deaddead.head
      }
      else {
        pairs.head.head
      }
    }
    else {
      // this is a gin - but lets pick a card anyways
      val sets = Hand.findSets(game.computerHand)
      val first = sets.find( (s) => { s.size == 4 } )
      first.get.head
    }

  }
}

class RunMaker extends Player {

  def stock( game : GinGame ): Boolean = {

    // does the current discard make a set?
    game.discard.headOption match {
      case Some(c) => {
        !makesRun( game, c )
      }
      case _ => {
        true
      }
    }
  }

  def makesRun( game : GinGame, card : Card ) : Boolean = {

    // find existing runs or neighbors
    val runs = Hand.findRuns(Hand.sortCards(game.computerHand))

    val remainder = game.computerHand.diff(runs.flatten)

    val neighbors = Hand.findNeighbors(remainder)

    // does the card extends any of these runs or neighbors

    val related = runs ++ neighbors

    related.find( (r) => { Hand.extendsRun(r,card) } ) match {
      case Some(r) => { true }
      case _ => { false }
    }

  }

  def discard(game: GinGame): Card = {

    // find deadwood
    val runs = Hand.findRuns(Hand.sortCards(game.computerHand))
    var runsflat = runs.flatten
    var deadwood = game.computerHand.diff(runsflat)

    val neighbors = Hand.findNeighbors(deadwood)
    deadwood = deadwood.diff(neighbors.flatten)

    if( !deadwood.isEmpty ){
      deadwood.last
    }
    else {

      // pull a card off one of the longer runs
      val first = runs.find( (s) => { s.size >= 4 } )
      first.get.head

    }

  }
}