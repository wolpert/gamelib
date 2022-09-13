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

package com.codeheadsystems.gamelib.entity.entitysystem;

/**
 * Purpose: Execution priorities. Lower is higher priority.
 */
public enum Priorities {

  BASE(0),
  MOVEMENT(20),
  CAMERA(500), // Nothing drawn until after this.
  BACKGROUND(600),
  BACKGROUND_LAYER1(610),
  BACKGROUND_LAYER2(620),
  BACKGROUND_LAYER3(630),
  SPRITES(900),
  PHYSICS(950),
  PLAYER(1000),
  UI(1100);

  private final int priority;

  Priorities(final int priority) {
    this.priority = priority;
  }

  public int priority() {
    return priority;
  }

}
