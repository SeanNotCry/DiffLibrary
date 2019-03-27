package com.privat.wt.diffbase.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.disk.NoOpDiskTrimmableRegistry;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;


/**
 * Created by Administrator on 2016/7/7.
 */
public class ImagePipelineConfigUtils {

    //分配的可用内存(int) Runtime.getRuntime().maxMemory()
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    //使用的缓存数量
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;

    //小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    private static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 20 * ByteConstants.MB;

    //小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    private static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 60 * ByteConstants.MB;

    //默认图极低磁盘空间缓存的最大值
    private static final int MAX_DISK_CACHE_VERYLOW_SIZE = 20 * ByteConstants.MB;

    //默认图低磁盘空间缓存的最大值
    private static final int MAX_DISK_CACHE_LOW_SIZE = 60 * ByteConstants.MB;

    //默认图磁盘缓存的最大值
    private static final int MAX_DISK_CACHE_SIZE = 100 * ByteConstants.MB;

    //小图所放路径的文件夹名
    public static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = ".ImagePipelineCacheSmall";

    //默认图所放路径的文件夹名
    public static final String IMAGE_PIPELINE_CACHE_DIR = ".ImagePipelineCacheDefault";

    public static ImagePipelineConfig getDefaultImagePipelineConfig(Context context) {

        //内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE,// 内存缓存中总图片的最大大小,以字节为单位。
                10,// 内存缓存中图片的最大数量。
                MAX_MEMORY_CACHE_SIZE,// 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                10,// 内存缓存中准备清除的总图片的最大数量。
                200 * ByteConstants.KB);// 内存缓存中单个图片的最大大小。

        //修改内存图片缓存数量，空间策略（这个方式有点恶心）
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };
        // 就是这段代码，用于清理缓存
        NoOpMemoryTrimmableRegistry memoryTrimmableRegistry = new NoOpMemoryTrimmableRegistry();
        memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                Log.e("kk", +suggestedTrimRatio + "");
                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                        ) {
                    ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
                }
            }
        });
        memoryTrimmableRegistry.unregisterMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                Log.e("kk", +suggestedTrimRatio + "");
                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                        ) {
                    ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
                }
            }
        });
        //小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(new File(FileUtil.DIR_HOME))//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)//文件夹名
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
                .build();

        //默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context).setBaseDirectoryPath(new File(FileUtil.DIR_HOME))//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)//文件夹名
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
                .build();

        //缓存图片配置
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                .setResizeAndRotateEnabledForNetwork(true);


        return configBuilder.build();
    }

    public static Uri getUriForID(int id, Context context) {
        return Uri.parse("res://" + context.getPackageName() + "/" + id);
    }

    public static Uri getUriForFILE(String path) {
        return Uri.parse("file://" + path);
    }

    public static Uri getUriForHttp(String path) {
        return Uri.parse("http://" + path);
    }

    public static Uri getUriForHttps(String path) {
        return Uri.parse("https://" + path);
    }

    /**
     * 加载图片 自动判断是GIF还是非gif
     *
     * @param mImg
     * @param uri
     * @param r_w
     * @param r_h
     */
    public static void setImgForAut(SimpleDraweeView mImg, Uri uri, int r_w, int r_h) {
        if (isGif(uri.getPath())) {
            setImgForGif(mImg, uri);
        } else {
            setImg(mImg, uri, r_w, r_h);
        }
    }

    /**
     *  加载图片，判断地址是否为空
     * @param mImg
     * @param url
     * @param r_w
     * @param r_h
     */
    public static void setImgForWh(SimpleDraweeView mImg, String url, int r_w, int r_h) {
        if(!TextUtils.isEmpty(url)){
            if (isGif(Uri.parse(url).getPath())) {
                setImgForGif(mImg, Uri.parse(url));
            } else {
                setImg(mImg, Uri.parse(url), r_w, r_h);
            }
        }

    }

    /**
     * 缩放图片
     *
     * @param mImg
     * @param uri
     * @param width
     * @param height
     */
    public static void setImg(SimpleDraweeView mImg, Uri uri, int width, int height) {

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(mImg.getController())
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();
        mImg.setController(controller);
    }

    /**
     * 加载GIF
     *
     * @param mImg
     * @param uri
     */
    public static void setImgForGif(SimpleDraweeView mImg, Uri uri) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        mImg.setController(controller);
    }

    /**
     * 是否为gif
     *
     * @param path
     * @return
     */
    public static boolean isGif(String path) {
        String type = ".jpg";
        if (path.lastIndexOf(".") != -1) {
            type = path.substring(path.lastIndexOf("."), path.length());
        }
        if (type.equals(".gif")) {
            return true;
        }
        return false;
    }

    public static boolean isPhotoID(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        } else if (path.startsWith("http")) {
            return false;
        } else {
            return true;
        }
    }

}
