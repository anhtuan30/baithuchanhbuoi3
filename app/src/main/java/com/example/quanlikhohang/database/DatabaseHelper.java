package com.example.quanlikhohang.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import com.example.quanlikhohang.model.Product;
import java.util.ArrayList;

import android.content.ContentValues;
import com.example.quanlikhohang.model.Product;




public class DatabaseHelper extends SQLiteOpenHelper {

    // Thông tin cơ sở dữ liệu
    private static final String DATABASE_NAME = "warehouse.db";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Được gọi khi tạo database lần đầu
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tạo bảng users
        String createUsers = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";

        db.execSQL(createUsers);

        // Tạo bảng products
        String createProducts = "CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "price INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "category TEXT NOT NULL, " +
                "active INTEGER NOT NULL, " +
                "description TEXT)";

        db.execSQL(createProducts);

        // Thêm tài khoản mẫu
        db.execSQL(
                "INSERT INTO users(username, password) " +
                        "VALUES('admin', '123456')"
        );

        // Thêm sản phẩm mẫu
        db.execSQL("INSERT INTO products " +
                "(name, price, quantity, category, active, description) " +
                "VALUES('Chuột không dây', 150000, 20, 'Điện tử', 1, '')");

        db.execSQL("INSERT INTO products " +
                "(name, price, quantity, category, active, description) " +
                "VALUES('Bàn phím cơ', 450000, 3, 'Điện tử', 1, 'Bàn phím cơ')");

        db.execSQL("INSERT INTO products " +
                "(name, price, quantity, category, active, description) " +
                "VALUES('Sổ tay A5', 25000, 40, 'Văn phòng phẩm', 1, '')");
    }

    // Gọi khi nâng cấp phiên bản database
    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        // Chưa cần xử lý trong phiên bản 1
    }

    // Kiểm tra đăng nhập
    public boolean checkLogin(String username, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM users WHERE username = ? AND password = ?",
                new String[]{username, password}
        );

        boolean result = cursor.moveToFirst();

        cursor.close();

        return result;
    }

    public ArrayList<Product> getAllProducts() {

        // Mở database để đọc
        SQLiteDatabase db = this.getReadableDatabase();

        // Tạo danh sách chứa kết quả truy vấn
        ArrayList<Product> list = new ArrayList<>();

        // Truy vấn toàn bộ sản phẩm
        Cursor cursor = db.rawQuery(
                "SELECT * FROM products",
                null
        );

        // Đọc từng dòng dữ liệu
        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name")
                );

                int price = cursor.getInt(
                        cursor.getColumnIndexOrThrow("price")
                );

                int quantity = cursor.getInt(
                        cursor.getColumnIndexOrThrow("quantity")
                );

                String category = cursor.getString(
                        cursor.getColumnIndexOrThrow("category")
                );

                int active = cursor.getInt(
                        cursor.getColumnIndexOrThrow("active")
                );

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow("description")
                );

                // Tạo đối tượng Product
                Product p = new Product(
                        id, name, price, quantity,
                        category, active, description
                );

                // Thêm vào danh sách
                list.add(p);

            } while (cursor.moveToNext());
        }

        // Đóng Cursor
        cursor.close();

        return list;
    }


    public long getTotalValue() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(price * quantity) FROM products",
                null
        );

        long total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getLong(0);
        }

        cursor.close();

        return total;
    }

    public int countLowStock() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM products WHERE quantity < ?",
                new String[]{"5"}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();

        return count;
    }


    public int countProducts() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM products",
                null
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();

        return count;
    }

    public long insertProduct(Product p) {

        // Mở database để ghi dữ liệu
        SQLiteDatabase db = this.getWritableDatabase();

        // Tạo đối tượng ContentValues
        ContentValues values = new ContentValues();

        // Đưa dữ liệu vào ContentValues
        values.put("name", p.getName());
        values.put("price", p.getPrice());
        values.put("quantity", p.getQuantity());
        values.put("category", p.getCategory());
        values.put("active", p.getActive());
        values.put("description", p.getDescription());

        // Thêm dữ liệu vào bảng products
        long result = db.insert(
                "products",
                null,
                values
        );

        // Trả về ID sản phẩm vừa thêm
        return result;
    }


    public Product getProductById(int id) {

        // Mở database để đọc
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn sản phẩm theo ID
        Cursor cursor = db.rawQuery(
                "SELECT * FROM products WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        Product product = null;

        // Kiểm tra có dữ liệu hay không
        if (cursor.moveToFirst()) {

            int productId = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow("name")
            );

            int price = cursor.getInt(
                    cursor.getColumnIndexOrThrow("price")
            );

            int quantity = cursor.getInt(
                    cursor.getColumnIndexOrThrow("quantity")
            );

            String category = cursor.getString(
                    cursor.getColumnIndexOrThrow("category")
            );

            int active = cursor.getInt(
                    cursor.getColumnIndexOrThrow("active")
            );

            String description = cursor.getString(
                    cursor.getColumnIndexOrThrow("description")
            );

            // Tạo đối tượng Product
            product = new Product(
                    productId,
                    name,
                    price,
                    quantity,
                    category,
                    active,
                    description
            );
        }

        // Đóng Cursor
        cursor.close();

        return product;
    }

    public int updateQuantity(int id, int quantity) {

        // Mở database để ghi
        SQLiteDatabase db = this.getWritableDatabase();

        // Chuẩn bị dữ liệu cần cập nhật
        ContentValues values = new ContentValues();

        values.put("quantity", quantity);

        // Cập nhật sản phẩm theo ID
        int result = db.update(
                "products",
                values,
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        return result;
    }

    public int deleteProduct(int id) {

        // Mở database để ghi
        SQLiteDatabase db = this.getWritableDatabase();

        // Xóa sản phẩm theo ID
        int result = db.delete(
                "products",
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        return result;
    }

}

