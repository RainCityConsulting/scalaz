package scalaz

import collection.TraversableLike

trait Empty[E[_]] {
  def empty[A]: E[A]
}

trait Emptys {
  def <∅>[E[_], A](implicit e: Empty[E]): E[A] = e.empty
}

object Empty extends Emptys {
  import Identity._
  import Zero._

  implicit def ZipStreamEmpty: Empty[ZipStream] = new Empty[ZipStream] {
    import ZipStream._
    
    def empty[A] = emptyZipStream
  }

  implicit def OptionEmpty: Empty[Option] = new Empty[Option] {
    def empty[A] = None
  }

  implicit def EitherLeftEmpty[X: Zero]: Empty[PartialApply1Of2[Either.LeftProjection, X]#Flip] = new Empty[PartialApply1Of2[Either.LeftProjection, X]#Flip] {
    def empty[A] = Right(∅[X]).left
  }

  implicit def EitherRightEmpty[X: Zero]: Empty[PartialApply1Of2[Either.RightProjection, X]#Apply] = new Empty[PartialApply1Of2[Either.RightProjection, X]#Apply] {
    def empty[A] = Left(∅[X]).right
  }
  
  implicit def TraversableEmpty[CC[X] <: TraversableLike[X, CC[X]] : CanBuildAnySelf]: Empty[CC] = new Empty[CC] {
    def empty[A] = {
      val builder = implicitly[CanBuildAnySelf[CC]].apply[Nothing, A]
      builder.result
    }
  }

  import java.util._
  import java.util.concurrent._

  implicit def JavaArrayListEmpty: Empty[ArrayList] = new Empty[ArrayList] {
    def empty[A] = new ArrayList[A]
  }

  implicit def JavaHashSetEmpty: Empty[HashSet] = new Empty[HashSet] {
    def empty[A] = new HashSet[A]
  }

  implicit def JavaLinkedHashSetEmpty: Empty[LinkedHashSet] = new Empty[LinkedHashSet] {
    def empty[A] = new LinkedHashSet[A]
  }

  implicit def JavaLinkedListEmpty: Empty[LinkedList] = new Empty[LinkedList] {
    def empty[A] = new LinkedList[A]
  }

  implicit def JavaPriorityQueueEmpty: Empty[PriorityQueue] = new Empty[PriorityQueue] {
    def empty[A] = new PriorityQueue[A]
  }

  implicit def JavaStackEmpty: Empty[Stack] = new Empty[Stack] {
    def empty[A] = new Stack[A]
  }

  implicit def JavaTreeSetEmpty: Empty[TreeSet] = new Empty[TreeSet] {
    def empty[A] = new TreeSet[A]
  }

  implicit def JavaVectorEmpty: Empty[Vector] = new Empty[Vector] {
    def empty[A] = new Vector[A]
  }

  implicit def JavaArrayBlockingQueueEmpty: Empty[ArrayBlockingQueue] = new Empty[ArrayBlockingQueue] {
    def empty[A] = new ArrayBlockingQueue[A](0)
  }

  implicit def JavaConcurrentLinkedQueueEmpty: Empty[ConcurrentLinkedQueue] = new Empty[ConcurrentLinkedQueue] {
    def empty[A] = new ConcurrentLinkedQueue[A]
  }

  implicit def JavaCopyOnWriteArrayListEmpty: Empty[CopyOnWriteArrayList] = new Empty[CopyOnWriteArrayList] {
    def empty[A] = new CopyOnWriteArrayList[A]
  }

  implicit def JavaCopyOnWriteArraySetEmpty: Empty[CopyOnWriteArraySet] = new Empty[CopyOnWriteArraySet] {
    def empty[A] = new CopyOnWriteArraySet[A]
  }

  implicit def JavaLinkedBlockingQueueEmpty: Empty[LinkedBlockingQueue] = new Empty[LinkedBlockingQueue] {
    def empty[A] = new LinkedBlockingQueue[A]
  }

  implicit def JavaSynchronousQueueEmpty: Empty[SynchronousQueue] = new Empty[SynchronousQueue] {
    def empty[A] = new SynchronousQueue[A]
  }
}
