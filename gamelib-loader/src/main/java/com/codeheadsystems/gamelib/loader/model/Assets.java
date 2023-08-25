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

package com.codeheadsystems.gamelib.loader.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains assets to load.
 */
public class Assets {

  private HashMap<String, ArrayList<String>> assetsToLoad;
  private ArrayList<Loader> loaders;

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

}
