package ru.snatcher.stoket.binding;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import javax.inject.Inject;

import ru.snatcher.stoket.data.vo.Product;
import ru.snatcher.stoket.data.vo.Status;
import ru.snatcher.stoket.ui.common.RetryCallback;
import ru.snatcher.stoket.ui.common.pagers.ImageViewPagerAdapter;

/**
 * Binding adapters that work with a fragment instance.
 */
public class FragmentBindingAdapters {
    private final Fragment fragment;

    @Inject
    FragmentBindingAdapters(Fragment fragment) {
        this.fragment = fragment;
    }

    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, String url) {
        Glide.with(fragment)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions()
                        .encodeFormat(Bitmap.CompressFormat.PNG)
                        .encodeQuality(100)
                        .centerInside()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .diskCacheStrategy(DiskCacheStrategy.DATA))
                .into(imageView);
    }

    @BindingAdapter("imageUrlShopInfo")
    public void bindImageShopInfo(ImageView imageView, String url) {
        Glide.with(fragment).load(url).into(imageView);
    }

    @BindingAdapter("adapter")
    public void bindAdapter(ViewPager viewPager, Product product) {
        viewPager.setAdapter(new ImageViewPagerAdapter(fragment.getContext(), product));
    }

    @BindingAdapter("refresh_listener")
    public void bindRefreshListener(SwipeRefreshLayout view, RetryCallback retryCallback) {
        view.setOnRefreshListener(retryCallback::retry);
    }

    @BindingAdapter("refreshing")
    public void bindRefreshing(SwipeRefreshLayout view, Status status) {
        view.setRefreshing(status == Status.LOADING);
    }
}