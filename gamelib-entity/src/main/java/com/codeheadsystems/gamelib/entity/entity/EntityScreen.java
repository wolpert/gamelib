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

package com.codeheadsystems.gamelib.entity.entity;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;
import static com.codeheadsystems.gamelib.entity.dagger.GameLibEntityModule.ENTITY_SCREEN_HIDE_CONSUMER;
import static com.codeheadsystems.gamelib.entity.dagger.GameLibEntityModule.ENTITY_SCREEN_SHOW_CONSUMER;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.entity.manager.EngineManager;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * This is the screen to use for an entity screen. It provides the runtime needed for the
 * engine updating. You can have other screens as needed, this is effectively the first
 * one for your libGdx application. How you manage this is up to you.
 */
@Singleton
public class EntityScreen extends ScreenAdapter {
    private static final Logger LOGGER = logger(EntityScreen.class);

    private final EngineManager engineManager;
    private final Optional<Consumer<EntityScreen>> showConsumer;
    private final Optional<Consumer<EntityScreen>> hideConsumer;
    private final Set<EntityGenerator> entityGenerators;

  /**
   * Instantiates a new Entity screen.
   *
   * @param engineManager    the engine manager
   * @param showConsumer     the show consumer
   * @param hideConsumer     the hide consumer
   * @param entityGenerators the entity generators
   */
  @Inject
    public EntityScreen(final EngineManager engineManager,
                        @Named(ENTITY_SCREEN_SHOW_CONSUMER) final Optional<Consumer<EntityScreen>> showConsumer,
                        @Named(ENTITY_SCREEN_HIDE_CONSUMER) final Optional<Consumer<EntityScreen>> hideConsumer,
                        final Set<EntityGenerator> entityGenerators) {
        this.engineManager = engineManager;
        this.showConsumer = showConsumer;
        this.hideConsumer = hideConsumer;
        this.entityGenerators = entityGenerators;
        LOGGER.info("EntityScreen()");
    }

    @Override
    public void show() {
        super.show();
        entityGenerators.forEach(EntityGenerator::generate);
        showConsumer.ifPresent(c -> c.accept(this));
    }

    @Override
    public void hide() {
        super.hide();
        hideConsumer.ifPresent(c -> c.accept(this));
    }

    @Override
    public void render(final float delta) {
        super.render(delta);
        engineManager.update(delta);
    }
}
