package org.github.gin

import scala.collection.mutable.ListBuffer

class GinGame {

  var playerTurn = false

  var computerHand = List[Card]()
  var playerHand = List[Card]()

  var discard = List[Card]()
  var stock = List[Card]()

  var prev: Option[GinGame] = None
  var next: Option[GinGame] = None

  var play: Play = new Start()

}

object GinGame {

  def link(g1: GinGame, g2: GinGame) = {
    g1.next = Some(g2)
    g2.prev = Some(g1)
  }

  def clone(src: GinGame): GinGame = {

    val dest = new GinGame()

    dest.playerTurn = src.playerTurn
    dest.computerHand = src.computerHand.toList
    dest.playerHand = src.playerHand.toList
    dest.discard = src.discard.toList
    dest.stock = src.stock.toList
    dest.play = src.play

    dest
  }

  def createGame() = {
    new GinGame()
  }

  def deal(game: GinGame) = {

    val next = clone(game)
    link(game, next)

    next.play = new Deal(next.playerTurn)

    val deck = Deck.standard52CardDeck()
    var shuffled = Deck.shuffle(deck, 100)

    val p1 = new ListBuffer[Card]
    val p2 = new ListBuffer[Card]

    for (i <- 1 to 10) {

      p1 += shuffled.head
      shuffled = shuffled.tail
      p2 += shuffled.head
      shuffled = shuffled.tail

    }

    if (next.playerTurn) {
      next.computerHand = p1.toList
      next.playerHand = p2.toList
    } else {
      next.playerHand = p1.toList
      next.computerHand = p2.toList
    }

    next.discard = List(shuffled.head)

    next.stock = shuffled.tail

    next.playerTurn = !next.playerTurn

    next

  }

  def discard(game: GinGame) = {

    // take the first card off the discard stack
    // and give to a player

    game.discard.headOption match {
      case Some(c) => {

        val next = clone(game)
        link(game, next)

        next.play = new Discard(next.playerTurn, c)
        next.discard = next.discard.tail

        if (next.playerTurn) {
          next.playerHand = next.playerHand ++ List(c)
        } else {
          next.computerHand = next.computerHand ++ List(c)
        }

        next

      }
      case None => {
        // cant pick from discard
        // leave game alone
        game
      }
    }

  }

  def removeFromHand(cards: List[Card], card: Card): List[Card] = {
    cards.filterNot { c => { c.equals(card) } }
  }

  def drop(game: GinGame, card: Card) = {

    val next = clone(game)
    link(game, next)

    next.play = new Drop(next.playerTurn, card)

    if (next.playerTurn) {
      next.playerHand = removeFromHand(next.playerHand, card)
    } else {
      next.computerHand = removeFromHand(next.computerHand, card)
    }

    next.discard = List(card) ++ next.discard

    next.playerTurn = !next.playerTurn

    next

  }

  def stock(game: GinGame) = {

    // take the first card off the deck
    game.stock.headOption match {

      case Some(c) => {

        val next = clone(game)
        link(game, next)

        next.play = new Stock(next.playerTurn, c)

        if (next.playerTurn) {
          next.playerHand = next.playerHand ++ List(c)
        } else {
          next.computerHand = next.computerHand ++ List(c)
        }

        next.stock = next.stock.tail

        next
      }
      case _ => {
        game
      }

    }

  }

  def detectGin(game: GinGame, player: Boolean): Boolean = {

    val gin = if (player) {
      Hand.detectGin(game.playerHand)
    } else {
      Hand.detectGin(game.computerHand)
    }

    gin

  }

  def sortHand(game: GinGame, player: Boolean): Unit = {

    if (player) {
      game.playerHand = Hand.sortCards(game.playerHand)
    } else {
      game.computerHand = Hand.sortCards(game.computerHand)
    }

  }

  // returns a list of games from game through prev to the start
  def toList(game: GinGame): List[GinGame] = {
    addGame(game, List[GinGame]())
  }

  def addGame(game: GinGame, accum: List[GinGame]): List[GinGame] = {

    val games = accum ++ List(game)

    game.prev match {
      case Some(g) => {
        addGame(g, games)
      }
      case _ => { games }
    }

  }

  def detectDraw(game: GinGame) = {
    game.stock.size <= 2
  }

  def draw(game: GinGame) = {

    if (detectDraw(game)) {

      val next = clone(game)
      link(game, next)

      next.play = new Draw(next.playerTurn)
      next.playerTurn = !next.playerTurn

      next
    } else {
      game
    }
  }

  def gin(game: GinGame): GinGame = {

    // make sure somebody has gin?
    if (Hand.detectGin(game.playerHand) || Hand.detectGin(game.computerHand)) {

      val next = clone(game)
      link(game, next)

      next.play = new Gin(next.playerTurn)
      next.playerTurn = !next.playerTurn

      next

    } else {
      game
    }

  }

  def knock(game: GinGame, computer: Boolean): GinGame = {

    val next = clone(game)
    link(game, next)

    next.play = new Knock(next.playerTurn)
    next.playerTurn = !next.playerTurn
    next

  }
}