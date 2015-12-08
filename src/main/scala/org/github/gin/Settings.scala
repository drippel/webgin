package org.github.gin

class Settings (
  val allowKnock : Boolean = true,
  val alternateDeal : Boolean = false,
  val initialPass : Boolean = true,
  val undercutBonus : Int = 10,
  val ginBonus : Int = 20,
  val bigGinBonus : Int = 30,
  val matchBonus : Int = 100,
  val lineBonus : Int = 25,
  val shutoutBonus : Int = 100,
  val mahjongGin : Boolean =false,
  val knockPoint  : Int = 10,
  val doubleHand : Boolean = false,
  val matchScoreGoal  : Int = 100,
  val gameScoreGoal  : Int = 500
)

object Settings {

  def default() = { new Settings() }
}