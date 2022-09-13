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

package com.codeheadsystems.gamelib.core.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.codeheadsystems.gamelib.core.GdxTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CameraManagerTest extends GdxTest {

    private static final int X = 1;
    private static final int Y = 2;

    @Mock private OrthographicCamera camera;
    @Mock private Vector3 vector3;
    @Captor private ArgumentCaptor<Vector3> vector3ArgumentCaptor;

    private CameraManager cameraManager;

    @BeforeEach
    void setUp() {
        cameraManager = new CameraManager(camera);
    }

    @Test
    void unproject() {
        when(camera.unproject(vector3ArgumentCaptor.capture())).thenReturn(vector3);
        assertThat(cameraManager.unproject(X,Y))
                .isNotNull()
                .isEqualTo(vector3);
        assertThat(vector3ArgumentCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("x", (float)X)
                .hasFieldOrPropertyWithValue("y", (float)Y)
                .hasFieldOrPropertyWithValue("z", 0f);
    }
}