package com.libra.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;


public class ImageUtil {
    /**
     * The tag.
     */
    private static String TAG = "ImageUtil";

    /**
     * 请求相册
     */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;

    /**
     * 请求相机
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;

    /**
     * 请求裁剪
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;

    /**
     * 图片处理：裁剪.
     */
    public static final int CUTIMG = 0;

    /**
     * 图片处理：缩放.
     */
    public static final int SCALEIMG = 1;

    /**
     * 图片处理：不处理.
     */
    public static final int ORIGINALIMG = 2;

    /**
     * 图片缩放比例
     */
    public static int SCALEIMAGESIZE_W = 320;

    public static int SCALEIMAGESIZE_H = 480;

    /**
     * 写图片文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @throws IOException
     */
    public static void saveImage(Context context, String fileName, Bitmap bitmap)
            throws IOException {
        saveImage(context, fileName, bitmap, 100);
    }

    public static void saveImage(Context context, String fileName,
                                 Bitmap bitmap, int quality) {
        if (bitmap == null || fileName == null || context == null) {
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, quality, stream);
            byte[] bytes = stream.toByteArray();
            fos.write(bytes);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 写图片文件到SD卡
     *
     * @param ctx
     * @param filePath 文件路径
     * @param bitmap   图片
     * @param quality  图片质量
     * @throws IOException
     */
    public static void saveImageToSD(Context ctx, String filePath,
                                     Bitmap bitmap, int quality) {
        if (bitmap != null) {
            File file = new File(filePath.substring(0,
                    filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(filePath));
                bitmap.compress(CompressFormat.JPEG, quality, bos);
                bos.flush();
                if (ctx != null) {
                    scanPhoto(ctx, filePath);
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    /**
     * 让Gallery上能马上看到该图片
     *
     * @param ctx
     * @param imgFileName 文件名
     */
    public static void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取/data/data/PACKAGE_NAME/files 目录下bitmap
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = context.openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return Bitmap
     */
    public static Bitmap getBitmapByPath(String filePath) {
        Options opt = new Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        return getBitmapByPath(filePath, opt);
    }

    /**
     * 获取bitmap
     *
     * @param filePath 文件路径
     * @param
     * @return Bitmap
     */
    public static Bitmap getBitmapByPath(String filePath,
                                         Options opts) {
        File file = new File(filePath);
        Bitmap bitmap = getBitmapByFile(file, opts);
        return bitmap;
    }

    /**
     * 获取bitmap
     *
     * @param file
     * @return Bitmap
     */
    public static Bitmap getBitmapByFile(File file) {
        Options opt = new Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        Bitmap bitmap = getBitmapByFile(file, opt);
        return bitmap;
    }

    /**
     * 获取bitmap
     *
     * @param file
     * @return Bitmap
     */
    public static Bitmap getBitmapByFile(File file, Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 使用当前时间戳（yyyy-MM-dd_HH-mm-ss_SS）拼接一个唯一的文件名
     *
     * @return
     */
    public static String getTempFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
        String fileName = format.format(new Timestamp(System
                .currentTimeMillis()));
        return fileName;
    }

    /**
     * 通过uri获取文件的绝对路径
     *
     * @param uri
     * @return
     */
    public static String getFilePathFromContentUri(Activity context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String imagePath = uri.getPath();
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    imagePath = cursor.getString(column_index);
                }
                // 4.0以上的版本会自动关闭 (4.0--14)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    cursor.close();
                }
            } else {
                return null;
            }

        } catch (Exception e) {

        }
        return imagePath;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ", new String[]{filePath},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            // 4.0以上的版本会自动关闭 (4.0--14)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                cursor.close();
            }
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (!imageFile.exists()) {
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                }
            }
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
    }

    /**
     * 获取图片缩略图 只有Android2.1以上版本支持
     *
     * @param
     * @param kind MediaStore.Images.Thumbnails.MICRO_KIND
     * @return
     */
    public static Bitmap loadImgThumbnail(Activity context, String imgPath,
                                          int kind) {
        Bitmap bitmap = null;
        try {
            String[] proj = {MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA};
            Cursor cursor = context.managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj,
                    MediaStore.Images.Media.DATA + "='" + imgPath + "'", null,
                    null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                ContentResolver crThumb = context.getContentResolver();
                Options options = new Options();
                options.inSampleSize = 1;
                bitmap = MediaStore.Images.Thumbnails.getThumbnail(crThumb,
                        cursor.getInt(0), kind, options);
                // 4.0以上的版本会自动关闭 (4.0--14)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
        } catch (OutOfMemoryError e) {
        }
        return bitmap;
    }

    /**
     * 获取指定宽高的图片
     *
     * @param filePath
     * @param w        指定宽度
     * @param h        指定高度
     * @return
     */
    public static Bitmap loadImgThumbnail(String filePath, int w, int h) {
        Bitmap bitmap = getBitmapByPath(filePath);
        return scaleImg(bitmap, w, h);
    }

    /**
     * 获取SD卡中最新图片路径
     *
     * @return
     */
    public static String getLatestImage(Activity context) {
        String latestImage = null;
        String[] items = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, items, null,
                null, MediaStore.Images.Media._ID + " desc");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                latestImage = cursor.getString(1);
                break;
            }
            // 4.0以上的版本会自动关闭 (4.0--14)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                cursor.close();
            }
        }
        return latestImage;
    }

    /**
     * 计算缩放图片的宽高
     *
     * @param img_size
     * @param square_size
     * @return
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size) {
            return img_size;
        }
        double ratio = square_size
                / (double) Math.max(img_size[0], img_size[1]);
        return new int[]{(int) (img_size[0] * ratio),
                (int) (img_size[1] * ratio)};
    }

    /**
     * 创建缩略图
     *
     * @param context
     * @param largeImagePath 原始大图路径
     * @param thumbfilePath  输出缩略图路径
     * @param square_size    输出图片宽度
     * @param quality        输出图片质量
     * @throws IOException
     */
    public static void createImageThumbnail(Context context,
                                            String largeImagePath, String thumbfilePath, int square_size,
                                            int quality) throws IOException {
        Options opts = new Options();
        opts.inSampleSize = 1;
        // 原始图片bitmap
        Bitmap cur_bitmap = getBitmapByPath(largeImagePath, opts);
        if (cur_bitmap == null) {
            return;
        }
        // 原始图片的高宽
        int[] cur_img_size = new int[]{cur_bitmap.getWidth(),
                cur_bitmap.getHeight()};
        // 计算原始图片缩放后的宽高
        int[] new_img_size = scaleImageSize(cur_img_size, square_size);
        // 生成缩放后的bitmap
        Bitmap thb_bitmap = scaleImg(cur_bitmap, new_img_size[0],
                new_img_size[1]);
        // 生成缩放后的图片文件
        saveImageToSD(null, thumbfilePath, thb_bitmap, quality);
    }

    /**
     * 直接获取互联网上的图片.
     *
     * @param imageUrl  要下载文件的网络地址
     * @param type      图片的处理类型（剪切或者缩放到指定大小）
     * @param newWidth  新图片的宽
     * @param newHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getBitmapFormURL(String imageUrl, int type,
                                          int newWidth, int newHeight) {
        Bitmap bm = null;
        URLConnection con = null;
        InputStream is = null;
        try {
            URL url = new URL(imageUrl);
            con = url.openConnection();
            con.setDoInput(true);
            con.connect();
            is = con.getInputStream();
            // 获取资源图片
            Bitmap wholeBm = BitmapFactory.decodeStream(is, null, null);
            if (type == CUTIMG) {
                bm = cutImg(wholeBm, newWidth, newHeight);
            } else if (type == SCALEIMG) {
                bm = scaleImg(wholeBm, newWidth, newHeight);
            } else {
                bm = wholeBm;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    /**
     * 描述：缩放图片.压缩
     *
     * @param file      File对象
     * @param newWidth  新图片的宽
     * @param newHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap scaleImg(File file, int newWidth, int newHeight) {
        Bitmap resizeBmp = null;
        if (newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
        }
        Options opts = new Options();
        // 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), opts);
        // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
        opts.inSampleSize = calculateInSampleSize(opts, newWidth, newHeight);
        // 创建内存
        opts.inJustDecodeBounds = false;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        // 使图片不抖动
        opts.inDither = false;
        resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
        return resizeBmp;
    }

    /**
     * 描述：缩放图片,不压缩的缩放.
     *
     * @param bitmap    the bitmap
     * @param newWidth  新图片的宽
     * @param newHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap scaleImg(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
        }
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }
        // 计算缩放比例
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                true);
        return newBm;
    }

    /**
     * 描述：缩放图片,不压缩的缩放.
     *
     * @param
     * @param newWidth  新图片的宽
     * @param newHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap scaleImgUnCompress(File file, int newWidth,
                                            int newHeight) {
        Bitmap bitmap = getBitmapByFile(file);
        if (bitmap == null) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
        }
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }
        // 计算缩放比例
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                true);
        return newBm;
    }

    /**
     * 描述：根据比例缩放图片.
     *
     * @param bitmap the bitmap
     * @param scale  比例
     * @return Bitmap 新图片
     */
    public static Bitmap scaleImg(Bitmap bitmap, float scale) {
        Bitmap resizeBmp = null;
        try {
            // 获取Bitmap资源的宽和高
            int bmpW = bitmap.getWidth();
            int bmpH = bitmap.getHeight();
            // 注意这个Matirx是android.graphics底下的那个
            Matrix mt = new Matrix();
            mt.postScale(scale, scale);
            resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpW, bmpH, mt, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resizeBmp;
    }

    /**
     * 描述：裁剪图片.
     *
     * @param file      File对象
     * @param newWidth  新图片的宽
     * @param newHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap cutImg(File file, int newWidth, int newHeight) {
        Bitmap newBitmap = null;
        if (newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
        }
        Options opts = new Options();
        // 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), opts);
        // inSampleSize=2表示图片宽高都为原来的二分之一，即图片为原来的四分之一
        // 缩放可以将像素点打薄,裁剪前将图片缩放一些
        int srcWidth = opts.outWidth; // 获取图片的原始宽度
        int srcHeight = opts.outHeight;// 获取图片原始高度
        int destWidth = 0;
        int destHeight = 0;
        int cutSrcWidth = newWidth * 2;
        int cutSrcHeight = newHeight * 2;
        // 缩放的比例
        double ratio = 0.0;
        if (srcWidth < cutSrcWidth || srcHeight < cutSrcHeight) {
            ratio = 0.0;
            destWidth = srcWidth;
            destHeight = srcHeight;
            // 按比例计算缩放后的图片大小
        } else if (srcWidth > srcHeight) {
            ratio = (double) srcWidth / cutSrcWidth;
            destWidth = cutSrcWidth;
            destHeight = (int) (srcHeight / ratio);
        } else {
            ratio = (double) srcHeight / cutSrcHeight;
            destHeight = cutSrcHeight;
            destWidth = (int) (srcWidth / ratio);
        }
        // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
        opts.inSampleSize = (int) ratio + 1;
        // 设置大小
        opts.outHeight = destHeight;
        opts.outWidth = destWidth;
        // 创建内存
        opts.inJustDecodeBounds = false;
        // 使图片不抖动
        opts.inDither = false;
        Bitmap resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
        if (resizeBmp != null) {
            newBitmap = cutImg(resizeBmp, newWidth, newHeight);
        }
        if (newBitmap != null) {
            return newBitmap;
        } else {
            return resizeBmp;
        }
    }

    /**
     * 描述：裁剪图片.
     *
     * @param bitmap    the bitmap
     * @param newWidth  新图片的宽
     * @param newHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap cutImg(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
        }
        Bitmap newBitmap = null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }
        int offsetX = 0;
        int offsetY = 0;
        if (width > newWidth) {
            offsetX = (width - newWidth) / 2;
        } else {
            newWidth = width;
        }
        if (height > newHeight) {
            offsetY = (height - newHeight) / 2;
        } else {
            newHeight = height;
        }
        newBitmap = Bitmap.createBitmap(bitmap, offsetX, offsetY, newWidth,
                newHeight);
        return newBitmap;
    }

    /**
     * Drawable转Bitmap.
     *
     * @param drawable 要转化的Drawable
     * @return Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                                : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Drawable对象转换Bitmap对象.
     *
     * @param bitmap 要转化的Bitmap对象
     * @return Drawable 转化完成的Drawable对象
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        BitmapDrawable mBitmapDrawable = null;
        try {
            if (bitmap == null) {
                return null;
            }
            mBitmapDrawable = new BitmapDrawable(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmapDrawable;
    }

    /**
     * 将Bitmap转换为byte[].
     *
     * @param bitmap          the bitmap
     * @param mCompressFormat 图片格式 Bitmap.CompressFormat.JPEG,CompressFormat.PNG
     * @param needRecycle     是否需要回收
     * @return byte[] 图片的byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap,
                                      CompressFormat mCompressFormat, final boolean needRecycle) {
        byte[] result = null;
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            bitmap.compress(mCompressFormat, 100, output);
            result = output.toByteArray();
            if (needRecycle) {
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获取Bitmap大小.
     *
     * @param bitmap          the bitmap
     * @param mCompressFormat 图片格式 Bitmap.CompressFormat.JPEG,CompressFormat.PNG
     * @return 图片的大小
     */
    public static int getByteCount(Bitmap bitmap,
                                   CompressFormat mCompressFormat) {
        int size = 0;
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            bitmap.compress(mCompressFormat, 100, output);
            byte[] result = output.toByteArray();
            size = result.length;
            bitmap.recycle();
            result = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    /**
     * 描述：将byte[]转换为Bitmap.
     *
     * @param b 图片格式的byte[]数组
     * @return bitmap 得到的Bitmap
     */
    public static Bitmap bytes2Bimap(byte[] b) {
        Bitmap bitmap = null;
        try {
            if (b.length != 0) {
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 将ImageView转换为Bitmap.
     *
     * @param view 要转换为bitmap的View
     * @return byte[] 图片的byte[]
     */
    public static Bitmap imageView2Bitmap(ImageView view) {
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 将View转换为Drawable.需要最上层布局为Linearlayout
     *
     * @param view 要转换为Drawable的View
     * @return BitmapDrawable Drawable
     */
    public static Drawable view2Drawable(View view) {
        BitmapDrawable mBitmapDrawable = null;
        try {
            Bitmap newbmp = view2Bitmap(view);
            if (newbmp != null) {
                mBitmapDrawable = new BitmapDrawable(newbmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmapDrawable;
    }

    /**
     * 将View转换为Bitmap.需要最上层布局为Linearlayout
     *
     * @param view 要转换为bitmap的View
     * @return byte[] 图片的byte[]
     */
    public static Bitmap view2Bitmap(View view) {
        Bitmap bitmap = null;
        try {
            if (view != null) {
                view.setDrawingCacheEnabled(true);
                view.measure(
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                view.layout(0, 0, view.getMeasuredWidth(),
                        view.getMeasuredHeight());
                view.buildDrawingCache();
                bitmap = view.getDrawingCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 将View转换为byte[].
     *
     * @param view           要转换为byte[]的View
     * @param compressFormat the compress format
     * @return byte[] View图片的byte[]
     */
    public static byte[] view2Bytes(View view,
                                    CompressFormat compressFormat) {
        byte[] b = null;
        try {
            Bitmap bitmap = ImageUtil.view2Bitmap(view);
            b = ImageUtil.bitmap2Bytes(bitmap, compressFormat, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 描述：旋转Bitmap为一定的角度.
     *
     * @param bitmap  the bitmap
     * @param degrees the degrees
     * @return the bitmap
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        Bitmap mBitmap = null;
        try {
            Matrix m = new Matrix();
            m.setRotate(degrees % 360);
            mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    /**
     * 描述：旋转Bitmap为一定的角度并四周暗化处理.
     *
     * @param bitmap  the bitmap
     * @param degrees the degrees
     * @return the bitmap
     */
    public static Bitmap rotateBitmapTranslate(Bitmap bitmap, float degrees) {
        Bitmap mBitmap = null;
        int width;
        int height;
        try {
            Matrix matrix = new Matrix();
            if (degrees / 90 % 2 != 0) {
                width = bitmap.getWidth();
                height = bitmap.getHeight();
            } else {
                width = bitmap.getHeight();
                height = bitmap.getWidth();
            }
            int cx = width / 2;
            int cy = height / 2;
            matrix.preTranslate(-cx, -cy);
            matrix.postRotate(degrees);
            matrix.postTranslate(cx, cy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    /**
     * 转换图片转换成圆形.
     *
     * @param bitmap 传入Bitmap对象
     * @return the bitmap
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 转换图片转换成镜面效果的图片.
     *
     * @param bitmap 传入Bitmap对象
     * @return the bitmap
     */
    public static Bitmap toReflectionBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            int reflectionGap = 1;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            // This will not scale but will flip on the Y axis
            Matrix matrix = new Matrix();
            matrix.preScale(1, -1);
            // Create a Bitmap with the flip matrix applied to it.
            // We only want the bottom half of the image
            Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                    width, height / 2, matrix, false);
            // Create a new bitmap with same width but taller to fit
            // reflection
            Bitmap bitmapWithReflection = Bitmap.createBitmap(width, height
                    + height / 2, Config.ARGB_8888);
            // Create a new Canvas with the bitmap that's big enough for
            // the image plus gap plus reflection
            Canvas canvas = new Canvas(bitmapWithReflection);
            // Draw in the original image
            canvas.drawBitmap(bitmap, 0, 0, null);
            // Draw in the gap
            Paint deafaultPaint = new Paint();
            canvas.drawRect(0, height, width, height + reflectionGap,
                    deafaultPaint);
            // Draw in the reflection
            canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
            // Create a shader that is a linear gradient that covers the
            // reflection
            Paint paint = new Paint();
            LinearGradient shader = new LinearGradient(0, bitmap.getHeight(),
                    0, bitmapWithReflection.getHeight() + reflectionGap,
                    0x70ffffff, 0x00ffffff, TileMode.CLAMP);
            // Set the paint to use this shader (linear gradient)
            paint.setShader(shader);
            // Set the Transfer mode to be porter duff and destination in
            paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
            // Draw a rectangle using the paint with our linear gradient
            canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                    + reflectionGap, paint);
            bitmap = bitmapWithReflection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 释放Bitmap对象.
     *
     * @param bitmap 要释放的Bitmap
     */
    public static void releaseBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            try {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            } catch (Exception e) {
            }
            bitmap = null;
        }
    }

    /**
     * 释放Bitmap数组.
     *
     * @param bitmaps 要释放的Bitmap数组
     */
    public static void releaseBitmapArray(Bitmap[] bitmaps) {
        if (bitmaps != null) {
            try {
                for (Bitmap bitmap : bitmaps) {
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取图片类型
     *
     * @param file
     * @return
     */
    public static String getImageType(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            String type = getImageType(in);
            return type;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取图片的类型信息
     *
     * @param in
     * @return
     * @see #getImageType(byte[])
     */
    public static String getImageType(InputStream in) {
        if (in == null) {
            return null;
        }
        try {
            byte[] bytes = new byte[8];
            in.read(bytes);
            return getImageType(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取图片的类型信息
     *
     * @param bytes 2~8 byte at beginning of the image file
     * @return image mimetype or null if the file is not image
     */
    public static String getImageType(byte[] bytes) {
        if (isJPEG(bytes)) {
            return "image/jpeg";
        }
        if (isGIF(bytes)) {
            return "image/gif";
        }
        if (isPNG(bytes)) {
            return "image/png";
        }
        if (isBMP(bytes)) {
            return "application/x-bmp";
        }
        return null;
    }

    private static boolean isJPEG(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return b[0] == (byte) 0xFF && b[1] == (byte) 0xD8;
    }

    private static boolean isGIF(byte[] b) {
        if (b.length < 6) {
            return false;
        }
        return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    private static boolean isPNG(byte[] b) {
        if (b.length < 8) {
            return false;
        }
        return b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
                && b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
                && b[6] == (byte) 26 && b[7] == (byte) 10;
    }

    private static boolean isBMP(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return b[0] == 0x42 && b[1] == 0x4d;
    }

    /**
     * bitmap转file
     */
    public static boolean saveBitmap2file(Bitmap bmp, String filepath) {
        if (bmp == null) {
            return false;
        }
        boolean flag = false;
        CompressFormat format = CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filepath);
            flag = bmp.compress(format, quality, stream);
        } catch (FileNotFoundException e) {
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
            }
        }
        return flag;
    }

    /**
     * 组合指定bitmap对象
     */
    public static Bitmap getCombineBitmaps(List<MyBitmapEntity> mEntityList,
                                           Bitmap... bitmaps) {
        Bitmap newBitmap = Bitmap.createBitmap(100, 100, Config.ARGB_8888);
        for (int i = 0; i < mEntityList.size(); i++) {
            newBitmap = mixtureBitmap(newBitmap, bitmaps[i], new PointF(
                    mEntityList.get(i).x, mEntityList.get(i).y));
        }
        return newBitmap;
    }

    /**
     * 混合两张图片为一张
     */
    public static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
                                       PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(),
                first.getHeight(), Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return newBitmap;
    }

    /**
     * 组合图片实体类
     */
    public static class MyBitmapEntity {
        float x;

        float y;

        float width;

        float height;

        static int devide = 1;

        int index = -1;

        @Override
        public String toString() {
            return "MyBitmap [x=" + x + ", y=" + y + ", width=" + width
                    + ", height=" + height + ", devide=" + devide + ", index="
                    + index + "]";
        }
    }

    /**
     * 压缩图片：宽高缩小一倍，质量压缩到500K以下
     *
     * @param pic
     * @param destPic
     */
    public static void compressPic(File pic, File destPic) {
        String picPath = pic.getAbsolutePath();
        if (pic.length() / 1024 > 300) {// 如果原图大于300k则进行缩放压缩
            Bitmap bitmap = null;
            Options options = new Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inDither = false;
            options.inSampleSize = 2;// 宽高缩小一倍
            try {
                FileInputStream fis = new FileInputStream(picPath);
                bitmap = BitmapFactory.decodeStream(fis, null, options);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            int exifRotation = ImageUtil.readPictureDegree(picPath);
            if (exifRotation != 0) {
                Matrix mtx = new Matrix();
                mtx.postRotate(exifRotation);
                Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), mtx, true);
                if (adjustedBitmap != bitmap) {
                    bitmap.recycle();
                    bitmap = adjustedBitmap;
                }
            }
            // 质量压缩到100K以下
            int quality = 100;
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.JPEG, quality, baos);
                while (baos.toByteArray().length / 1024 > 500) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
                    baos.reset();// 重置baos即清空baos
                    quality -= 10;// 每次都减少10
                    bitmap.compress(CompressFormat.JPEG, quality, baos);
                }
                FileOutputStream stream = new FileOutputStream(destPic);
                baos.writeTo(stream);
                baos.flush();
                baos.close();
                stream.close();
                bitmap.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        } else {// 如果原图小于300k则不进行缩放压缩
            FileUtil.copyFile(picPath, destPic.getAbsolutePath());
        }
    }

    public static int calculateInSampleSize(Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (height > reqHeight && reqHeight != 0) {
                inSampleSize = (int) Math.floor((double) height
                        / (double) reqHeight);
            }
            int tmp = 0;
            if (width > reqWidth && reqWidth != 0) {
                tmp = (int) Math.floor((double) width / (double) reqWidth);
            }
            inSampleSize = Math.max(inSampleSize, tmp);
        }
        int roundedSize;
        if (inSampleSize <= 8) {
            roundedSize = 1;
            while (roundedSize < inSampleSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (inSampleSize + 7) / 8 * 8;
        }
        return roundedSize;
    }
}
