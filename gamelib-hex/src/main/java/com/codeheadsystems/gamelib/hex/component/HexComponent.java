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

package com.codeheadsystems.gamelib.hex.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.codeheadsystems.gamelib.hex.manager.LayoutManager;
import com.codeheadsystems.gamelib.hex.model.Hex;
import com.codeheadsystems.gamelib.hex.model.Layout;

/**
 * The type Hex component.
 */
public class HexComponent implements Pool.Poolable, Component {

    private Hex hex;
    private Polygon polygon;
    private float[][] triangles;
    private float originX, originY, width;

    @Override
    public void reset() {
        originX = 0;
        originY = 0;
        width = 0;
        hex = null;
        polygon = null;
        if (triangles != null) {
            for (int i = 0; i < triangles.length; i++) {
                triangles[i] = null;
            }
            triangles = null;
        }
    }

  /**
   * Initialize hex component.
   *
   * @param hex           the hex
   * @param layout        the layout
   * @param layoutManager the layout manager
   * @return the hex component
   */
  public HexComponent initialize(final Hex hex,
                                   final Layout layout,
                                   final LayoutManager layoutManager) {
        final float[] vertices = layoutManager.vertices(layout, hex);
        this.polygon = new Polygon(vertices);
        this.hex = hex;
        final Vector2 center = layoutManager.hexToPixel(layout, hex);
        this.polygon.setOrigin(center.x, center.y);
        this.polygon.setPosition(center.x, center.y);
        final int maxsize = vertices.length - 2;
        triangles = new float[vertices.length / 2][];
        for (int i = 0; i < maxsize; i += 2) {
            triangles[i / 2] = new float[]{center.x, center.y, vertices[i], vertices[i + 1], vertices[i + 2], vertices[i + 3]};
        }
        triangles[triangles.length - 1] = new float[]{center.x, center.y, vertices[0], vertices[1], vertices[vertices.length - 2], vertices[vertices.length - 1]};
        // Cache the meaningful values.
        originX = polygon.getOriginX();
        originY = polygon.getOriginY();
        width = polygon.getBoundingRectangle().getWidth();
        return this;
    }

  /**
   * Triangles float [ ] [ ].
   *
   * @return the float [ ] [ ]
   */
  public float[][] triangles() {
        return triangles;
    }

  /**
   * Vertices float [ ].
   *
   * @return the float [ ]
   */
  public float[] vertices() {
        if (polygon != null) {
            return polygon.getVertices();
        } else {
            return null;
        }
    }

  /**
   * Hex hex.
   *
   * @return the hex
   */
  public Hex hex() {
        return hex;
    }

  /**
   * Polygon polygon.
   *
   * @return the polygon
   */
  public Polygon polygon() {
        return polygon;
    }

  /**
   * Is hex boolean.
   *
   * @param otherHex the other hex
   * @return the boolean
   */
  public boolean isHex(final Hex otherHex) {
        if (hex != null) {
            return hex.equals(otherHex);
        } else {
            return false;
        }
    }

  /**
   * Is hex boolean.
   *
   * @param q the q
   * @param r the r
   * @param s the s
   * @return the boolean
   */
  public boolean isHex(final int q, final int r, final int s) {
        if (hex != null) {
            return hex.sameHex(q, r, s);
        } else {
            return false;
        }
    }

  /**
   * Gets origin x.
   *
   * @return the origin x
   */
  public float getOriginX() {
        return originX;
    }

  /**
   * Gets origin y.
   *
   * @return the origin y
   */
  public float getOriginY() {
        return originY;
    }

  /**
   * Gets width.
   *
   * @return the width
   */
  public float getWidth() {
        return width;
    }
}
