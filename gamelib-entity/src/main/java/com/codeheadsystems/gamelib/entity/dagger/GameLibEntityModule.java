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

package com.codeheadsystems.gamelib.entity.dagger;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.entity.configuration.AshleyGameConfiguration;
import com.codeheadsystems.gamelib.entity.entity.EntityScreen;
import com.codeheadsystems.gamelib.entity.entitysystem.CameraEntitySystem;
import com.codeheadsystems.gamelib.entity.entitysystem.SpriteBatchRenderer;
import dagger.BindsOptionalOf;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import java.util.function.Consumer;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Purpose: Provides entity systems.
 */
@Module(includes = {GameLibEntityModule.Binder.class})
public class GameLibEntityModule {

  public static final String CONFIGURATION_JSON = "ashleyGameConfiguration.json";
  public static final String ENTITY_SCREEN_SHOW_CONSUMER = "EntityScreenShowConsumer";
  public static final String ENTITY_SCREEN_HIDE_CONSUMER = "EntityScreenHideConsumer";
  private static final Logger LOGGER = logger(GameLibEntityModule.class);

  @IntoSet
  @Provides
  @Singleton
  public Entity background(final PooledEngine pooledEngine) {
    return pooledEngine.createEntity();
  }

  @IntoSet
  @Provides
  @Singleton
  public EntitySystem spriteRenderEntitySystem(final SpriteBatchRenderer system) {
    return system;
  }

  @IntoSet
  @Provides
  @Singleton
  public EntitySystem cameraEntitySystem(final CameraEntitySystem entitySystem) {
    return entitySystem;
  }

  @Provides
  @Singleton
  public PooledEngine pooledEngine(final AshleyGameConfiguration configuration) {
    final PooledEngine pooledEngine = new PooledEngine(
        configuration.entityPoolInitialSize,
        configuration.entityPoolMaxSize,
        configuration.componentPoolInitialSize,
        configuration.componentPoolMaxSize);
    return pooledEngine;
  }

  @Provides
  @Singleton
  public AshleyGameConfiguration ashleyGameConfiguration(final FileHandleResolver fileHandleResolver,
                                                         final Json json) {
    final FileHandle fileHandle = fileHandleResolver.resolve(CONFIGURATION_JSON);
    return json.fromJson(AshleyGameConfiguration.class, fileHandle);
  }

  @Module
  interface Binder {
    /**
     * This is the entity manager version of the libgdx show() method.
     * <p>
     * Implement this method to configure the entity screen as needed. The consumer is called only
     * when the system is loaded, including all assets. This is your primary entrance method in
     * most case.
     *
     * @return a consumer of the entity screen.
     */
    @BindsOptionalOf
    @Named(ENTITY_SCREEN_SHOW_CONSUMER)
    Consumer<EntityScreen> showConsumer();

    /**
     * This is the entity manager version of the libgdx hide() method.
     *
     * @return a consumer method for the entity screen.
     */
    @BindsOptionalOf
    @Named(ENTITY_SCREEN_HIDE_CONSUMER)
    Consumer<EntityScreen> hideConsumer();
  }
}
