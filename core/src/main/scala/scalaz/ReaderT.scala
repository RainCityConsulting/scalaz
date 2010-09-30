package scalaz

// Reader monad transformer
// Can be used to add environment reading capabilities to other monads
case class ReaderT[M[_], R, A](value: R => M[A]) extends NewType[R => M[A]]

object ReaderT {
  import MA._

  implicit def readerTPure[M[_], R](implicit m: Pure[M]) =
    new Pure[PartialApplyKA[ReaderT, M, R]#Apply] {
      def pure[A](a: => A) = ReaderT((r: R) => m.pure(a))
    }

  implicit def readerTBind[M[_]:Bind, R] =
    new Bind[PartialApplyKA[ReaderT, M, R]#Apply] {
      def bind[A,B](m: ReaderT[M, R, A], k: A => ReaderT[M, R, B]) =
        ReaderT((r: R) => m.value(r) >>= (a => k(a).value(r)))
    }

}
