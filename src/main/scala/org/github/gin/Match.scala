package org.github.gin

import scala.collection.mutable.ListBuffer

class Match {

  val rounds = ListBuffer[GinGame]()

  var computerScore = 0
  var playerScore = 0

  var computerHands = 0
  var playerHands = 0
}