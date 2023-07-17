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

package com.codeheadsystems.gamelib.core.util;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;

/**
 * Purpose: To manage the file handle resolver if everything has an internal path.
 */
public class InternalPrefixedFileHandleResolver implements FileHandleResolver {

  private static final Logger LOGGER = logger(InternalPrefixedFileHandleResolver.class);

  private final String internalPath;

  /**
   * Instantiates a new Internal prefixed file handle resolver.
   *
   * @param internalPath the internal path
   */
  public InternalPrefixedFileHandleResolver(final String internalPath) {
    if (internalPath == null) {
      this.internalPath = "";
    } else {
      this.internalPath = internalPath;
    }
  }

  @Override
  public FileHandle resolve(final String fileName) {
    final String fullPath = internalPath + fileName;
    LOGGER.info("Resolving: " + fullPath);
    return Gdx.files.internal(fullPath);
  }
}
