package util;

import android.graphics.Bitmap;

public interface BitmapCallback {
    void onBitmapLoaded(Bitmap bitmap);
    void onFailure(Exception e);
}
