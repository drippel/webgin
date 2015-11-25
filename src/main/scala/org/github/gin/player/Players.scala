package org.github.gin.player

import scala.util.Random
import org.github.gin.GinGame
import org.github.gin.Card

abstract class Player {

  def stock( game : GinGame ) : Boolean

  def discard( game : GinGame, computer : Boolean ) : Card

}

class RandomPlayer extends Player {

  val random = new Random()

  def stock( game : GinGame ): Boolean = {
    random.nextBoolean()
  }

  def discard(game: GinGame, computer: Boolean): Card = {
    val hand = if( computer ){
      game.computerHand
    }
    else {
      game.playerHand
    }

    hand(random.nextInt(hand.size))

  }
}