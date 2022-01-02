package recfun

object RecFun extends RecFunInterface:

  def main(args: Array[String]): Unit =
    println("Pascal's Triangle")
    for row <- 0 to 10 do
      for col <- 0 to row do
        print(s"${pascal(col, row)} ")
      println()

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = (c, r) match {
    case (0, _) | (_, 0) => 1
    case (x, y) if x == y => 1
    case _ => pascal(c-1, r-1) + pascal(c, r-1)
  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def balanceInner(charsLeft: List[Char], balanceCount: Int = 0): Boolean = charsLeft match {
      case Nil => balanceCount == 0
      case x::xs => if(balanceCount < 0){
        false
      }else{
        val balanceIncr: Int = x match {
          case ')' => -1
          case '(' => 1
          case _ => 0
        }
        balanceInner(xs, balanceCount + balanceIncr)
      }
    }

    balanceInner(chars)
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = money match {
    case 0 => 1
    case x if x<0 => 0
    case x if x>0 => coins match {
      case Nil => 0
      case c::cs => countChange(money - c, coins) +
          countChange(money, cs)
    }
  }
