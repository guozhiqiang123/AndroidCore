package com.example.module_usage.utils;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pl.droidsonroids.gif.GifDrawable;
import ru.noties.markwon.renderer.ImageSize;
import ru.noties.markwon.renderer.ImageSizeResolver;
import ru.noties.markwon.spans.AsyncDrawable;

public class GifAwareAsyncDrawable extends AsyncDrawable {

    public interface OnGifResultListener {
        void onGifResult(@NonNull GifAwareAsyncDrawable drawable);
    }

    private final Drawable gifPlaceholder;
    private OnGifResultListener onGifResultListener;
    private boolean isGif;

    public GifAwareAsyncDrawable(
            @NonNull Drawable gifPlaceholder,
            @NonNull String destination,
            @NonNull Loader loader,
            @Nullable ImageSizeResolver imageSizeResolver,
            @Nullable ImageSize imageSize) {
        super(destination, loader, imageSizeResolver, imageSize);
        this.gifPlaceholder = gifPlaceholder;
    }

    public void onGifResultListener(@Nullable OnGifResultListener onGifResultListener) {
        this.onGifResultListener = onGifResultListener;
    }

    @Override
    public void setResult(@NonNull Drawable result) {
        super.setResult(result);
        isGif = result instanceof GifDrawable;
        if (isGif && onGifResultListener != null) {
            onGifResultListener.onGifResult(this);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        if (isGif) {
            final GifDrawable drawable = (GifDrawable) getResult();
            if (!drawable.isPlaying()) {
                gifPlaceholder.setBounds(drawable.getBounds());
                gifPlaceholder.draw(canvas);
            }
        }
    }
}
