package ru.snatcher.stoket.ui.fragments.shop;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import javax.inject.Inject;

import ru.snatcher.stoket.data.repositories.ShopsRepository;
import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Resource;
import ru.snatcher.stoket.data.vo.Shop;

public class ShopInfoFragmentViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<Long> shopId;

    final ShopsRepository repository;

    @Inject
    ShopInfoFragmentViewModel(ShopsRepository repository) {
        shopId = new MutableLiveData<>();
        this.repository = repository;
    }

    final LiveData<Resource<Shop>> getShop(long shopId) {
        this.shopId.setValue(shopId);
        return Transformations.switchMap(this.shopId, repository::load);
    }

    final LiveData<Resource<List<Product>>> loadProducts(Shop shop, int limit, long offset) {
        return repository.loadProducts(shop, limit, offset);
    }

    final void saveShop(Shop shop) {
        repository.save(shop);
    }
}