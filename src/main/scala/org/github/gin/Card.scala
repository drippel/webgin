package org.github.gin

class Card( val suit : Suit, val rank : Rank ) {

  override def equals(obj : Any ) : Boolean = {

    if( obj == null ){
      false
    }
    else if( !obj.isInstanceOf[Card] ){
        false
    }
    else {

      val c = obj.asInstanceOf[Card]

      suit.value.equals(c.suit.value) && rank.value.equals(c.rank.value)

    }

  }

}