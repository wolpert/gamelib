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

package com.codeheadsystems.gamelib.net.server.manager;

import static com.codeheadsystems.gamelib.net.server.manager.AuthenticationManager.AUTH_FAIL;
import static com.codeheadsystems.gamelib.net.server.manager.AuthenticationManager.AUTH_TIMER_EXPIRED;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.codeheadsystems.gamelib.net.exception.JsonException;
import com.codeheadsystems.gamelib.net.manager.JsonManager;
import com.codeheadsystems.gamelib.net.model.Identity;
import com.codeheadsystems.gamelib.net.server.Authenticator;
import com.codeheadsystems.gamelib.net.server.NetClientHandler;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationManagerTest {
  private static final String MESSAGE = "this is a message";

  @Mock private Authenticator authenticator;
  @Mock private TimerManager timerManager;
  @Mock private JsonManager jsonManager;
  @Mock private NetClientHandler handler;

  @Mock private Future future;
  @Mock private Identity identity;

  @InjectMocks private AuthenticationManager authenticationManager;

  @Test
  void timerExpired() {
    authenticationManager.timerExpired();
    verify(handler).shutdown(AUTH_TIMER_EXPIRED);
    authenticationManager.timerExpired();
    verifyNoMoreInteractions(handler);
  }

  @Test
  void authenticate_timerExpired() {
    when(timerManager.enabledAuthTimeoutHandler(authenticationManager)).thenReturn(future);

    authenticationManager.initialized();
    authenticationManager.timerExpired();
    authenticationManager.authenticate(MESSAGE);
    verifyNoInteractions(future);
    verify(timerManager).enabledAuthTimeoutHandler(authenticationManager);
  }

  @Test
  void authenticate_identityValid() {
    when(timerManager.enabledAuthTimeoutHandler(authenticationManager)).thenReturn(future);
    when(jsonManager.fromJson(MESSAGE, Identity.class)).thenReturn(identity);
    when(authenticator.isAuthenticated(identity)).thenReturn(true);

    authenticationManager.initialized();
    authenticationManager.authenticate(MESSAGE);

    verify(timerManager).enabledAuthTimeoutHandler(authenticationManager);
    verify(future).cancel(false);
    verify(handler).authenticated();
  }

  @Test
  void authenticate_identityInvalid() {
    when(timerManager.enabledAuthTimeoutHandler(authenticationManager)).thenReturn(future);
    when(jsonManager.fromJson(MESSAGE, Identity.class)).thenReturn(identity);
    when(authenticator.isAuthenticated(identity)).thenReturn(false);

    authenticationManager.initialized();
    authenticationManager.authenticate(MESSAGE);

    verify(timerManager).enabledAuthTimeoutHandler(authenticationManager);
    verify(future).cancel(false);
    verify(handler, never()).authenticated();
    verify(handler).shutdown(AUTH_FAIL);
  }

  @Test
  void authenticate_identityAuthException() {
    when(timerManager.enabledAuthTimeoutHandler(authenticationManager)).thenReturn(future);
    when(jsonManager.fromJson(MESSAGE, Identity.class)).thenReturn(identity);
    when(authenticator.isAuthenticated(identity)).thenThrow(new IllegalArgumentException());

    authenticationManager.initialized();
    authenticationManager.authenticate(MESSAGE);

    verify(timerManager).enabledAuthTimeoutHandler(authenticationManager);
    verify(future).cancel(false);
    verify(handler, never()).authenticated();
    verify(handler).shutdown(AUTH_FAIL);
  }

  @Test
  void authenticate_badJson() {
    when(timerManager.enabledAuthTimeoutHandler(authenticationManager)).thenReturn(future);
    when(jsonManager.fromJson(MESSAGE, Identity.class)).thenThrow(new JsonException());

    authenticationManager.initialized();
    authenticationManager.authenticate(MESSAGE);

    verify(timerManager).enabledAuthTimeoutHandler(authenticationManager);
    verify(future).cancel(false);
    verify(handler, never()).authenticated();
    verify(handler).shutdown(AUTH_FAIL);
  }


  @Test
  void initialized() {
    when(timerManager.enabledAuthTimeoutHandler(authenticationManager)).thenReturn(future);

    authenticationManager.initialized();

    verify(timerManager).enabledAuthTimeoutHandler(authenticationManager);
  }

}