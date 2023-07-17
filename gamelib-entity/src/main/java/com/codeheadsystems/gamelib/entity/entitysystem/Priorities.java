/*
 *   Copyright (c) 2023. Ned Wolpert <ned.wolpert@gmail.com>
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

  /**
   * Base priorities.
   */
  BASE(0),
  /**
   * Movement priorities.
   */
  MOVEMENT(20),
  /**
   * Camera priorities.
   */
  CAMERA(500), // Nothing drawn until after this.
  /**
   * Background priorities.
   */
  BACKGROUND(600),
  /**
   * Background layer 1 priorities.
   */
  BACKGROUND_LAYER1(610),
  /**
   * Background layer 2 priorities.
   */
  BACKGROUND_LAYER2(620),
  /**
   * Background layer 3 priorities.
   */
  BACKGROUND_LAYER3(630),
  /**
   * Sprites priorities.
   */
  SPRITES(900),
  /**
   * Physics priorities.
   */
  PHYSICS(950),
  /**
   * Player priorities.
   */
  PLAYER(1000),
  /**
   * Ui priorities.
   */
  UI(1100);

  private final int priority;

  Priorities(final int priority) {
    this.priority = priority;
  }

  /**
   * Priority int.
   *
   * @return the int
   */
  public int priority() {
    return priority;
  }

}
