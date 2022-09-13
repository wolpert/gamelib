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

package com.codeheadsystems.gamelib.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.assets.AssetManager;
import com.codeheadsystems.gamelib.core.manager.ResizeManager;
import com.codeheadsystems.gamelib.core.screen.LoadingScreen;
import com.codeheadsystems.gamelib.core.util.GameListener;
import dagger.Lazy;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameLauncherTest extends GdxTest {

  @Mock private LoadingScreen loadingScreen;
  @Mock private GameListener gameListener;
  @Mock private ResizeManager resizeManager;
  @Mock private AssetManager assetManager;
  @Mock private Lazy<LoadingScreen> lazyLoadingScreen;
  @Mock private Lazy<Set<GameListener>> lazyGameListener;
  @Mock private Lazy<ResizeManager> lazyResizeManager;

  private GameLauncher gameLauncher;

  @BeforeEach
  void setUp() {
    final Set<GameListener> set = new HashSet<>();
    set.add(gameListener);
    lenient().when(lazyGameListener.get()).thenReturn(set);
    lenient().when(lazyLoadingScreen.get()).thenReturn(loadingScreen);
    lenient().when(lazyResizeManager.get()).thenReturn(resizeManager);
    gameLauncher = new GameLauncher(lazyLoadingScreen, lazyGameListener, lazyResizeManager, assetManager);
  }

  @Test
  void create() {
    gameLauncher.create();

    verify(gameListener).setGame(gameLauncher);
    assertThat(gameLauncher)
        .hasFieldOrPropertyWithValue("screen", loadingScreen);

  }

  @Test
  void resize() {
    gameLauncher.resize(1, 2);
    verify(resizeManager).resize(1, 2);
  }

  @Test
  void dispose() {
    gameLauncher.dispose();
    verify(assetManager).dispose();
  }
}