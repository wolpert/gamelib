package com.codeheadsystems.gamelib.entity.entity;

import com.badlogic.ashley.core.Entity;

/**
 * Creating this as a mechanism to formalize entity creation. Useful for dagger ioc.
 */
@FunctionalInterface
public interface EntityGenerator {

  /**
   * Generate entity.
   *
   * @return the entity
   */
  Entity generate();

}
