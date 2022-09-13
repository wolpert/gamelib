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
import com.codeheadsystems.gamelib.core.dagger.GameLibModule;
import com.codeheadsystems.gamelib.entity.dagger.GameLibEntityModule;
import com.codeheadsystems.terrapin.sample.SampleEntityModule;
import dagger.Component;
import javax.inject.Singleton;

public class EntityDesktopLauncher {


  public static void main(String[] arg) {
    DaggerEntityDesktopLauncher_EntityDesktopComponent.builder()
        .build()
        .application();
  }

  @Singleton
  @Component(modules = {
      SampleEntityModule.class,
      GameLibModule.class,
      GameLibEntityModule.class
  })
  public interface EntityDesktopComponent {

    Lwjgl3Application application();

  }

}
