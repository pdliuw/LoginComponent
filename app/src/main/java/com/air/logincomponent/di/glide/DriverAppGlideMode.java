package com.air.logincomponent.di.glide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.module.AppGlideModule;

/**
 * @author air on 2017/3/6.
 * <p>
 * Glide的配置
 * </p>
 * <p>
 * 1、Memory、Disk cache.
 * 2、Glide throwable catch.
 * </p>
 * <p>
 * Document: https://muyangmin.github.io/glide-docs-cn/doc/transformations.html
 * </p>
 */
@GlideModule
public final class DriverAppGlideMode extends AppGlideModule {
    /**
     * 磁盘缓存的文件夹名称
     */
    private static final String DISK_CACHE_DIR = "Scmsafe_Driver_cacheFolderName";

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);

        //Memory cache.
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));

        //Bitmap pool
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setBitmapPoolScreens(3)
                .build();
        builder.setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()));

        //Disk cache.
        int diskCacheSizeBytes = 1024 * 1024 * 1024;//100MB

        String diskCacheName = DISK_CACHE_DIR;

        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheName, diskCacheSizeBytes));

        //Throwable
        final GlideExecutor.UncaughtThrowableStrategy uncaughtThrowableStrategy = GlideExecutor.UncaughtThrowableStrategy.DEFAULT;

        builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor());
        builder.setResizeExecutor(GlideExecutor.newSourceExecutor(uncaughtThrowableStrategy));

        builder.setLogLevel(Log.DEBUG);
    }

    public static String getDiskCacheDir() {
        return DISK_CACHE_DIR;
    }
}
