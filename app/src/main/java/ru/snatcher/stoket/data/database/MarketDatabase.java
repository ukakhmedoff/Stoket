package ru.snatcher.stoket.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Shop;

@Database(entities = {Product.class, Shop.class}, version = 6)
public abstract class MarketDatabase extends RoomDatabase {
    public abstract ProductDao productDao();

    public abstract ShopDao shopDao();
}