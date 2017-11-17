package ru.snatcher.stoket.ui.activity.main;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import ru.snatcher.stoket.R;
import ru.snatcher.stoket.data.vo.Shop;
import ru.snatcher.stoket.ui.activity.intro.IntroActivity;
import ru.snatcher.stoket.ui.fragments.basket.BasketFragment;
import ru.snatcher.stoket.ui.fragments.main.MainFragment;
import ru.snatcher.stoket.ui.fragments.product.ProductInfoFragment;
import ru.snatcher.stoket.ui.fragments.shop.ShopInfoFragment;
import ru.snatcher.stoket.ui.fragments.shops.ShopsFragment;
import ru.snatcher.stoket.ui.fragments.shops.maps.MapsFragment;
import ru.snatcher.stoket.util.Constants;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;
import ru.terrakok.cicerone.commands.Replace;

public class MainActivity extends AppCompatActivity implements LifecycleRegistryOwner,
        HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener {

    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    private final Navigator navigator =
            new SupportFragmentNavigator(getSupportFragmentManager(), R.id.navigation_container) {
                @Override
                protected Fragment createFragment(String screenKey, Object data) {
                    switch (screenKey) {
                        case Constants.FRAGMENT_MAIN_ID:
                            return new MainFragment();

                        case Constants.FRAGMENT_SHOPS_ID:
                            return new ShopsFragment();

                        case Constants.FRAGMENT_BASKET_ID:
                            return new BasketFragment();

                        case Constants.FRAGMENT_SHOP_ID:
                            return ShopInfoFragment.create((long) data);

                        case Constants.FRAGMENT_PRODUCT_ID:
                            return ProductInfoFragment.create((long) data);

                        case Constants.FRAGMENT_MAPS_ID:
                            return MapsFragment.create(((Shop) data).id);

                        default:
                            throw new RuntimeException("Navigation exception");
                    }
                }

                @Override
                protected void showSystemMessage(String message) {

                }

                @Override
                protected void exit() {
                    finish();
                }

            };

    @Inject
    DispatchingAndroidInjector<Fragment> injector;
    @Inject
    NavigatorHolder navigatorHolder;

    @Inject
    ViewModelProvider.Factory viewModelProviderFactory;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAnalytics.getInstance(this);

        final MainActivityViewModel viewModel = ViewModelProviders
                .of(this, viewModelProviderFactory)
                .get(MainActivityViewModel.class);

        if (!viewModel.appStarted()) {
            startActivity(new Intent(this, IntroActivity.class));
        }
        initView();
    }

    @Override
    public final void onBackPressed() {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigator.applyCommand(new Replace(Constants.FRAGMENT_MAIN_ID, 0));
    }

    @Override
    public final boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                navigator.applyCommand(new Replace(Constants.FRAGMENT_MAIN_ID, 0));
                break;
            case R.id.nav_shops:
                navigator.applyCommand(new Replace(Constants.FRAGMENT_SHOPS_ID, 1));
                break;
            case R.id.nav_basket:
                navigator.applyCommand(new Replace(Constants.FRAGMENT_BASKET_ID, 2));
                break;
        }

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initView() {

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
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

    @Override
    public final AndroidInjector<Fragment> supportFragmentInjector() {
        return injector;
    }
}