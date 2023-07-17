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
import static com.codeheadsystems.gamelib.hex.utilities.MathConverter.toFloat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.hex.model.*;
import com.codeheadsystems.gamelib.core.util.PoolerImpl;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Layout manager.
 */
@Singleton
public class LayoutManager extends PoolerImpl<Layout> {

    private static final Logger LOGGER = logger(LayoutManager.class);
    private final Vector2Manager vector2Manager;
    private final FractionalHexManager fractionalHexManager;

  /**
   * Instantiates a new Layout manager.
   *
   * @param vector2Manager       the vector 2 manager
   * @param fractionalHexManager the fractional hex manager
   */
  @Inject
    public LayoutManager(final Vector2Manager vector2Manager,
                         final FractionalHexManager fractionalHexManager) {
        super(Layout::new);
        this.vector2Manager = vector2Manager;
        this.fractionalHexManager = fractionalHexManager;
        LOGGER.debug("LayoutManager()");
    }

  /**
   * Generate layout.
   *
   * @param configuration the configuration
   * @return the layout
   */
  public Layout generate(final HexFieldConfiguration configuration) {
        return this.obtain()
            .setOrientation(configuration.getOrientation())
            .setSize(vector2Manager.obtain().set(configuration.getSizeX(), configuration.getSizeY()))
            .setOrigin(vector2Manager.obtain().set(configuration.getOriginX(), configuration.getOriginY()));
    }

    @Override
    public void free(final Layout tInstance) {
        vector2Manager.free(tInstance.origin());
        vector2Manager.free(tInstance.size());
        super.free(tInstance);
    }

  /**
   * Hex to pixel vector 2.
   *
   * @param layout the layout
   * @param h      the h
   * @return the vector 2
   */
  public Vector2 hexToPixel(final Layout layout, final Hex h) {
        final Orientation M = layout.orientation();
        final float x = (M.f0() * h.q() + M.f1() * h.r()) * layout.size().x;
        final float y = (M.f2() * h.q() + M.f3() * h.r()) * layout.size().y;
        return vector2Manager.obtain().set(x + layout.origin().x, y + layout.origin().y);
    }

  /**
   * Pixel to hex fractional hex.
   *
   * @param layout the layout
   * @param x      the x
   * @param y      the y
   * @return the fractional hex
   */
  public FractionalHex pixelToHex(final Layout layout, final float x, final float y) {
        final Orientation m = layout.orientation();
        final Vector2 pt = new Vector2().set((x - layout.origin().x) / layout.size().x, (y - layout.origin().y) / layout.size().y);
        final float q = m.b0() * pt.x + m.b1() * pt.y;
        final float r = m.b2() * pt.x + m.b3() * pt.y;
        return fractionalHexManager.obtain().setQ(q).setR(r).setS(-q - r);
    }

  /**
   * Pixel to hex fractional hex.
   *
   * @param layout the layout
   * @param p      the p
   * @return the fractional hex
   */
  public FractionalHex pixelToHex(final Layout layout, final Vector2 p) {
        return pixelToHex(layout, p.x, p.y);
    }

  /**
   * Hex corner offset vector 2.
   *
   * @param layout the layout
   * @param corner the corner
   * @return the vector 2
   */
  public Vector2 hexCornerOffset(final Layout layout, final int corner) {
        final Orientation m = layout.orientation();
        final double angle = 2.0 * Math.PI * (m.startAngle() - corner) / 6.0;
        return vector2Manager.obtain().set(layout.size().x * toFloat(Math.cos(angle)), layout.size().y * toFloat(Math.sin(angle)));
    }

  /**
   * Polygon corners list.
   *
   * @param layout the layout
   * @param h      the h
   * @return the list
   */
  public List<Vector2> polygonCorners(final Layout layout, final Hex h) {
        final List<Vector2> corners = new ArrayList<>();
        final Vector2 center = hexToPixel(layout, h);
        for (int i = 0; i < 6; i++) {
            final Vector2 offset = hexCornerOffset(layout, i);
            corners.add(vector2Manager.obtain().set(center.x + offset.x, center.y + offset.y));
        }
        return corners;
    }

  /**
   * Vertices float [ ].
   *
   * @param layout the layout
   * @param hex    the hex
   * @return the float [ ]
   */
  public float[] vertices(final Layout layout, final Hex hex) {
        final List<Vector2> Vector2s = polygonCorners(layout, hex);
        final float[] result = new float[Vector2s.size() * 2];
        int idx = 0;
        for (Vector2 Vector2 : Vector2s) {
            result[idx++] = Vector2.x;
            result[idx++] = Vector2.y;
        }
        return result;
    }
}
