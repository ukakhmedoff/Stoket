package ru.snatcher.stoket.data.api;

import android.arch.lifecycle.LiveData;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Shop;
import ru.snatcher.stoket.data.vo.Type;

/**
 * REST API access points
 */
public interface MarketService {

    @GET("products")
    LiveData<ApiResponse<List<Product>>> getProducts(@Query("limit") long limit,
                                                     @Query("offset") long offset,
                                                     @Query("brand") String brand,
                                                     @Query("typeName") String type,
                                                     @Query("priceMin") String priceMin,
                                                     @Query("priceMax") String priceMax,
                                                     @Query("gender") int gender);

    @GET("products/{id}")
    LiveData<ApiResponse<Product>> getProduct(@Path("id") long id);

    @GET("products")
    LiveData<ApiResponse<List<Product>>> getShopProducts(@Query("shopId") long shopId,
                                                         @Query("limit") int limit,
                                                         @Query("offset") long offset);

    @GET("shops")
    LiveData<ApiResponse<List<Shop>>> getShops();

    @GET("shops/{id}")
    LiveData<ApiResponse<Shop>> getShop(@Path("id") long id);

    @GET("dictionaries/types")
    LiveData<ApiResponse<List<Type>>> getTypes();

    @GET("dictionaries/brands")
    LiveData<ApiResponse<List<String>>> getBrands();
}