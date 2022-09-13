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

package com.codeheadsystems.terrapin.sample;

import static com.codeheadsystems.gamelib.core.dagger.LoadingModule.MAIN_SCREEN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.codeheadsystems.gamelib.hex.component.HexComponent;
import com.codeheadsystems.gamelib.hex.manager.HexComponentManager;
import com.codeheadsystems.gamelib.hex.manager.HexFieldSearchManager;
import com.codeheadsystems.gamelib.hex.model.HexFieldLayout;
import com.codeheadsystems.gamelib.hex.model.Layout;
import com.codeheadsystems.gamelib.hex.model.Orientation;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class SampleHexScreen extends ScreenAdapter {

    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final AssetManager assetManager;
    private final Set<HexComponent> hexField;
    private final HexComponentManager hexComponentManager;
    private final Layout layout;
    private final HexFieldSearchManager hexFieldSearchManager;

    @Inject
    public SampleHexScreen(final SpriteBatch batch,
                           final ShapeRenderer shapeRenderer,
                           final AssetManager assetManager,
                           final HexComponentManager hexComponentManager,
                           final HexFieldLayout hexFieldLayout,
                           final HexFieldSearchManager hexFieldSearchManager) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.assetManager = assetManager;
        this.hexComponentManager = hexComponentManager;
        this.hexFieldSearchManager = hexFieldSearchManager;
        this.hexField = hexComponentManager.generate(hexFieldLayout);
        this.layout = hexFieldLayout.layout();
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                final Optional<HexComponent> component = hexFieldSearchManager.fromPoint(screenX, screenY, layout, hexField);
                if (component.isPresent()) {
                    System.out.println("Found " + component.get().hex());
                } else {
                    System.out.println("Nothing there");
                }
                return true;
            }
        });
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        hexField.stream()
                .map(HexComponent::vertices)
                .forEach(shapeRenderer::polygon);
        shapeRenderer.end();
    }

    /**
     * Placed the module here to stop code bloat for the default sample. Typically, you would
     * have it in a separate file.
     */
    @Module(includes = {HexModule.HexConfiguration.class})
    public interface HexModule {

        @Binds
        @Named(MAIN_SCREEN)
        Screen mainScreen(SampleHexScreen impl);

        @Module
        class HexConfiguration {

            @Provides
            @Singleton
            public Layout layout() {
                return new Layout()
                    .setOrientation(Orientation.flat)
                    .setOrigin(new Vector2().set(40, 40))
                    .setSize(new Vector2().set(40, 40));
            }

            @Provides
            @Singleton
            @Named("HexConfiguration.rows")
            public int rows() {
                return 5;
            }

            @Provides
            @Singleton
            @Named("HexConfiguration.cols")
            public int cols() {
                return 6;
            }

            @Provides
            @Singleton
            public HexFieldLayout hexFieldLayout(final Layout layout,
                                                 @Named("HexConfiguration.rows") final int rows,
                                                 @Named("HexConfiguration.cols") final int cols) {
                return new HexFieldLayout()
                    .setRows(rows)
                    .setCols(cols)
                    .setLayout(layout);
            }
        }
    }

}
