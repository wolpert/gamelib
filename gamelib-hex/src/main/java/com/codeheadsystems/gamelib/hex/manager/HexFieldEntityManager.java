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

package com.codeheadsystems.gamelib.hex.manager;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.core.util.Pooler;
import com.codeheadsystems.gamelib.entity.manager.EngineManager;
import com.codeheadsystems.gamelib.hex.component.HexComponent;
import com.codeheadsystems.gamelib.hex.model.HexField;
import com.codeheadsystems.gamelib.hex.model.HexFieldConfiguration;
import com.codeheadsystems.gamelib.hex.model.HexFieldLayout;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provides a way to generate a hex field and manage it from within an entity system.
 */
@Singleton
public class HexFieldEntityManager {

    private static final Logger LOGGER = logger(HexFieldEntityManager.class);
    private static final Family HEXS = Family.all(HexComponent.class).get();
    private final EngineManager engineManager;
    private final HexManager hexManager;
    private final LayoutManager layoutManager;
    private final HexFieldLayoutManager hexFieldLayoutManager;
    private final Pooler<HexField> pool;
    private final HexFieldSearchManager hexFieldSearchManager;

  /**
   * Instantiates a new Hex field entity manager.
   *
   * @param engineManager         the engine manager
   * @param hexManager            the hex manager
   * @param layoutManager         the layout manager
   * @param hexFieldLayoutManager the hex field layout manager
   * @param hexFieldSearchManager the hex field search manager
   */
  @Inject
    public HexFieldEntityManager(final EngineManager engineManager,
                                 final HexManager hexManager,
                                 final LayoutManager layoutManager,
                                 final HexFieldLayoutManager hexFieldLayoutManager,
                                 final HexFieldSearchManager hexFieldSearchManager) {
        this.engineManager = engineManager;
        this.hexManager = hexManager;
        this.layoutManager = layoutManager;
        this.hexFieldLayoutManager = hexFieldLayoutManager;
        this.hexFieldSearchManager = hexFieldSearchManager;
        pool = Pooler.of(HexField::new);
        LOGGER.debug("HexFieldEntityManager()");
    }

  /**
   * Generate hex field.
   *
   * @param configuration        the configuration
   * @param additionalComponents the additional components
   * @return the hex field
   */
  public HexField generate(final HexFieldConfiguration configuration,
                             final Function<HexComponent, Set<Component>> additionalComponents) {
        final HexFieldLayout hexFieldLayout = hexFieldLayoutManager.obtain(configuration);
        final Set<Entity> entities = hexManager.generate(hexFieldLayout.cols(), hexFieldLayout.rows())
                .stream()
                .map(h -> engineManager.createComponent(HexComponent.class)
                        .initialize(h, hexFieldLayout.layout(), layoutManager))
                .map(hexComponent -> createEntity(hexComponent, additionalComponents))
                .collect(Collectors.toSet());
        entities.forEach(engineManager::addEntity);
        return pool.obtain()
                .setHexEntityHashMap(hexFieldSearchManager.hexEntityHashMap(entities))
                .setHexFieldLayout(hexFieldLayout);
    }

    private Entity createEntity(final HexComponent hexComponent,
                                final Function<HexComponent, Set<Component>> additionalComponents) {
        final Entity entity = engineManager.createEntity().add(hexComponent);
        additionalComponents.apply(hexComponent).forEach(entity::add);
        return entity;
    }

  /**
   * Release.
   *
   * @param hexField the hex field
   */
  public void release(final HexField hexField) {
        hexFieldLayoutManager.free(hexField.getHexFieldLayout()); // free the layout first.
        engineManager.engine().getEntitiesFor(HEXS).forEach(e->{
            final HexComponent component = e.getComponent(HexComponent.class);
            hexManager.free(component.hex());
        });
        engineManager.engine().removeAllEntities(HEXS); // We do this since freeing each hex entity individually can retain some due to delay.
        pool.free(hexField);
    }

}
