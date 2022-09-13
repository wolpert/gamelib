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

package com.codeheadsystems.gamelib.entity.entitysystem;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.entity.component.SortComponent;
import com.codeheadsystems.gamelib.entity.component.SpriteComponent;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Purpose: Render anything with a sprite.
 */
@Singleton
public class SpriteBatchRenderer extends WrapperSortedIteratingSystem {

    private static final Logger LOGGER = logger(SpriteBatchRenderer.class);
    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private final SpriteBatch spriteBatch;

    @Inject
    public SpriteBatchRenderer(final SpriteBatch spriteBatch,
                               final SortComparator sortComparator) {
        super(
            Family.all(SpriteComponent.class, SortComponent.class).get(),
            sortComparator,
            Priorities.SPRITES.priority()); // run after everything else
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void beforeIteration(float deltaTime) {
        spriteBatch.begin();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        sm.get(entity).getSprite().draw(spriteBatch);
    }

    @Override
    public void afterIteration(float deltaTime) {
        spriteBatch.end();
    }

    @Override
    public void addedToEngine(final Engine engine) {
        LOGGER.info("Added engine: " + engine);
        super.addedToEngine(engine);
    }

    @Override
    public void entityAdded(final Entity entity) {
        LOGGER.info("Added entity: " + entity);
        super.entityAdded(entity);
    }
}
