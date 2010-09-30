package scalaz

/**
 * Covariant function application in an environment. i.e. a covariant Functor.
 *
 * <p>
 * All functor instances must satisfy 2 laws:
 * <ol>
 * <li><strong>identity</strong><br/><code>forall a. a == fmap(a, identity)</code></li>
 * <li><strong>composition</strong><br/><code>forall a f g. fmap(a, f compose g) == fmap(fmap(g, a), f)</code></li>
 * </ol>
 * </p>
 */
trait Functor[F[_]] extends InvariantFunctor[F] {
  def fmap[A, B](r: F[A], f: A => B): F[B]

  final def xmap[A, B](ma: F[A], f: A => B, g: B => A) = fmap(ma, f)
}

object Functor {
  import Identity._
  import MA._

  implicit def IdentityFunctor: Functor[Identity] = new Functor[Identity] {
    def fmap[A, B](r: Identity[A], f: A => B) = f(r.value)
  }

  implicit def TraversableFunctor[CC[X] <: collection.TraversableLike[X, CC[X]] : CanBuildAnySelf]: Functor[CC] = new Functor[CC] {
    def fmap[A, B](r: CC[A], f: A => B) = {
      implicit val cbf = implicitly[CanBuildAnySelf[CC]].builder[A, B]
      r map f
    }
  }

  implicit def NonEmptyListFunctor = new Functor[NonEmptyList] {
    def fmap[A, B](r: NonEmptyList[A], f: A => B) = r map f
  }

  implicit def ConstFunctor[BB: Monoid] = new Functor[PartialApply1Of2[Const, BB]#Apply] {
    def fmap[A, B](r: Const[BB, A], f: (A) => B) = Const(r.value)
  }

  implicit def StateFunctor[S] = new Functor[PartialApply1Of2[State, S]#Apply] {
    def fmap[A, B](r: State[S, A], f: A => B) = r map f
  }

  implicit def ZipStreamFunctor: Functor[ZipStream] = new Functor[ZipStream] {
    import StreamW._

    def fmap[A, B](r: ZipStream[A], f: A => B) = r.value map f ʐ
  }

  implicit def Tuple1Functor: Functor[Tuple1] = new Functor[Tuple1] {
    def fmap[A, B](r: Tuple1[A], f: A => B) = Tuple1(f(r._1))
  }

  implicit def Tuple2Functor[R]: Functor[PartialApply1Of2[Tuple2, R]#Apply] = new Functor[PartialApply1Of2[Tuple2, R]#Apply] {
    def fmap[A, B](r: (R, A), f: A => B) = (r._1, f(r._2))
  }

  implicit def Tuple3Functor[R, S]: Functor[PartialApply2Of3[Tuple3, R, S]#Apply] = new Functor[PartialApply2Of3[Tuple3, R, S]#Apply] {
    def fmap[A, B](r: (R, S, A), f: A => B) = (r._1, r._2, f(r._3))
  }

  implicit def Tuple4Functor[R, S, T]: Functor[PartialApply3Of4[Tuple4, R, S, T]#Apply] = new Functor[PartialApply3Of4[Tuple4, R, S, T]#Apply] {
    def fmap[A, B](r: (R, S, T, A), f: A => B) = (r._1, r._2, r._3, f(r._4))
  }

  implicit def Tuple5Functor[R, S, T, U]: Functor[PartialApply4Of5[Tuple5, R, S, T, U]#Apply] = new Functor[PartialApply4Of5[Tuple5, R, S, T, U]#Apply] {
    def fmap[A, B](r: (R, S, T, U, A), f: A => B) = (r._1, r._2, r._3, r._4, f(r._5))
  }

  implicit def Tuple6Functor[R, S, T, U, V]: Functor[PartialApply5Of6[Tuple6, R, S, T, U, V]#Apply] = new Functor[PartialApply5Of6[Tuple6, R, S, T, U, V]#Apply] {
    def fmap[A, B](r: (R, S, T, U, V, A), f: A => B) = (r._1, r._2, r._3, r._4, r._5, f(r._6))
  }

  implicit def Tuple7Functor[R, S, T, U, V, W]: Functor[PartialApply6Of7[Tuple7, R, S, T, U, V, W]#Apply] = new Functor[PartialApply6Of7[Tuple7, R, S, T, U, V, W]#Apply] {
    def fmap[A, B](r: (R, S, T, U, V, W, A), f: A => B) = (r._1, r._2, r._3, r._4, r._5, r._6, f(r._7))
  }

  implicit def Function0Functor: Functor[Function0] = new Functor[Function0] {
    def fmap[A, B](r: Function0[A], f: A => B) = new Function0[B] {
      def apply = f(r.apply)
    }
  }

  implicit def Function1Functor[R]: Functor[PartialApply1Of2[Function1, R]#Apply] = new Functor[PartialApply1Of2[Function1, R]#Apply] {
    def fmap[A, B](r: R => A, f: A => B) = r andThen f
  }

  implicit def Function2Functor[R, S]: Functor[PartialApply2Of3[Function2, R, S]#Apply] = new Functor[PartialApply2Of3[Function2, R, S]#Apply] {
    def fmap[A, B](r: (R, S) => A, f: A => B) = (t1: R, t2: S) => f(r(t1, t2))
  }

  implicit def Function3Functor[R, S, T]: Functor[PartialApply3Of4[Function3, R, S, T]#Apply] = new Functor[PartialApply3Of4[Function3, R, S, T]#Apply] {
    def fmap[A, B](r: (R, S, T) => A, f: A => B) = (t1: R, t2: S, t3: T) => f(r(t1, t2, t3))
  }

  implicit def Function4Functor[R, S, T, U]: Functor[PartialApply4Of5[Function4, R, S, T, U]#Apply] = new Functor[PartialApply4Of5[Function4, R, S, T, U]#Apply] {
    def fmap[A, B](r: (R, S, T, U) => A, f: A => B) = (t1: R, t2: S, t3: T, t4: U) => f(r(t1, t2, t3, t4))
  }

  implicit def Function5Functor[R, S, T, U, V]: Functor[PartialApply5Of6[Function5, R, S, T, U, V]#Apply] = new Functor[PartialApply5Of6[Function5, R, S, T, U, V]#Apply] {
    def fmap[A, B](r: (R, S, T, U, V) => A, f: A => B) = (t1: R, t2: S, t3: T, t4: U, t5: V) => f(r(t1, t2, t3, t4, t5))
  }

  implicit def Function6Functor[R, S, T, U, V, W]: Functor[PartialApply6Of7[Function6, R, S, T, U, V, W]#Apply] = new Functor[PartialApply6Of7[Function6, R, S, T, U, V, W]#Apply] {
    def fmap[A, B](r: (R, S, T, U, V, W) => A, f: A => B) = (t1: R, t2: S, t3: T, t4: U, t5: V, t6: W) => f(r(t1, t2, t3, t4, t5, t6))
  }

  implicit def OptionFunctor: Functor[Option] = new Functor[Option] {
    def fmap[A, B](r: Option[A], f: A => B) = r map f
  }

  implicit def FirstOptionFunctor: Functor[FirstOption] = new Functor[FirstOption] {
    import OptionW._

    def fmap[A, B](r: FirstOption[A], f: A => B) = (r.value map f).fst
  }

  implicit def LastOptionFunctor: Functor[LastOption] = new Functor[LastOption] {
    import OptionW._
    
    def fmap[A, B](r: LastOption[A], f: A => B) = (r.value map f).lst
  }

  implicit def EitherLeftFunctor[X]: Functor[PartialApply1Of2[Either.LeftProjection, X]#Flip] = new Functor[PartialApply1Of2[Either.LeftProjection, X]#Flip] {
    def fmap[A, B](r: Either.LeftProjection[A, X], f: A => B) = r.map(f).left
  }

  implicit def EitherRightFunctor[X]: Functor[PartialApply1Of2[Either.RightProjection, X]#Apply] = new Functor[PartialApply1Of2[Either.RightProjection, X]#Apply] {
    def fmap[A, B](r: Either.RightProjection[X, A], f: A => B) = r.map(f).right
  }

  implicit def ResponderFunctor: Functor[Responder] = new Functor[Responder] {
    def fmap[A, B](r: Responder[A], f: A => B) = r map f
  }

  implicit def IterVFunctor[X]: Functor[PartialApply1Of2[IterV, X]#Apply] = new Functor[PartialApply1Of2[IterV, X]#Apply] {
    import IterV._
    def fmap[A, B](r: IterV[X, A], f: A => B) = {
      r fold (
              done = (a, i) => Done(f(a), i),
              cont = k => Cont(i => fmap(k(i), f))
              )
    }
  }

  implicit def KleisliFunctor[M[_], P](implicit ff: Functor[M]): Functor[PartialApplyKA[Kleisli, M, P]#Apply] = new Functor[PartialApplyKA[Kleisli, M, P]#Apply] {
    import Kleisli._

    def fmap[A, B](k: Kleisli[M, P, A], f: A => B): Kleisli[M, P, B] = ☆((p: P) => ff.fmap(k(p), f))
  }

  import java.util.concurrent.Callable

  implicit def CallableFunctor: Functor[Callable] = new Functor[Callable] {
    def fmap[A, B](r: Callable[A], f: A => B) = new Callable[B] {
      def call = f(r.call)
    }
  }

  import java.util.Map.Entry
  import java.util.AbstractMap.SimpleImmutableEntry

  implicit def MapEntryFunctor[X]: Functor[PartialApply1Of2[Entry, X]#Apply] = new Functor[PartialApply1Of2[Entry, X]#Apply] {
    def fmap[A, B](r: Entry[X, A], f: A => B) = new SimpleImmutableEntry(r.getKey, f(r.getValue))
  }

  implicit def ValidationFunctor[X]: Functor[PartialApply1Of2[Validation, X]#Apply] = new Functor[PartialApply1Of2[Validation, X]#Apply] {
    def fmap[A, B](r: Validation[X, A], f: A => B) = r match {
      case Success(a) => Success(f(a))
      case Failure(e) => Failure(e)
    }
  }

  implicit def ValidationFailureFunctor[X]: Functor[PartialApply1Of2[FailProjection, X]#Flip] = new Functor[PartialApply1Of2[FailProjection, X]#Flip] {
    def fmap[A, B](r: FailProjection[A, X], f: A => B) = (r.validation match {
      case Success(a) => Success(a)
      case Failure(e) => Failure(f(e))
    }).fail
  }

  implicit def ZipperFunctor: Functor[Zipper] = new Functor[Zipper] {
    import Zipper._

    def fmap[A, B](z: Zipper[A], f: A => B) = zipper(z.lefts map f, f(z.focus), z.rights map f)
  }

  implicit def TreeFunctor: Functor[Tree] = new Functor[Tree] {
    import Tree._
    
    def fmap[A, B](t: Tree[A], f: A => B): Tree[B] = node(f(t.rootLabel), t.subForest.map(fmap(_: Tree[A], f)))
  }

  implicit def TreeLocFunctor: Functor[TreeLoc] = new Functor[TreeLoc] {
    import TreeLoc._

    def fmap[A, B](t: TreeLoc[A], f: A => B): TreeLoc[B] = {
      val ff = (_: Tree[A]).map(f)
      loc(t.tree map f, t.lefts map ff, t.rights map ff,
        t.parents.map((ltr) => (ltr._1 map ff, f(ltr._2), ltr._3 map ff)))
    }
  }

  import FingerTree._

  implicit def ViewLFunctor[S[_]](implicit s: Functor[S]): Functor[PartialType2[ViewL, S]#Apply] = new Functor[PartialType2[ViewL, S]#Apply] {
    def fmap[A, B](t: ViewL[S, A], f: A => B): ViewL[S, B] =
      t.fold(EmptyL[S, B], (x, xs) => f(x) &: s.fmap(xs, f))
  }

  implicit def ViewRFunctor[S[_]](implicit s: Functor[S]): Functor[PartialType2[ViewR, S]#Apply] = new Functor[PartialType2[ViewR, S]#Apply] {
    def fmap[A, B](t: ViewR[S, A], f: A => B): ViewR[S, B] =
      t.fold(EmptyR[S, B], (xs, x) => s.fmap(xs, f) :& f(x))
  }

  import scalaz.concurrent.Promise
  implicit def PromiseFunctor: Functor[Promise] = new Functor[Promise] {
    import Promise._
    
    def fmap[A, B](t: Promise[A], f: A => B): Promise[B] = {
      t.bind(a => promise(f(a))(t.strategy))
    }
  }

  // todo use this rather than all the specific java.util._ Functor instances once the scala bug is fixed.
  // http://lampsvn.epfl.ch/trac/scala/ticket/2782
  /*implicit*/
  def JavaCollectionFunctor[S[X] <: java.util.Collection[X] : Empty]: Functor[S] = new Functor[S] {
    import Empty._

    def fmap[A, B](r: S[A], f: A => B) = {
      val a: S[B] = <∅>[S, B]
      val i = r.iterator
      while (i.hasNext)
        a.add(f(i.next))
      a
    }
  }

  import java.util._
  import java.util.concurrent._

  implicit def JavaArrayListFunctor: Functor[ArrayList] = JavaCollectionFunctor

  implicit def JavaLinkedListFunctor: Functor[LinkedList] = JavaCollectionFunctor

  implicit def JavaPriorityQueueFunctor: Functor[PriorityQueue] = JavaCollectionFunctor

  implicit def JavaStackFunctor: Functor[Stack] = JavaCollectionFunctor

  implicit def JavaVectorFunctor: Functor[Vector] = JavaCollectionFunctor

  implicit def JavaArrayBlockingQueueFunctor: Functor[ArrayBlockingQueue] = JavaCollectionFunctor

  implicit def JavaConcurrentLinkedQueueFunctor: Functor[ConcurrentLinkedQueue] = JavaCollectionFunctor

  implicit def JavaCopyOnWriteArrayListFunctor: Functor[CopyOnWriteArrayList] = JavaCollectionFunctor

  implicit def JavaLinkedBlockingQueueFunctor: Functor[LinkedBlockingQueue] = JavaCollectionFunctor

  implicit def JavaSynchronousQueueFunctor: Functor[SynchronousQueue] = JavaCollectionFunctor
}
