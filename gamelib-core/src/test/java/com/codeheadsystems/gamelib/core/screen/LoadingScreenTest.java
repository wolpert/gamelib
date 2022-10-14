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

package com.codeheadsystems.gamelib.core.screen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.codeheadsystems.gamelib.core.GdxTest;
import com.codeheadsystems.gamelib.core.manager.LoadingBar;
import com.codeheadsystems.gamelib.core.manager.LoadingManager;
import com.codeheadsystems.gamelib.core.manager.MainScreenManager;
import com.codeheadsystems.gamelib.core.manager.ResourcePathManager;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoadingScreenTest extends GdxTest {

  public static final float DELTA = 5f;
  private static final String LOADING_IMAGE = "tiny.png";
  @Mock private SpriteBatch spriteBatch;
  @Mock private LoadingManager loadingManager;
  @Mock private LoadingBar loadingBar;
  @Mock private Screen screen;
  @Mock private MainScreenManager mainScreenManager;
  @Mock private Game game;
  @Mock private Texture texture;
  @Mock private Sprite sprite;
  @Mock private FileHandle fileHandle;
  @Mock private ResourcePathManager resourcePathManager;

  private LoadingScreen loadingScreen;

  @BeforeEach
  void setUp() {
    when(resourcePathManager.path(LOADING_IMAGE)).thenReturn(LOADING_IMAGE);
    loadingScreen = new LoadingScreen(resourcePathManager, spriteBatch, loadingManager, loadingBar, LOADING_IMAGE, mainScreenManager);
    loadingScreen.setGame(game);
  }


  @Test
  void show() throws IOException {
    final InputStream is = LoadingScreenTest.class.getClassLoader().getResourceAsStream(LOADING_IMAGE);
    when(files.internal(LOADING_IMAGE)).thenReturn(fileHandle);
    when(fileHandle.name()).thenReturn(LOADING_IMAGE);
    when(fileHandle.readBytes()).thenReturn(IOUtils.toByteArray(is));

    loadingScreen.show();

    assertThat(loadingScreen.getLoadingTexture())
        .isNotNull();
    assertThat(loadingScreen.getLoadingSprite())
        .isNotNull();
    verify(loadingBar).show(graphics);
  }

  @Test
  void testRender_updateIsFalse() {
    loadingScreen.setLoadingSprite(sprite);
    loadingScreen.setLoadingTexture(texture);
    when(loadingManager.update()).thenReturn(false);
    when(loadingManager.getProgress()).thenReturn(4f);

    loadingScreen.render(DELTA);

    verify(spriteBatch).begin();
    verify(sprite).draw(spriteBatch);
    verify(spriteBatch).end();
    verify(loadingBar).render(4f);
  }

  @Test
  void testRender_updateIsTrue() {
    loadingScreen.setLoadingSprite(sprite);
    loadingScreen.setLoadingTexture(texture);
    when(loadingManager.update()).thenReturn(true);
    when(loadingManager.getProgress()).thenReturn(10f);
    when(mainScreenManager.mainScreen()).thenReturn(screen);

    loadingScreen.render(DELTA);
    loadingScreen.render(DELTA);

    verify(spriteBatch, times(2)).begin();
    verify(sprite, times(2)).draw(spriteBatch);
    verify(spriteBatch, times(2)).end();
    verify(game, times(1)).setScreen(screen);
    verify(loadingBar, never()).render(anyFloat());
  }

  @Test
  void resize_nullSprite() {
    loadingScreen.resize(0, 0);
    verifyNoInteractions(sprite);
  }

  @Test
  void resize_withSprite() {
    loadingScreen.setLoadingSprite(sprite);

    loadingScreen.resize(8, 10);
    verify(sprite).setCenter(4f, 5f);
    verify(sprite).setSize(8f, 10f);
  }

  @Test
  void dispose() {
    loadingScreen.setLoadingTexture(texture);
    loadingScreen.setLoadingSprite(sprite);
    loadingScreen.dispose();

    verify(texture).dispose();
    assertThat(loadingScreen.getLoadingSprite())
        .isNull();
    assertThat(loadingScreen.getLoadingTexture())
        .isNull();
  }
}