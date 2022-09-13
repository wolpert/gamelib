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

import com.badlogic.gdx.utils.Logger;
import com.codeheadsystems.gamelib.hex.model.FractionalHex;
import com.codeheadsystems.gamelib.hex.model.Hex;
import com.codeheadsystems.gamelib.core.util.PoolerImpl;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FractionalHexManager extends PoolerImpl<FractionalHex> {

    private static final Logger LOGGER = logger(FractionalHexManager.class);
    private final HexManager hexManager;

    @Inject
    public FractionalHexManager(final HexManager hexManager) {
        super(FractionalHex::new);
        this.hexManager = hexManager;
        LOGGER.debug("FractionalHexManager()");
    }

    /**
     * Does not free old hexes.
     */
    public List<Hex> hexLinedraw(final Hex a, final Hex b) {
        final int n = hexManager.distance(a, b);
        final FractionalHex a_nudge = obtain().setQ(a.q() + 1e-06).setR(a.r() + 1e-06).setS(a.s() - 2e-06).checkConstructorArguments();
        final FractionalHex b_nudge = obtain().setQ(b.q() + 1e-06).setR(b.r() + 1e-06).setS(b.s() - 2e-06).checkConstructorArguments();
        final double step = 1.0 / Math.max(n, 1);
        final List<Hex> collect = IntStream.rangeClosed(0, n)
                .mapToObj(i -> hexRound(hexLerp(a_nudge, b_nudge, step * i), true))
                .collect(Collectors.toList());
        free(a_nudge);
        free(b_nudge);
        return collect;
    }

    public Hex hexRound(final FractionalHex fractionalHex, boolean freeOld) {
        int qi = (int) (Math.round(fractionalHex.q()));
        int ri = (int) (Math.round(fractionalHex.r()));
        int si = (int) (Math.round(fractionalHex.s()));
        final double q_diff = Math.abs(qi - fractionalHex.q());
        final double r_diff = Math.abs(ri - fractionalHex.r());
        final double s_diff = Math.abs(si - fractionalHex.s());
        if (q_diff > r_diff && q_diff > s_diff) {
            qi = -ri - si;
        } else if (r_diff > s_diff) {
            ri = -qi - si;
        } else {
            si = -qi - ri;
        }
        final Hex result = hexManager.obtain().setQ(qi).setR(ri).setS(si);
        if (freeOld) {
            free(fractionalHex);
        }
        return result;
    }

    /**
     * Does not free old FractionalHex.
     */
    public FractionalHex hexLerp(final FractionalHex a, final FractionalHex b, final double t) {
        return obtain()
                .setQ(a.q() * (1.0 - t) + b.q() * t)
                .setR(a.r() * (1.0 - t) + b.r() * t)
                .setS(a.s() * (1.0 - t) + b.s() * t)
                .checkConstructorArguments();
    }
}
