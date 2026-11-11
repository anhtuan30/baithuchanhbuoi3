package com.example.quanlikhohang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlikhohang.R;
import com.example.quanlikhohang.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    // Khai báo các thành phần giao diện
    EditText edtUsername, edtPassword;
    TextView tvError;
    Button btnLogin;

    // Khai báo database
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kết nối với giao diện XML
        setContentView(R.layout.activity_login);

        // Ánh xạ View
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvError = findViewById(R.id.tvError);
        btnLogin = findViewById(R.id.btnLogin);

        // Khởi tạo database
        databaseHelper = new DatabaseHelper(this);

        // Bắt sự kiện nhấn nút đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    // Phương thức xử lý đăng nhập
    private void checkLogin() {

        // Lấy dữ liệu người dùng nhập
        String username = edtUsername.getText().toString().trim();

        String password = edtPassword.getText().toString();

        // Ẩn thông báo lỗi cũ
        tvError.setVisibility(View.GONE);

        // Kiểm tra bỏ trống
        if (username.isEmpty()) {
            edtUsername.setError("Vui lòng nhập tên đăng nhập");
            edtUsername.requestFocus();
            return;
        }

        if (password.trim().isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
            return;
        }

        // Kiểm tra tài khoản trong SQLite
        boolean isLogin = databaseHelper.checkLogin(
                username, password
        );

        if (isLogin) {

            // Đăng nhập thành công
            Intent intent = new Intent(
                    LoginActivity.this,
                    MainActivity.class
            );

            startActivity(intent);

            // Không cho quay lại màn hình đăng nhập
            finish();

        } else {

            // Đăng nhập thất bại
            tvError.setText(
                    "Sai tên đăng nhập hoặc mật khẩu"
            );

            tvError.setVisibility(View.VISIBLE);
        }
    }
}
