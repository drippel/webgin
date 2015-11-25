package org.github.gin

import scala.collection.mutable.ListBuffer

class GinGame {

  var computerDeal = true
  var playerTurn = true

  var computerHand = List[Card]()
  var playerHand = List[Card]()

  var discard = List[Card]()
  var stock = List[Card]()

  var lastAction: String = null

}

object GinGame {

  def createGame() = {
    val game = new GinGame()
    game
  }

  def startGame(game: GinGame) = {

    val deck = Deck.standard52CardDeck()
    var shuffled = Deck.shuffle(deck, 10)

    val p1 = new ListBuffer[Card]
    val p2 = new ListBuffer[Card]

    for (i <- 1 to 10) {

      p1 += shuffled.head
      shuffled = shuffled.tail
      p2 += shuffled.head
      shuffled = shuffled.tail

    }

    if (game.computerDeal) {
      game.playerHand = p1.toList
      game.computerHand = p2.toList
    } else {
      game.playerHand = p2.toList
      game.computerHand = p1.toList
    }

    game.discard = List(shuffled.head)
    shuffled = shuffled.tail

    game.stock = shuffled

    game.playerTurn = game.computerDeal

    game.lastAction = "start"

    game

  }

  def discard(game: GinGame) = {

    // take the first card off the discard stack
    // and give to a player

    game.discard.headOption match {
      case Some(c) => {

        game.discard = game.discard.tail

        if (game.playerTurn) {
          game.playerHand = game.playerHand ++ List(c)
          game.lastAction = "take discard player"
        } else {
          game.computerHand = game.computerHand ++ List(c)
          game.lastAction = "take discard computer"
        }

      }
      case None => {
        // cant pick from discard
        // leave game alone
        game.lastAction = "take discard fail"
      }
    }

    game

  }

  def removeFromHand(cards: List[Card], card: Card): List[Card] = {
    cards.filterNot { c => { c.equals(card) } }
  }

  def discard(game: GinGame, card: Card ) = {

    // which player has 11 cards?
    if ( game.playerTurn ) {

      game.playerHand = removeFromHand(game.playerHand, card)
      game.discard = List(card) ++ game.discard
      game.lastAction = "discard player"
    } else if (game.computerHand.size == 11) {

      game.computerHand = removeFromHand(game.computerHand, card)
      game.discard = List(card) ++ game.discard
      game.lastAction = "discard computer"
    } else {
      game.lastAction = "discard fail"
    }

    game.playerTurn = !game.playerTurn

    game

  }

  def stock(game: GinGame) = {

    // take the first card off the deck

    game.stock.headOption match {

      case Some(c) => {

        if (game.playerTurn) {
          game.playerHand = game.playerHand ++ List(c)
          game.lastAction = "stock player"
        } else {
          game.computerHand = game.computerHand ++ List(c)
          game.lastAction = "stock computer"
        }

      }
      case _ => {

        game.lastAction = "stock fail"
      }

    }

    game.stock = game.stock.tail

    game
  }

}