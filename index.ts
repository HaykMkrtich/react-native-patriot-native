
import { NativeModules } from 'react-native';

type PatriotNativeType = {
  installWatchface(packageName: string): Promise<void>;
};

const { PatriotNative } = NativeModules as { PatriotNative: PatriotNativeType };

export function installWatchface(packageName: string): Promise<void> {
  return PatriotNative.installWatchface(packageName);
}
