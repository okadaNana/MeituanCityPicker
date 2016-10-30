package com.owen.meituancitypicker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 把 assets 中的数据库文件复制到SD卡中的 data/data/包名/ 下
 */
public class DbTransformer {

    public static SQLiteDatabase go(Context context, String assetsFileName, String dbFileName) {
        String dbFileDirPath = getDbFileDirPath(context);

        File fileDir = new File(dbFileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File dbFile = new File(dbFileDirPath + dbFileName);
        if (!dbFile.exists()) {
            assetsToFile(context, assetsFileName, dbFile);
        }
        return SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    private static void assetsToFile(Context context, String assetsFileName, File targetFile) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getResources().getAssets().open(assetsFileName);
            os = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer, 0, buffer.length)) != -1) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * /data/data/package_name/db_name
     */
    private static String getDbFileDirPath(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(File.separator)
                .append("data")
                .append(Environment.getDataDirectory().getAbsolutePath())
                .append(File.separator)
                .append(context.getPackageName())
                .append(File.separator)
                .append("databases")
                .append(File.separator);
        return sb.toString();
    }

}
