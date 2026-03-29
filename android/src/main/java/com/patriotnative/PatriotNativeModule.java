package com.patriotnative;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeClient;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * NewArch implementation of PatriotNative (TurboModule) for React Native 0.77+
 */
public class PatriotNativeModule extends NativePatriotNativeSpec {
    private static final String NAME = "PatriotNative";
    private static final String PLAY_STORE_APP_URI = "market://details?id=";

    private final ReactApplicationContext reactContext;

    public PatriotNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    private Context getContext() {
        Context context = getCurrentActivity();
        if (context == null) {
            context = getReactApplicationContext();
        }
        return context;
    }

    @Override
    @ReactMethod
    public void installWatchface(String packageName, Promise promise) {
        try {
            Context context = getContext();

            NodeClient nodeClient = Wearable.getNodeClient(context);
            Task<List<Node>> nodesTask = nodeClient.getConnectedNodes();
            List<Node> nodes = Tasks.await(nodesTask);

            if (nodes.isEmpty()) {
                promise.reject("NO_NODES", "No connected WearOS devices found");
                return;
            }

            String appUri = PLAY_STORE_APP_URI + packageName;
            boolean installationSent = false;

            for (Node node : nodes) {
                try {
                    Task<Integer> sendTask = Wearable.getMessageClient(context)
                            .sendMessage(node.getId(), "/install_watchface", appUri.getBytes());
                    Tasks.await(sendTask);
                    installationSent = true;
                } catch (Exception e) {
                    continue;
                }
            }

            if (installationSent) {
                if (getCurrentActivity() != null) {
                    final Context finalContext = context;
                    getCurrentActivity().runOnUiThread(() ->
                            Toast.makeText(finalContext, "Check your watch for installation prompt", Toast.LENGTH_LONG).show()
                    );
                }
                promise.resolve(null);
            } else {
                promise.reject("INSTALL_FAILED", "Failed to send installation request to any connected device");
            }

        } catch (ExecutionException | InterruptedException e) {
            promise.reject("INSTALL_FAILED", "Failed to communicate with WearOS device: " + e.getMessage());
        } catch (Exception e) {
            promise.reject("INSTALL_FAILED", "Unexpected error: " + e.getMessage());
        }
    }

    @Override
    @ReactMethod
    public void getConnectedDevices(Promise promise) {
        try {
            Context context = getContext();

            NodeClient nodeClient = Wearable.getNodeClient(context);
            Task<List<Node>> nodesTask = nodeClient.getConnectedNodes();
            List<Node> nodes = Tasks.await(nodesTask);

            CapabilityClient capabilityClient = Wearable.getCapabilityClient(context);

            WritableArray devicesArray = Arguments.createArray();

            for (Node node : nodes) {
                WritableMap deviceMap = Arguments.createMap();
                deviceMap.putString("id", node.getId());
                deviceMap.putString("displayName", node.getDisplayName());
                deviceMap.putBoolean("isNearby", node.isNearby());
                deviceMap.putString("type", "watch");

                try {
                    Task<CapabilityInfo> capabilityTask = capabilityClient.getCapability(
                            "wear_app_runtime", CapabilityClient.FILTER_REACHABLE);
                    CapabilityInfo capabilityInfo = Tasks.await(capabilityTask);

                    boolean isWearOS = false;
                    for (Node capableNode : capabilityInfo.getNodes()) {
                        if (capableNode.getId().equals(node.getId())) {
                            isWearOS = true;
                            break;
                        }
                    }
                    deviceMap.putString("platform", isWearOS ? "wearOS" : "unknown");
                } catch (Exception e) {
                    deviceMap.putString("platform", "wearOS");
                }

                devicesArray.pushMap(deviceMap);
            }

            promise.resolve(devicesArray);

        } catch (ExecutionException | InterruptedException e) {
            promise.reject("DETECTION_FAILED", "Failed to get connected devices: " + e.getMessage());
        } catch (Exception e) {
            promise.reject("DETECTION_FAILED", "Unexpected error: " + e.getMessage());
        }
    }

    @Override
    @ReactMethod
    public void isAppInstalledOnWatch(String packageName, Promise promise) {
        try {
            Context context = getContext();

            CapabilityClient capabilityClient = Wearable.getCapabilityClient(context);
            Task<CapabilityInfo> capabilityTask = capabilityClient.getCapability(
                    packageName, CapabilityClient.FILTER_ALL);
            CapabilityInfo capabilityInfo = Tasks.await(capabilityTask);

            Set<Node> capableNodes = capabilityInfo.getNodes();

            WritableMap result = Arguments.createMap();
            result.putBoolean("isInstalled", !capableNodes.isEmpty());

            WritableArray nodeIds = Arguments.createArray();
            for (Node node : capableNodes) {
                nodeIds.pushString(node.getId());
            }
            result.putArray("installedOnNodes", nodeIds);

            promise.resolve(result);

        } catch (ExecutionException | InterruptedException e) {
            promise.reject("CHECK_FAILED", "Failed to check app installation: " + e.getMessage());
        } catch (Exception e) {
            promise.reject("CHECK_FAILED", "Unexpected error: " + e.getMessage());
        }
    }

    @Override
    @ReactMethod
    public void sendMessageToWatch(String nodeId, String path, String data, Promise promise) {
        try {
            Context context = getContext();

            Task<Integer> sendTask = Wearable.getMessageClient(context)
                    .sendMessage(nodeId, path, data.getBytes());
            Tasks.await(sendTask);

            promise.resolve(null);

        } catch (ExecutionException | InterruptedException e) {
            promise.reject("MESSAGE_FAILED", "Failed to send message: " + e.getMessage());
        } catch (Exception e) {
            promise.reject("MESSAGE_FAILED", "Unexpected error: " + e.getMessage());
        }
    }
}
