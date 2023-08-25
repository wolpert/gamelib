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

package com.codeheadsystems.gamelib.loader;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.loader.manager.ListenerManager;

/**
 * The type Gdx game.
 */
public class GdxGame implements ApplicationListener {

  private static final Logger LOGGER = new Logger(GdxGame.class.getSimpleName(), Logger.DEBUG);
  /**
   * The constant INSTANCE.
   */
  protected static volatile GdxGame INSTANCE;
  /**
   * The Disposable listeners.
   */
  protected final ListenerManager<Disposable> disposableListeners;
  /**
   * The Renderable listener manager.
   */
  protected final ListenerManager<Renderable> renderableListenerManager;
  /**
   * The Resizable listener manager.
   */
  protected final ListenerManager<Resizable> resizableListenerManager;

  /**
   * The Screen.
   */
  protected Screen screen;

  /**
   * Instantiates a new Gdx game.
   */
  protected GdxGame() {
    disposableListeners = new ListenerManager<>();
    renderableListenerManager = new ListenerManager<>();
    resizableListenerManager = new ListenerManager<>();
  }

  /**
   * Instance gdx game.
   *
   * @return the gdx game
   */
  public static GdxGame instance() {
    if (INSTANCE == null) {
      synchronized (GdxGame.class) {
        if (INSTANCE == null) {
          INSTANCE = new GdxGame();
        }
      }
    }
    return INSTANCE;
  }

  @Override
  public void create() {

  }

  @Override
  public void dispose() {
    if (screen != null) screen.hide();
    disposableListeners.forEach(Disposable::dispose);
  }

  @Override
  public void pause() {
    if (screen != null) screen.pause();
  }

  @Override
  public void resume() {
    if (screen != null) screen.resume();
  }

  @Override
  public void render() {
    final float deltaTime = Gdx.graphics.getDeltaTime();
    if (screen != null) screen.render(deltaTime);
    renderableListenerManager.forEach(r -> r.render(deltaTime));
  }


  @Override
  public void resize(int width, int height) {
    if (screen != null) screen.resize(width, height);
    resizableListenerManager.forEach(r -> r.resize(width, height));
  }

  /**
   * Gets screen.
   *
   * @return the currently active {@link Screen}.
   */
  public Screen getScreen() {
    return screen;
  }

  /**
   * Sets the current screen. {@link Screen#hide()} is called on any old screen, and {@link Screen#show()} is called on the new
   * screen, if any.
   *
   * @param screen may be {@code null}
   */
  public void setScreen(Screen screen) {
    if (this.screen != null) this.screen.hide();
    this.screen = screen;
    if (this.screen != null) {
      this.screen.show();
      this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
  }

  /**
   * The interface Renderable.
   */
  @FunctionalInterface
  public interface Renderable {
    /**
     * Render.
     *
     * @param deltaTime the delta time
     */
    void render(float deltaTime);
  }

  /**
   * The interface Resizable.
   */
  @FunctionalInterface
  public interface Resizable {
    /**
     * Resize.
     *
     * @param width  the width
     * @param height the height
     */
    void resize(int width, int height);
  }
}
