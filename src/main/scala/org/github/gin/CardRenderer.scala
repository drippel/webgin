package org.github.gin

case class Suit(val value : String)
object Spades extends Suit("A")
object Hearts extends Suit("B")
object Diamonds extends Suit("C")
object Clubs extends Suit("D")

case class Rank(val value : String)
object Ace extends Rank("1")
object Two extends Rank("2")
object Three extends Rank("3")
object Four extends Rank("4")
object Five extends Rank("5")
object Six extends Rank("6")
object Seven extends Rank("7")
object Eight extends Rank("8")
object Nine extends Rank("9")
object Ten extends Rank("A")
object Jack extends Rank("B")
object Knight extends Rank("C")
object Queen extends Rank("D")
object King extends Rank("E")

object CardRenderer {

  def suitToUnicode( suit : Suit ) = {

    suit match {

      case Spades  => { "&#x2660; " }
      case Hearts => { "&#x2665; " }
      case Diamonds => { "&#x2666; " }
      case Clubs => { "&#x2663; " }

    }
  }

  // outlined U+2664	U+2661	U+2662	U+2667
  def cardToUnicode( card : Card ) : String = { cardToUnicode(card.suit,card.rank) }

  def cardToUnicode( suit : Suit, rank : Rank ) : String = {
    "&#x1F0" + suit.value + rank.value +";"
  }

  def cardBack() = { "&#x1F0A0" }
  def redJoker() = { "&#x1F0BF" }
  def blackJoker() = { "&#x1F0CF" }
  def whiteJoker() = { "&#x1F0DF" }

}