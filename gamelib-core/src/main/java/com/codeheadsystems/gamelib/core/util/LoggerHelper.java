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

import com.badlogic.gdx.utils.Logger;

/**
 * Static utility to provide logger functions across GDX apps. This standardization allows us to change
 * loggers across all gamelib products in moment. After log4j hell, this is considered a good thing.
 */
public class LoggerHelper {

    /**
     * Just pass in your class and this will allow any debug message or higher. Remember, Application.setLevel()
     * is what changes the displayable logging level. The level here doesn't really matter. It's overkill.
     *
     * @param clazz that is logging.
     * @return a usable logger.
     */
    public static Logger logger(final Class<?> clazz) {
        return new Logger(clazz.getSimpleName(), Logger.DEBUG);
    }

}
