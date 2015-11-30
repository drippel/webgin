object ListTest {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val l1 = List("A","B","C","D")                  //> l1  : List[String] = List(A, B, C, D)

  val l2 = List("C","B")                          //> l2  : List[String] = List(C, B)

  val l3 = l1.diff(l2)                            //> l3  : List[String] = List(A, D)
}