package com.example.frank.jyanfapa.SQLite;

/**
 * Created by Frank on 2018/2/10.
 */


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.frank.jyanfapa.entities.Groups;
import com.example.frank.jyanfapa.entities.Pigeon;


// 資料功能類別
public class GroupsDAO {
    // 表格名稱
    public static final String TABLE_NAME = "groups";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String MEMBER_COLUMN = "member";
    public static final String NAME_COLUMN = "name";


    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME_COLUMN + " TEXT NOT NULL, " +
                    MEMBER_COLUMN+"1" + " INTEGER, " +
                    MEMBER_COLUMN+"2" + " INTEGER, " +
                    MEMBER_COLUMN+"3" + " INTEGER, " +
                    MEMBER_COLUMN+"4" + " INTEGER, " +
                    MEMBER_COLUMN+"5" + " INTEGER, " +
                    MEMBER_COLUMN+"6" + " INTEGER, " +
                    MEMBER_COLUMN+"7" + " INTEGER, " +
                    MEMBER_COLUMN+"8" + " INTEGER, " +
                    MEMBER_COLUMN+"9" + " INTEGER, " +
                    MEMBER_COLUMN+"10" + " INTEGER )";



    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public GroupsDAO(Context context) {
        db = DBHelper.getDatabase(context);
    }


    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public Groups insert(Groups item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(NAME_COLUMN, item.getGroupname());
        Log.d("Sqlite",item.getGroupname());
        int index = 0;
        for(int list : item.getGrouplist())
        {
            Log.d("Sqlite",list+"");
            index++;
            cv.put(MEMBER_COLUMN+index,list);
        }
        for(int i = index+1;i<=10;i++)
        {
            Log.d("Sqlite2",i+1+"");
            Log.d("sqlite3",MEMBER_COLUMN+i);
            cv.put(MEMBER_COLUMN+i,0);
        }



        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        int id = (int)db.insert(TABLE_NAME, null, cv);


        // 設定編號
        item.setGroupid(id);
        // 回傳結果
        return item;
    }

    // 修改參數指定的物件
    public boolean update(Groups item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料

        cv.put(NAME_COLUMN, item.getGroupname());
        int index = 0;
        for(int list : item.getGrouplist())
        {
            index++;
            cv.put(MEMBER_COLUMN+index,list);
        }

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + item.getGroupidd();

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    // 讀取所有記事資料
    public List<Groups> getAll() {
        List<Groups> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public Groups get(int id) {
        // 準備回傳結果用的物件
        Groups item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    // 把Cursor目前的資料包裝為物件
    public Groups getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Groups groups = new Groups();

        groups.setGroupid(cursor.getInt(0));
        groups.setGroupname(cursor.getString(1));

        int index = 2;
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            list.add(cursor.getInt(i+index));
        }
       groups.setGrouplist(list);




        // 回傳結果
        return groups;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    // 建立範例資料
    public void sample() {


        List<Integer> list1 = new ArrayList<>();
        list1.add(13);
        Groups item = new Groups(1,"群組1",list1);

        insert(item);


    }

}