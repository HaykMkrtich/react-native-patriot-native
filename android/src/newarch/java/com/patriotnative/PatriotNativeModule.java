package com.patriotnative;

import com.facebook.react.bridge.ReactApplicationContext;

/**
 * NewArch implementation of PatriotNative for React Native 0.77+
 * This class is used when the new architecture (TurboModules) is enabled
 */
public class PatriotNativeModule extends com.patriotnative.NativePatriotNativeSpec {

    public PatriotNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    // The actual implementation is inherited from the base class
    // This file exists to ensure proper architecture separation
}
