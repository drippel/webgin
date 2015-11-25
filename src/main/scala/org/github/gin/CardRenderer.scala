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

  val suits = List(Spades,Hearts,Diamonds,Clubs)
  val ranks = List(Ace,Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten,Jack,Queen,King)

  def suitToUnicode( suit : Suit ) = {

    suit match {

      case Spades  => { "&#x2660; " }
      case Hearts => { "&#x2665; " }
      case Diamonds => { "&#x2666; " }
      case Clubs => { "&#x2663; " }

    }
  }

  def renderCard( card : Card ) : String = {

    if( card.suit.equals(Spades) || card.suit.equals(Clubs) ){
      "<font color='black'>" + cardToUnicode(card) + "</font>"
    }
    else{
      "<font color='red'>" + cardToUnicode(card) + "</font>"
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

  def stringToCard( s : String ) : Card = {

    var escape = unicodeEscape(s).reverse

    var r = escape.head.toString

    var rank = ranks.find( { k => k.value.equals(r) } )

    escape = escape.tail

    var t = escape.head.toString

    var suit = suits.find( { k => k.value.equals(t) } )

    new Card(suit.get, rank.get)

  }

   def unicodeEscape(s: String): String = {
    val sb = new StringBuilder();
    for (i <- 0 until s.length()) {
      val c = s.charAt(i);
      if ((c >> 7) > 0) {
        sb.append("\\u");
        sb.append(hexChar((c >> 12) & 0xF)); // append the hex character for the left-most 4-bits
        sb.append(hexChar((c >> 8) & 0xF)); // hex for the second group of 4-bits from the left
        sb.append(hexChar((c >> 4) & 0xF)); // hex for the third group
        sb.append(hexChar(c & 0xF)); // hex for the last group, e.g., the right most 4-bits
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  def hexChar = List('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F');

}