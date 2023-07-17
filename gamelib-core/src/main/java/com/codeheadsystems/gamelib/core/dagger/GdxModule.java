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

package com.codeheadsystems.gamelib.core.dagger;

import static com.codeheadsystems.gamelib.core.dagger.GameResources.RESOURCE_PATH;
import static com.codeheadsystems.gamelib.core.dagger.GameResources.VIEWPORT_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Json;
import com.codeheadsystems.gamelib.core.manager.ResizeManager;
import com.codeheadsystems.gamelib.core.util.InternalPrefixedFileHandleResolver;
import com.codeheadsystems.gamelib.core.util.JsonFactory;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import java.util.Optional;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Purpose: A common source of gdx components. You have the option of adding a GDX manager to this
 * when using the builder if you have one setup already.
 */
@Module
public class GdxModule {

  /**
   * Sprite batch sprite batch.
   *
   * @return the sprite batch
   */
  @Provides
  @Singleton
  public SpriteBatch spriteBatch() {
    return new SpriteBatch();
  }

  /**
   * Shape renderer shape renderer.
   *
   * @return the shape renderer
   */
  @Provides
  @Singleton
  public ShapeRenderer shapeRenderer() {
    return new ShapeRenderer();
  }

  /**
   * Orthographic camera orthographic camera.
   *
   * @return the orthographic camera
   */
  @Provides
  @Singleton
  public OrthographicCamera orthographicCamera() {
    return new OrthographicCamera();
  }

  /**
   * Resize camera listener resize manager . listener.
   *
   * @param viewportWidth the viewport width
   * @param camera        the camera
   * @return the resize manager . listener
   */
  @Provides
  @Singleton
  @IntoSet
  public ResizeManager.Listener resizeCameraListener(@Named(VIEWPORT_WIDTH) Optional<Float> viewportWidth,
                                                     OrthographicCamera camera) {
    return (w, h) -> {
      final float width = viewportWidth.orElseGet(() -> (float) Gdx.graphics.getWidth());
      final float height = width / w * h;
      camera.setToOrtho(false, width, height);
    };
  }

  /**
   * Asset manager asset manager.
   *
   * @param fileHandleResolver the file handle resolver
   * @return the asset manager
   */
  @Provides
  @Singleton
  public AssetManager assetManager(final FileHandleResolver fileHandleResolver) {
    return new AssetManager(fileHandleResolver);
  }

  /**
   * File handle resolver file handle resolver.
   *
   * @param resourcePath the resource path
   * @return the file handle resolver
   */
  @Provides
  @Singleton
  public FileHandleResolver fileHandleResolver(@Named(RESOURCE_PATH) Optional<String> resourcePath) {
    return new InternalPrefixedFileHandleResolver(resourcePath.orElse(""));
  }

  /**
   * Json json.
   *
   * @param jsonFactory the json factory
   * @return the json
   */
  @Provides
  @Singleton
  public Json json(final JsonFactory jsonFactory) {
    return jsonFactory.json();
  }


}
