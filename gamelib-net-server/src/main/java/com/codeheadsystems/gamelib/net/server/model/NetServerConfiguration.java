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

package com.codeheadsystems.gamelib.net.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * The interface Net server configuration.
 */
@JsonSerialize(as = ImmutableNetServerConfiguration.class)
@JsonDeserialize(builder = ImmutableNetServerConfiguration.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Immutable
public interface NetServerConfiguration {

  /**
   * Name string.
   *
   * @return the string
   */
  @JsonProperty("name")
  @Value.Default
  default String name() {
    return "GameLibServer";
  }

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
   * Default pool size fot the timer executor.
   *
   * @return poolsize. int
   */
  @JsonProperty
  @Value.Default
  default int timerExecutorPoolSize() {
    return 5;
  }

  /**
   * How many milliseconds to wait for a response from the
   * client before closing the connection.
   *
   * @return default is five seconds worth.
   */
  @JsonProperty
  @Value.Default
  default long authTimeoutMilliseconds() {
    return 5000L;
  }

}
