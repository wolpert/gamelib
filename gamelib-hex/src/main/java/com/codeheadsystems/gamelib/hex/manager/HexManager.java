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

package com.codeheadsystems.gamelib.hex.manager;

import static com.codeheadsystems.gamelib.core.util.LoggerHelper.logger;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.hex.component.HexComponent;
import com.codeheadsystems.gamelib.hex.model.Hex;
import com.codeheadsystems.gamelib.core.util.PoolerImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HexManager extends PoolerImpl<Hex> {
    private static final Logger LOGGER = logger(HexManager.class);
    public static List<Hex> directions = new ArrayList<Hex>() {{
        add(Hex.of(1, 0, -1)); // NE
        add(Hex.of(1, -1, 0)); // SE
        add(Hex.of(0, -1, 1));
        add(Hex.of(-1, 0, 1));
        add(Hex.of(-1, 1, 0));
        add(Hex.of(0, 1, -1));
    }};
    public static List<Hex> diagonals = new ArrayList<Hex>() {{
        add(Hex.of(2, -1, -1));
        add(Hex.of(1, -2, 1));
        add(Hex.of(-1, -1, 2));
        add(Hex.of(-2, 1, 1));
        add(Hex.of(-1, 2, -1));
        add(Hex.of(1, 1, -2));
    }};

    @Inject
    public HexManager() {
        super(Hex::new);
        LOGGER.debug("HexManager()");
    }

    public Hex direction(final int direction) {
        return directions.get(direction);
    }

    public Hex axial(final int x, final int y) {
        return obtain().setQ(x).setR(y).setS(-x - y).checkConstructorArguments();
    }

    /**
     * Generates a set of hexes. Note that its exact rows/cols given, but assumes each row
     * zig/zag to the right. (Starts at bottom left corner. Each row goes up.)
     * <p>
     * FlatTop only. Generates new hexes.
     */
    public HashSet<Hex> generate(final int cols, final int rows) {
        final HashSet<Hex> hashSet = new HashSet<>();
        for (int y = 0; y < rows; y++) { // flat top
            Hex currentHex = axial(0, y);
            hashSet.add(currentHex);
            for (int x = 1; x < cols; x++) {
                final int direction = (x % 2 == 0 ? 1 : 0); // even, pick 0, else pick 1. ZigZag all the way up.
                currentHex = add(currentHex, direction(direction));
                hashSet.add(currentHex);
            }
        }
        return hashSet;
    }

    /**
     * Does not free old hexes.
     */
    public Hex add(final Hex a, final Hex b) {
        return obtain().setQ(a.q() + b.q()).setR(a.r() + b.r()).setS(a.s() + b.s());
    }

    /**
     * Does not free old hexes.
     */
    public Hex subtract(final Hex a, final Hex b) {
        return obtain().setQ(a.q() - b.q()).setR(a.r() - b.r()).setS(a.s() - b.s());
    }

    /**
     * Does not free old hexes.
     */
    public Hex scale(final Hex a, final int k) {
        return obtain().setQ(a.q() * k).setR(a.r() * k).setS(a.s() * k);
    }

    /**
     * Does not free old hexes.
     */
    public Hex rotateLeft(final Hex a) {
        return obtain().setQ(-a.s()).setR(-a.q()).setS(-a.r());
    }

    /**
     * Does not free old hexes.
     */
    public Hex rotateRight(final Hex a) {
        return obtain().setQ(-a.r()).setR(-a.s()).setS(-a.q());
    }

    /**
     * Does not free old hexes.
     */
    public Hex neighbor(final Hex a, final int direction) {
        return add(a, direction(direction));
    }

    /**
     * Does not free old hexes.
     */
    public Hex diagonalNeighbor(final Hex a, final int direction) {
        return add(a, diagonals.get(direction));
    }

    public int distance(final Hex a, final Hex b) {
        final Hex tempHex = subtract(a, b);
        final int result = tempHex.length();
        free(tempHex);
        return result;
    }

    /**
     * Checks the distance between the two hex entities. If they have no entities, this will blow up.
     *
     * @param first  entity.
     * @param second entity.
     * @return the distance.
     */
    public int distance(final Entity first,
                        final Entity second) {
        final Hex firstHex = Optional.ofNullable(first.getComponent(HexComponent.class))
                .map(HexComponent::hex)
                .orElseThrow(IllegalArgumentException::new);
        final Hex secondHex = Optional.ofNullable(second.getComponent(HexComponent.class))
                .map(HexComponent::hex)
                .orElseThrow(IllegalArgumentException::new);
        return distance(firstHex, secondHex);
    }
}
