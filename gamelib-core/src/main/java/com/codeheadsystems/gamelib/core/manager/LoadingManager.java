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

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.core.model.Assets;
import com.codeheadsystems.gamelib.core.model.Loader;
import com.codeheadsystems.gamelib.core.model.LoadingConfiguration;
import com.codeheadsystems.gamelib.core.util.LoggerHelper;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Purpose: Provide steps for the loading screen.
 */
@Singleton
public class LoadingManager {

    private static final Logger LOGGER = LoggerHelper.logger(LoadingManager.class);
    private final AssetManager assetManager;
    private final FileHandleResolver fileHandleResolver;
    private final LoadingConfiguration loadingConfiguration;
    private final JsonManager jsonManager;
    private Stages currentStage;
    private Assets assets;

    @Inject
    public LoadingManager(final AssetManager assetManager,
                          final FileHandleResolver fileHandleResolver,
                          final LoadingConfiguration loadingConfiguration,
                          final JsonManager jsonManager) {
        this.assetManager = assetManager;
        this.fileHandleResolver = fileHandleResolver;
        this.loadingConfiguration = loadingConfiguration;
        this.jsonManager = jsonManager;
        currentStage = Stages.INIT;
        LOGGER.info("LoadingManager()");
    }

    /**
     * Returns false if no more updates are needed.
     *
     * @return boolean
     */
    public boolean update() {
        LOGGER.info("Update() : " + currentStage);
        switch (currentStage) {
            case INIT:
                setAssets(jsonManager.fromJson(Assets.class, fileHandleResolver.resolve(loadingConfiguration.getAssetsFilename())));
                setCurrentStage(Stages.ASSET_LOADERS);
                return false;
            case ASSET_LOADERS:
                setCurrentStage(Stages.QUEUE_ASSETS);
                assetManager.setLoader(TiledMap.class, ".tmx", new TmxMapLoader(fileHandleResolver));
                // Loop through the loaders.
                assets.loaders().forEach(this::buildLoader);
                return false;
            case QUEUE_ASSETS:
                for (Map.Entry<String, ArrayList<String>> entry : assets.getAssetsToLoad().entrySet()) {
                    final String clazzName = entry.getKey();
                    LOGGER.info("Processing: " + clazzName);
                    try {
                        final Class<?> clazz = Class.forName(clazzName);
                        for (String filename : entry.getValue()) {
                            LOGGER.info("  adding to queue: " + clazz.getSimpleName() + ":" + filename);
                            assetManager.load(filename, clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException(e);
                    }
                }
                setCurrentStage(Stages.LOAD_ASSETS);
                return false;
            case LOAD_ASSETS:
                if (assetManager.update()) {
                    setCurrentStage(Stages.DONE);
                }
                return false;
            case DONE:
            default:
                return true;
        }
    }

    private <T> void buildLoader(final Loader l) {
        try {
            LOGGER.info("Asset class to load: " + l.classToLoad());
            final Class<T> classToLoad = getParemeterizedClass(l.classToLoad());
            LOGGER.info("    Loading class for the asset: " + l.loaderClass());
            final Class<AsynchronousAssetLoader<T, AssetLoaderParameters<T>>> loaderClazz = getParemeterizedClass(l.loaderClass());
            final AsynchronousAssetLoader<T, AssetLoaderParameters<T>> instance = loaderClazz.getConstructor(FileHandleResolver.class).newInstance(fileHandleResolver);
            assetManager.setLoader(classToLoad, l.suffix(), instance);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            LOGGER.error("Failed to add loader: " + l.classToLoad() + ":" + l.loaderClass(), e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Isolate the evil here. Needs to be a better way to get the class as a generalized class.
     * @param classToLoad what we are loading.
     * @param <T> whatever the type is supposed to be.
     * @return the class.
     * @throws ClassNotFoundException Which could also be due to the type not matching.
     */
    private <T> Class<T> getParemeterizedClass(final String classToLoad) throws ClassNotFoundException {
        return (Class<T>) Class.forName(classToLoad);
    }

    /**
     * Returns the current progress as float.
     *
     * @return progress bar.
     */
    public float getProgress() {
        switch (currentStage) {
            case INIT:
                return 0f;
            case ASSET_LOADERS:
                return 0.1f;
            case QUEUE_ASSETS:
                return 0.2f;
            case LOAD_ASSETS:
                return 0.3f + (0.7f * assetManager.getProgress());
            case DONE:
            default:
                return 1f;
        }
    }

    public AssetManager assetManager() {
        return this.assetManager;
    }

    public Assets assets() {
        return assets;
    }

    public void setAssets(final Assets assets) {
        this.assets = assets;
    }

    public String getStageTitle() {
        return currentStage.title;
    }

    Stages getCurrentStage() {
        return currentStage;
    }

    void setCurrentStage(final Stages currentStage) {
        this.currentStage = currentStage;
    }

    enum Stages {
        INIT("Initializing system"),
        ASSET_LOADERS("Setting up asset loaders"),
        QUEUE_ASSETS("Queueing assets"),
        LOAD_ASSETS("Loading assets from system"),
        DONE("Complete!");

        private final String title;

        Stages(String title) {
            this.title = title;
        }
    }

}
