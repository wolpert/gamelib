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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.codeheadsystems.gamelib.hex.manager.HexManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HexTest {

    private HexManager manager;

    @BeforeEach
    public void setup() {
        manager = new HexManager();
    }

    @Test
    void testValidSettings() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Hex().setQ(0).setR(1).setS(2).checkConstructorArguments());
    }

    @Test
    void testAxialGeneration() {
        assertThat(manager.axial(2, 3))
                .isEqualTo(Hex.of(2, 3, -5));
    }

    @Test
    public void testHexArithmetic() {
        assertThat(Hex.of(4, -10, 6))
                .isEqualTo(manager.add(Hex.of(1, -3, 2), Hex.of(3, -7, 4)));
        assertThat(Hex.of(-2, 4, -2))
                .isEqualTo(manager.subtract(Hex.of(1, -3, 2), Hex.of(3, -7, 4)));
    }

    @Test
    public void testHexDirection() {
        assertThat(Hex.of(0, -1, 1))
                .isEqualTo(manager.direction(2));
    }

    @Test
    public void testHexNeighbor() {
        assertThat(Hex.of(1, -3, 2))
                .isEqualTo(manager.neighbor(Hex.of(1, -2, 1), 2));
    }

    @Test
    public void testHexDiagonal() {
        assertThat(Hex.of(-1, -1, 2))
                .isEqualTo(manager.diagonalNeighbor(Hex.of(1, -2, 1), 3));
    }

    @Test
    public void testHexDistance() {
        assertThat(7)
                .isEqualTo(manager.distance(Hex.of(3, -7, 4), Hex.of(0, 0, 0)));
    }

    @Test
    public void testHexRotateRight() {
        assertThat(manager.rotateRight(Hex.of(1, -3, 2)))
                .isEqualTo(Hex.of(3, -2, -1));
    }

    @Test
    public void testHexRotateLeft() {
        assertThat(manager.rotateLeft(Hex.of(1, -3, 2)))
                .isEqualTo(Hex.of(-2, -1, 3));
    }

    @Test
    public void generate() {

        assertThat(manager.generate(4, 10))
                .hasSize(40);
    }
}