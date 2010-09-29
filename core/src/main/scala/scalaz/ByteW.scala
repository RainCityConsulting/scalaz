package scalaz

sealed trait ByteW extends PimpedType[Byte] {
  import Multiplication._
  
  def ∏ : ByteMultiplication = multiplication(value)
}

trait Bytes {
  implicit def ByteTo(n: Byte): ByteW = new ByteW {
    val value = n
  }
}

object ByteW extends Bytes
