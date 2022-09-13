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

import static org.assertj.core.api.Assertions.assertThat;

import com.badlogic.gdx.math.Vector2;
import com.codeheadsystems.gamelib.hex.GdxTest;
import com.codeheadsystems.gamelib.hex.component.HexComponent;
import com.codeheadsystems.gamelib.hex.model.HexFieldLayout;
import com.codeheadsystems.gamelib.hex.model.Layout;
import com.codeheadsystems.gamelib.hex.model.Orientation;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HexComponentManagerTest extends GdxTest {

    private static final HexFieldLayout HEX_FIELD_LAYOUT = new HexFieldLayout()
            .setRows(2)
            .setCols(5)
            .setLayout(Layout.of(
                    Orientation.flat,
                    new Vector2().set(5, 5),
                    new Vector2().set(10, 10))
            );

    private HexManager hexManager;
    private LayoutManager layoutManager;
    private HexComponentManager manager;

    @BeforeEach
    void setup() {
        hexManager = new HexManager();
        layoutManager = new LayoutManager(new Vector2Manager(), new FractionalHexManager(hexManager));
        manager = new HexComponentManager(hexManager, layoutManager);
    }

    @Test
    void defaultObject() {
        assertThat(manager.poolSize())
                .isEqualTo(0);
    }

    @Test
    void generate() {
        final Set<HexComponent> result = manager.generate(HEX_FIELD_LAYOUT);
        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(10);
        assertThat(manager.poolSize())
                .isEqualTo(0);
    }

    @Test
    void release() {
        final Set<HexComponent> result = manager.generate(HEX_FIELD_LAYOUT);
        manager.release(result);
        assertThat(manager.poolSize())
                .isEqualTo(10);
    }
}