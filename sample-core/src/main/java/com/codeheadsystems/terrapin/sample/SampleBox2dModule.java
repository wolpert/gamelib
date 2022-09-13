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

import static com.codeheadsystems.gamelib.core.dagger.GameResources.VIEWPORT_WIDTH;
import static com.codeheadsystems.gamelib.entity.dagger.GameLibEntityModule.ENTITY_SCREEN_SHOW_CONSUMER;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.codeheadsystems.gamelib.box2d.builder.WorldComponentBuilder;
import com.codeheadsystems.gamelib.box2d.manager.WorldManager;
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

@Module(includes = {SampleBox2dModule.Binder.class})
public class SampleBox2dModule {

  @Provides
  @Singleton
  @Named(VIEWPORT_WIDTH)
  Float viewportWidth() {
    return 10F; // 10 meters.
  }

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
                                      final OrthographicCamera camera,
                                      final WorldManager worldManager,
                                      final WorldComponentBuilder builder) {
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

      final World world = worldManager.world();
      // Ground
      BodyDef groundBodyDef = new BodyDef();
      groundBodyDef.position.set(new Vector2(0, camera.viewportHeight * 0.05f));
      Body groundBody = world.createBody(groundBodyDef);
      builder.doWithShape(PolygonShape::new, box -> {
        box.setAsBox(camera.viewportWidth, camera.viewportHeight * 0.05f);
        return groundBody.createFixture(box, 0.0f);
      });

      // Ball
      BodyDef bodyDef = new BodyDef();
      bodyDef.type = BodyDef.BodyType.DynamicBody;
      bodyDef.position.set(camera.viewportWidth / 4f, camera.viewportHeight / 2f);
      Body body = world.createBody(bodyDef);
      builder.doWithShape(CircleShape::new, circle -> {
        circle.setRadius(camera.viewportHeight * 0.1f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.8f; // Make it bounce a bit, but degrade.
        return body.createFixture(fixtureDef);
      });
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
    @Named("mainScreen")
    Screen mainScreen(EntityScreen impl);
  }
}
