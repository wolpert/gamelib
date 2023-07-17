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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.UUID;
import org.immutables.value.Value;

/**
 * The interface Transfer object.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Authenticated.class, name = Authenticated.TYPE),
    @JsonSubTypes.Type(value = Disconnect.class, name = Disconnect.TYPE),
    @JsonSubTypes.Type(value = Identity.class, name = Identity.TYPE),
    @JsonSubTypes.Type(value = Message.class, name = Message.TYPE),
    @JsonSubTypes.Type(value = Notification.class, name = Notification.TYPE),
    @JsonSubTypes.Type(value = ServerDetails.class, name = ServerDetails.TYPE),
})
public interface TransferObject {

  /**
   * Type string.
   *
   * @return the string
   */
  @JsonProperty("type")
  String type();

  /**
   * Uuid uuid.
   *
   * @return the uuid
   */
  @JsonProperty("uuid")
  @Value.Default
  default UUID uuid() {
    return UUID.randomUUID();
  }

}
