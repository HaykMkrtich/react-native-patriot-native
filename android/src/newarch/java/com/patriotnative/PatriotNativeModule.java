package com.patriotnative;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeClient;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

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

    @Override
    @ReactMethod
    public void installWatchface(String packageName, Promise promise) {
        try {
            Context context = getCurrentActivity();
            if (context == null) {
                context = getReactApplicationContext();
            }

            NodeClient nodeClient = Wearable.getNodeClient(context);
            Task<Set<Node>> nodesTask = nodeClient.getConnectedNodes();

            Set<Node> nodes = Tasks.await(nodesTask);

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
    public void getConnectedWatchProperties(Promise promise) {
        try {
            Context context = getCurrentActivity();
            if (context == null) {
                context = getReactApplicationContext();
            }

            NodeClient nodeClient = Wearable.getNodeClient(context);
            Task<Set<Node>> nodesTask = nodeClient.getConnectedNodes();

            Set<Node> nodes = Tasks.await(nodesTask);

            if (nodes.isEmpty()) {
                WritableMap result = new WritableNativeMap();
                result.putBoolean("isDisconnected", true);
                promise.resolve(result);
                return;
            }

            Node firstNode = nodes.iterator().next();

            WritableMap result = new WritableNativeMap();
            result.putString("id", firstNode.getId());
            result.putString("displayName", firstNode.getDisplayName());
            result.putBoolean("isNearby", firstNode.isNearby());
            result.putString("type", "watch");

            CapabilityClient capabilityClient = Wearable.getCapabilityClient(context);
            try {
                Task<CapabilityInfo> capabilityTask = capabilityClient.getCapability("wear_app_runtime", CapabilityClient.FILTER_REACHABLE);
                CapabilityInfo capabilityInfo = Tasks.await(capabilityTask);

                if (capabilityInfo.getNodes().size() > 0) {
                    result.putString("platform", "wearOS");
                } else {
                    result.putString("platform", "unknown");
                }
            } catch (Exception e) {
                result.putString("platform", "wearOS");
            }

            promise.resolve(result);

        } catch (ExecutionException | InterruptedException e) {
            promise.reject("DETECTION_FAILED", "Failed to detect connected devices: " + e.getMessage());
        } catch (Exception e) {
            promise.reject("DETECTION_FAILED", "Unexpected error: " + e.getMessage());
        }
    }
}
