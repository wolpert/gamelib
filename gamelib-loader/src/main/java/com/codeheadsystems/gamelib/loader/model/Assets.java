/*
 * Copyright (c) 2023. Ned Wolpert
 */

package com.codeheadsystems.gamelib.loader.model;

import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains assets to load.
 */
public class Assets {

  private HashMap<String, ArrayList<String>> assetsToLoad;
  private ArrayList<Loader> loaders;
  private LoadingScreenConfiguration loadingScreenConfiguration;
  private HashMap<String, FreetypeFontLoader.FreeTypeFontLoaderParameter> fonts;

  /**
   * Gets assets to load.
   *
   * @return the assets to load
   */
  public HashMap<String, ArrayList<String>> getAssetsToLoad() {
    return assetsToLoad;
  }

  /**
   * Loaders array list.
   *
   * @return the array list
   */
  public ArrayList<Loader> loaders() {
    return loaders;
  }

  /**
   * Details about the loading screen.
   *
   * @return the loading screen
   */
  public LoadingScreenConfiguration getLoadingScreenConfiguration() {
    return loadingScreenConfiguration;
  }

  /**
   * Gets fonts.
   *
   * @return the fonts
   */
  public HashMap<String, FreetypeFontLoader.FreeTypeFontLoaderParameter> getFonts() {
    return fonts;
  }
}
