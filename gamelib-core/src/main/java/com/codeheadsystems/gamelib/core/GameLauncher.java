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

package com.codeheadsystems.gamelib.core;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.core.manager.ResizeManager;
import com.codeheadsystems.gamelib.core.screen.LoadingScreen;
import com.codeheadsystems.gamelib.core.util.GameListener;
import dagger.Lazy;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Use this as the main game for libGDX.
 */
@Singleton
public class GameLauncher extends Game {

    private static final Logger LOGGER = logger(GameLauncher.class);

    /**
     * This has to be lazy. The rest of the GDX app needs to be initialized before we get the loading screen.
     * By making this lazy, the app is initialized later.
     */
    private final Lazy<LoadingScreen> loadingScreen;
    private final Lazy<Set<GameListener>> gameListeners;
    private final Lazy<ResizeManager> resizeManager;
    private final AssetManager assetManager;

    @Inject
    public GameLauncher(final Lazy<LoadingScreen> loadingScreen,
                        final Lazy<Set<GameListener>> gameListeners,
                        final Lazy<ResizeManager> resizeManager,
                        final AssetManager assetManager) {
        this.loadingScreen = loadingScreen;
        this.gameListeners = gameListeners;
        this.resizeManager = resizeManager;
        this.assetManager = assetManager;
    }

    @Override
    public void create() {
        gameListeners.get().forEach(l -> l.setGame(this));
        final LoadingScreen screen = loadingScreen.get();
        LOGGER.info("Setting screen: " + screen);
        setScreen(screen);
    }

    @Override
    public void resize(final int width, final int height) {
        LOGGER.info("resize: X:" + width + " Y:" + height);
        resizeManager.get().resize(width, height);
        super.resize(width, height);
    }

    /**
     * We're dead.... remove all the assets.
     */
    @Override
    public void dispose() {
        LOGGER.info("Disposing...");
        super.dispose();
        assetManager.dispose();
        LOGGER.info("Disposed");
    }
}
