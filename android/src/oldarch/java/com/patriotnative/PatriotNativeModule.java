package com.patriotnative;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

/**
 * OldArch implementation of PatriotNative for React Native < 0.77
 * This class is used when the legacy architecture is enabled
 */
public class PatriotNativeModule extends ReactContextBaseJavaModule {

    public PatriotNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "PatriotNative";
    }

    // Implementation methods would be copied from main PatriotNativeModule
    // This is handled by the build system
}