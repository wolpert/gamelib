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

package com.codeheadsystems.gamelib.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.files.FileHandle;
import com.codeheadsystems.gamelib.core.GdxTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InternalPrefixedFileHandleResolverTest extends GdxTest {

  private static final String PATH = "PATH";
  private static final String FILE_NAME = "NAME";

  @Mock private FileHandle fileHandle;

  @Test
  void resolve_nullPath() {
    final InternalPrefixedFileHandleResolver resolver = new InternalPrefixedFileHandleResolver(null);
    when(files.internal(FILE_NAME)).thenReturn(fileHandle);

    final FileHandle result = resolver.resolve(FILE_NAME);

    assertThat(result)
        .isNotNull()
        .isEqualTo(fileHandle);
  }

  @Test
  void resolve_withPath() {
    final InternalPrefixedFileHandleResolver resolver = new InternalPrefixedFileHandleResolver(PATH);
    when(files.internal(PATH + FILE_NAME)).thenReturn(fileHandle);

    final FileHandle result = resolver.resolve(FILE_NAME);

    assertThat(result)
        .isNotNull()
        .isEqualTo(fileHandle);
  }
}