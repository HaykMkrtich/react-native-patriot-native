# React Native Patriot Native

🎯 Seamlessly install WearOS watch faces from your React Native mobile app!

## Package Structure for WearOS Development

This module is a critical component for WearOS watch face companion apps. When publishing your complete wearable solution:

⚠️ **Important:** Your Google Play Console setup must include:
- A WearOS watch face app
- A mobile companion app (using this module)
- **Both apps must share the same package name**

This package name consistency ensures:
- Proper communication between companion app and watch face
- Correct app discovery and installation flow
- Smooth user experience during watch face installation

## Why React Native Patriot Native?

Installing watch faces on WearOS devices traditionally requires users to navigate through their watch's Play Store. This module simplifies the process by allowing users to trigger watch face installations directly from your React Native mobile app.

## Quick Start

1. Install the package:
```bash
npm install @haykmkrtich/react-native-patriot-native
```

2. Import and use:
```typescript
import { installWatchface } from '@haykmkrtich/react-native-patriot-native';

// Inside your component or function
const installMyWatchface = async () => {
  try {
    await installWatchface('com.your.watchface.package');
    // Success! Check your watch for the installation prompt
  } catch (error) {
    // Handle any errors
    console.error(error);
  }
};
```

## Examples

### Basic Usage
```typescript
import { installWatchface } from '@haykmkrtich/react-native-patriot-native';

// Inside a React component
const WatchfaceInstaller = () => {
  const handleInstall = async () => {
    try {
      await installWatchface('com.awesome.watchface');
      // Installation request sent successfully
    } catch (error) {
      if (error.code === 'NO_NODES') {
        // No WearOS device connected
        Alert.alert('Please connect your WearOS device');
      } else {
        // Other installation errors
        Alert.alert('Installation failed', error.message);
      }
    }
  };

  return (
    <Button 
      title="Install Watchface" 
      onPress={handleInstall} 
    />
  );
};
```

### Error Handling
```typescript
try {
  await installWatchface('com.example.watchface');
} catch (error) {
  switch (error.code) {
    case 'NO_NODES':
      console.log('No WearOS device connected');
      break;
    case 'INSTALL_FAILED':
      console.log('Installation failed:', error.message);
      break;
    default:
      console.log('Unknown error:', error);
  }
}
```

## Common Use Cases

- Watch face marketplace apps
- Companion apps for WearOS watch faces
- Apps that offer multiple watch face styles
- Enterprise apps managing company-wide watch face deployment

## Tips and Best Practices

1. Always check for errors and provide user feedback
2. Ensure the watch face package exists on Google Play
3. Verify WearOS device connection before attempting installation
4. Consider implementing a retry mechanism

## Need Help?

- 📖 [Full Documentation](https://github.com/HaykMkrtich/react-native-patriot-native)
- 🐛 [Report Issues](https://github.com/HaykMkrtich/react-native-patriot-native/issues)
- 💡 [Feature Requests](https://github.com/HaykMkrtich/react-native-patriot-native/issues)
