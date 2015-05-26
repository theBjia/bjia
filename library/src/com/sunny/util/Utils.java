/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunny.util;

import android.os.Build;

/**
 * Class containing some static utility methods.
 */
public class Utils {

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= 8;// Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= 9;// Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;// Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= 12;// Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= 16;// Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeamMR1() {
        return Build.VERSION.SDK_INT >= 17;// android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeamMR2() {
        return false;
        // return Build.VERSION.SDK_INT >=
        // android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKITKAT() {
        return false;
        // return Build.VERSION.SDK_INT >=
        // android.os.Build.VERSION_CODES.KITKAT;
    }
}
