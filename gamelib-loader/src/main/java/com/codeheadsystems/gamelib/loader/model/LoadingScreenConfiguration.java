/*
 * Copyright (c) 2023. Ned Wolpert
 */

package com.codeheadsystems.gamelib.loader.model;

/**
 * The type Loading screen.
 */
public class LoadingScreenConfiguration {

  private String screenProvider;

  private String postQueueAssetsHook;
  private String preQueueAssetsHook;

  /**
   * Gets post queue assets hook.
   *
   * @return the post queue assets hook
   */
  public String getPostQueueAssetsHook() {
    return postQueueAssetsHook;
  }

  /**
   * Gets pre queue assets hook.
   *
   * @return the pre queue assets hook
   */
  public String getPreQueueAssetsHook() {
    return preQueueAssetsHook;
  }

  /**
   * This is the name of the provider that creates the first screen of your game.
   * We will show this after everything is loaded.
   * It must implement @ScreenProvider
   *
   * @return the name of the provider.
   */
  public String getScreenProvider() {
    return screenProvider;
  }
}
