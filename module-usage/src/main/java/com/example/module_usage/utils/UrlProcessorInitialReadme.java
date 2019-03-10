package com.example.module_usage.utils;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import ru.noties.markwon.UrlProcessor;
import ru.noties.markwon.UrlProcessorRelativeToAbsolute;

class UrlProcessorInitialReadme implements UrlProcessor {

    private static final String GITHUB_BASE = "https://github.com/noties/Markwon/raw/master/";

    private final UrlProcessorRelativeToAbsolute processor
            = new UrlProcessorRelativeToAbsolute(GITHUB_BASE);

    @NonNull
    @Override
    public String process(@NonNull String destination) {
        String out;
        final Uri uri = Uri.parse(destination);
        if (TextUtils.isEmpty(uri.getScheme())) {
            out = processor.process(destination);
        } else {
            out = destination;
        }
        return out;
    }
}
