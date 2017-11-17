package ru.snatcher.stoket.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class AutoClearedValue<T> {
    T value;

    public AutoClearedValue(Fragment fragment, T value) {
        final FragmentManager fragmentManager = fragment.getFragmentManager();
        fragmentManager.registerFragmentLifecycleCallbacks(
                new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public final void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                        AutoClearedValue.this.value = null;
                        fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                    }
                }, false);
        this.value = value;
    }

    public final T get() {
        return value;
    }
}
