package com.example.quanlikhohang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlikhohang.R;
import com.example.quanlikhohang.database.DatabaseHelper;
import com.example.quanlikhohang.model.Product;

public class AddProductActivity extends AppCompatActivity {

    // 1. Khai báo các View
    EditText edtProductName;
    EditText edtPrice;
    EditText edtQuantity;
    EditText edtDescription;

    Spinner spCategory;

    Switch swActive;

    Button btnSaveProduct;

    // 2. Khai báo DatabaseHelper
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kết nối giao diện XML
        setContentView(R.layout.activity_add_product);

        // 3. Ánh xạ View
        edtProductName = findViewById(R.id.edtProductName);

        edtPrice = findViewById(R.id.edtPrice);

        edtQuantity = findViewById(R.id.edtQuantity);

        edtDescription = findViewById(R.id.edtDescription);

        spCategory = findViewById(R.id.spCategory);

        swActive = findViewById(R.id.swActive);

        btnSaveProduct = findViewById(R.id.btnSaveProduct);

        // 4. Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // 5. Thiết lập Spinner
        setupSpinner();

        // 6. Bắt sự kiện nút Lưu
        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    // Thiết lập danh sách loại sản phẩm
    private void setupSpinner() {

        String[] categories = {
                "Điện tử",
                "Thực phẩm",
                "Văn phòng phẩm",
                "Gia dụng",
                "Khác"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spCategory.setAdapter(adapter);
    }

    // Xử lý lưu sản phẩm
    private void saveProduct() {

        // 1. Lấy dữ liệu từ EditText
        String name = edtProductName.getText()
                .toString().trim();

        String priceText = edtPrice.getText()
                .toString().trim();

        String quantityText = edtQuantity.getText()
                .toString().trim();

        String description = edtDescription.getText()
                .toString().trim();

        // 2. Kiểm tra tên sản phẩm
        if (name.isEmpty()) {

            edtProductName.setError(
                    "Vui lòng nhập tên sản phẩm"
            );

            edtProductName.requestFocus();

            return;
        }

        // 3. Kiểm tra giá bán không được trống
        if (priceText.isEmpty()) {

            edtPrice.setError(
                    "Vui lòng nhập giá bán"
            );

            edtPrice.requestFocus();

            return;
        }

        // 4. Kiểm tra số lượng không được trống
        if (quantityText.isEmpty()) {

            edtQuantity.setError(
                    "Vui lòng nhập số lượng"
            );

            edtQuantity.requestFocus();

            return;
        }

        // 5. Chuyển giá và số lượng sang kiểu int
        int price;
        int quantity;

        try {

            price = Integer.parseInt(priceText);

            quantity = Integer.parseInt(quantityText);

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Giá bán hoặc số lượng không hợp lệ",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // 6. Kiểm tra giá bán phải lớn hơn 0
        if (price <= 0) {

            edtPrice.setError(
                    "Giá bán phải lớn hơn 0"
            );

            edtPrice.requestFocus();

            return;
        }

        // 7. Kiểm tra số lượng không được âm
        if (quantity < 0) {

            edtQuantity.setError(
                    "Số lượng không được âm"
            );

            edtQuantity.requestFocus();

            return;
        }

        // 8. Lấy loại sản phẩm từ Spinner
        String category =
                spCategory.getSelectedItem().toString();

        // 9. Lấy trạng thái kinh doanh
        int active;

        if (swActive.isChecked()) {
            active = 1;
        } else {
            active = 0;
        }

        // 10. Tạo đối tượng Product
        Product product = new Product(
                0,
                name,
                price,
                quantity,
                category,
                active,
                description
        );

        // 11. Lưu sản phẩm vào SQLite
        long result = databaseHelper.insertProduct(product);

        if (result != -1) {

            // Thêm thành công
            Toast.makeText(
                    this,
                    "Thêm sản phẩm thành công",
                    Toast.LENGTH_SHORT
            ).show();

            // Đóng màn hình hiện tại
            finish();

        } else {

            // Thêm thất bại
            Toast.makeText(
                    this,
                    "Không thể thêm sản phẩm",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
