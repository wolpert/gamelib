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

package com.codeheadsystems.terrapin.sample.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.codeheadsystems.gamelib.box2d.dagger.GameLibBox2dModule;
import com.codeheadsystems.gamelib.box2d.manager.WorldManager;
import com.codeheadsystems.gamelib.core.dagger.GameLibModule;
import com.codeheadsystems.gamelib.entity.dagger.GameLibEntityModule;
import com.codeheadsystems.terrapin.sample.SampleBox2dModule;
import dagger.Component;
import javax.inject.Singleton;

public class Box2dDesktopLauncher {

  public static void main(String[] arg) {
    DaggerBox2dDesktopLauncher_Box2dDesktopComponent.builder()
        .build()
        .application();
  }

  @Singleton
  @Component(modules = {
      GameLibModule.class,
      GameLibEntityModule.class,
      GameLibBox2dModule.class,
      SampleBox2dModule.class
  })
  public interface Box2dDesktopComponent {

    Lwjgl3Application application();

    WorldManager worldManager();

  }
}
