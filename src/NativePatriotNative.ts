/**
 * This is the JavaScript spec for PatriotNative TurboModule
 * This file is required for React Native 0.77+ Codegen support
 */

import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface WatchProperties {
    id: string;
    displayName: string;
    isNearby: boolean;
    type: string;
    platform: string;
    isDisconnected?: boolean;
}

export interface Spec extends TurboModule {
    installWatchface(packageName: string): Promise<void>;
    getConnectedWatchProperties(): Promise<WatchProperties>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('PatriotNative');
