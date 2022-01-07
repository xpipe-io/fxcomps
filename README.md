
## Principles

#### A comp is a node/control factory, not just another fancy wrapper for existing classes

#### A comp should produce a transparent representation of Regions and Controls

In JavaFX, using Skins allows for full flexibility when generating the look and feel for a control.
One limitation of this approach is that the generated node tree is not very transparent
for developers who are especially interested in styling it.
This is caused by the fact that a skin does not expose the information required to style
it completely or even alter it without creating a new Skin class.

A comp is designed to allow developers to easily expose as much information
about the produced node tree structure using the CompStructure class as follows:

If you don't want to expose the detailed structure of your comp, you can also just use a very simple structure:

#### The generation process of a comp can be augmented

As comps are factories, any changes that should be applied to all produced
region instances must be integrated into the factory pipeline. This can be achieved with the Augment class:

#### Properties managed by Comps should be managed by the user, not the Comp itself

#### A comp should not break when used Observables are updated from a thread that is not the platform thread

One common limitation of using JavaFX is that many things break when
calling any method from another thread that is not the platform thread.
While in many cases these issues can be mitigated by wrapping a problematic call in a Platform.runLater(...), 
some problematic instances are harder to fix, for example Observable bindings.
In JavaFX, there is currently no way to propagate changes of an Observable
to other bound Observables using the platform thread, when the original change was made from a different thread.
The FxComps library provides a solution with the PlatformUtil.wrap(...) methods and strongly encourages that
Comps make use of these methods in combination with user-managed properties
to allow for value changes from any thread without issue.
