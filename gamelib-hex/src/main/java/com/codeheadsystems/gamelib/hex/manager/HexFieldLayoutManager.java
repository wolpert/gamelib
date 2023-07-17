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

import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.core.util.PoolerImpl;
import com.codeheadsystems.gamelib.hex.model.HexFieldConfiguration;
import com.codeheadsystems.gamelib.hex.model.HexFieldLayout;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Hex field layout manager.
 */
@Singleton
public class HexFieldLayoutManager extends PoolerImpl<HexFieldLayout> {

  private static final Logger LOGGER = logger(HexFieldLayoutManager.class);
  private final LayoutManager layoutManager;

  /**
   * Instantiates a new Hex field layout manager.
   *
   * @param layoutManager the layout manager
   */
  @Inject
  public HexFieldLayoutManager(final LayoutManager layoutManager) {
    super(HexFieldLayout::new);
    this.layoutManager = layoutManager;
    LOGGER.debug("HexFieldLayoutManager()");
  }

  /**
   * Obtain hex field layout.
   *
   * @param configuration the configuration
   * @return the hex field layout
   */
  public HexFieldLayout obtain(final HexFieldConfiguration configuration) {
    return obtain()
        .setLayout(layoutManager.generate(configuration))
        .setCols(configuration.getCols())
        .setRows(configuration.getRows());
  }

  @Override
  public void free(final HexFieldLayout tInstance) {
    layoutManager.free(tInstance.layout());
    super.free(tInstance);
  }
}
