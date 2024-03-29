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
import com.codeheadsystems.gamelib.loader.screen.LoadingScreen;

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
   * The Resizable listener manager.
   */
  protected final ListenerManager<Resizable> resizableListenerManager;
  /**
   * The Pausable listener manager.
   */
  protected final ListenerManager<Pausable> pausableListenerManager;
  /**
   * The Resumable listener manager.
   */
  protected final ListenerManager<Resumable> resumableListenerManager;

  /**
   * The Screen.
   */
  protected Screen screen;
  /**
   * The Infrastructure.
   */
  protected Infrastructure infrastructure;

  /**
   * Instantiates a new Gdx game.
   */
  protected GdxGame() {
    // we cannot use the logger yet since this occurs before the GDX infrastructure is setup.
    System.out.println("GdxGame()");
    disposableListeners = new ListenerManager<>();
    resizableListenerManager = new ListenerManager<>();
    pausableListenerManager = new ListenerManager<>();
    resumableListenerManager = new ListenerManager<>();
  }

  /**
   * Instance gdx game. This can be called anywhere. It's effectively the container.
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

  /**
   * Add disposable listener.
   *
   * @param disposable the disposable
   */
  public void addDisposableListener(Disposable disposable) {
    disposableListeners.addListener(disposable);
  }

  /**
   * Remove disposable listener.
   *
   * @param disposable the disposable
   */
  public void removeDisposableListener(Disposable disposable) {
    disposableListeners.removeListener(disposable);
  }

  /**
   * Add resizable listener.
   *
   * @param resizable the resizable
   */
  public void addResizableListener(Resizable resizable) {
    resizableListenerManager.addListener(resizable);
  }

  /**
   * Remove resizable listener.
   *
   * @param resizable the resizable
   */
  public void removeResizableListener(Resizable resizable) {
    resizableListenerManager.removeListener(resizable);
  }

  /**
   * Add pausable listener.
   *
   * @param pausable the pausable
   */
  public void addPausableListener(Pausable pausable) {
    pausableListenerManager.addListener(pausable);
  }

  /**
   * Remove pausable listener.
   *
   * @param pausable the pausable
   */
  public void removePausableListener(Pausable pausable) {
    pausableListenerManager.removeListener(pausable);
  }

  /**
   * Add resumable listener.
   *
   * @param resumable the resumable
   */
  public void addResumableListener(Resumable resumable) {
    resumableListenerManager.addListener(resumable);
  }

  /**
   * Remove resumable listener.
   *
   * @param resumable the resumable
   */
  public void removeResumableListener(Resumable resumable) {
    resumableListenerManager.removeListener(resumable);
  }

  @Override
  public void create() {
    create(Infrastructure.build());
  }

  /**
   * Package protected so we can use this for testing.
   *
   * @param infrastructure the infrastructure
   */
  void create(Infrastructure infrastructure) {
    LOGGER.info("create(" + infrastructure + ")");
    this.infrastructure = infrastructure;
    setScreen(new LoadingScreen());
  }

  @Override
  public void dispose() {
    LOGGER.info("dispose()");
    if (screen != null) screen.dispose();
    if (infrastructure != null) infrastructure.dispose();
    disposableListeners.forEach(Disposable::dispose);
  }

  @Override
  public void pause() {
    LOGGER.info("pause()");
    if (screen != null) screen.pause();
    pausableListenerManager.forEach(Pausable::pause);
  }

  @Override
  public void resume() {
    LOGGER.info("resume()");
    if (screen != null) screen.resume();
    resumableListenerManager.forEach(Resumable::resume);
  }

  @Override
  public void render() {
    final float deltaTime = Gdx.graphics.getDeltaTime();
    if (screen != null) screen.render(deltaTime);
  }


  @Override
  public void resize(int width, int height) {
    LOGGER.info("resize(" + width + "," + height + ")");
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
    LOGGER.info("setScreen(" + screen + ")");
    if (this.screen != null) this.screen.hide();
    this.screen = screen;
    if (this.screen != null) {
      this.screen.show();
      this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
  }

  /**
   * Gets infrastructure.
   *
   * @return the infrastructure
   */
  public Infrastructure getInfrastructure() {
    return infrastructure;
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

  /**
   * The interface Pausable.
   */
  @FunctionalInterface
  public interface Pausable {
    /**
     * Pause.
     */
    void pause();
  }

  /**
   * The interface Resumable.
   */
  @FunctionalInterface
  public interface Resumable {
    /**
     * Resume.
     */
    void resume();
  }

}
