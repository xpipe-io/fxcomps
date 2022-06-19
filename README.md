[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.xpipe/fxcomps/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.xpipe/fxcomps)
[![javadoc](https://javadoc.io/badge2/io.xpipe/fxcomps/javadoc.svg)](https://javadoc.io/doc/io.xpipe/fxcomps)
[![Build Status](https://github.com/xpipe-io/fxcomps/actions/workflows/publish.yml/badge.svg)](https://github.com/xpipe-io/fxcomps/actions/workflows/publish.yml)

# FxComps (WIP)

The FxComps library provides a new approach to creating JavaFX interfaces and
offers a quicker and more robust user interface development workflow.
This library is compatible and can be used with any other JavaFX library.

It is currently in early development.

## Principles

#### A comp is a Node/Region factory, not just another fancy wrapper for existing classes

By using a factory architecture, the scene contents can be rebuilt entirely.
See the [hot reload](#Hot-Reload) on why this useful.

#### A comp should produce a transparent representation of Regions and Controls

In JavaFX, using Skins allows for full flexibility when generating the look and feel for a control.
One limitation of this approach is that the generated node tree is not very transparent
for developers who are especially interested in styling it.
This is caused by the fact that a skin does not expose the information required to style
it completely or even alter it without creating a new Skin class.

A comp is designed to allow developers to easily expose as much information
about the produced node tree structure using the CompStructure class.
In case you don't want to expose the detailed structure of your comp,
you can also just use a very simple structure.

#### The generation process of a comp can be augmented

As comps are factories, any changes that should be applied to all produced
region instances must be integrated into the factory pipeline. This can be achieved with the Augment class.

#### Properties used by Comps should be managed by the user, not the Comp itself

This allows Comps to only be a thin wrapper around already existing
Observables/Properties and gives the user the ability to complete control the handling of Properties.
This approach is also required for the next point.

#### A comp should not break when used Observables are updated from a thread that is not the platform thread

One common limitation of using JavaFX is that many things break when
calling any method from another thread that is not the platform thread.
While in many cases these issues can be mitigated by wrapping a problematic call in a Platform.runLater(...), 
some problematic instances are harder to fix, for example Observable bindings.
In JavaFX, there is currently no way to propagate changes of an Observable
to other bound Observables using the platform thread, when the original change was made from a different thread.
The FxComps library provides a solution with the PlatformUtil.wrap(...) methods and strongly encourages that
Comps make use of these methods in combination with user-managed properties
to allow for value changes for Observables from any thread without issue.

## Hot reload

The reason a Comp is designed to be a factory is to allow for hot
reloading your created GUI in conjunction with the hot-reload functionality in your IDE:

````java
    void setupReload(Scene scene, Comp<?> content) {
        var contentR = content.createRegion();
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.F5)) {
                var newContentR = content.createRegion();
                scene.setRoot(newContentR);
                event.consume();
            }
        });
    }
````

If you for example bind your IDE Hot Reload to F4 and your Scene reload listener to F5,
you can almost instantly apply any changes made to your GUI code without restarting.
You can also implement something similar to reload your stylesheets and translations.


## Contained contents

Aside from the base classes needed to implement the principles listed above,
this library also comes with a few Comp implementations and some Augments.
These are very general implementations and can be seen as example implementations.

#### Comps
- [AspectComp](src/main/java/io/xpipe/fxcomps/comp/AspectComp.java): Tries to maintain a fixed aspect ratio of
  the contained Comp and actively changes its preferred size for that.
  Requires that exactly one preferred size property, width or height, must be bound.
- [HorizontalComp](src/main/java/io/xpipe/fxcomps/comp/HorizontalComp.java) / (VerticalComp)[]: Simple Comp implementation to create a HBox/VBox using Comps as input
- [StackComp](src/main/java/io/xpipe/fxcomps/comp/StackComp.java): Simple Comp implementation to easily create a stack pane using Comps as input
- [SectionComp](src/main/java/io/xpipe/fxcomps/comp/SectionComp.java): A Comp that layouts its contents as a titled section where each entry has a name
- [SvgComp](src/main/java/io/xpipe/fxcomps/comp/SvgComp.java): A Comp that creates an `.svg` image node using a WebView
- [WrapperComp](src/main/java/io/xpipe/fxcomps/comp/WrapperComp.java): A Comp that represents an anonymous Comp and is defined by a Supplier<Region>

#### Augments
- [GrowAugment](src/main/java/io/xpipe/fxcomps/augment/GrowAugment.java): Binds the width/height of a Comp to its parent, adjusted for parent padding
- [PopupMenuComp](src/main/java/io/xpipe/fxcomps/augment/PopupMenuAugment.java): Allows you to show a context menu when a comp is left-clicked in addition to right-click


## Creating your own Comps

As the central idea of this library is that you create your own Comps, it is designed to be very simple:

````java
        var b = Comp.of(() -> new Button("Button"));
        var l = Comp.of(() -> new Label("Label"));
        
        // Create an HBox factory and apply some Augments to it
        var layout = new HorizontalComp(List.of(b, l))
                .apply(struc -> struc.get().setAlignment(Pos.CENTER))
                .apply(GrowAugment.create(true, true))
                .styleClass("layout");
        
        // You can now create node instances of your layout
        var region = layout.createRegion();
````

## Installation

Note that as this library is relatively new and is primarily used for internal projects, it might not be production ready for other purposes.
If you are still interested in trying it out, you can find it in the
[Maven Central Repository](https://search.maven.org/artifact/io.xpipe/fxcomps).
The javadocs are available at [javadoc.io](https://javadoc.io/doc/io.xpipe/fxcomps).