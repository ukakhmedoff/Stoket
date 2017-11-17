package ru.snatcher.stoket.di;

import javax.inject.Inject;

import dagger.Subcomponent;
import ru.snatcher.stoket.ui.activity.main.MainActivityViewModel;
import ru.snatcher.stoket.ui.fragments.basket.BasketViewModel;
import ru.snatcher.stoket.ui.fragments.intro.IntroFragmentViewModel;
import ru.snatcher.stoket.ui.fragments.main.MainFragmentViewModel;
import ru.snatcher.stoket.ui.fragments.product.ProductInfoFragmentViewModel;
import ru.snatcher.stoket.ui.fragments.shop.ShopInfoFragmentViewModel;
import ru.snatcher.stoket.ui.fragments.shops.list.ListFragmentViewModel;
import ru.snatcher.stoket.ui.fragments.shops.maps.MapsFragmentViewModel;
import ru.snatcher.stoket.viewmodel.MarketViewModelFactory;

/**
 * A sub component to create ViewModels. It is called by the
 * {@link MarketViewModelFactory}. Using this component allows
 * ViewModels to define {@link Inject} constructors.
 */
@Subcomponent
public interface ViewModelSubComponent {
    MainFragmentViewModel mainFragmentViewModel();

    ProductInfoFragmentViewModel productInfoFragmentViewModel();

    ShopInfoFragmentViewModel shopInfoFragmentViewModel();

    MapsFragmentViewModel mapsFragmentViewModel();

    ListFragmentViewModel listFragmentViewModel();

    BasketViewModel basketViewModel();

    IntroFragmentViewModel introFragmentViewModel();

    MainActivityViewModel mainActivityViewModel();

    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }
}