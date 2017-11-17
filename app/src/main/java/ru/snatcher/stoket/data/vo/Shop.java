package ru.snatcher.stoket.data.vo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "shops")
public class Shop {

    @PrimaryKey
    public long id;
    public String name;
    public String address;
    public String phone;
    public String logo;
    public double latitude;
    public double longitude;
    public boolean favourite;

    public Shop(long id, String name, String address, String phone, String logo, double latitude,
                double longitude, boolean favourite) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.logo = logo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.favourite = favourite;
    }
}
