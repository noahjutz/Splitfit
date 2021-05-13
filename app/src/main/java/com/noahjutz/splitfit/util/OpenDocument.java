package com.noahjutz.splitfit.util;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

public class OpenDocument extends ActivityResultContracts.OpenDocument {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, @NonNull String[] input) {
        super.createIntent(context, input);
        return new Intent(Intent.ACTION_OPEN_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("*/*");
    }

}
