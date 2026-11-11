package com.example.quanlikhohang.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlikhohang.R;
import com.example.quanlikhohang.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class ProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Product> list;

    public ProductAdapter(Context context,
                          ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {

        // Tạo giao diện cho một dòng
        if (convertView == null) {

            convertView = LayoutInflater.from(context)
                    .inflate(
                            R.layout.item_product,
                            parent,
                            false
                    );
        }

        // Ánh xạ các TextView
        TextView tvName =
                convertView.findViewById(R.id.tvProductName);

        TextView tvPrice =
                convertView.findViewById(R.id.tvProductPrice);

        TextView tvQuantity =
                convertView.findViewById(R.id.tvQuantity);

        TextView tvCategory =
                convertView.findViewById(R.id.tvCategory);

        TextView tvWarning =
                convertView.findViewById(R.id.tvWarning);

        // Lấy sản phẩm tại vị trí hiện tại
        Product p = list.get(position);

        // Hiển thị tên
        tvName.setText(p.getName());

        // Định dạng giá tiền
        NumberFormat format =
                NumberFormat.getInstance(new Locale("vi", "VN"));

        tvPrice.setText(
                "Giá bán: " + format.format(p.getPrice()) + " đ"
        );

        // Hiển thị số lượng
        tvQuantity.setText("SL: " + p.getQuantity());

        // Hiển thị loại
        tvCategory.setText(p.getCategory());

        // Kiểm tra sắp hết hàng
        if (p.getQuantity() < 5) {

            tvWarning.setVisibility(View.VISIBLE);

            tvQuantity.setTextColor(Color.RED);

        } else {

            tvWarning.setVisibility(View.GONE);

            tvQuantity.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    // Cập nhật danh sách mới
    public void updateData(ArrayList<Product> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }




}

