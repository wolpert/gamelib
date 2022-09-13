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
import com.codeheadsystems.gamelib.hex.model.HexFieldConfiguration;
import com.codeheadsystems.gamelib.hex.model.Orientation;
import com.codeheadsystems.gamelib.core.util.PoolerImpl;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HexFieldConfigurationManager extends PoolerImpl<HexFieldConfiguration> {

    private static final Logger LOGGER = logger(HexFieldConfigurationManager.class);
    @Inject
    public HexFieldConfigurationManager() {
        super(HexFieldConfiguration::new);
    }

    /**
     * Standardize way to create a hex field that must fit in the width/height.
     *
     * @param rows
     * @param cols
     * @param width
     * @param height
     * @return
     */
    public HexFieldConfiguration generate(final int rows,
                                          final int cols,
                                          final float width,
                                          final float height) {
        final float maxHexHeight = height / rows;
        final float maxHexWidth = width / cols;
        final float hexSize = Math.min(maxHexHeight, maxHexWidth)/2;
        LOGGER.info("Hex Size: " + hexSize);
        return obtain()
                .setCols(cols)
                .setRows(rows)
                .setOrientation(Orientation.flat)
                .setOriginX(hexSize)
                .setOriginY(hexSize)
                .setSizeX(hexSize)
                .setSizeY(hexSize);
    }

    public void with(final int rows,
                     final int cols,
                     final float width,
                     final float height,
                     Consumer<HexFieldConfiguration> consumer){
        final HexFieldConfiguration configuration = generate(rows, cols, width, height);
        consumer.accept(configuration);
        free(configuration);
    }

}
