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


@Scope("session")
@Controller
@RequestMapping(Array("/gin"))
class GinController {

  @Autowired
  var httpSession : HttpSession = null

  var game = GinGame.createGame()

    @RequestMapping( method = Array( RequestMethod.GET ) )
    def welcome() =  { buildModelAndView() }

    @RequestMapping( method = Array( RequestMethod.POST ) )
    def post( @RequestParam(required=true, value="action") action : String ) =  {


      action match {
        case "new" => { newGame() }
        case "discard" => { discard() }
        case _ => { Console.print("hmmm...") }
      }

      buildModelAndView();
    }

    def newGame() = {

      game = GinGame.startGame(game)

    }

    def discard() = {
      game = GinGame.discard(game)
    }

    def buildModelAndView() = {

      val modelAndView = new ModelAndView("index")
      modelAndView.addObject("game",game)
      modelAndView.addObject("computerHand", asJavaCollection(renderCards(game.computerHand)))
      modelAndView.addObject("playerHand", asJavaCollection( renderCards(game.playerHand)))
      modelAndView.addObject("discard", asJavaCollection(renderCards(game.discard)))
      modelAndView.addObject("stock", asJavaCollection(renderCards(game.stock)))


      val dcard = if( game.discard.isEmpty ){
        CardRenderer.whiteJoker()
      }
      else {
        CardRenderer.cardToUnicode(game.discard.head)
      }
      modelAndView.addObject("discardCard", dcard)
      modelAndView.addObject("stockCard",  CardRenderer.cardBack())

      modelAndView

    }

    def renderCards( cards : List[Card] ) = {

      cards.map { c => CardRenderer.cardToUnicode(c) }

    }
}

