package com.libra.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    /**
     * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     */
    public static void write(Context context, String fileName, String content) {
        if (content == null) {
            content = "";
        }
        try {
            FileOutputStream fos = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取文本文件， 文件在/data/data/PACKAGE_NAME/files 目录下
     */
    public static String read(Context context, String fileName) {
        try {
            FileInputStream in = context.openFileInput(fileName);
            return readInStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private static String readInStream(FileInputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i("FileTest", e.getMessage());
        }
        return null;
    }


    /**
     * 创建文件
     *
     * @param folderPath 文件夹
     * @param fileName 文件名
     */
    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName + fileName);
    }


    /**
     * 读文件
     *
     * @param filePath 文件路径
     * @return if file not exist, return null, else return content of file
     * @throws IOException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }


    /**
     * 写文件
     *
     * @param filePath 文件存放路径
     * @param content 数据
     * @param append 是否追加 is append, if true, write to the end of file, else
     * clear
     * content of file and write into it
     * @return return true
     * @throws IOException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }


    /**
     * 写文件
     *
     * @param filePath 文件存放路径
     * @param stream 数据流
     * @return return true
     * @throws IOException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        OutputStream o = null;
        try {
            o = new FileOutputStream(filePath);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }


    /**
     * 写文件
     *
     * @param buffer 数据
     * @param filePath 文件存放路径
     * @return return true
     * @throws IOException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(byte[] buffer, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(buffer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("IOException occurred. ", e);
            }
        }
    }


    /**
     * 写文件
     *
     * @param buffer 数据
     * @param folder 文件夹
     * @param fileName 文件名
     */
    public static boolean writeFile(byte[] buffer, String folder, String fileName) {
        boolean writeSucc = false;
        boolean sdCardExist = Environment.getExternalStorageState()
                                         .equals(Environment.MEDIA_MOUNTED);
        String folderPath = "";
        if (sdCardExist) {
            folderPath = Environment.getExternalStorageDirectory() +
                    File.separator + folder + File.separator;
        }
        else {
            writeSucc = false;
        }
        File fileDir = new File(folderPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(folderPath + fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(buffer);
            writeSucc = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writeSucc;
    }


    /**
     * 根据文件绝对路径获取文件名
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }


    /**
     * 根据文件的绝对路径获取文件名但不包含扩展名
     */
    public static String getFileNameNoFormat(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int point = filePath.lastIndexOf('.');
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
                point);
    }


    /**
     * 获取文件扩展名
     */
    public static String getFileFormat(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        int point = fileName.lastIndexOf('.');
        if (point == -1) {
            return "";
        }
        else {
            return fileName.substring(point);
        }
    }


    /**
     * 获取图片文件扩展名
     */
    public static String getImageFormat(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        int point = fileName.lastIndexOf('.');
        if (point == -1) {
            return ".png";
        }
        else {
            return fileName.substring(point);
        }
    }


    /**
     * 获取文件大小
     */
    public static long getFileSize(String filePath) {
        long size = 0;
        File file = new File(filePath);
        if (file != null && file.exists()) {
            size = file.length();
        }
        return size;
    }


    /**
     * 获取文件大小
     *
     * @param size 字节
     */
    public static String getFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
        float temp = (float) size / 1024;
        if (temp >= 1024) {
            return df.format(temp / 1024) + "M";
        }
        else {
            return df.format(temp) + "K";
        }
    }


    /**
     * 格式化单位
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        //        if (kiloByte < 1) {
        //            return size + "B";
        //        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                          .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                          .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                          .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() +
                "TB";
    }


    /**
     * 获取目录文件大小
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            }
            else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }


    /**
     * 获取目录文件个数
     */
    public long getFileList(File dir) {
        long count = 0;
        File[] files = dir.listFiles();
        count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                count = count + getFileList(file);// 递归
                count--;
            }
        }
        return count;
    }


    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            out.write(ch);
        }
        byte buffer[] = out.toByteArray();
        out.close();
        return buffer;
    }


    /**
     * 检查文件是否存在
     */
    public static boolean checkFileExists(String name) {
        boolean status;
        if (!name.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + name);
            status = newPath.exists();
        }
        else {
            status = false;
        }
        return status;
    }


    /**
     * 检查路径是否存在
     */
    public static boolean checkFilePathExists(String path) {
        return new File(path).exists();
    }


    /**
     * 计算SD卡的剩余空间
     *
     * @return 返回-1，说明没有安装sd卡
     */
    public static long getFreeDiskSpace() {
        String status = Environment.getExternalStorageState();
        long freeSpace = 0;
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                freeSpace = availableBlocks * blockSize / 1024;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            return -1;
        }
        return freeSpace;
    }


    /**
     * 新建目录
     */
    public static boolean createDirectory(String directoryName) {
        boolean status;
        if (!directoryName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + directoryName);
            status = newPath.mkdir();
            status = true;
        }
        else {
            status = false;
        }
        return status;
    }


    /**
     * 检查是否安装SD卡
     */
    public static boolean checkSaveLocationExists() {
        String sDCardStatus = Environment.getExternalStorageState();
        boolean status;
        status = sDCardStatus.equals(Environment.MEDIA_MOUNTED);
        return status;
    }


    /**
     * 获取文件夹大小
     *
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                }
                else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    /**
     * 删除目录(包括：目录里的所有文件)
     */
    public static boolean deleteDirectory(String filePath) {
        boolean status;
        SecurityManager checker = new SecurityManager();
        if (!filePath.equals("")) {
            File newPath = new File(filePath);
            checker.checkDelete(newPath.toString());
            if (newPath.isDirectory()) {
                String[] listfile = newPath.list();
                // delete all files within the specified directory and then
                // delete the directory
                try {
                    for (int i = 0; i < listfile.length; i++) {
                        File deletedFile = new File(newPath.toString() + "/" +
                                listfile[i].toString());
                        deletedFile.delete();
                    }
                    newPath.delete();
                    Log.i("deleteDirectory", filePath);
                    status = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    status = false;
                }
            }
            else {
                status = false;
            }
        }
        else {
            status = false;
        }
        return status;
    }


    /**
     * 删除文件
     */
    public static boolean deleteFile(String fileName) {
        boolean status;
        SecurityManager checker = new SecurityManager();
        if (!fileName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            checker.checkDelete(newPath.toString());
            if (newPath.isFile()) {
                try {
                    Log.i("deleteFile", fileName);
                    newPath.delete();
                    status = true;
                } catch (SecurityException se) {
                    se.printStackTrace();
                    status = false;
                }
            }
            else {
                status = false;
            }
        }
        else {
            status = false;
        }
        return status;
    }


    /**
     * 删除空目录
     *
     * @return 返回 0代表成功 ,1 代表没有删除权限, 2代表不是空目录,3 代表未知错误
     */
    public static int deleteBlankPath(String path) {
        File f = new File(path);
        if (!f.canWrite()) {
            return 1;
        }
        if (f.list() != null && f.list().length > 0) {
            return 2;
        }
        if (f.delete()) {
            return 0;
        }
        return 3;
    }


    /**
     * 重命名
     */
    public static boolean reNamePath(String oldName, String newName) {
        File f = new File(oldName);
        return f.renameTo(new File(newName));
    }


    /**
     * 删除文件
     */
    public static boolean deleteFileWithPath(String filePath) {
        SecurityManager checker = new SecurityManager();
        File f = new File(filePath);
        checker.checkDelete(filePath);
        if (f.isFile()) {
            Log.i("deleteFile", filePath);
            f.delete();
            return true;
        }
        return false;
    }


    /**
     * 获取SD卡的根目录，末尾带\
     */
    public static String getSDRoot() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator;
    }


    /**
     * 列出root目录下所有子目录
     *
     * @return 绝对路径
     */
    public static List<String> listPath(String root) {
        List<String> allDir = new ArrayList<String>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory()) {
                    allDir.add(f.getAbsolutePath());
                }
            }
        }
        return allDir;
    }


    public enum PathStatus {
        SUCCESS, EXITS, ERROR
    }


    /**
     * 创建目录
     *
     * @return PathStatus
     */
    public static PathStatus createPath(String newPath) {
        File path = new File(newPath);
        if (path.exists()) {
            return PathStatus.EXITS;
        }
        if (path.mkdir()) {
            return PathStatus.SUCCESS;
        }
        else {
            return PathStatus.ERROR;
        }
    }


    /**
     * 截取路径名
     *
     * @return String
     */
    public static String getPathName(String absolutePath) {
        int start = absolutePath.lastIndexOf(File.separator) + 1;
        int end = absolutePath.length();
        return absolutePath.substring(start, end);
    }


    public static void copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                inStream = new FileInputStream(oldPath);
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    /**
     * 列出目录下所有的指定后缀名文件
     *
     * @return String
     */
    public static File[] listFiles(String parentPath, final String postfix) {
        File parent = new File(parentPath);
        FileFilter mFileFilter = new FileFilter() {
            @Override public boolean accept(File file) {
                // TODO Auto-generated method stub
                return file.getName().endsWith(postfix);
            }
        };
        if (parent.exists() && parent.isDirectory()) {
            return parent.listFiles(mFileFilter);
        }
        return null;
    }
}