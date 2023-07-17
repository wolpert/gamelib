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

package com.codeheadsystems.gamelib.entity.configuration;

/**
 * Provides a configuration for ashley
 */
public class AshleyGameConfiguration {

  /**
   * The Entity pool initial size.
   */
  public int entityPoolInitialSize;
  /**
   * The Entity pool max size.
   */
  public int entityPoolMaxSize;
  /**
   * The Component pool initial size.
   */
  public int componentPoolInitialSize;
  /**
   * The Component pool max size.
   */
  public int componentPoolMaxSize;

}
