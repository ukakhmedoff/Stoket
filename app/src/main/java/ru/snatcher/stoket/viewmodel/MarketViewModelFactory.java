package ru.snatcher.stoket.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.util.ArrayMap;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.snatcher.stoket.di.ViewModelSubComponent;
import ru.snatcher.stoket.ui.activity.main.MainActivityViewModel;
import ru.snatcher.stoket.ui.fragments.basket.BasketViewModel;
import ru.snatcher.stoket.ui.fragments.intro.IntroFragmentViewModel;
import ru.snatcher.stoket.ui.fragments.main.MainFragmentViewModel;
import ru.snatcher.stoket.ui.fragments.product.ProductInfoFragmentViewModel;
import ru.snatcher.stoket.ui.fragments.shop.ShopInfoFragmentViewModel;
import ru.snatcher.stoket.ui.fragments.shops.list.ListFragmentViewModel;
import ru.snatcher.stoket.ui.fragments.shops.maps.MapsFragmentViewModel;

@Singleton
public class MarketViewModelFactory implements ViewModelProvider.Factory {
    private final ArrayMap<Class, Callable<? extends ViewModel>> creators;

    @Inject
    public MarketViewModelFactory(ViewModelSubComponent viewModelSubComponent) {
        creators = new ArrayMap<>();
        // we cannot inject view models directly because they won't be bound to the owner's
        // view model scope.
        creators.put(ProductInfoFragmentViewModel.class,
                viewModelSubComponent::productInfoFragmentViewModel);
        creators.put(MainFragmentViewModel.class, viewModelSubComponent::mainFragmentViewModel);
        creators.put(ShopInfoFragmentViewModel.class, viewModelSubComponent::shopInfoFragmentViewModel);
        creators.put(MapsFragmentViewModel.class, viewModelSubComponent::mapsFragmentViewModel);
        creators.put(ListFragmentViewModel.class, viewModelSubComponent::listFragmentViewModel);
        creators.put(BasketViewModel.class, viewModelSubComponent::basketViewModel);
        creators.put(IntroFragmentViewModel.class, viewModelSubComponent::introFragmentViewModel);
        creators.put(MainActivityViewModel.class, viewModelSubComponent::mainActivityViewModel);
    }

    @Override
    public final <T extends ViewModel> T create(Class<T> modelClass) {
        Callable<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class, Callable<? extends ViewModel>> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }
        try {
            return (T) creator.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
