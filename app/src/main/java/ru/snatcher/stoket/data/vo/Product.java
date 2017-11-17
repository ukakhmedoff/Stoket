package ru.snatcher.stoket.data.vo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import ru.snatcher.stoket.data.database.TypeConverter;

@Entity(tableName = "products")
@TypeConverters(TypeConverter.class)
public class Product {

    @PrimaryKey
    public long id;
    public String brand;
    public String description;
    public String price;
    public List<String> imageUrls;
    @Embedded(prefix = "type_")
    public Type type;
    @Embedded(prefix = "shop_")
    public Shop shop;
    public boolean isBasket;
    public int gender;

    public Product(long id, String brand, String description, String price, List<String> imageUrls,
                   Type type, Shop shop, boolean isBasket, int gender) {
        this.id = id;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.imageUrls = imageUrls;
        this.type = type;
        this.shop = shop;
        this.isBasket = isBasket;
        this.gender = gender;
    }
}