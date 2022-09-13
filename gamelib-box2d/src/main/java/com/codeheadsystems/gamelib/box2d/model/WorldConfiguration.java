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

package com.codeheadsystems.gamelib.box2d.model;

import com.badlogic.gdx.math.Vector2;

public class WorldConfiguration {

  public Vector2 gravity = new Vector2(0, -10);
  public boolean doSleep = true;     // If false, it can eat up battery.
  public boolean debug = true;       // sets up your debug renderer.
  public float stepTime = 1 / 60f;     // between 1/45 and 1/300.
  public int velocityIterations = 6; // really don't want this to change
  public int positionIterations = 2; // really don't want this to change
  public float maxFrameTime = 0.25f; // When we want to force the step time.

}
