package org.github.gin

import scala.collection.mutable.ListBuffer

class GinGame {

  var computerDeal = true
  var playerTurn = true
  var finished = false

  var lastAction: String = null

  var computerHand = List[Card]()
  var playerHand = List[Card]()

  var discard = List[Card]()
  var stock = List[Card]()

  var prev : Option[GinGame] = None
  var next : Option[GinGame] = None

}

object GinGame {

  def link( g1 : GinGame, g2 : GinGame ) = {
    g1.next = Some(g2)
    g2.prev = Some(g1)
  }

  def clone( src : GinGame ) : GinGame = {

    val dest = new GinGame()

    dest.computerDeal = src.computerDeal
    dest.playerTurn = src.playerTurn
    dest.finished = src.finished
    dest.lastAction = src.lastAction
    dest.computerHand = src.computerHand.toList
    dest.playerHand = src.playerHand.toList
    dest.discard = src.discard.toList
    dest.stock = src.stock.toList

    dest
  }

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

    val next = clone(game)

    link(game,next)

    // take the first card off the discard stack
    // and give to a player

    next.discard.headOption match {
      case Some(c) => {

        next.discard = next.discard.tail

        if (next.playerTurn) {
          next.playerHand = next.playerHand ++ List(c)
          next.lastAction = "take discard player"
        } else {
          next.computerHand = next.computerHand ++ List(c)
          next.lastAction = "take discard computer"
        }

      }
      case None => {
        // cant pick from discard
        // leave game alone
        next.lastAction = "take discard fail"
      }
    }

    next

  }

  def removeFromHand(cards: List[Card], card: Card): List[Card] = {
    cards.filterNot { c => { c.equals(card) } }
  }

  def discard(game: GinGame, card: Card ) = {

    val next = clone(game)

    link(game,next)

    // which player has 11 cards?
    if ( next.playerTurn ) {

      next.playerHand = removeFromHand(next.playerHand, card)
      next.discard = List(card) ++ next.discard
      next.lastAction = "discard player"
    } else if (next.computerHand.size == 11) {

      next.computerHand = removeFromHand(next.computerHand, card)
      next.discard = List(card) ++ next.discard
      next.lastAction = "discard computer"
    } else {
      next.lastAction = "discard fail"
    }

    next.playerTurn = !next.playerTurn

    next

  }

  def stock(game: GinGame) = {

    val next = clone(game)

    link(game,next)

    // take the first card off the deck

    next.stock.headOption match {

      case Some(c) => {

        if (next.playerTurn) {
          next.playerHand = next.playerHand ++ List(c)
          next.lastAction = "stock player"
        } else {
          next.computerHand = next.computerHand ++ List(c)
          next.lastAction = "stock computer"
        }

      }
      case _ => {

        next.lastAction = "stock fail"
      }

    }

    next.stock = next.stock.tail

    next

  }


  def detectGin( game : GinGame, player : Boolean ) : Boolean = {

    val gin = if( player ){
      Hand.detectGin(game.playerHand)
    }
    else {
      Hand.detectGin(game.computerHand)
    }

    if( gin ){
      game.finished = true
    }

    gin

  }

  def sortHand( game : GinGame, player : Boolean ) : Unit = {

    if(player){
      game.playerHand = Hand.sortCards(game.playerHand)
    }
    else {
      game.computerHand = Hand.sortCards(game.computerHand)
    }

  }

  // returns a list of games from game through prev to the start
  def toList( game : GinGame ) : List[GinGame] = {
    addGame( game, List[GinGame]() )
  }

  def addGame( game : GinGame, accum : List[GinGame] ) : List[GinGame] = {

    val games = accum ++ List(game)

    game.prev match {
      case Some(g) => {
        addGame( g, games )
      }
      case _ => { games }
    }

  }

}