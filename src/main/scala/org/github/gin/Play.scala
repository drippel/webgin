package org.github.gin

case class Play( val computer : Boolean ) { }

class Start() extends Play(false)
class Deal( override val computer : Boolean ) extends Play(computer)
class Discard( override val computer : Boolean, card : Card ) extends Play(computer)
class Stock( override val computer : Boolean, card : Card ) extends Play(computer)
class Drop( override val computer : Boolean, card : Card ) extends Play(computer)
class Knock( override val computer : Boolean ) extends Play(computer)
class Gin( override val computer : Boolean ) extends Play(computer)
class End( override val computer : Boolean ) extends Play(computer)
class Draw( override val computer : Boolean ) extends Play(computer)
class Pass( override val computer : Boolean ) extends Play(computer)
class Sort( override val computer : Boolean ) extends Play(computer)