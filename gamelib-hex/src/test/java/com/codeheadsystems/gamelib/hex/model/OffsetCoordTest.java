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

package com.codeheadsystems.gamelib.hex.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.codeheadsystems.gamelib.hex.manager.HexManager;
import com.codeheadsystems.gamelib.hex.manager.OffsetCoordManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OffsetCoordTest {

    private OffsetCoordManager manager;

    @BeforeEach
    public void setup() {
        manager = new OffsetCoordManager(new HexManager());
    }

    @Test
    public void testOffsetRoundtrip() {
        final Hex a = Hex.of(3, 4, -7);
        final OffsetCoord b = OffsetCoord.of(1, -3);

        assertThat(a)
                .isEqualTo(manager.qoffsetToCube(OffsetCoord.Offset.EVEN, manager.qoffsetFromCube(OffsetCoord.Offset.EVEN, a)));
        assertThat(b)
                .isEqualTo(manager.qoffsetFromCube(OffsetCoord.Offset.EVEN, manager.qoffsetToCube(OffsetCoord.Offset.EVEN, b)));
        assertThat(a)
                .isEqualTo(manager.qoffsetToCube(OffsetCoord.Offset.ODD, manager.qoffsetFromCube(OffsetCoord.Offset.ODD, a)));
        assertThat(b)
                .isEqualTo(manager.qoffsetFromCube(OffsetCoord.Offset.ODD, manager.qoffsetToCube(OffsetCoord.Offset.ODD, b)));
        assertThat(a)
                .isEqualTo(manager.roffsetToCube(OffsetCoord.Offset.EVEN, manager.roffsetFromCube(OffsetCoord.Offset.EVEN, a)));
        assertThat(b)
                .isEqualTo(manager.roffsetFromCube(OffsetCoord.Offset.EVEN, manager.roffsetToCube(OffsetCoord.Offset.EVEN, b)));
        assertThat(a)
                .isEqualTo(manager.roffsetToCube(OffsetCoord.Offset.ODD, manager.roffsetFromCube(OffsetCoord.Offset.ODD, a)));
        assertThat(b)
                .isEqualTo(manager.roffsetFromCube(OffsetCoord.Offset.ODD, manager.roffsetToCube(OffsetCoord.Offset.ODD, b)));
    }

    @Test
    public void testOffsetFromCube() {
        assertThat(OffsetCoord.of(1, 3))
                .isEqualTo(manager.qoffsetFromCube(OffsetCoord.Offset.EVEN, Hex.of(1, 2, -3)));
        assertThat(OffsetCoord.of(1, 2))
                .isEqualTo(manager.qoffsetFromCube(OffsetCoord.Offset.ODD, Hex.of(1, 2, -3)));
    }

    @Test
    public void testOffsetToCube() {
        assertThat(Hex.of(1, 2, -3))
                .isEqualTo(manager.qoffsetToCube(OffsetCoord.Offset.EVEN, OffsetCoord.of(1, 3)));
        assertThat(Hex.of(1, 2, -3))
                .isEqualTo(manager.qoffsetToCube(OffsetCoord.Offset.ODD, OffsetCoord.of(1, 2)));
    }
}