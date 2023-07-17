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

package com.codeheadsystems.gamelib.core.manager;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Arrow manager.
 */
@Singleton
public class ArrowManager {

    private static final double DEGREES = Math.toRadians(30);
    private static final float COS_0 = (float) Math.cos(DEGREES);
    private static final float SIN_0 = (float) Math.sin(DEGREES);
  /**
   * The constant DEFAULT_WIDTH.
   */
  public static final int DEFAULT_WIDTH = 4;

    private final ShapeRenderer shapeRenderer;

  /**
   * Instantiates a new Arrow manager.
   *
   * @param shapeRenderer the shape renderer
   */
  @Inject
    public ArrowManager(final ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

  /**
   * Render.
   *
   * @param points the points
   */
  public void render(final float[] points) {
        render(points, DEFAULT_WIDTH);
    }

  /**
   * Render.
   *
   * @param points the points
   * @param width  the width
   */
  public void render(final float[] points, final int width) {
        shapeRenderer.rectLine(points[0], points[1], points[2], points[3], width);
        shapeRenderer.rectLine(points[2], points[3], points[4], points[5], width);
        shapeRenderer.rectLine(points[2], points[3], points[6], points[7], width);
    }

  /**
   * Get arrow float [ ].
   *
   * @param x1 the x 1
   * @param y1 the y 1
   * @param x2 the x 2
   * @param y2 the y 2
   * @return the float [ ]
   */
  public float[] getArrow(final float x1, final float y1,
                            final float x2, final float y2) {
        final float l1 = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        final float l2 = l1 / 3f;
        final float length = l2 / l1;
        final float[] points = new float[8];
        // Arrow head https://math.stackexchange.com/questions/1314006/drawing-an-arrow
        points[0] = x1;
        points[1] = y1;
        points[2] = x2;
        points[3] = y2;
        points[4] = x2 + (length * (((x1 - x2) * COS_0) + ((y1 - y2) * SIN_0))); // x3
        points[5] = y2 + (length * (((y1 - y2) * COS_0) - ((x1 - x2) * SIN_0))); // y3
        points[6] = x2 + (length * (((x1 - x2) * COS_0) - ((y1 - y2) * SIN_0))); // x4
        points[7] = y2 + (length * (((y1 - y2) * COS_0) + ((x1 - x2) * SIN_0))); // y4
        return points;
    }

}
