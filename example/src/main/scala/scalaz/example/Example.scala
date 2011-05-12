package scalaz.example


object Example {
  def run {
    ExampleApplicative.run
    ExampleArrow.run
    ExampleBifunctor.run
    ExampleBoolean.run
    ExampleBKTree.run
    ExampleCategory.run
    ExampleContravariant.run
    ExampleComp.run
    ExampleDistance.run
    ExampleEndo.run
    ExampleFunctor.run
    ExampleFold.run
    ExampleEqual.run
    ExampleKleisli.run
    ExampleIdentity.run
    ExampleList.run
    ExampleMonad.run
    ExampleMonoid.run
    ExamplePlus.run
    ExampleState.run
    ExampleTree.run
    ExampleTraverse.run
    ExampleValidation.run
    geo.ExampleVincenty.run
    concurrent.ExampleActor.run
    concurrent.HammerTime.run
    WordCount.wordCount
    iteratees.ExampleIteratee.run
  }

  def main(args: Array[String]) = run
}
