package ru.snatcher.stoket.ui.common.pagers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.data.vo.Product;

public class ImageViewPagerAdapter extends PagerAdapter {

    private final Context context;
    private final LayoutInflater inflater;

    private final Product product;

    public ImageViewPagerAdapter(final Context context, Product product) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.product = product;
    }

    @Override
    public final int getCount() {
        if (product != null && product.imageUrls != null) {
            return product.imageUrls.size();
        }
        return 0;
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.image_sliders_item, container, false);
        container.addView(view);

        String url = String.format(context.getResources().getString(R.string.image_url),
                String.valueOf(product.shop.id), String.valueOf(product.id),
                product.imageUrls.get(position));

        Glide.with(context).asBitmap().apply(new RequestOptions()
                .centerInside()
                .format(DecodeFormat.PREFER_ARGB_8888))
                .load(url)
                .into((ImageView) view.findViewById(R.id.imageView));

        return view;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}