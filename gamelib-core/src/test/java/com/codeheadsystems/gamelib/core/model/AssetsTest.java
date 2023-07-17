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

package com.codeheadsystems.gamelib.core.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.badlogic.gdx.utils.Json;
import com.codeheadsystems.gamelib.core.util.JsonFactory;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.io.IOUtil;

/**
 * Purpose: This test validates we can read real JSON and create a loading configuration.
 */
class AssetsTest {

  private JsonFactory jsonFactory;

  @BeforeEach
  void setup() {
    jsonFactory = new JsonFactory();
  }

  @Test
  void constructorViaJson() {
    final Json json = jsonFactory.json();
    final InputStream stream = this.getClass().getClassLoader().getResourceAsStream("assetsTest.json");
    final String jsonText = String.join("", IOUtil.readLines(stream));

    final Assets assets = json.fromJson(Assets.class, jsonText);

    assertThat(assets).isNotNull();
    assertThat(assets.getAssetsToLoad())
        .isNotNull()
        .hasEntrySatisfying("one", (v) -> assertThat(v).hasSize(2).contains("a", "b"))
        .hasEntrySatisfying("two", (v) -> assertThat(v).hasSize(1).contains("c"));
  }


}