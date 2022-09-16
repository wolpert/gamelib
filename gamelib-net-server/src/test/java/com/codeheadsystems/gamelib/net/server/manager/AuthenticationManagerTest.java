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

import static com.codeheadsystems.gamelib.net.server.manager.AuthenticationManager.AUTH_TIMER_EXPIRED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.codeheadsystems.gamelib.net.manager.JsonManager;
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

  @Mock private Authenticator authenticator;
  @Mock private TimerManager timerManager;
  @Mock private JsonManager jsonManager;
  @Mock private NetClientHandler handler;

  @Mock private Future future;

  @InjectMocks private AuthenticationManager authenticationManager;

  @Test
  void timerExpired(){
    authenticationManager.timerExpired();
    verify(handler).shutdown(AUTH_TIMER_EXPIRED);
    authenticationManager.timerExpired();
    verifyNoMoreInteractions(handler);
  }

  @Test
  void initialized(){
    when(timerManager.enabledAuthTimeoutHandler(authenticationManager)).thenReturn(future);

    authenticationManager.initialized();

    verify(timerManager).enabledAuthTimeoutHandler(authenticationManager);
  }

}