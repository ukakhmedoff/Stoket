package ru.snatcher.stoket.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.snatcher.stoket.data.vo.Shop;

/**
 * Interface for database access for Shop related operations.
 */
@Dao
public interface ShopDao {
    @Query("SELECT * FROM shops")
    LiveData<List<Shop>> loadAll();

    @Query("SELECT * FROM shops WHERE favourite = 1")
    LiveData<List<Shop>> loadFavourite();

    @Query("SELECT * FROM shops WHERE id = :shopId")
    LiveData<Shop> load(long shopId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Shop> shops);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Shop shop);

    @Query("DELETE FROM shops WHERE favourite = 0")
    void clearAll();

    @Query("DELETE FROM products WHERE isBasket = 0 AND id = :id")
    void clear(long id);
}
