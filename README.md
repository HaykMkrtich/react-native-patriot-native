# React Native Patriot Native

A React Native module that enables seamless installation of WearOS watch faces directly from your React Native mobile application.

## Important Note for WearOS Development

This module is specifically designed for WearOS watch face companion apps. When developing a complete wearable product, it's crucial to follow the Google Play Console best practices:

1. Create two applications with **identical package names**:
   - One for the mobile companion app (using this React Native module)
   - One for the WearOS watch face itself

This package name consistency is required for proper functionality and distribution through the Google Play Store.

## Features

- Install watch faces on paired WearOS devices
- Handle connection status with WearOS devices
- Promise-based API for easy integration
- Native Toast notifications for user feedback

## Installation

```bash
npm install @haykmkrtich/react-native-patriot-native
# or
yarn add @haykmkrtich/react-native-patriot-native
```

## Requirements

- React Native >= 0.60.0
- Android API level 21+ (Android 5.0 or later)
- Paired WearOS device

## Usage

```typescript
import { installWatchface } from '@haykmkrtich/react-native-patriot-native';

// Example: Installing a watch face
try {
  await installWatchface('com.example.watchface.package');
  // Watch face installation initiated on the connected WearOS device
} catch (error) {
  // Handle errors (e.g., no connected device, installation failed)
  console.error(error);
}
```

## API Reference

### `installWatchface(packageName: string): Promise<void>`

Initiates the installation of a watch face on the connected WearOS device.

#### Parameters

- `packageName` (string): The package name of the watch face to install from the Google Play Store.

#### Returns

- Promise that resolves when the installation request is successfully sent to the watch.
- Promise rejection if:
  - No WearOS device is connected
  - Installation process fails

#### Error Codes

- `NO_NODES`: No connected WearOS device found
- `INSTALL_FAILED`: Installation process failed

## How It Works

The module uses the WearOS Remote API to:
1. Check for connected WearOS devices
2. Send an installation request to the connected watch
3. Open the Google Play Store page for the specified watch face package

The user will receive a prompt on their WearOS device to install the watch face.

## Requirements for Android

Add the following to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.google.android.gms:play-services-wearable:18.1.0'
    implementation 'androidx.wear:wear-remote-interactions:1.0.0'
}
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

MIT License - see the [LICENSE](LICENSE) file for details.

## Author

Hayk Mkrtich

## Support

For issues and feature requests, please [create an issue](https://github.com/HaykMkrtich/react-native-patriot-native/issues) on GitHub.
