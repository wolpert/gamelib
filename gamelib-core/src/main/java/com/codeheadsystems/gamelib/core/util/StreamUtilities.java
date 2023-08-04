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

package com.codeheadsystems.gamelib.core.util;

import java.util.stream.Stream;

/**
 * The type Stream utilities.
 */
public class StreamUtilities {

  /**
   * Given the start, and for everytime start is greater than reduceBy, reduce start and add that
   * number to the list. If start is always smaller than reduceBy, the stream is empty.
   *
   * @param start    the start
   * @param reduceBy the reduce by
   * @return the stream
   */
  public static Stream<Float> reduceStream(final float start, final float reduceBy) {
    return Stream.iterate(start - reduceBy, f -> f >= 0, f -> f - reduceBy);
  }

}
