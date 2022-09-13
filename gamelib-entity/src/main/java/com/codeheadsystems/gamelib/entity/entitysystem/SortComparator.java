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

package com.codeheadsystems.gamelib.entity.entitysystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.codeheadsystems.gamelib.entity.component.SortComponent;
import java.util.Comparator;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Purpose: Provides the sort comparator. Reusable.
 */
@Singleton
public class SortComparator implements Comparator<Entity> {

  private final ComponentMapper<SortComponent> sm;

  @Inject
  public SortComparator() {
    sm = ComponentMapper.getFor(SortComponent.class);
  }

  @Override
  public int compare(Entity e1, Entity e2) {
    return (int) Math.signum(sm.get(e1).z - sm.get(e2).z);
  }
}
