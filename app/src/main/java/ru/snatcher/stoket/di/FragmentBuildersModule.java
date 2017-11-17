package ru.snatcher.stoket.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.snatcher.stoket.ui.fragments.basket.BasketFragment;
import ru.snatcher.stoket.ui.fragments.intro.IntroFragment;
import ru.snatcher.stoket.ui.fragments.main.MainFragment;
import ru.snatcher.stoket.ui.fragments.product.ProductInfoFragment;
import ru.snatcher.stoket.ui.fragments.shop.ShopInfoFragment;
import ru.snatcher.stoket.ui.fragments.shops.list.ListFragment;
import ru.snatcher.stoket.ui.fragments.shops.maps.MapsFragment;

@Module
public interface FragmentBuildersModule {
    @ContributesAndroidInjector
    MainFragment contributeMainFragment();

    @ContributesAndroidInjector
    ProductInfoFragment contributeProductInfoFragment();

    @ContributesAndroidInjector
    ShopInfoFragment contributeShopInfoFragment();

    @ContributesAndroidInjector
    MapsFragment contributeMapsFragment();

    @ContributesAndroidInjector
    ListFragment contributeListFragment();

    @ContributesAndroidInjector
    BasketFragment contributeBasketFragment();

    @ContributesAndroidInjector
    IntroFragment contributeIntroFragment();

}