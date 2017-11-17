package ru.snatcher.stoket.data.repositories;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import ru.snatcher.stoket.AppExecutors;
import ru.snatcher.stoket.data.api.ApiResponse;
import ru.snatcher.stoket.data.api.MarketService;
import ru.snatcher.stoket.data.database.ProductDao;
import ru.snatcher.stoket.data.database.ShopDao;
import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Resource;
import ru.snatcher.stoket.data.vo.Shop;

public class ShopsRepository {

    final MarketService service;
    final ShopDao shopDao;
    final ProductDao productDao;
    private final AppExecutors appExecutors;

    @Inject
    ShopsRepository(AppExecutors appExecutors, final MarketService service, ShopDao shopDao,
                    ProductDao productDao) {
        this.service = service;
        this.appExecutors = appExecutors;
        this.shopDao = shopDao;
        this.productDao = productDao;
    }

    public final LiveData<Resource<List<Product>>> loadProducts(Shop shop, int limit, long offset) {
        return new NetworkBoundResource<List<Product>, List<Product>>(appExecutors) {

            @Override
            protected void clear() {
            }

            @Override
            protected void saveCallResult(@NonNull List<Product> item) {
                productDao.insertAll(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Product> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Product>> loadFromDb() {
                return productDao.loadAllFromShop(shop.id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Product>>> createCall() {
                return service.getShopProducts(shop.id, limit, offset);
            }
        }.asLiveData();
    }

    public final LiveData<Resource<Shop>> load(long id) {
        return new NetworkBoundResource<Shop, Shop>(appExecutors) {

            @Override
            protected void clear() {
                shopDao.clear(id);
            }

            @Override
            protected void saveCallResult(@NonNull Shop item) {
                shopDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Shop data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<Shop> loadFromDb() {
                return shopDao.load(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Shop>> createCall() {
                return service.getShop(id);
            }
        }.asLiveData();
    }

    public final LiveData<Resource<List<Shop>>> loadAll(boolean isFavourite) {
        return new NetworkBoundResource<List<Shop>, List<Shop>>(appExecutors) {
            @Override
            protected void clear() {
                shopDao.clearAll();
            }

            @Override
            protected void saveCallResult(@NonNull List<Shop> item) {
                shopDao.insertAll(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Shop> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Shop>> loadFromDb() {
                if (isFavourite) {
                    return shopDao.loadFavourite();
                } else {
                    return shopDao.loadAll();
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Shop>>> createCall() {
                return service.getShops();
            }
        }.asLiveData();
    }

    public final void save(Shop shop) {
        shopDao.insert(shop);
    }
}
