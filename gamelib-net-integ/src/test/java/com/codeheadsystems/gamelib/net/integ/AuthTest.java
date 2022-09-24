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

package com.codeheadsystems.gamelib.net.integ;

import static com.codeheadsystems.gamelib.net.integ.authenticator.Authenticators.AlwaysAuth;
import static com.codeheadsystems.gamelib.net.integ.authenticator.Authenticators.AlwaysFail;
import static org.assertj.core.api.Assertions.assertThat;

import com.codeheadsystems.gamelib.net.client.manager.ClientManager;
import com.codeheadsystems.gamelib.net.factory.ObjectMapperFactory;
import com.codeheadsystems.gamelib.net.integ.util.NetComponents;
import com.codeheadsystems.gamelib.net.manager.JsonManager;
import com.codeheadsystems.gamelib.net.model.Authenticated;
import com.codeheadsystems.gamelib.net.model.Disconnect;
import com.codeheadsystems.gamelib.net.model.Identity;
import com.codeheadsystems.gamelib.net.model.ImmutableIdentity;
import com.codeheadsystems.gamelib.net.model.ServerDetails;
import com.codeheadsystems.gamelib.net.server.manager.ServerManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthTest.class);

  private JsonManager jsonManager;

  @BeforeEach
  public void setup() {
    final ObjectMapper mapper = new ObjectMapperFactory().objectMapper();
    jsonManager = new JsonManager(mapper);
  }

  @Test
  public void TestAuthSuccess() throws InterruptedException {
    final NetComponents net = new NetComponents(AlwaysAuth).start();
    final Identity identity = ImmutableIdentity.builder().id("id").token("token").build();
    validateServerDetails(net.queue().poll(500, TimeUnit.MILLISECONDS));
    // auth
    net.sendMessageFromClient(jsonManager.toJson(identity));
    validateAuthenticated(net.queue().poll(500, TimeUnit.MILLISECONDS));
    net.stop();
  }

  @Test
  public void TestAuthFailure() throws InterruptedException {
    final NetComponents net = new NetComponents(AlwaysFail).start();
    final Identity identity = ImmutableIdentity.builder().id("id").token("token").build();
    validateServerDetails(net.queue().poll(500, TimeUnit.MILLISECONDS));
    // auth
    net.sendMessageFromClient(jsonManager.toJson(identity));
    validateAuthFailure(net.queue().poll(500, TimeUnit.MILLISECONDS));
    net.stop();
  }

  private void validateAuthFailure(final String msg) {
    LOGGER.info("validateAuthFailure({})", msg);
    assertThat(msg).isNotNull();
    final Disconnect details = jsonManager.fromJson(msg, Disconnect.class);
    assertThat(details).isNotNull();
  }


  private void validateAuthenticated(final String msg) {
    LOGGER.info("validateAuthenticated({})", msg);
    assertThat(msg).isNotNull();
    final Authenticated details = jsonManager.fromJson(msg, Authenticated.class);
    assertThat(details).isNotNull();
  }

  private void validateServerDetails(final String msg) {
    LOGGER.info("validateServerDetails({})", msg);
    assertThat(msg).isNotNull();
    final ServerDetails details = jsonManager.fromJson(msg, ServerDetails.class);
    assertThat(details).isNotNull();
  }

}
