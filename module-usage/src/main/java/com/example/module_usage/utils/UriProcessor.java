package com.example.module_usage.utils;

import android.net.Uri;
import android.support.annotation.NonNull;

@SuppressWarnings("WeakerAccess")
public interface UriProcessor {
    Uri process(@NonNull Uri uri);
}
