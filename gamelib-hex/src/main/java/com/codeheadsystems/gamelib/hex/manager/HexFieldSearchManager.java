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
import static java.lang.Math.sqrt;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.hex.component.HexComponent;
import com.codeheadsystems.gamelib.hex.model.Hex;
import com.codeheadsystems.gamelib.hex.model.HexField;
import com.codeheadsystems.gamelib.hex.model.Layout;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provides the ability to search a hex field.
 */
@Singleton
public class HexFieldSearchManager {
    private static final Logger LOGGER = logger(HexFieldSearchManager.class);
    private final ComponentMapper<HexComponent> hm = ComponentMapper.getFor(HexComponent.class);
    private final HexManager hexManager;

  /**
   * Instantiates a new Hex field search manager.
   *
   * @param hexManager the hex manager
   */
  @Inject
    public HexFieldSearchManager(final HexManager hexManager) {
        this.hexManager = hexManager;
        LOGGER.debug("HexFieldSearchManager()");
    }

  /**
   * Hex entity hash map map.
   *
   * @param entities the entities
   * @return the map
   */
  public Map<Hex, Entity> hexEntityHashMap(final Set<Entity> entities) {
        return entities.stream()
            .collect(Collectors.toMap(this::hexForEntity, e -> e));
    }

  /**
   * Hex for entity hex.
   *
   * @param entity the entity
   * @return the hex
   */
  public Hex hexForEntity(final Entity entity) {
        return hm.get(entity).hex();
    }

  /**
   * Provides a way to convert a point to a hex that can then be used for a search function.
   *
   * @param <T>            the type parameter
   * @param x              from the layout. Must be unprojected from the screen. (See camera manager)
   * @param y              from the layout. Must be unprojected from the screen. (See camera manager).
   * @param layout         to be used.
   * @param searchFunction the search function.
   * @return whatever type they found.
   */
  protected <T> Optional<T> fromPoint(final float x,
                                        final float y,
                                        final Layout layout,
                                        final Function<Hex, Optional<T>> searchFunction) {
        // Convert the point on the screen to the whole field, based on the camera.
        final float layoutX = x - layout.origin().x;
        final float layoutY = y - layout.origin().y;
        // Convert the point to a fractional q/r/s via https://www.redblobgames.com/grids/hexagons/#pixel-to-hex
        final float fractionalQ = toFloat(2. / 3 * layoutX) / layout.size().x;
        final float fractionalR = toFloat(-1. / 3 * layoutX + sqrt(3) / 3 * layoutY) / layout.size().y;
        final float fractionalS = -fractionalQ - fractionalR;

        // Finally, getting the hex by converting to an integer q/r/s based on where the fractional q/r/s was in the hex.
        // https://www.redblobgames.com/grids/hexagons/#rounding
        int q = Math.round(fractionalQ);
        int r = Math.round(fractionalR);
        int s = Math.round(fractionalS);
        final float q_diff = Math.abs(q - fractionalQ);
        final float r_diff = Math.abs(r - fractionalR);
        final float s_diff = Math.abs(s - fractionalS);
        if (q_diff > r_diff && q_diff > s_diff) {
            q = -r - s;
        } else if (r_diff > s_diff) {
            r = -q - s;
        } else {
            s = -q - r;
        }
        Hex checkHex = null;
        try {
            checkHex = hexManager.obtain().set(q, r, s);
            // finally, lets see if anything is there.
            return searchFunction.apply(checkHex);
        } finally {
            if (checkHex != null) {
                hexManager.free(checkHex);
            }
        }
    }

  /**
   * Note, the x/y here must be in the same space that was used for the hex map. This likely needs conversion
   * from the layout to work correctly.
   *
   * @param x               mouse point.
   * @param y               mouse point.
   * @param layout          the layout of the hex field.
   * @param hexComponentSet we are looking at.
   * @return optional
   */
  public Optional<HexComponent> fromPoint(final float x,
                                            final float y,
                                            final Layout layout,
                                            final Set<HexComponent> hexComponentSet) {
        return fromPoint(x, y, layout, h -> hexComponentSet.stream()
            .filter(hc -> hc.isHex(h))
            .findFirst());
    }

  /**
   * Note, the x/y here must be in the same space that was used for the hex map. This likely needs conversion
   * from the layout to work correctly.
   *
   * @param x        mouse point.
   * @param y        mouse point.
   * @param hexField we are looking at.
   * @return optional
   */
  public Optional<Entity> fromPoint(final float x,
                                      final float y,
                                      final HexField hexField) {
        return fromPoint(x, y, hexField.getHexFieldLayout().layout(), h ->
            Optional.ofNullable(hexField.hexEntityHashMap().get(h)));
    }
}
