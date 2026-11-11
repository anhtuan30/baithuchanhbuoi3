
package com.example.quanlikhohang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlikhohang.R;
import com.example.quanlikhohang.database.DatabaseHelper;
import com.example.quanlikhohang.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Khai báo View
    TextView tvTotalProducts;
    TextView tvTotalValue;
    TextView tvLowStock;

    ListView lvProducts;
    Button btnAddProduct;

    // Khai báo database
    DatabaseHelper databaseHelper;

    // Danh sách sản phẩm
    ArrayList<Product> productList;

    // Adapter
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kết nối giao diện
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvLowStock = findViewById(R.id.tvLowStock);

        lvProducts = findViewById(R.id.lvProducts);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        // Khởi tạo database
        databaseHelper = new DatabaseHelper(this);

        // Tải dữ liệu lần đầu
        loadData();

        // Sự kiện nhấn vào sản phẩm
        lvProducts.setOnItemClickListener(
                (parent, view, position, id) -> {

                    Product p = productList.get(position);

                    Intent intent = new Intent(
                            MainActivity.this,
                            ProductDetailActivity.class
                    );

                    // Truyền ID sản phẩm
                    intent.putExtra("productId", p.getId());

                    startActivity(intent);
                }
        );

        // Sự kiện nút thêm sản phẩm
        btnAddProduct.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddProductActivity.class
            );

            startActivity(intent);
        });
    }

    // Tải dữ liệu từ SQLite
    private void loadData() {

        // Lấy toàn bộ sản phẩm
        productList = databaseHelper.getAllProducts();

        // Khởi tạo Adapter
        if (adapter == null) {

            adapter = new ProductAdapter(
                    this,
                    productList
            );

            lvProducts.setAdapter(adapter);

        } else {

            adapter.updateData(productList);
        }

        // Cập nhật thống kê
        updateStatistics();
    }

    // Cập nhật thống kê
    private void updateStatistics() {

        // Tổng sản phẩm
        int totalProducts = databaseHelper.countProducts();

        // Tổng giá trị kho
        long totalValue = databaseHelper.getTotalValue();

        // Số sản phẩm sắp hết
        int lowStock = databaseHelper.countLowStock();

        // Hiển thị số liệu
        tvTotalProducts.setText(
                String.valueOf(totalProducts)
        );

        NumberFormat format =
                NumberFormat.getInstance(new Locale("vi", "VN"));

        tvTotalValue.setText(
                format.format(totalValue) + " đ"
        );

        tvLowStock.setText(
                String.valueOf(lowStock)
        );
    }

    // Tải lại dữ liệu khi quay lại màn hình
    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadData();
        }
    }

}
