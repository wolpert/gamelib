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

package com.codeheadsystems.terrapin.sample;

import static com.codeheadsystems.gamelib.core.dagger.LoadingModule.MAIN_SCREEN;
import static com.codeheadsystems.gamelib.entity.dagger.GameLibEntityModule.ENTITY_SCREEN_SHOW_CONSUMER;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.codeheadsystems.gamelib.entity.component.ResizableComponent;
import com.codeheadsystems.gamelib.entity.component.SortComponent;
import com.codeheadsystems.gamelib.entity.component.SpriteComponent;
import com.codeheadsystems.gamelib.entity.entity.EntityScreen;
import com.codeheadsystems.gamelib.entity.manager.EngineManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.util.function.Consumer;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(includes = {SampleEntityModule.Binder.class})
public class SampleEntityModule {

  /**
   * You cannot use Textures or other things that are loaded by the asset manager during
   * dagger injection. The asset manager has to have loaded everything up... which only
   * happens during the loading screen. You can have your own entity screen that creates
   * the sprites if you want, or use the method shown below.
   */
  @Provides
  @Singleton
  @Named(ENTITY_SCREEN_SHOW_CONSUMER)
  Consumer<EntityScreen> showConsumer(final EngineManager engineManager,
                                      final AssetManager assetManager,
                                      final OrthographicCamera camera) {
    return screen -> {
      System.out.println("Sample Sprite!");
      // This texture only gets disposed once the asset manager gets disposed.
      final Texture img = assetManager.get("badlogic.jpg", Texture.class);
      final Sprite sprite = new Sprite(img);
      sprite.setX(0);
      sprite.setY(0);
      // sets the size based on the viewport....
      // note, probably should have a resize handler....
      float width = camera.viewportWidth / 3f;
      float height = width / img.getWidth() * img.getHeight();
      sprite.setSize(width, height);
      sprite.setCenter(camera.viewportWidth / 2f, camera.viewportHeight / 2f);
      final Entity entity = engineManager.createEntity()
          .add(engineManager.createComponent(SpriteComponent.class).sprite(sprite))
          .add(engineManager.createComponent(ResizableComponent.class).setWidth(width))
          .add(engineManager.createComponent(SortComponent.class));
      engineManager.addEntity(entity);
    };
  }

  /**
   * Binder methods only. Must be interfaces.
   */
  @Module
  interface Binder {

    /**
     * Here, we want the first screen to be used once loaded as the entity screen.
     * You can actually setup your own screen first that once the game starts, goes
     * to the entity screen.
     */
    @Binds
    @Named(MAIN_SCREEN)
    Screen mainScreen(EntityScreen impl);
  }
}
