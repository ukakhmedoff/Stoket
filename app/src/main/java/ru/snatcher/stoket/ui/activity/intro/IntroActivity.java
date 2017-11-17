package ru.snatcher.stoket.ui.activity.intro;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import ru.snatcher.stoket.R;
import ru.snatcher.stoket.ui.fragments.intro.IntroFragment;
import ru.snatcher.stoket.util.Constants;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;
import ru.terrakok.cicerone.commands.Replace;

public class IntroActivity extends AppCompatActivity implements LifecycleRegistryOwner,
        HasSupportFragmentInjector {

    private final LifecycleRegistry registry = new LifecycleRegistry(this);
    @Inject
    DispatchingAndroidInjector<Fragment> injector;

    @Inject
    NavigatorHolder navigatorHolder;
    private Navigator navigator = new SupportFragmentNavigator(getSupportFragmentManager(), R.id.navigation_container) {
        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            return new IntroFragment();
        }

        /**
         * Shows system message.
         *
         * @param message message to show
         */
        @Override
        protected void showSystemMessage(String message) {

        }

        @Override
        protected void exit() {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        if (savedInstanceState == null) {
            navigator.applyCommand(new Replace(Constants.SCREEN + Constants.FRAGMENT_INTRO_ID, 3));
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    public final LifecycleRegistry getLifecycle() {
        return registry;
    }

    /**
     * Returns an {@link AndroidInjector} of {@link Fragment}s.
     */
    @Override
    public final AndroidInjector<Fragment> supportFragmentInjector() {
        return injector;
    }
}
