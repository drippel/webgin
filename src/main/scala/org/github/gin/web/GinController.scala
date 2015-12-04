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
import org.github.gin.Hand
import org.github.gin.Hand
import org.github.gin.player.SetMaker
import org.github.gin.player.RunMaker

@Scope("session")
@Controller
@RequestMapping(Array("/gin"))
class GinController {

  @Autowired
  var httpSession: HttpSession = null

  var game = GinGame.createGame()

  var status = ""

  var player = new RunMaker()

  var lastTake: String = ""
  var lastCard: String = ""
  var cheat = false

  var openSettings = false

  @RequestMapping(method = Array(RequestMethod.GET))
  def welcome() = { buildModelAndView() }

  @RequestMapping(method = Array(RequestMethod.POST))
  def post(@RequestParam(required = true, value = "action") action: String,
    @RequestParam(required = false, value = "playerCardList") playerCardList: String) = {

    if (StringUtils.isNotEmpty(playerCardList)) {
      sortPlayerHand(game, playerCardList)
    }

    openSettings = false

    action match {
      case "new" => { newGame() }
      case "discard" => { discard() }
      case "stock" => { stock() }
      case "drop" => { drop(playerCardList) }
      case "cheat" => { cheat = !cheat }
      case "settings" => { settings() }
      case "undo" => { undo() }
      case _ => { Console.println(action) }
    }

    buildModelAndView();
  }

  def settings() = {
    openSettings = true
  }

  def drop(playerCardList: String): Unit = {
    if (game.playerHand.size == 11) {
      dropCard(playerCardList)
      if (checkForGin(true)) {
        // notification
        status = "You Win!!!!!!"
      } else {
        computerTurn()
        if (checkForGin(false)) {
          // notification
          status = "You Lose!!!!!!"
        } else {
          status = "Your pick"
        }
      }
    }
  }

  def newGame() = {
    game = GinGame.deal(game)
    GinGame.sortHand(game,true)
    GinGame.sortHand(game,false)
    status = "New Game"
  }

  def discard() = {
    game = GinGame.discard(game)
    if( checkForGin(true) ){
        status = "You Win!!!!!!"
    }
    else {
      status = "Your Discard"
    }
  }

  def buildModelAndView() = {

    if (!openSettings) {

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

      modelAndView.addObject("status", status)
      modelAndView.addObject("cheat", cheat)

      modelAndView.addObject("deadwood", Hand.countDeadwood(game.playerHand))

      if( cheat ){
        modelAndView.addObject("discardCards", asJavaCollection(renderCards(game.discard)))
        modelAndView.addObject("stockCards",   asJavaCollection(renderCards(game.stock)))
      }

      modelAndView

    } else {

      openSettings = false
      val modelAndView = new ModelAndView("settings")
      modelAndView
    }

  }

  def renderCards(cards: List[Card]) = {
    // cards.map { c => CardRenderer.cardToUnicode(c) }
    cards.map { c => CardRenderer.renderCard(c) }
  }

  def renderCardsAsBacks(cards: List[Card]) = {
    cards.map { c => CardRenderer.cardBack() }
  }

  def dropCard(cardList: String) = {

    val cards = toCards(cardList)
    game = GinGame.drop(game, cards.last)

  }

  def toCards(cardList: String) = {
    val cards = cardList.split('|').filter({ s => { s.length() > 0 } }).toList
    cards.map { (c) => { CardRenderer.stringToCard(c) } }
  }

  def stock() = {
    game = GinGame.stock(game)
    if( checkForGin(true) ){
      status = "You win!!!"
    }
    else {
      status = "Your Discard"
    }
  }

  def computerTurn() = {

    // discard or stock
    game = if (player.stock(game)) {
      lastTake = "stock"
      lastCard = CardRenderer.cardBack()
      GinGame.stock(game)
    } else {
      //
      lastTake = "discard"
      lastCard = CardRenderer.renderCard(game.discard.head)
      GinGame.discard(game)
    }

    if( !checkForGin(false) ){
      game = GinGame.drop(game, player.discard(game))
    }
    else {
      game
    }

    // drop
  }

  def sortPlayerHand(game: GinGame, playerCardList: String) = {
    game.playerHand = toCards(playerCardList)
  }

  def checkForGin(player: Boolean): Boolean = {
    GinGame.detectGin(game, player)
  }

  def undo() : Unit = {

    var games = GinGame.toList(game)

    // take off head
    games = games.tail

    val prev = games.find( (g) => { g.playerTurn } )

    game = prev match {
      case Some(g) => {
        g
      }
      case _ => {
        game
      }
    }

    game.next = None

  }
}

