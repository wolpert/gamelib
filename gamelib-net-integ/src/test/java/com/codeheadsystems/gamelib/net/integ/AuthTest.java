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

package com.codeheadsystems.gamelib.net.integ;

import static com.codeheadsystems.gamelib.net.integ.authenticator.Authenticators.AlwaysAuth;
import static com.codeheadsystems.gamelib.net.integ.authenticator.Authenticators.AlwaysFail;
import static com.codeheadsystems.gamelib.net.integ.listener.Listeners.IGNORE_LISTENER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.codeheadsystems.gamelib.net.factory.ObjectMapperFactory;
import com.codeheadsystems.gamelib.net.integ.util.NetComponents;
import com.codeheadsystems.gamelib.net.manager.JsonManager;
import com.codeheadsystems.gamelib.net.model.Authenticated;
import com.codeheadsystems.gamelib.net.model.Disconnect;
import com.codeheadsystems.gamelib.net.model.Identity;
import com.codeheadsystems.gamelib.net.model.ImmutableIdentity;
import com.codeheadsystems.gamelib.net.model.ServerDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthTest.class);

  private JsonManager jsonManager;
  private NetComponents net;

  @BeforeEach
  public void setup() {
    final ObjectMapper mapper = new ObjectMapperFactory().objectMapper();
    jsonManager = new JsonManager(mapper);
    net = null;
  }

  @AfterEach
  void tearDown() {
    if (net != null) {
      net.stop();
      net = null;
    }
  }

  @Test
  public void TestAuthSuccess() throws InterruptedException, ExecutionException, TimeoutException {
    net = new NetComponents(AlwaysAuth, IGNORE_LISTENER).start();
    final Identity identity = ImmutableIdentity.builder().id("id").token("token").build();
    validateServerDetails(net);
    // auth
    net.sendMessageFromClient(jsonManager.toJson(identity));
    validateAuthenticated(net);
  }

  @Test
  public void TestAuthFailure() throws InterruptedException, ExecutionException, TimeoutException {
    net = new NetComponents(AlwaysFail, IGNORE_LISTENER).start();
    final Identity identity = ImmutableIdentity.builder().id("id").token("token").build();
    validateServerDetails(net);
    // auth
    net.sendMessageFromClient(jsonManager.toJson(identity));
    if (!net.clientManager().closeFuture().await(500, TimeUnit.MILLISECONDS)) {
      fail("Connection didn't close in time");
    }
  }


  private void validateAuthenticated(final NetComponents net) throws ExecutionException, InterruptedException, TimeoutException {
    LOGGER.info("validateAuthenticated({})", net);
    final Authenticated authenticated = net.clientManager().getAuthenticatedFuture().get(500, TimeUnit.MILLISECONDS);
    assertThat(authenticated).isNotNull();
  }

  private void validateServerDetails(final NetComponents net) throws ExecutionException, InterruptedException, TimeoutException {
    LOGGER.info("validateServerDetails({})", net);
    final ServerDetails serverDetails = net.clientManager().getServerDetailsFuture().get(500, TimeUnit.MILLISECONDS);
    assertThat(serverDetails).isNotNull();
  }

}
