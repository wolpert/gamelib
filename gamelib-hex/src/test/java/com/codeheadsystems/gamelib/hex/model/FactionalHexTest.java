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

package com.codeheadsystems.gamelib.hex.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.codeheadsystems.gamelib.hex.manager.FractionalHexManager;
import com.codeheadsystems.gamelib.hex.manager.HexManager;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FactionalHexTest {

    private FractionalHexManager manager;

    @BeforeEach
    public void setup() {
        manager = new FractionalHexManager(new HexManager());
    }

    @Test
    void testValidSettings() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FractionalHex().setQ(0).setR(1).setS(2).checkConstructorArguments());
    }

    @Test
    public void testHexRound() {
        final FractionalHex a = FractionalHex.of(0.0, 0.0, 0.0);
        final FractionalHex b = FractionalHex.of(1.0, -1.0, 0.0);
        final FractionalHex c = FractionalHex.of(0.0, -1.0, 1.0);

        assertThat(Hex.of(5, -10, 5))
                .isEqualTo(manager.hexRound(manager.hexLerp(FractionalHex.of(0.0, 0.0, 0.0), FractionalHex.of(10.0, -20.0, 10.0), 0.5), true));
        assertThat(manager.hexRound(a, false))
                .isEqualTo(manager.hexRound(manager.hexLerp(a, b, 0.499), false));
        assertThat(manager.hexRound(b, false))
                .isEqualTo(manager.hexRound(manager.hexLerp(a, b, 0.501), false));
        assertThat(manager.hexRound(a, false))
                .isEqualTo(manager.hexRound(FractionalHex.of(a.q() * 0.4 + b.q() * 0.3 + c.q() * 0.3, a.r() * 0.4 + b.r() * 0.3 + c.r() * 0.3, a.s() * 0.4 + b.s() * 0.3 + c.s() * 0.3), false));
        assertThat(manager.hexRound(c, false))
                .isEqualTo(manager.hexRound(FractionalHex.of(a.q() * 0.3 + b.q() * 0.3 + c.q() * 0.4, a.r() * 0.3 + b.r() * 0.3 + c.r() * 0.4, a.s() * 0.3 + b.s() * 0.3 + c.s() * 0.4), false));
    }

    @Test
    public void testHexLinedraw() {
        assertThat(new ArrayList<Hex>() {{
            add(Hex.of(0, 0, 0));
            add(Hex.of(0, -1, 1));
            add(Hex.of(0, -2, 2));
            add(Hex.of(1, -3, 2));
            add(Hex.of(1, -4, 3));
            add(Hex.of(1, -5, 4));
        }})
                .isEqualTo(manager.hexLinedraw(Hex.of(0, 0, 0), Hex.of(1, -5, 4)));
    }
}