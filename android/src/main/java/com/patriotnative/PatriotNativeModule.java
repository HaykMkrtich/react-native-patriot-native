
package com.patriotnative;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.wear.remote.interactions.RemoteActivityHelper;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.Executors;

public class PatriotNativeModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;

    public PatriotNativeModule(ReactApplicationContext context) {
        super(context);
        this.reactContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return "PatriotNative";
    }

    @ReactMethod
    public void installWatchface(String packageName, Promise promise) {
        new Thread(() -> {
            try {
                Task<List<Node>> nodeListTask = Wearable.getNodeClient(reactContext).getConnectedNodes();
                List<Node> nodes = Tasks.await(nodeListTask);

                if (nodes.isEmpty()) {
                    showToast("Watch not connected");
                    promise.reject("NO_NODES", "No connected WearOS device found.");
                    return;
                }

                for (Node node : nodes) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("market://details?id=" + packageName));

                    RemoteActivityHelper helper = new RemoteActivityHelper(reactContext, Executors.newSingleThreadExecutor());
                    helper.startRemoteActivity(intent, node.getId());
                }

                showToast("Check your watch");
                promise.resolve(null);
            } catch (Exception e) {
                promise.reject("INSTALL_FAILED", e.getMessage());
            }
        }).start();
    }

    private void showToast(String msg) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(reactContext, msg, Toast.LENGTH_SHORT).show());
    }
}
