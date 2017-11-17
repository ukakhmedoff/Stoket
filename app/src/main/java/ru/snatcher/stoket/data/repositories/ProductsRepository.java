package ru.snatcher.stoket.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.snatcher.stoket.AppExecutors;
import ru.snatcher.stoket.data.api.ApiResponse;
import ru.snatcher.stoket.data.api.MarketService;
import ru.snatcher.stoket.data.database.ProductDao;
import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Resource;
import ru.snatcher.stoket.data.vo.Type;

public class ProductsRepository {

    final MarketService service;
    final ProductDao dao;
    private final AppExecutors appExecutors;

    @Inject
    ProductsRepository(AppExecutors appExecutors, MarketService service, ProductDao dao) {
        this.service = service;
        this.appExecutors = appExecutors;
        this.dao = dao;
    }

    public LiveData<Resource<List<Product>>> loadProducts(long limit, long offset, String brand,
                                                          String type, String priceMin,
                                                          String priceMax, boolean isBasket,
                                                          boolean isUpdate, int gender) {
        return new NetworkBoundResource<List<Product>, List<Product>>(appExecutors) {

            @Override
            protected void clear() {
                if (isUpdate) {
                    dao.clearAll();
                }
            }

            @Override
            protected void saveCallResult(@NonNull List<Product> item) {
                dao.insertAll(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Product> data) {
                return !isBasket;
            }

            @Override
            @NonNull
            protected LiveData<List<Product>> loadFromDb() {
                if (isBasket) {
                    return dao.loadFavourite();
                } else if (brand.trim().length() > 0 | type.trim().length() > 0 | priceMin.trim().length() > 0 | priceMax.trim().length() > 0) {
                    MutableLiveData<List<Product>> result = new MutableLiveData<>();
                    List<Product> products = new ArrayList<>();
                    products.add(null);
                    result.setValue(products);
                    return result;
                } else {
                    return dao.loadAll(gender);
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Product>>> createCall() {
                return service.getProducts(limit, offset, getText(brand), getText(type),
                        getText(priceMin), getText(priceMax), gender);
            }
        }.asLiveData();
    }

    private String getText(String text) {
        return text.length() > 0 ? text : null;
    }

    public LiveData<Resource<Product>> getProduct(final long id) {
        return new NetworkBoundResource<Product, Product>(appExecutors) {

            @Override
            protected void clear() {
                dao.clear(id);
            }

            @Override
            protected void saveCallResult(@NonNull Product item) {
                dao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Product data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<Product> loadFromDb() {
                return dao.load(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Product>> createCall() {
                return service.getProduct(id);
            }
        }.asLiveData();
    }

    public void saveProduct(Product product) {
        dao.insert(product);
    }

    public LiveData<Resource<List<String>>> getBrands() {
        LiveData<ApiResponse<List<String>>> brandsResponse = service.getBrands();

        MediatorLiveData<Resource<List<String>>> result = new MediatorLiveData<>();
        result.addSource(brandsResponse, brands -> {
            if (brands != null && brands.isSuccessful()) {
                result.setValue(Resource.success(brands.body));
            }
        });
        return result;
    }

    public LiveData<Resource<List<String>>> getTypes() {
        LiveData<ApiResponse<List<Type>>> typesResponse = service.getTypes();

        MediatorLiveData<Resource<List<String>>> result = new MediatorLiveData<>();
        result.addSource(typesResponse, types -> {
            if (types != null && types.isSuccessful()) {
                List<String> names = new ArrayList<>();
                for (Type type : types.body) {
                    names.add(type.name);
                }
                result.setValue(Resource.success(names));

            }
        });
        return result;
    }
}