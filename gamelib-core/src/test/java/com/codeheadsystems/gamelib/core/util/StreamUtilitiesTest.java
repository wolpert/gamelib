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

package com.codeheadsystems.gamelib.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StreamUtilitiesTest {
  @Test
  public void reduceStream() {
    assertThat(StreamUtilities.reduceStream(5.1f, 0.5f))
        .hasSize(10);
    assertThat(StreamUtilities.reduceStream(0.5f, 5.1f))
        .isEmpty();
    assertThat(StreamUtilities.reduceStream(2f, 1f))
        .hasSize(2);
  }
}