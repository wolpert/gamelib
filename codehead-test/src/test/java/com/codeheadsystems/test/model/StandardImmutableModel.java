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

package com.codeheadsystems.test.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableStandardImmutableModel.class)
@JsonDeserialize(builder = ImmutableStandardImmutableModel.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface StandardImmutableModel {

  String someString();

  @JsonProperty("some unique String")
  String someWeirdString();

  @JsonProperty("someInt")
  int someInt();

  @JsonProperty("bunchOfString annotated")
  List<String> bunchOfOtherString();

  List<String> bunchOfString();

  @JsonProperty("nullableString")
  @Nullable
  String nullableString();

  @JsonProperty("optionalString")
  Optional<String> optionalString();

  @JsonProperty("aMap")
  Map<String, String> aMap();

  @JsonProperty("defaultString")
  default String defaultString() {
    return "defaultString";
  }
}
