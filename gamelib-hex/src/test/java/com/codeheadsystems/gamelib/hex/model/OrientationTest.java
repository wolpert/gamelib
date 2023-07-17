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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrientationTest {

    private Orientation orientation;

    @BeforeEach
    void setup() {
        orientation = Orientation.of(0, 1, 2, 3, 4, 5, 6, 7, 8);
    }

    @Test
    void testHashCode() {
        assertThat(orientation.hashCode())
                .isEqualTo(357134623);
    }

    @Test
    void testReset() {
        orientation.reset();
        assertThat(orientation.hashCode())
                .isEqualTo(-196513505);
    }

    @Test
    void equals() {
        assertThat(orientation.equals(null)).isFalse();
        assertThat(orientation.equals(Orientation.class)).isFalse();
        assertThat(orientation.equals(orientation)).isTrue();
        assertThat(new Orientation().equals(new Orientation())).isTrue();
        assertThat(new Orientation().equals(new Orientation().setF0(1))).isFalse();
        assertThat(new Orientation().equals(new Orientation().setF1(1))).isFalse();
        assertThat(new Orientation().equals(new Orientation().setF2(1))).isFalse();
        assertThat(new Orientation().equals(new Orientation().setF3(1))).isFalse();
        assertThat(new Orientation().equals(new Orientation().setB0(1))).isFalse();
        assertThat(new Orientation().equals(new Orientation().setB1(1))).isFalse();
        assertThat(new Orientation().equals(new Orientation().setB2(1))).isFalse();
        assertThat(new Orientation().equals(new Orientation().setB3(1))).isFalse();
        assertThat(new Orientation().equals(new Orientation().setStartAngle(1))).isFalse();
    }

}