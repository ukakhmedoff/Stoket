package ru.snatcher.stoket.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.snatcher.stoket.data.vo.Product;

/**
 * Interface for database access for Product related operations.
 */
@Dao
public interface ProductDao {

    @Query("SELECT * FROM products WHERE gender = :gender ORDER BY id DESC")
    LiveData<List<Product>> loadAll(int gender);

    @Query("SELECT * FROM products WHERE shop_id = :id ORDER BY id DESC")
    LiveData<List<Product>> loadAllFromShop(long id);

    @Query("SELECT * FROM products WHERE isBasket = 1 ORDER BY id DESC")
    LiveData<List<Product>> loadFavourite();

    @Query("SELECT * FROM products WHERE id = :id")
    LiveData<Product> load(long id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Product> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Query("DELETE FROM products WHERE isBasket = 0")
    void clearAll();

    @Query("DELETE FROM products WHERE id = :id")
    void clear(long id);
}