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

package com.codeheadsystems.gamelib.net.integ.listener;

import com.codeheadsystems.gamelib.net.server.GameListener;
import com.codeheadsystems.gamelib.net.server.ImmutableGameListener;
import com.codeheadsystems.gamelib.net.server.MessageHandler;

public class Listeners {

  public static MessageHandler IGNORE_HANDLER = (m,c) -> {};
  public static GameListener IGNORE_LISTENER = ImmutableGameListener.builder()
      .authenticatedMessageHandler(IGNORE_HANDLER)
      .availableMessageHandler(IGNORE_HANDLER)
      .build();


}
