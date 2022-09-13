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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.codeheadsystems.gamelib.core.GdxTest;
import com.codeheadsystems.gamelib.core.model.Assets;
import com.codeheadsystems.gamelib.core.model.LoadingConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Purpose: testing the loading manager.
 */
@ExtendWith(MockitoExtension.class)
public class LoadingManagerTest extends GdxTest {

  private static final String FILENAME = "filename";

  @Mock
  private AssetManager assetManager;
  @Mock
  private FileHandleResolver fileHandleResolver;
  @Mock
  private LoadingConfiguration loadingConfiguration;
  @Mock
  private JsonManager jsonManager;
  @Mock
  private FileHandle fileHandle;
  @Mock
  private Assets assets;

  @Captor
  private ArgumentCaptor<AssetLoader> assetLoaderArgumentCaptor;

  private LoadingManager loadingManager;

  @BeforeEach
  void setUp() {
    this.loadingManager = new LoadingManager(assetManager, fileHandleResolver, loadingConfiguration, jsonManager);
  }

  @Test
  void constructor() {
    assertThat(loadingManager)
        .isNotNull()
        .extracting("currentStage")
        .isEqualTo(LoadingManager.Stages.INIT);
  }

  @Test
  void update_init() {
    when(loadingConfiguration.getAssetsFilename()).thenReturn(FILENAME);
    when(fileHandleResolver.resolve(FILENAME)).thenReturn(fileHandle);
    when(jsonManager.fromJson(Assets.class, fileHandle)).thenReturn(assets);

    final boolean result = loadingManager.update();

    assertThat(result).isFalse();
    assertThat(loadingManager).extracting("currentStage").isEqualTo(LoadingManager.Stages.ASSET_LOADERS);
  }

  @Test
  void update_asset_loaders() {
    loadingManager.setCurrentStage(LoadingManager.Stages.ASSET_LOADERS);
    loadingManager.setAssets(assets);
    when(assets.loaders()).thenReturn(new ArrayList<>());

    final boolean result = loadingManager.update();

    verify(assetManager).setLoader(eq(TiledMap.class), eq(".tmx"), assetLoaderArgumentCaptor.capture());
    assertThat(assetLoaderArgumentCaptor.getValue())
        .isNotNull();
    assertThat(result).isFalse();
  }

  @Test
  void update_queueassets() {
    loadingManager.setCurrentStage(LoadingManager.Stages.QUEUE_ASSETS);
    loadingManager.setAssets(assets);
    final HashMap<String, ArrayList<String>> map = (HashMap<String, ArrayList<String>>) Maps.newHashMap("java.lang.Object", Lists.newArrayList("file1", "file2"));
    when(assets.getAssetsToLoad()).thenReturn(map);

    final boolean result = loadingManager.update();

    assertThat(result).isFalse();
    assertThat(loadingManager).extracting("currentStage").isEqualTo(LoadingManager.Stages.LOAD_ASSETS);
    verify(assetManager).load("file1", Object.class);
    verify(assetManager).load("file2", Object.class);
    verifyNoMoreInteractions(assetManager);
  }

  @Test
  void update_queueassets_failure() {
    loadingManager.setCurrentStage(LoadingManager.Stages.QUEUE_ASSETS);
    loadingManager.setAssets(assets);
    final HashMap<String, ArrayList<String>> map = (HashMap<String, ArrayList<String>>) Maps.newHashMap("thisdoesnotexist", Lists.newArrayList("file1", "file2"));
    when(assets.getAssetsToLoad()).thenReturn(map);

    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> loadingManager.update());
  }

  @Test
  void update_loadassets() {
    loadingManager.setCurrentStage(LoadingManager.Stages.LOAD_ASSETS);
    when(assetManager.update()).thenReturn(false);

    final boolean result = loadingManager.update();

    assertThat(result).isFalse();
    assertThat(loadingManager).extracting("currentStage").isEqualTo(LoadingManager.Stages.LOAD_ASSETS);
  }

  @Test
  void update_loadassets_done() {
    loadingManager.setCurrentStage(LoadingManager.Stages.LOAD_ASSETS);
    when(assetManager.update()).thenReturn(true);

    final boolean result = loadingManager.update();

    assertThat(result).isFalse();
    assertThat(loadingManager).extracting("currentStage").isEqualTo(LoadingManager.Stages.DONE);
  }

  @Test
  void update_done() {
    loadingManager.setCurrentStage(LoadingManager.Stages.DONE);

    final boolean result = loadingManager.update();

    assertThat(result).isTrue();
    assertThat(loadingManager).extracting("currentStage").isEqualTo(LoadingManager.Stages.DONE);
  }

  @Test
  void getProgress_INIT() {
    loadingManager.setCurrentStage(LoadingManager.Stages.INIT);

    final float result = loadingManager.getProgress();

    assertThat(result).isEqualTo(0f);
  }

  @Test
  void getProgress_ASSET_LOADERS() {
    loadingManager.setCurrentStage(LoadingManager.Stages.ASSET_LOADERS);

    final float result = loadingManager.getProgress();

    assertThat(result).isEqualTo(0.1f);
  }

  @Test
  void getProgress_QUEUE_ASSETS() {
    loadingManager.setCurrentStage(LoadingManager.Stages.QUEUE_ASSETS);

    final float result = loadingManager.getProgress();

    assertThat(result).isEqualTo(0.2f);
  }

  @Test
  void getProgress_LOAD_ASSETS() {
    loadingManager.setCurrentStage(LoadingManager.Stages.LOAD_ASSETS);
    when(assetManager.getProgress()).thenReturn(0.5f);

    final float result = loadingManager.getProgress();

    assertThat(result).isEqualTo(0.65f);
  }

  @Test
  void getProgress_DONE() {
    loadingManager.setCurrentStage(LoadingManager.Stages.DONE);

    final float result = loadingManager.getProgress();

    assertThat(result).isEqualTo(1f);
  }

  @Test
  void assetManager() {
    assertThat(loadingManager.assetManager())
        .isEqualTo(assetManager);
  }

  @Test
  void getStageTitle_INIT() {
    loadingManager.setCurrentStage(LoadingManager.Stages.INIT);

    final String result = loadingManager.getStageTitle();

    assertThat(result)
        .isNotNull()
        .isEqualTo("Initializing system");

  }

  @Test
  void getStageTitle_ASSET_LOADERS() {
    loadingManager.setCurrentStage(LoadingManager.Stages.ASSET_LOADERS);

    final String result = loadingManager.getStageTitle();

    assertThat(result)
        .isNotNull()
        .isEqualTo("Setting up asset loaders");

  }

  @Test
  void getStageTitle_QUEUE_ASSETS() {
    loadingManager.setCurrentStage(LoadingManager.Stages.QUEUE_ASSETS);

    final String result = loadingManager.getStageTitle();

    assertThat(result)
        .isNotNull()
        .isEqualTo("Queueing assets");

  }

  @Test
  void getStageTitle_LOAD_ASSETS() {
    loadingManager.setCurrentStage(LoadingManager.Stages.LOAD_ASSETS);

    final String result = loadingManager.getStageTitle();

    assertThat(result)
        .isNotNull()
        .isEqualTo("Loading assets from system");

  }

  @Test
  void getStageTitle_DONE() {
    loadingManager.setCurrentStage(LoadingManager.Stages.DONE);

    final String result = loadingManager.getStageTitle();

    assertThat(result)
        .isNotNull()
        .isEqualTo("Complete!");
  }
}