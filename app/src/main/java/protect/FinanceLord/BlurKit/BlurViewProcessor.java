package protect.FinanceLord.BlurKit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import protect.FinanceLord.R;

import static androidx.core.graphics.drawable.IconCompat.getResources;

public class BlurViewProcessor {
    Bitmap mBlurredBitmap = null;
    Context context;
    RenderScript rs;

    public BlurViewProcessor(Context context){
        this.context = context;
    }

    public void setBackgroundOnView(View view, Bitmap bitmap) {
        Drawable d;
        if (bitmap != null) {
            d = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
            ((RoundedBitmapDrawable) d).setCornerRadius(context.getResources().getDimensionPixelOffset(R.dimen.rounded_corner));

        } else {
            d = ContextCompat.getDrawable(context, R.drawable.white_background);
        }
        view.setBackground(d);
    }


    public Bitmap loadBitmap(View backgroundView, View targetView) {
        Rect backgroundBounds = new Rect();
        backgroundView.getHitRect(backgroundBounds);
        if (!targetView.getLocalVisibleRect(backgroundBounds)) {
            // NONE of the imageView is within the visible window
            return null;
        }

        Bitmap blurredBitmap = captureView(backgroundView);
        //capture only the area covered by our target view
        int[] loc = new int[2];
        int[] bgLoc = new int[2];
        backgroundView.getLocationInWindow(bgLoc);
        targetView.getLocationInWindow(loc);
        int height = targetView.getHeight();
        int y = loc[1];
        if (bgLoc[1] >= loc[1]) {
            //view is going off the screen at the top
            height -= (bgLoc[1] - loc[1]);
            if (y < 0)
                y = 0;
        }
        if (y + height > blurredBitmap.getHeight()) {
            height = blurredBitmap.getHeight() - y;
            Log.d("TAG", "Height = " + height);
            if (height <= 0) {
                //below the screen
                return null;
            }
        }
        Matrix matrix = new Matrix();
        //half the size of the cropped bitmap
        //to increase performance, it will also
        //increase the blur effect.
        matrix.setScale(0.5f, 0.5f);
        Bitmap bitmap = Bitmap.createBitmap(blurredBitmap,
                (int) targetView.getX(),
                y,
                targetView.getMeasuredWidth(),
                height,
                matrix,
                true);

        return bitmap;
        //If handling rounded corners yourself.
        //Create rounded corners on the Bitmap
        //keep in mind that our bitmap is half
        //the size of the original view, setting
        //it as the background will stretch it out
        //so you will need to use a smaller value
        //for the rounded corners than you would normally
        //to achieve the correct look.
        //ImageHelper.roundCorners(
        //bitmap,
        //getResources().getDimensionPixelOffset(R.dimen.rounded_corner),
        //false);
    }

    public Bitmap captureView(View view) {
        if (mBlurredBitmap != null) {
            return mBlurredBitmap;
        }
        //Find the view we are after
        //Create a Bitmap with the same dimensions
        mBlurredBitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444); //reduce quality and remove opacity
        //Draw the view inside the Bitmap
        Canvas canvas = new Canvas(mBlurredBitmap);
        view.draw(canvas);

        //blur it
        blurBitmapWithRenderscript(rs, mBlurredBitmap);

        //Make it frosty
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ColorFilter filter = new LightingColorFilter(0xFFFFFFFF, 0x00222222); // lighten
        //ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        paint.setColorFilter(filter);
        canvas.drawBitmap(mBlurredBitmap, 0, 0, paint);

        return mBlurredBitmap;
    }


    public static void blurBitmapWithRenderscript(RenderScript rs, Bitmap bitmap2) {
        //this will blur the bitmapOriginal with a radius of 25 and save it in bitmapOriginal
        final Allocation input = Allocation.createFromBitmap(rs, bitmap2); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // must be >0 and <= 25
        script.setRadius(25f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap2);
    }

    public static Bitmap roundCorners(Bitmap bitmap,
                                      int cornerRadiusInPixels,
                                      boolean captureCircle) {
        Bitmap output = Bitmap.createBitmap(
                bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        final int color = 0xffffffff;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = cornerRadiusInPixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        if (captureCircle) {
            canvas.drawCircle(rectF.centerX(),
                    rectF.centerY(),
                    bitmap.getWidth() / 2,
                    paint);
        } else {
            canvas.drawRoundRect(rectF,
                    roundPx,
                    roundPx,
                    paint);
        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getRoundedCornerAndLightenBitmap(Bitmap bitmap, int cornerRadiusInPixels, boolean captureCircle) {
        Bitmap output = Bitmap.createBitmap(
                bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        final int color = 0xffffffff;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = cornerRadiusInPixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        if (captureCircle) {
            canvas.drawCircle(rectF.centerX(),
                    rectF.centerY(),
                    bitmap.getWidth() / 2,
                    paint);
        } else {
            canvas.drawRoundRect(rectF,
                    roundPx,
                    roundPx,
                    paint);
        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ColorFilter filter = new LightingColorFilter(0xFFFFFFFF, 0x00222222); // lighten
        //ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        paint.setColorFilter(filter);
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
