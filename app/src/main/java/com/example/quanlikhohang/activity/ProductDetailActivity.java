package com.example.quanlikhohang.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlikhohang.R;
import com.example.quanlikhohang.database.DatabaseHelper;
import com.example.quanlikhohang.model.Product;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    // Các TextView hiển thị thông tin
    TextView tvDetailName;
    TextView tvDetailCategory;
    TextView tvDetailPrice;
    TextView tvDetailQuantity;
    TextView tvDetailValue;
    TextView tvDetailWarning;
    TextView tvDetailDescription;

    // Các Button
    Button btnImport;
    Button btnSell;
    Button btnBack;
    Button btnDelete;

    // Database
    DatabaseHelper databaseHelper;

    // Sản phẩm hiện tại
    Product product;

    // ID sản phẩm
    int productId;

    // Định dạng tiền Việt Nam
    NumberFormat format =
            NumberFormat.getInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kết nối giao diện
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ TextView
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        tvDetailQuantity = findViewById(R.id.tvDetailQuantity);
        tvDetailValue = findViewById(R.id.tvDetailValue);
        tvDetailWarning = findViewById(R.id.tvDetailWarning);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);

        // Ánh xạ Button
        btnImport = findViewById(R.id.btnImport);
        btnSell = findViewById(R.id.btnSell);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        // Khởi tạo database
        databaseHelper = new DatabaseHelper(this);

        // Nhận ID từ Intent
        productId = getIntent().getIntExtra(
                "productId", -1
        );

        // Kiểm tra ID
        if (productId == -1) {

            Toast.makeText(
                    this,
                    "Không tìm thấy mã sản phẩm",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // Tải thông tin sản phẩm
        loadProduct();

        // Sự kiện nhập thêm
        btnImport.setOnClickListener(v -> importProduct());

        // Sự kiện bán hàng
        btnSell.setOnClickListener(v -> sellProduct());

        // Sự kiện quay lại
        btnBack.setOnClickListener(v -> finish());

        // Sự kiện xóa
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void loadProduct() {

        // Truy vấn sản phẩm theo ID
        product = databaseHelper.getProductById(productId);

        // Kiểm tra sản phẩm có tồn tại không
        if (product == null) {

            Toast.makeText(
                    this,
                    "Sản phẩm không tồn tại",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // Hiển thị tên sản phẩm
        tvDetailName.setText(product.getName());

        // Hiển thị loại sản phẩm
        tvDetailCategory.setText(
                "Loại sản phẩm: " + product.getCategory()
        );

        // Hiển thị giá bán
        tvDetailPrice.setText(
                "Giá bán: " + format.format(product.getPrice()) + " đ"
        );

        // Hiển thị số lượng
        tvDetailQuantity.setText(
                "Số lượng tồn: " + product.getQuantity()
        );

        // Tính giá trị tồn kho
        long totalValue =
                (long) product.getPrice() * product.getQuantity();

        // Hiển thị giá trị tồn kho
        tvDetailValue.setText(
                "Giá trị tồn kho: " + format.format(totalValue) + " đ"
        );

        // Hiển thị mô tả
        String description = product.getDescription();

        if (description == null || description.trim().isEmpty()) {
            tvDetailDescription.setText("Không có mô tả");
        } else {
            tvDetailDescription.setText(description);
        }

        // Kiểm tra số lượng tồn
        if (product.getQuantity() < 5) {

            tvDetailWarning.setVisibility(TextView.VISIBLE);

            tvDetailWarning.setText("Cảnh báo: Sản phẩm sắp hết hàng!");

        } else {

            tvDetailWarning.setVisibility(TextView.GONE);
        }
    }

    private void importProduct() {

        // Lấy số lượng hiện tại
        int currentQuantity = product.getQuantity();

        // Kiểm tra giới hạn số nguyên
        if (currentQuantity == Integer.MAX_VALUE) {

            Toast.makeText(
                    this,
                    "Số lượng đã đạt giới hạn",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Tăng số lượng thêm 1
        int newQuantity = currentQuantity + 1;

        // Cập nhật vào SQLite
        int result = databaseHelper.updateQuantity(
                productId,
                newQuantity
        );

        if (result > 0) {

            // Tải lại thông tin
            loadProduct();

            Toast.makeText(
                    this,
                    "Nhập thêm hàng thành công",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Không thể cập nhật số lượng",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void sellProduct() {

        // Lấy số lượng hiện tại
        int currentQuantity = product.getQuantity();

        // Kiểm tra hết hàng
        if (currentQuantity == 0) {

            Toast.makeText(
                    this,
                    "Hết hàng",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Giảm số lượng đi 1
        int newQuantity = currentQuantity - 1;

        // Cập nhật vào SQLite
        int result = databaseHelper.updateQuantity(
                productId,
                newQuantity
        );

        if (result > 0) {

            // Tải lại thông tin sản phẩm
            loadProduct();

            Toast.makeText(
                    this,
                    "Bán hàng thành công",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Không thể cập nhật số lượng",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void confirmDelete() {

        new AlertDialog.Builder(this)

                .setTitle("Xác nhận xóa")

                .setMessage(
                        "Bạn có chắc chắn muốn xóa sản phẩm "
                                + product.getName() + " không?"
                )

                .setPositiveButton("Xóa", (dialog, which) -> {

                    // Gọi phương thức xóa trong SQLite
                    int result = databaseHelper.deleteProduct(
                            productId
                    );

                    if (result > 0) {

                        Toast.makeText(
                                this,
                                "Xóa sản phẩm thành công",
                                Toast.LENGTH_SHORT
                        ).show();

                        // Quay về danh sách
                        finish();

                    } else {

                        Toast.makeText(
                                this,
                                "Xóa sản phẩm thất bại",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })

                .setNegativeButton("Hủy", (dialog, which) -> {

                    // Đóng hộp thoại, không xóa
                    dialog.dismiss();

                })

                .show();
    }
}
