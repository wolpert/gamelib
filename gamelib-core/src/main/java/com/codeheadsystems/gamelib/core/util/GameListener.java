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

package com.codeheadsystems.gamelib.core.util;

import com.badlogic.gdx.Game;

/**
 * Implement this and provide it in a module if you need to have the game variable
 * set locally once the main game launcher is active.
 */
@FunctionalInterface
public interface GameListener {

  /**
   * Sets game.
   *
   * @param game the game
   */
  void setGame(Game game);

}
