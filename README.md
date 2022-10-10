# Gamelib

This project contains common routines I use with libGDX for games I wrote.
It was a way to quickly start a project that was entity based, with or
without hex coordinates and/or box2d support. Of course, dagger rules.
Originally android friendly just missing a better version of guava... and
its now JDK17 required. So no clue on the android support anymore.

## Licensed

[Apache 2.0](https://opensource.org/licenses/Apache-2.0)

Basically, you do do not owe me money to use this in your own project,
and you can charge other people for stuff you do, just give credit where
it's due. See the LICENSE file for details.

## Using

Here are the full sets of libraries available.

*WARNING* gamelib-net-client and gamelib-net-server are preliminary only.
They are only available as `1.0.0-SNAPSHOT` versions

```groovy

ext {
    gamelibVersion = '1.0.0'
}
dependencies {
    implementation "com.codeheadsystems:gamelib-core:${gamelibVersion}"
    implementation "com.codeheadsystems:gamelib-box2d:${gamelibVersion}"
    implementation "com.codeheadsystems:gamelib-entity:${gamelibVersion}"
    implementation "com.codeheadsystems:gamelib-hex:${gamelibVersion}"
    implementation "com.codeheadsystems:gamelib-net-client:1.0.0-SNAPSHOT"
    implementation "com.codeheadsystems:gamelib-net-server:1.0.0-SNAPSHOT"
}
```

## Sample usage

If you want an example in how to use it, clone https://github.com/wolpert/sample-game
and you can run
```
gradle :desktop:run
gradle :desktop:entity
gradle :desktop:hex
gradle :desktop:box2d
```



## Current status

*IN PROGRESS*

I opened source this to see if it can help others, and to get feedback in
the new game communications library I'm doing. Basically I got bored and 
wanted to write a client/server framework that was fairly high performant.
Currently uses JSON but I plan on switching to protobufs. (Eventually)

I'll have a sample client in Java and Rust when its done. 

## What this is not

This is not well tested via jUnit tests, though some of it is. This is not
the best way to use libGDX. This was what I did for Java games I wrote. 
