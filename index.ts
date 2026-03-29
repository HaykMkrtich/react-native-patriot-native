import { NativeModules, Platform } from 'react-native';

// Import TurboModule spec for RN 0.77+ compatibility
let PatriotNativeModule: any;

try {
  // Try to use TurboModule first (RN 0.77+)
  PatriotNativeModule = require('./src/NativePatriotNative').default;
} catch (e) {
  // Fallback to legacy NativeModules (RN < 0.77)
  PatriotNativeModule = NativeModules.PatriotNative;
}

if (!PatriotNativeModule) {
  throw new Error(
      `PatriotNative module is not properly linked. Please check your installation:
    1. Run 'npx react-native clean' and rebuild
    2. Ensure Android dependencies are properly installed
    3. Check that your React Native version is 0.60+`
  );
}

// Type definitions for better TypeScript support
export interface WatchProperties {
  id: string;
  displayName: string;
  isNearby: boolean;
  type: string;
  platform: string;
  isDisconnected?: boolean;
}

export const installWatchface = (packageName: string): Promise<void> => {
  if (Platform.OS !== 'android') {
    return Promise.reject(new Error('PatriotNative is only supported on Android'));
  }
  return PatriotNativeModule.installWatchface(packageName);
};

export const getConnectedWatchProperties = (): Promise<WatchProperties> => {
  if (Platform.OS !== 'android') {
    return Promise.reject(new Error('PatriotNative is only supported on Android'));
  }
  return PatriotNativeModule.getConnectedWatchProperties();
};
