package ru.snatcher.stoket.di;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.snatcher.stoket.data.api.MarketService;
import ru.snatcher.stoket.data.database.MarketDatabase;
import ru.snatcher.stoket.data.database.ProductDao;
import ru.snatcher.stoket.data.database.ShopDao;
import ru.snatcher.stoket.util.LiveDataCallAdapterFactory;
import ru.snatcher.stoket.viewmodel.MarketViewModelFactory;

@Module(subcomponents = ViewModelSubComponent.class)
class AppModule {

    @Singleton
    @Provides
    MarketDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, MarketDatabase.class, "market")
                .allowMainThreadQueries()
                .build();
    }

    @Singleton
    @Provides
    ProductDao provideProductDao(MarketDatabase db) {
        return db.productDao();
    }

    @Singleton
    @Provides
    ShopDao provideShopDao(MarketDatabase db) {
        return db.shopDao();
    }

    @Singleton
    @Provides
    MarketService provideMarketService() {
        return new Retrofit.Builder()
                .baseUrl("http://stoket.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(MarketService.class);
    }

    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(ViewModelSubComponent.Builder builder) {
        return new MarketViewModelFactory(builder.build());
    }
}