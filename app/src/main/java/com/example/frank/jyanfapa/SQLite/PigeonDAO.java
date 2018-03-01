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

import com.example.frank.jyanfapa.entities.Pigeon;


// 資料功能類別
public class PigeonDAO {
    // 表格名稱
    public static final String TABLE_NAME = "pigeon";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String EYE_COLUMN = "eye";
    public static final String FEATHER_COLUMN = "feather";
    public static final String BIRTH_COLUMN = "birth";
    public static final String NAME_COLUMN = "name";
    public static final String PHOTO_COLUMN = "photo";
    public static final String CIRCLE_COLUMN = "circle";
    public static final String SOURCE_COLUMN = "source";
    public static final String TYPE_COLUMN = "type";
    public static final String RATING_COLUMN = "rating";
    public static final String PS_COLUMN = "ps";
    public static final String FATHER_COLUMN = "father";
    public static final String MOTHER_COLUMN = "mother";
    public static final String LIVE_COLUMN = "live";


    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EYE_COLUMN + " TEXT NOT NULL, " +
                    FEATHER_COLUMN + " TEXT NOT NULL, " +
                    BIRTH_COLUMN + " TEXT NOT NULL, " +
                    NAME_COLUMN + " TEXT NOT NULL, " +
                    PHOTO_COLUMN + " TEXT NOT NULL, " +
                    CIRCLE_COLUMN + " TEXT NOT NULL, " +
                    SOURCE_COLUMN + " TEXT NOT NULL, " +
                    TYPE_COLUMN + " TEXT NOT NULL, " +
                    RATING_COLUMN + " INTEGER, " +
                    PS_COLUMN + " TEXT, " +
                    FATHER_COLUMN + " TEXT, " +
                    MOTHER_COLUMN + " INTEGER, " +
                    LIVE_COLUMN + " INTEGER )";



    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public PigeonDAO(Context context) {
        db = DBHelper.getDatabase(context);
    }


    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public Pigeon insert(Pigeon item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(EYE_COLUMN, item.getEye());
        cv.put(FEATHER_COLUMN, item.getFeather());
        cv.put(BIRTH_COLUMN, item.getBirth());
        cv.put(NAME_COLUMN, item.getName());
        cv.put(PHOTO_COLUMN, item.getPhoto());
        cv.put(CIRCLE_COLUMN, item.getCircle());
        cv.put(SOURCE_COLUMN, item.getSource());
        cv.put(TYPE_COLUMN, item.getType());
        cv.put(RATING_COLUMN,item.getRating());
        cv.put(PS_COLUMN,item.getPs());
        cv.put(FATHER_COLUMN,item.getFather());
        cv.put(MOTHER_COLUMN,item.getMother());
        cv.put(LIVE_COLUMN,item.getLive());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        int id = (int)db.insert(TABLE_NAME, null, cv);

        // 設定編號
        item.setId(id);
        // 回傳結果
        return item;
    }

    // 修改參數指定的物件
    public boolean update(Pigeon item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(EYE_COLUMN, item.getEye());
        cv.put(FEATHER_COLUMN, item.getFeather());
        cv.put(BIRTH_COLUMN, item.getBirth());
        cv.put(NAME_COLUMN, item.getName());
        cv.put(PHOTO_COLUMN, item.getPhoto());
        cv.put(CIRCLE_COLUMN, item.getCircle());
        cv.put(SOURCE_COLUMN, item.getSource());
        cv.put(TYPE_COLUMN, item.getType());
        cv.put(RATING_COLUMN,item.getRating());
        cv.put(PS_COLUMN,item.getPs());
        cv.put(FATHER_COLUMN,item.getFather());
        cv.put(MOTHER_COLUMN,item.getMother());
        cv.put(LIVE_COLUMN,item.getLive());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + item.getId();

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
    public List<Pigeon> getAll() {
        List<Pigeon> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public Pigeon get(int id) {
        // 準備回傳結果用的物件
        Pigeon item = null;
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
    public Pigeon getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Pigeon result = new Pigeon();

        result.setId(cursor.getInt(0));
        result.setEye(cursor.getString(1));
        result.setFeather(cursor.getString(2));
        result.setBirth(cursor.getString(3));
        result.setName(cursor.getString(4));
        result.setPhoto(cursor.getString(5));
        result.setCircle(cursor.getString(6));
        result.setSource(cursor.getString(7));
        result.setType(cursor.getString(8));
        result.setRating(cursor.getInt(9));
        result.setPs(cursor.getString(10));
        result.setFather(cursor.getInt(11));
        result.setMother(cursor.getInt(12));
        result.setLive(cursor.getInt(13));

        // 回傳結果
        return result;
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

        //Pigeon item = new Pigeon(0,"藍色","雞翅","9月","焦阿巴","沒有圖","3345678","撿來的","小鴿",1,"",0,0,0,"");
//        Item item2 = new Item(0, new Date().getTime(), Colors.BLUE, "一隻非常可愛的小狗狗!", "她的名字叫「大熱狗」，又叫\n作「奶嘴」，是一隻非常可愛\n的小狗。", "", 25.04719, 121.516981, 0);
//        Item item3 = new Item(0, new Date().getTime(), Colors.GREEN, "一首非常好聽的音樂！", "Hello content", "", 0, 0, 0);
//        Item item4 = new Item(0, new Date().getTime(), Colors.ORANGE, "儲存在資料庫的資料", "Hello content", "", 0, 0, 0);

    //    insert(item);
    }

}