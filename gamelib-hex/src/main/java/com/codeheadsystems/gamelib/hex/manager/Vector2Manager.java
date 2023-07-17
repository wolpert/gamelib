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

package com.codeheadsystems.gamelib.hex.manager;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.core.util.PoolerImpl;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Vector 2 manager.
 */
@Singleton
public class Vector2Manager extends PoolerImpl<Vector2> {

    private static final Logger LOGGER = logger(Vector2Manager.class);

  /**
   * Instantiates a new Vector 2 manager.
   */
  @Inject
    public Vector2Manager() {
        super(Vector2::new);
        LOGGER.debug("Vector2Manager()");
    }
}
