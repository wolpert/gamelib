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

package com.codeheadsystems.gamelib.hex.manager;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.core.util.Pooler;
import com.codeheadsystems.gamelib.hex.component.HexComponent;
import com.codeheadsystems.gamelib.hex.model.HexFieldLayout;
import com.codeheadsystems.gamelib.hex.model.Layout;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provides a way to generate a hex field. This class 'self-pools' and should not be used
 * if you are using an entity system.
 */
@Singleton
public class HexComponentManager {

    private static final Logger LOGGER = logger(HexComponentManager.class);
    private final Pooler<HexComponent> pool;
    private final HexManager hexManager;
    private final LayoutManager layoutManager;

  /**
   * Instantiates a new Hex component manager.
   *
   * @param hexManager    the hex manager
   * @param layoutManager the layout manager
   */
  @Inject
    public HexComponentManager(final HexManager hexManager,
                               final LayoutManager layoutManager) {
        this.hexManager = hexManager;
        this.layoutManager = layoutManager;
        pool = Pooler.of(HexComponent::new);
        LOGGER.debug("HexComponentManager()");
    }

  /**
   * Generate set.
   *
   * @param hexFieldLayout the hex field layout
   * @return the set
   */
  public Set<HexComponent> generate(final HexFieldLayout hexFieldLayout) {
        return generate(hexFieldLayout.cols(), hexFieldLayout.rows(), hexFieldLayout.layout());
    }

  /**
   * Generate set.
   *
   * @param cols   the cols
   * @param rows   the rows
   * @param layout the layout
   * @return the set
   */
  public Set<HexComponent> generate(final int cols, final int rows, final Layout layout) {
        return hexManager.generate(cols, rows)
                .stream()
                .map(h -> pool.obtain().initialize(h, layout, layoutManager))
                .collect(Collectors.toSet());
    }

  /**
   * Release.
   *
   * @param field the field
   */
  public void release(final Set<HexComponent> field) {
        field.forEach(this::release);
    }

  /**
   * Release.
   *
   * @param component the component
   */
  public void release(final HexComponent component) {
        hexManager.free(component.hex());
        pool.free(component);
    }

  /**
   * Pool size int.
   *
   * @return the int
   */
  public int poolSize() {
        return pool.poolSize();
    }

}
