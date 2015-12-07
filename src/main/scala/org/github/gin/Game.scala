package org.github.gin

import scala.collection.mutable.ListBuffer

class Game {

  val matches = ListBuffer[Match]()

  var computerScore = 0
  var playerScore = 0

  var computerMatches = 0
  var playerMatches = 0

  var computerHands = 0
  var playerHands = 0
}