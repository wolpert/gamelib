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

import java.util.function.Supplier;

/**
 * Purpose: This class has the option to hold an object or something that supplies is. Used for generating
 * components that could be held. Its allows for lazy loading.
 */
public class SupplierHolder<T> {

    private final Supplier<T> supplier;
    private T object;
    public SupplierHolder(final T object, final Supplier<T> supplier) {
        this.object = object;
        this.supplier = supplier;
    }

    public static <T> SupplierHolder<T> with(final Supplier<T> supplier) {
        return new SupplierHolder<>(null, supplier);
    }

    public synchronized T get() {
        if (object == null) {
            object = supplier.get();
        }
        return object;
    }

    public synchronized void set(final T t) {
        object = t;
    }
}
