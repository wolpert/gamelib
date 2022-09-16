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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codeheadsystems.gamelib.net.server.model.NetServerConfiguration;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimerManagerTest {

  @Mock private ScheduledThreadPoolExecutor executorService;
  @Mock private NetServerConfiguration configuration;
  @Mock private AuthenticationManager authenticationManager;

  @Captor private ArgumentCaptor<Runnable> captor;
  @InjectMocks private TimerManager manager;

  @Test
  public void enabledAuthTimeoutHandler() {
    when(configuration.authTimeoutMilliseconds()).thenReturn(10L);

    manager.enabledAuthTimeoutHandler(authenticationManager);

    verify(executorService).schedule(captor.capture(), eq(10L), eq(TimeUnit.MILLISECONDS));
  }

}