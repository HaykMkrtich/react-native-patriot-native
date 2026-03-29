import { Platform } from 'react-native';
import NativePatriotNative from './src/NativePatriotNative';

const PatriotNativeModule = NativePatriotNative;

if (!PatriotNativeModule) {
  throw new Error(
      `PatriotNative module is not properly linked. Please check your installation:
    1. Run 'npx react-native clean' and rebuild
    2. Ensure Android dependencies are properly installed
    3. Check that your React Native version is 0.77+`
  );
}

export interface ConnectedDevice {
  id: string;
  displayName: string;
  isNearby: boolean;
  type: string;
  platform: string;
}

export interface AppInstallStatus {
  isInstalled: boolean;
  installedOnNodes: string[];
}

/**
 * Install a watch face on connected WearOS devices.
 */
export const installWatchface = (packageName: string): Promise<void> => {
  if (Platform.OS !== 'android') {
    return Promise.reject(new Error('PatriotNative is only supported on Android'));
  }
  return PatriotNativeModule.installWatchface(packageName);
};

/**
 * Get all connected WearOS devices with full details (id, name, proximity, type, platform).
 * Returns an empty array if no devices are connected.
 */
export const getConnectedDevices = (): Promise<ConnectedDevice[]> => {
  if (Platform.OS !== 'android') {
    return Promise.reject(new Error('PatriotNative is only supported on Android'));
  }
  return PatriotNativeModule.getConnectedDevices();
};

/**
 * Check if a specific app/capability is installed on connected watches.
 */
export const isAppInstalledOnWatch = (packageName: string): Promise<AppInstallStatus> => {
  if (Platform.OS !== 'android') {
    return Promise.reject(new Error('PatriotNative is only supported on Android'));
  }
  return PatriotNativeModule.isAppInstalledOnWatch(packageName);
};

/**
 * Send a custom message to a specific watch node.
 */
export const sendMessageToWatch = (nodeId: string, path: string, data: string = ''): Promise<void> => {
  if (Platform.OS !== 'android') {
    return Promise.reject(new Error('PatriotNative is only supported on Android'));
  }
  return PatriotNativeModule.sendMessageToWatch(nodeId, path, data);
};

/** @deprecated Use getConnectedDevices() instead. Returns the first device or { isDisconnected: true }. */
export const getConnectedWatchProperties = async (): Promise<ConnectedDevice & { isDisconnected?: boolean }> => {
  const devices = await getConnectedDevices();
  if (devices.length === 0) {
    return { isDisconnected: true } as any;
  }
  return devices[0];
};
