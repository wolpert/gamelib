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

package com.codeheadsystems.gamelib.net.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;


/**
 * The interface Net client configuration.
 */
@JsonSerialize(as = ImmutableNetClientConfiguration.class)
@JsonDeserialize(builder = ImmutableNetClientConfiguration.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Immutable
public interface NetClientConfiguration {

  /**
   * Version int.
   *
   * @return the int
   */
  @JsonProperty("version")
  @Value.Default
  default int version() {
    return 0;
  }

  /**
   * Build number long.
   *
   * @return the long
   */
  @JsonProperty("buildNumber")
  @Value.Default
  default long buildNumber() {
    return 1;
  }

  /**
   * Port int.
   *
   * @return the int
   */
  @JsonProperty("port")
  @Value.Default
  default int port() {
    return 8992;
  }

  /**
   * Host string.
   *
   * @return the string
   */
  @JsonProperty("host")
  @Value.Default
  default String host() {
    return "127.0.0.1";
  }

}
