package scalaz
package wrap

sealed trait ShortW {

  import newtypes._

  val value: Short

  def multiplication: ShortMultiplication =
    Pack.pack[Short, ShortMultiplication](value)

  def ∏ : ShortMultiplication =
    multiplication
}

trait ShortWs {
  implicit def ShortTo(n: Short): ShortW = new ShortW {
    val value = n
  }
}