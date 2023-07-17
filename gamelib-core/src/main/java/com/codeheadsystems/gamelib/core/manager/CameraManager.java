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

package com.codeheadsystems.gamelib.core.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.core.util.LoggerHelper;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Optional class used to manage the camera.
 */
@Singleton
public class CameraManager {
    private static final Logger LOGGER = LoggerHelper.logger(CameraManager.class);

    private final ThreadLocal<Vector3> vector3ThreadLocal = ThreadLocal.withInitial(Vector3::new);
    private final OrthographicCamera camera;

  /**
   * Instantiates a new Camera manager.
   *
   * @param camera the camera
   */
  @Inject
    public CameraManager(final OrthographicCamera camera) {
        LOGGER.info("CameraManager()");
        this.camera = camera;
    }

  /**
   * Width float.
   *
   * @return the float
   */
  public float width() {
        return camera.viewportWidth;
    }

  /**
   * Height float.
   *
   * @return the float
   */
  public float height() {
        return camera.viewportHeight;
    }

  /**
   * Use this method to covert the points on the screen to the location in the camera field.
   * The vector returned is unique to this thread. Retrieve the values from it and ignore it. (Do not
   * cache it.) This mechanism reduces the need to create Vector3 objects, and reuses the internals of
   * the camera. Garbage collection bad.
   *
   * @param screenX from mouse click.
   * @param screenY from mouse click.
   * @return thread local vector3.
   */
  public Vector3 unproject(final int screenX, final int screenY) {
        return camera.unproject(vector3ThreadLocal.get().set(screenX, screenY, 0));
    }

}
