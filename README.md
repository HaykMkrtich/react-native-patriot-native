# React Native Patriot Native

[![npm version](https://badge.fury.io/js/%40haykmkrtich%2Freact-native-patriot-native.svg)](https://badge.fury.io/js/%40haykmkrtich%2Freact-native-patriot-native)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/platform-react--native-lightgrey)](https://reactnative.dev/)

A React Native module that enables seamless interaction with WearOS devices — install watch faces, detect connected devices, check app installations, and send messages directly from your mobile app.

## Why React Native Patriot Native?

Installing watch faces on WearOS traditionally requires users to navigate through their watch's Play Store. This module simplifies the process by allowing direct interaction with WearOS devices from your React Native app — device detection, watch face installation, app status checks, and custom messaging all through a simple Promise-based API.

## Features

| Feature | Description |
|---------|-------------|
| **Watch Face Installation** | Install watch faces directly on paired WearOS devices |
| **Device Detection** | Get detailed info on all connected WearOS devices |
| **Platform Detection** | Identify WearOS vs other wearable platforms |
| **App Install Check** | Verify if a specific app is installed on the watch |
| **Custom Messaging** | Send arbitrary messages to specific watch nodes |
| **New Architecture** | TurboModule support for React Native 0.77+ |
| **16KB Page Size** | Compliant with Google Play's 16KB page size requirements |

## Installation

```bash
npm install @haykmkrtich/react-native-patriot-native
# or
yarn add @haykmkrtich/react-native-patriot-native
```

## Requirements

- React Native >= 0.60.0
- Android API level 24+ (Android 7.0+)
- Java 17+
- Paired WearOS device

## Quick Start

```typescript
import {
  installWatchface,
  getConnectedDevices,
  isAppInstalledOnWatch,
  sendMessageToWatch,
} from '@haykmkrtich/react-native-patriot-native';

// Get all connected devices
const devices = await getConnectedDevices();
console.log(devices);
// [{ id: "node_123", displayName: "Galaxy Watch 4", isNearby: true, type: "watch", platform: "wearOS" }]

// Install a watch face
await installWatchface('com.example.watchface.package');

// Check if an app is installed on the watch
const status = await isAppInstalledOnWatch('com.example.watchface');
// { isInstalled: true, installedOnNodes: ["node_123"] }

// Send a custom message to a specific watch
await sendMessageToWatch(devices[0].id, '/my-path', 'hello');
```

## API Reference

### `getConnectedDevices(): Promise<ConnectedDevice[]>`

Returns all connected WearOS devices with full details. Returns an empty array if no devices are connected.

```typescript
interface ConnectedDevice {
  id: string;           // Unique device identifier
  displayName: string;  // Human-readable device name
  isNearby: boolean;    // Device proximity status
  type: string;         // Device type (e.g., "watch")
  platform: string;     // Platform ("wearOS" | "unknown")
}
```

**Example:**
```typescript
const devices = await getConnectedDevices();

if (devices.length === 0) {
  console.log('No watches connected');
} else {
  devices.forEach(device => {
    console.log(`${device.displayName} (${device.platform}) - ${device.isNearby ? 'Nearby' : 'Away'}`);
  });
}
```

**Error Codes:**
- `DETECTION_FAILED` — Failed to communicate with WearOS API

---

### `installWatchface(packageName: string): Promise<void>`

Initiates watch face installation on all connected WearOS devices.

**Parameters:**
- `packageName` — The Google Play package name of the watch face to install

**Example:**
```typescript
try {
  await installWatchface('com.awesome.watchface');
  // User will see a toast and installation prompt on their watch
} catch (error) {
  if (error.code === 'NO_NODES') {
    console.log('No watch connected');
  }
}
```

**Error Codes:**
- `NO_NODES` — No connected WearOS device found
- `INSTALL_FAILED` — Installation request failed

---

### `isAppInstalledOnWatch(packageName: string): Promise<AppInstallStatus>`

Checks if a specific app or capability is available on connected watches.

```typescript
interface AppInstallStatus {
  isInstalled: boolean;     // Whether the app is found on any watch
  installedOnNodes: string[]; // IDs of watches that have the app
}
```

**Example:**
```typescript
const status = await isAppInstalledOnWatch('com.example.watchface');
if (status.isInstalled) {
  console.log(`Installed on ${status.installedOnNodes.length} device(s)`);
}
```

**Error Codes:**
- `CHECK_FAILED` — Failed to query capabilities

---

### `sendMessageToWatch(nodeId: string, path: string, data?: string): Promise<void>`

Sends a custom message to a specific watch node. Use `getConnectedDevices()` first to get the node ID.

**Parameters:**
- `nodeId` — Target device ID (from `getConnectedDevices()`)
- `path` — Message path (e.g., `/my-action`)
- `data` — Optional string payload (defaults to empty string)

**Example:**
```typescript
const devices = await getConnectedDevices();
if (devices.length > 0) {
  await sendMessageToWatch(devices[0].id, '/sync-settings', JSON.stringify({ theme: 'dark' }));
}
```

**Error Codes:**
- `MESSAGE_FAILED` — Failed to send message

---

### `getConnectedWatchProperties(): Promise<ConnectedDevice>` *(deprecated)*

Returns the first connected device. Use `getConnectedDevices()` instead.

## WearOS Development Best Practices

This module is designed for WearOS watch face companion apps. For Google Play Console compliance:

1. Create **two applications** with identical package names:
   - Mobile companion app (React Native, using this module)
   - WearOS watch face app
2. This package name consistency is required for proper functionality and distribution through Google Play Store.

## How It Works

1. **Device Discovery** — Scan for connected WearOS devices via NodeClient
2. **Remote Installation** — Send installation request to connected watches
3. **Play Store Integration** — Opens the watch face listing on the watch's Play Store
4. **User Confirmation** — User confirms installation on their watch

## Common Use Cases

- Watch face marketplace apps
- Companion apps for WearOS watch faces
- Apps that offer multiple watch face styles
- Enterprise apps managing company-wide watch face deployment
- Apps that need to communicate with WearOS devices

## Changelog

### 1.1.0
- **New:** `getConnectedDevices()` — returns all connected devices with full details (replaces `getConnectedWatchProperties`)
- **New:** `isAppInstalledOnWatch(packageName)` — check if an app is installed on connected watches
- **New:** `sendMessageToWatch(nodeId, path, data)` — send custom messages to specific watch nodes
- **Deprecated:** `getConnectedWatchProperties()` — use `getConnectedDevices()` instead
- **Fixed:** "No matching variant" build error on React Native 0.78+ / AGP 8.11+
  - Added `publishing` block for AGP 8.11+ compatibility
  - Updated `compileSdk`/`targetSdk` to 35
  - Java 17 compatibility
- **Fixed:** Duplicate class errors from broken newarch/oldarch stubs — both now have full implementations

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**Hayk Mkrtich**
- GitHub: [@HaykMkrtich](https://github.com/HaykMkrtich)
- NPM: [@haykmkrtich](https://www.npmjs.com/~haykmkrtich)

## Support

- [Report Issues](https://github.com/HaykMkrtich/react-native-patriot-native/issues)
- [Feature Requests](https://github.com/HaykMkrtich/react-native-patriot-native/issues)
