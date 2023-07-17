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
import com.codeheadsystems.gamelib.hex.model.DoubledCoord;
import com.codeheadsystems.gamelib.hex.model.Hex;
import com.codeheadsystems.gamelib.core.util.PoolerImpl;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Doubled coord manager.
 */
@Singleton
public class DoubledCoordManager extends PoolerImpl<DoubledCoord> {
    private static final Logger LOGGER = logger(DoubledCoordManager.class);

    private final HexManager hexManager;

  /**
   * Instantiates a new Doubled coord manager.
   *
   * @param hexManager the hex manager
   */
  @Inject
    public DoubledCoordManager(final HexManager hexManager) {
        super(DoubledCoord::new);
        this.hexManager = hexManager;
        LOGGER.debug("DoubledCoordManager()");
    }

  /**
   * Does not free old Hex.
   *
   * @param h the h
   * @return the doubled coord
   */
  public DoubledCoord qdoubledFromCube(final Hex h) {
        final int col = h.q();
        final int row = 2 * h.r() + h.q();
        return obtain().setCol(col).setRow(row);
    }

  /**
   * Does not free old Hex.
   *
   * @param h the h
   * @return the doubled coord
   */
  public DoubledCoord rdoubledFromCube(final Hex h) {
        final int col = 2 * h.q() + h.r();
        final int row = h.r();
        return obtain().setCol(col).setRow(row);
    }

  /**
   * Does not free old DoubledCoord.
   *
   * @param a the a
   * @return the hex
   */
  public Hex qdoubledToCube(final DoubledCoord a) {
        final int q = a.col();
        final int r = (a.row() - a.col()) / 2;
        final int s = -q - r;
        return hexManager.obtain().setQ(q).setR(r).setS(s).checkConstructorArguments();
    }

  /**
   * Does not free old DoubledCoord.
   *
   * @param a the a
   * @return the hex
   */
  public Hex rdoubledToCube(final DoubledCoord a) {
        final int q = (a.col() - a.row()) / 2;
        final int r = a.row();
        final int s = -q - r;
        return hexManager.obtain().setQ(q).setR(r).setS(s).checkConstructorArguments();
    }
}
