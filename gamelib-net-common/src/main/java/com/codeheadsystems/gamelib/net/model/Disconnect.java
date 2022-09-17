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

package com.codeheadsystems.gamelib.net.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * Used when a server disconnects the client..
 */
@JsonSerialize(as = ImmutableDisconnect.class)
@JsonDeserialize(builder = ImmutableDisconnect.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Immutable
public interface Disconnect extends TransferObject {

  String TYPE = "disconnect";

  @Value.Default
  @Override
  default String type() {
    return TYPE;
  }

  @JsonProperty("reason")
  String reason();

}
