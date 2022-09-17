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
 * Client request to auth to the server.
 */
@JsonSerialize(as = ImmutableIdentity.class)
@JsonDeserialize(builder = ImmutableIdentity.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Immutable
public interface Identity extends TransferObject {

  String TYPE = "identity";

  @Value.Default
  @Override
  default String type() {
    return TYPE;
  }

  @JsonProperty("id")
  String id();

  @JsonProperty("token")
  String token();

}
