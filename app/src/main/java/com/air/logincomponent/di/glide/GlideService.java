package com.air.logincomponent.di.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.concurrent.ExecutionException;

/**
 * @author air on 2017/3/6.
 *         <p>
 *         图片加载框架
 *         </p>
 *         <p>
 *         尽管及时取消不必要的加载是很好的实践，但这并不是必须的操作。
 *         实际上，当 Glide.with() 中传入的 Activity 或 Fragment 实例销毁时，Glide 会自动取消加载并回收资源。
 *         </p>
 *         <p>
 *         对 url 进行 null 检验并不是必须的，如果 url 为 null，Glide 会清空 View 的内容，或者显示 placeholder Drawable 或 fallback Drawable 的内容。
 *         </p>
 */

public class GlideService {

    public static void loadNormal(Context context, String url, ImageView imageView) {

        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    public static void loadBitmap(Context context, String url) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new Target<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });
    }

    public static Bitmap loadBitmapBySize(Context context, String url, int width, int height) throws ExecutionException, InterruptedException {


        FutureTarget<Bitmap> futureTarget = Glide.with(context)
                .asBitmap()
                .load(url)
                .submit(width, height);

        Bitmap bitmap = futureTarget.get();
        return bitmap;
    }
}
