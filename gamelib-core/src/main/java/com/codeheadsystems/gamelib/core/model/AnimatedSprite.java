/*
 * Copyright (c) 2023. Ned Wolpert
 */

package com.codeheadsystems.gamelib.core.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The type Animated sprite.
 */
public class AnimatedSprite extends Sprite {

  private final Animation<TextureRegion> animation;
  private final boolean looping;
  private float runtime;

  /**
   * Instantiates a new Animated sprite.
   *
   * @param animation the animation
   */
  public AnimatedSprite(final Animation<TextureRegion> animation) {
    this(animation, true);
  }

  /**
   * Instantiates a new Animated sprite.
   *
   * @param animation the animation
   * @param looping   the looping
   */
  public AnimatedSprite(final Animation<TextureRegion> animation,
                        final boolean looping) {
    super(animation.getKeyFrame(0));
    this.animation = animation;
    this.looping = looping;
    runtime = 0f;
  }

  @Override
  public void draw(final Batch batch) {
    batch.draw(
        animation.getKeyFrame(runtime),
        getX(), getY(),
        getOriginX(), getOriginY(),
        getWidth(), getHeight(),
        getScaleX(), getScaleY(),
        getRotation());
  }

  /**
   * Update.
   *
   * @param delta the delta
   */
  public void update(float delta) {
    if (looping) {
      runtime += delta;
      runtime %= animation.getAnimationDuration();
    } else {
      runtime = Math.min(runtime + delta, animation.getAnimationDuration());
    }
  }

  /**
   * Is animation finished boolean.
   *
   * @return the boolean
   */
  public boolean isAnimationFinished() {
    return !looping && runtime >= animation.getAnimationDuration();
  }

  /**
   * Gets animation.
   *
   * @return the animation
   */
  public Animation<TextureRegion> getAnimation() {
    return animation;
  }

  /**
   * Gets runtime.
   *
   * @return the runtime
   */
  public float getRuntime() {
    return runtime;
  }

  /**
   * Is looping boolean.
   *
   * @return the boolean
   */
  public boolean isLooping() {
    return looping;
  }
}
