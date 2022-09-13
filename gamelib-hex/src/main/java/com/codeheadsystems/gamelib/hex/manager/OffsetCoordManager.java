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
import com.codeheadsystems.gamelib.hex.model.Hex;
import com.codeheadsystems.gamelib.hex.model.OffsetCoord;
import com.codeheadsystems.gamelib.core.util.PoolerImpl;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OffsetCoordManager extends PoolerImpl<OffsetCoord> {

    private static final Logger LOGGER = logger(OffsetCoordManager.class);
    private final HexManager hexManager;

    @Inject
    public OffsetCoordManager(HexManager hexManager) {
        super(OffsetCoord::new);
        this.hexManager = hexManager;
        LOGGER.debug("OffsetCoordManager()");
    }

    public OffsetCoord qoffsetFromCube(OffsetCoord.Offset offset, Hex h) {
        int col = h.q();
        int row = h.r() + ((h.q() + offset.value * (h.q() & 1)) / 2);
        return obtain().setCol(col).setRow(row);
    }

    public Hex qoffsetToCube(OffsetCoord.Offset offset, OffsetCoord h) {
        int q = h.col();
        int r = h.row() - ((h.col() + offset.value * (h.col() & 1)) / 2);
        int s = -q - r;
        return hexManager.obtain().setQ(q).setR(r).setS(s);
    }

    public OffsetCoord roffsetFromCube(OffsetCoord.Offset offset, Hex h) {
        int col = h.q() + ((h.r() + offset.value * (h.r() & 1)) / 2);
        int row = h.r();
        return obtain().setCol(col).setRow(row);
    }

    public Hex roffsetToCube(OffsetCoord.Offset offset, OffsetCoord h) {
        int q = h.col() - ((h.row() + offset.value * (h.row() & 1)) / 2);
        int r = h.row();
        int s = -q - r;
        return hexManager.obtain().setQ(q).setR(r).setS(s);
    }

}
