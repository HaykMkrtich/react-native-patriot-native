/**
 * This is the JavaScript spec for PatriotNative TurboModule
 * This file is required for React Native 0.77+ Codegen support
 */

import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
    installWatchface(packageName: string): Promise<void>;
    getConnectedDevices(): Promise<Object>;
    isAppInstalledOnWatch(packageName: string): Promise<Object>;
    sendMessageToWatch(nodeId: string, path: string, data: string): Promise<void>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('PatriotNative');
