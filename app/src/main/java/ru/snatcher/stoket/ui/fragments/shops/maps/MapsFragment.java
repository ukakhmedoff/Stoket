package ru.snatcher.stoket.ui.fragments.shops.maps;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.data.vo.Shop;
import ru.snatcher.stoket.di.Injectable;
import ru.snatcher.stoket.ui.common.NavigationController;

public class MapsFragment extends LifecycleFragment implements Injectable,
        GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String SHOP_ID_KEY = "shop_id";

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    ViewModelProvider.Factory factory;

    @Inject
    NavigationController controller;
    GoogleMap map;
    private MapsFragmentViewModel viewModel;

    public static MapsFragment create(long id) {
        Bundle args = new Bundle();
        args.putLong(SHOP_ID_KEY, id);

        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.setArguments(args);
        return mapsFragment;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_maps, container, false);

        final SupportMapFragment fragment = SupportMapFragment.newInstance();
        fragment.getMapAsync(this);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, fragment);
        fragmentTransaction.commit();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(MapsFragmentViewModel.class);

        if (getArguments() != null && getArguments().containsKey(SHOP_ID_KEY)) {
            showShopOnMap(getArguments().getLong(SHOP_ID_KEY));
        } else {
            showShopsOnMap();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);
    }

    private void showShopsOnMap() {
        viewModel.getShops().observe(this, shopResource -> {
            if (shopResource != null && map != null && shopResource.data != null) {
                for (final Shop shop : shopResource.data) {
                    showShopOnMap(shop);
                }

                //Наводим камеру на Махачкалу
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(42.964060, 47.498928), 12.0f));
            }
        });
    }

    void showShopOnMap(final long id) {
        viewModel.getShop(id).observe(this, shopResource -> {
            if (shopResource != null && map != null && shopResource.data != null) {
                showShopOnMap(shopResource.data);
            }
        });
    }

    void showShopOnMap(final Shop data) {
        final LatLng latLng = new LatLng(data.latitude, data.longitude);
        final Marker marker = map.addMarker(getMarkerOptions(latLng).title(data.name));
        marker.setTag(data.id);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19.5f));
    }

    private MarkerOptions getMarkerOptions(final LatLng latLng) {
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        return markerOptions;
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        if (marker.getTag() != null) {
            final long tag = (Long) marker.getTag();
            controller.navigateToShopInfo(tag);
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.getTag() != null) {
            marker.showInfoWindow();
            return true;
        }
        return false;
    }
}