package com.libra.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import com.libra.R;
import java.io.File;

/**
 * Created by libra on 16/10/21 下午2:51.
 */

public class URIUtil {

    public static Uri fromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        }
        else {
            return FileProvider.getUriForFile(context,
                    context.getString(R.string.fileProviderAuthorities), file);
        }
    }
}
