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

package com.codeheadsystems.gamelib.core.manager;

import static org.mockito.Mockito.verify;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Purpose:
 */
@ExtendWith(MockitoExtension.class)
class ResizeManagerTest {

  private static final int WIDTH = 1;
  private static final int HEIGHT = 2;

  @Mock
  private ResizeManager.Listener listener1;
  @Mock
  private ResizeManager.Listener listener2;

  private ResizeManager resizeManager;

  @BeforeEach
  public void setup() {
    resizeManager = new ResizeManager(Sets.set(listener1, listener2));
  }

  @Test
  void resize() {
    resizeManager.resize(WIDTH, HEIGHT);

    verify(listener1).resize(WIDTH, HEIGHT);
    verify(listener2).resize(WIDTH, HEIGHT);
  }
}