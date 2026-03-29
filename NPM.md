# React Native Patriot Native

Seamlessly install WearOS watch faces, detect devices, and communicate with connected watches from your React Native mobile app.

## Why React Native Patriot Native?

Installing watch faces on WearOS traditionally requires users to navigate through their watch's Play Store. This module simplifies the process by allowing direct interaction with WearOS devices from your React Native app.

## Quick Start

```bash
npm install @haykmkrtich/react-native-patriot-native
```

```typescript
import {
  installWatchface,
  getConnectedDevices,
  isAppInstalledOnWatch,
  sendMessageToWatch,
} from '@haykmkrtich/react-native-patriot-native';

// Get all connected WearOS devices
const devices = await getConnectedDevices();
// [{ id: "node_123", displayName: "Galaxy Watch 4", isNearby: true, type: "watch", platform: "wearOS" }]

// Install a watch face
await installWatchface('com.example.watchface.package');

// Check if an app is installed on the watch
const status = await isAppInstalledOnWatch('com.example.watchface');
// { isInstalled: true, installedOnNodes: ["node_123"] }

// Send a custom message to a specific watch
await sendMessageToWatch(devices[0].id, '/my-path', 'hello');
```

## API

### `getConnectedDevices()`
Returns all connected WearOS devices with details: `id`, `displayName`, `isNearby`, `type`, `platform`.

### `installWatchface(packageName)`
Installs a watch face on all connected WearOS devices via Google Play.

### `isAppInstalledOnWatch(packageName)`
Checks if a specific app/capability is installed on connected watches. Returns `{ isInstalled, installedOnNodes }`.

### `sendMessageToWatch(nodeId, path, data?)`
Sends a custom message to a specific watch node.

## Requirements

- React Native >= 0.60.0
- Android API level 24+ (Android 7.0+)
- Java 17+
- Paired WearOS device

## Package Structure for WearOS Development

**Important:** Your Google Play Console setup must include:
- A WearOS watch face app
- A mobile companion app (using this module)
- **Both apps must share the same package name**

This ensures proper communication and correct app discovery during installation.

## Usage Examples

### Device Detection
```typescript
const devices = await getConnectedDevices();

if (devices.length === 0) {
  Alert.alert('No watch connected');
  return;
}

devices.forEach(device => {
  console.log(`${device.displayName} (${device.platform}) - ${device.isNearby ? 'Nearby' : 'Away'}`);
});
```

### Watch Face Installation with Error Handling
```typescript
try {
  await installWatchface('com.awesome.watchface');
  // Success — check your watch for the installation prompt
} catch (error) {
  switch (error.code) {
    case 'NO_NODES':
      Alert.alert('Please connect your WearOS device');
      break;
    case 'INSTALL_FAILED':
      Alert.alert('Installation failed', error.message);
      break;
  }
}
```

### Check App Installation
```typescript
const status = await isAppInstalledOnWatch('com.example.watchface');
if (status.isInstalled) {
  console.log(`Found on ${status.installedOnNodes.length} device(s)`);
} else {
  console.log('Not installed on any connected watch');
}
```

### Custom Messaging
```typescript
const devices = await getConnectedDevices();
if (devices.length > 0) {
  await sendMessageToWatch(
    devices[0].id,
    '/sync-settings',
    JSON.stringify({ theme: 'dark' })
  );
}
```

## Common Use Cases

- Watch face marketplace apps
- Companion apps for WearOS watch faces
- Apps that offer multiple watch face styles
- Enterprise apps managing company-wide watch face deployment
- Apps that need to communicate with WearOS devices

## Need Help?

- [Full Documentation](https://github.com/HaykMkrtich/react-native-patriot-native)
- [Report Issues](https://github.com/HaykMkrtich/react-native-patriot-native/issues)
- [Feature Requests](https://github.com/HaykMkrtich/react-native-patriot-native/issues)
