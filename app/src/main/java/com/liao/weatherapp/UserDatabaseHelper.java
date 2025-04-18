package com.liao.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.liao.weatherapp.ui.LoginActivity;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "UserDatabaseHelper"; // 统一的日志标签
    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "数据库助手初始化完成，数据库名称: " + DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_USERNAME + " TEXT,"
                    + KEY_PASSWORD + " TEXT)";
            db.execSQL(CREATE_TABLE);
            Log.d(TAG, "数据库表创建成功: " + TABLE_USERS);
        } catch (Exception e) {
            Log.e(TAG, "创建表时出错: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "数据库升级从版本 " + oldVersion + " 到 " + newVersion);
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            Log.d(TAG, "旧表已删除: " + TABLE_USERS);
            onCreate(db);
        } catch (Exception e) {
            Log.e(TAG, "升级数据库时出错: " + e.getMessage());
        }
    }

    public void addUser(String username, String password) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            Log.d(TAG, "获取可写数据库连接成功");

            ContentValues values = new ContentValues();
            values.put(KEY_USERNAME, username);
            values.put(KEY_PASSWORD, password);

            long result = db.insert(TABLE_USERS, null, values);
            if (result == -1) {
                Log.e(TAG, "插入用户数据失败: " + username);
            } else {
                Log.d(TAG, "用户数据插入成功，ID: " + result + ", 用户名: " + username);
            }
        } catch (Exception e) {
            Log.e(TAG, "添加用户时出错: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
                Log.d(TAG, "数据库连接已关闭");
            }
        }
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            Log.d(TAG, "获取可读数据库连接成功，检查用户名: " + username);

            cursor = db.query(TABLE_USERS,
                    new String[]{KEY_ID},
                    KEY_USERNAME + "=?",
                    new String[]{username},
                    null, null, null);

            boolean exists = cursor.getCount() > 0;
            Log.d(TAG, "用户名检查结果: " + username + " 存在? " + exists);
            return exists;
        } catch (Exception e) {
            Log.e(TAG, "检查用户名时出错: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
                Log.d(TAG, "数据库连接已关闭");
            }
        }
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();


            cursor = db.query(TABLE_USERS,
                    new String[]{KEY_ID},
                    KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?",
                    new String[]{username, password},
                    null, null, null);

            boolean exists = cursor.getCount() > 0;
            Log.d(TAG, "用户验证结果: " + username + " 验证通过? " + exists);
            return exists;
        } catch (Exception e) {
            Log.e(TAG, "验证用户时出错: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
                Log.d(TAG, "数据库连接已关闭");
            }
        }
    }
}