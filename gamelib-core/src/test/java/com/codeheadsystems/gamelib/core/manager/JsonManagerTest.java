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

package com.codeheadsystems.gamelib.core.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Purpose:
 */
@ExtendWith(MockitoExtension.class)
class JsonManagerTest {

  private static final String DATA = "data";

  @Mock
  private Json json;
  @Mock
  private FileHandle fileHandle;
  @Mock
  private TestClass testClass;

  private JsonManager jsonManager;

  @BeforeEach
  public void setup() {
    this.jsonManager = new JsonManager(json);
  }

  @Test
  void fromJson() {
    when(json.fromJson(TestClass.class, fileHandle)).thenReturn(testClass);

    final TestClass result = jsonManager.fromJson(TestClass.class, fileHandle);

    assertThat(result)
        .isNotNull()
        .isEqualTo(testClass);
  }

  class TestClass {
  }
}