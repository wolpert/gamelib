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

import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.core.GdxTest;
import org.junit.jupiter.api.Test;

class LoggerHelperTest extends GdxTest {

  @Test
  void logger() {
    new LoggerHelper();

    final Logger logger = LoggerHelper.logger(LoggerHelperTest.class);

    assertThat(logger.getLevel())
        .isEqualTo(Logger.DEBUG);
  }
}