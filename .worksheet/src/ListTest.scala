object ListTest {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(61); 
  println("Welcome to the Scala worksheet");$skip(34); 

  val l1 = List("A","B","C","D");System.out.println("""l1  : List[String] = """ + $show(l1 ));$skip(26); 

  val l2 = List("C","B");System.out.println("""l2  : List[String] = """ + $show(l2 ));$skip(24); 

  val l3 = l1.diff(l2);System.out.println("""l3  : List[String] = """ + $show(l3 ))}
}
