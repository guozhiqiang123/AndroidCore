package com.example.module_usage.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.noties.markwon.SpannableFactoryDef;
import ru.noties.markwon.renderer.ImageSize;
import ru.noties.markwon.renderer.ImageSizeResolver;
import ru.noties.markwon.spans.AsyncDrawable;
import ru.noties.markwon.spans.AsyncDrawableSpan;
import ru.noties.markwon.spans.SpannableTheme;

public class GifAwareSpannableFactory extends SpannableFactoryDef {

    private final GifPlaceholder gifPlaceholder;

    public GifAwareSpannableFactory(@NonNull GifPlaceholder gifPlaceholder) {
        this.gifPlaceholder = gifPlaceholder;
    }

    @Nullable
    @Override
    public Object image(@NonNull SpannableTheme theme, @NonNull String destination, @NonNull AsyncDrawable.Loader loader, @NonNull ImageSizeResolver imageSizeResolver, @Nullable ImageSize imageSize, boolean replacementTextIsLink) {
        return new AsyncDrawableSpan(
                theme,
                new GifAwareAsyncDrawable(
                        gifPlaceholder,
                        destination,
                        loader,
                        imageSizeResolver,
                        imageSize
                ),
                AsyncDrawableSpan.ALIGN_BOTTOM,
                replacementTextIsLink
        );
    }
}
