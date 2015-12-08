package org.github.gin

import scala.collection.mutable.ListBuffer

class GinGame(
  val playerTurn: Boolean = false,
  val computerHand: List[Card] = List[Card](),
  val playerHand: List[Card] = List[Card](),
  val discard: List[Card] = List[Card](),
  val stock: List[Card] = List[Card](),
  val prev: Option[GinGame] = None,
  val play: Play = new Start())

object GinGame {

  def createGame() = {
    new GinGame()
  }

  def deal(game: GinGame) = {

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

    val hands = if (game.playerTurn) {
      (p1.toList, p2.toList)
    } else {
      (p2.toList, p1.toList)
    }

    new GinGame(
      !game.playerTurn,
      hands._1,
      hands._2,
      List(shuffled.head),
      shuffled.tail,
      Some(game),
      new Deal(game.playerTurn))

  }

  def discard(game: GinGame) = {

    game.discard.headOption match {
      case Some(c) => {

        val hands = if (game.playerTurn) {
          (game.playerHand ++ List(c), game.computerHand.toList)
        } else {
          (game.playerHand.toList, game.computerHand ++ List(c))
        }

        new GinGame(
          game.playerTurn,
          hands._2,
          hands._1,
          game.discard.tail,
          game.stock.toList,
          Some(game),
          new Discard(game.playerTurn, c))

      }
      case None => {
        game
      }
    }

  }

  def removeFromHand(cards: List[Card], card: Card): List[Card] = {
    cards.filterNot { c => { c.equals(card) } }
  }

  def drop(game: GinGame, card: Card) = {

    val hands = if (game.playerTurn) {
      (removeFromHand(game.playerHand, card), game.computerHand)
    } else {
      (game.playerHand, removeFromHand(game.computerHand, card))
    }

    new GinGame(
      !game.playerTurn,
      hands._2,
      hands._1,
      (List(card) ++ game.discard),
      game.stock.toList,
      Some(game),
      new Drop(game.playerTurn, card))

  }

  def stock(game: GinGame) = {

    // take the first card off the deck
    game.stock.headOption match {

      case Some(c) => {

        val hands = if (game.playerTurn) {
          (game.playerHand ++ List(c), game.computerHand)
        } else {
          (game.playerHand, game.computerHand ++ List(c))
        }

        new GinGame(
          game.playerTurn,
          hands._2,
          hands._1,
          game.discard.toList,
          game.stock.tail,
          Some(game),
          new Stock(game.playerTurn, c))

      }
      case _ => {
        game
      }

    }

  }

  def detectGin(game: GinGame, player: Boolean): Boolean = {
    if (player) {
      Hand.detectGin(game.playerHand)
    } else {
      Hand.detectGin(game.computerHand)
    }
  }

  def sortHand(game: GinGame, player: Boolean): GinGame = {

    val hands = if (player) {
      (Hand.sortCards(game.playerHand), game.computerHand.toList)
    } else {
      (game.playerHand.toList, Hand.sortCards(game.computerHand))
    }

    new GinGame(
      game.playerTurn,
      hands._2,
      hands._1,
      game.discard.toList,
      game.stock.toList,
      Some(game),
      new Sort(game.playerTurn))

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

      new GinGame(
        !game.playerTurn,
        game.computerHand.toList,
        game.playerHand.toList,
        game.discard.toList,
        game.stock.toList,
        Some(game),
        new Draw(game.playerTurn))
    } else {
      game
    }
  }

  def gin(game: GinGame): GinGame = {

    // make sure somebody has gin?
    if (Hand.detectGin(game.playerHand) || Hand.detectGin(game.computerHand)) {

      new GinGame(
        !game.playerTurn,
        game.computerHand.toList,
        game.playerHand.toList,
        game.discard.toList,
        game.stock.toList,
        Some(game),
        new Gin(game.playerTurn))

    } else {
      game
    }

  }

  def knock(game: GinGame, computer: Boolean): GinGame = {

    new GinGame(
      !game.playerTurn,
      game.computerHand.toList,
      game.playerHand.toList,
      game.discard.toList,
      game.stock.toList,
      Some(game),
      new Knock(game.playerTurn))

  }

  def sortHand(game: GinGame, player: Boolean, cards : List[Card] ): GinGame = {

    val hands = if (player) {
      (cards, game.computerHand.toList)
    } else {
      (game.playerHand.toList, cards)
    }

    new GinGame(
      game.playerTurn,
      hands._2,
      hands._1,
      game.discard.toList,
      game.stock.toList,
      Some(game),
      new Sort(game.playerTurn))

  }

  def scoreGame( game : GinGame, settings : Settings ) : (Int,Int) = {
    (0,0)
  }
}