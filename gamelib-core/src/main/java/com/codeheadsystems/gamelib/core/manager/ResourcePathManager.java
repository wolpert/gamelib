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

package com.codeheadsystems.gamelib.core.manager;

import static com.codeheadsystems.gamelib.core.dagger.GameResources.RESOURCE_PATH;

import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * The type Resource path manager.
 */
@Singleton
public class ResourcePathManager {

  private final Function<String, String> resourceFunction;

  /**
   * Instantiates a new Resource path manager.
   *
   * @param resourcePath the resource path
   */
  @Inject
  public ResourcePathManager(@Named(RESOURCE_PATH) Optional<String> resourcePath) {
    if (resourcePath.isPresent()) {
      final String path = resourcePath.get();
      resourceFunction = (s) -> path + s;
    } else {
      resourceFunction = (s) -> s;
    }
  }

  /**
   * Path string.
   *
   * @param resource the resource
   * @return the string
   */
  public String path(final String resource) {
    return resourceFunction.apply(resource);
  }

}
