/*
 *   Copyright (c) 2022. Ned Wolpert <ned.wolpert@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.codeheadsystems.gamelib.box2d.builder;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldComponentBuilder {

  @Inject
  public WorldComponentBuilder() {

  }

  /**
   * Do this with the shape you want... makes sure to dispose of the shape properly.
   * Forces you to return a fixture from the shape, which we will dispose of.
   */
  public <T extends Shape> Fixture doWithShape(final Supplier<T> shapeSupplier,
                                               final Function<T, Fixture> fixtureFunction) {
    final T shape = shapeSupplier.get();
    final Fixture fixture = fixtureFunction.apply(shape);
    shape.dispose();
    return fixture;
  }

}
