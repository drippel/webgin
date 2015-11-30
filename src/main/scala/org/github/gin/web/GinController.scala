package org.github.gin.web

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.github.gin.GinGame
import org.springframework.web.servlet.ModelAndView
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.github.gin.Card
import org.github.gin.CardRenderer
import org.github.gin.player.RandomPlayer
import org.apache.commons.lang3.StringUtils

@Scope("session")
@Controller
@RequestMapping(Array("/gin"))
class GinController {

  @Autowired
  var httpSession: HttpSession = null

  var game = GinGame.createGame()

  var status = ""

  var player = new RandomPlayer

  var lastTake: String = ""
  var lastCard: String = ""
  var cheat = false

  @RequestMapping(method = Array(RequestMethod.GET))
  def welcome() = { buildModelAndView() }

  @RequestMapping(method = Array(RequestMethod.POST))
  def post(@RequestParam(required = true, value = "action") action: String,
    @RequestParam(required = false, value = "playerCardList") playerCardList: String) = {

    if (StringUtils.isNotEmpty(playerCardList)) {
      sortPlayerHand(game, playerCardList)
    }

    action match {
      case "new" => { newGame() }
      case "discard" => { discard() }
      case "stock" => { stock() }
      case "drop" => {
        if (game.playerHand.size == 11) {
          drop(playerCardList)
          if (checkForGin(true)) {
            // notification
            status = "You Win!!!!!!"
          } else {
            computerTurn()
            if( checkForGin(false) ){
              // notification
              status = "You Lose!!!!!!"
            }
            else {
              status = "Your pick"
            }
          }
        }
      }
      case "cheat" => { cheat = !cheat }
      case _ => { Console.println(action) }
    }

    buildModelAndView();
  }

  def newGame() = {
    game = GinGame.startGame(game)
    status = "New Game"
  }

  def discard() = {
    game = GinGame.discard(game)
    status = "Your Discard"
  }

  def buildModelAndView() = {

    val modelAndView = new ModelAndView("index")
    modelAndView.addObject("game", game)
    if (cheat) {
      modelAndView.addObject("computerHand", asJavaCollection(renderCards(game.computerHand)))
    } else {
      modelAndView.addObject("computerHand", asJavaCollection(renderCardsAsBacks(game.computerHand)))
    }
    modelAndView.addObject("playerHand", asJavaCollection(renderCards(game.playerHand)))
    modelAndView.addObject("discard", asJavaCollection(renderCards(game.discard)))
    modelAndView.addObject("stock", asJavaCollection(renderCards(game.stock)))

    val dcard = if (game.discard.isEmpty) {
      CardRenderer.whiteJoker()
    } else {
      // CardRenderer.cardToUnicode(game.discard.head)
      CardRenderer.renderCard(game.discard.head)
    }
    modelAndView.addObject("discardCard", dcard)
    modelAndView.addObject("stockCard", CardRenderer.cardBack())

    modelAndView.addObject("lastTake", lastTake)
    modelAndView.addObject("lastCard", lastCard)

    modelAndView.addObject("status",status)

    modelAndView

  }

  def renderCards(cards: List[Card]) = {
    // cards.map { c => CardRenderer.cardToUnicode(c) }
    cards.map { c => CardRenderer.renderCard(c) }
  }

  def renderCardsAsBacks(cards: List[Card]) = {
    cards.map { c => CardRenderer.cardBack() }
  }
  def drop(cardList: String) = {

    val cards = toCards(cardList)

    game = GinGame.discard(game, cards.last)

  }

  def toCards(cardList: String) = {
    val cards = cardList.split('|').filter({ s => { s.length() > 0 } }).toList
    cards.map { (c) => { CardRenderer.stringToCard(c) } }
  }

  def stock() = {
    game = GinGame.stock(game)
    status = "Your Discard"
  }

  def computerTurn() = {

    // discard or stock
    game = if (player.stock(game)) {
      //
      lastTake = "discard"
      lastCard = CardRenderer.cardToUnicode(game.discard.head)
      GinGame.discard(game)
    } else {
      lastTake = "stock"
      lastCard = CardRenderer.cardBack()
      GinGame.stock(game)
    }

    // drop
    game = GinGame.discard(game, player.discard(game, true))
  }

  def sortPlayerHand(game: GinGame, playerCardList: String) = {
    game.playerHand = toCards(playerCardList)
  }

  def checkForGin( player : Boolean ) : Boolean = {
    GinGame.detectGin(game,player)
  }
}

