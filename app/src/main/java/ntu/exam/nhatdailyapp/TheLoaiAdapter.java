package ntu.exam.nhatdailyapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TheLoaiAdapter extends RecyclerView.Adapter<TheLoaiAdapter.TheLoaiViewHolder> {
    private static final String TAG = "TheLoaiAdapter";
    Context context;
    ArrayList<TheLoai> theLoaiList;

    private OnTheLoaiClickListener listener; // Interface để xử lý sự kiện click

    // Interface để truyền sự kiện click ra ngoài Fragment/Activity
    public interface OnTheLoaiClickListener {
        void onTheLoaiClick(TheLoai theLoai);
    }

    // Constructor giống ArticleAdapter
    public TheLoaiAdapter(Context context, ArrayList<TheLoai> theLoaiList, OnTheLoaiClickListener listener) {
        this.context = context;
        this.theLoaiList = theLoaiList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TheLoaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho từng item (theloai_item.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.theloai_item, parent, false);
        return new TheLoaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheLoaiViewHolder holder, int position) {
        TheLoai theLoai = theLoaiList.get(position);

        // Ghi log để debug
        Log.d(TAG, "Vị trí: " + position);
        Log.d(TAG, "Tên thể loại: " + theLoai.getName());
        Log.d(TAG, "Resource ID Icon: " + theLoai.getIconResId());
        Log.d(TAG, "URL RSS: " + theLoai.getRssFeedUrl());

        // Hiển thị tên thể loại
        holder.tvTheLoai.setText(theLoai.getName());

        // Hiển thị icon
        holder.imgTheLoai.setImageResource(theLoai.getIconResId());
        Log.d(TAG, "Đã gán icon với ID: " + theLoai.getIconResId());


        // Xử lý sự kiện click trên từng mục
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                Log.d(TAG, "Click vào thể loại: " + theLoai.getName());
                listener.onTheLoaiClick(theLoai);
            }
        });
    }

    @Override
    public int getItemCount() {
        return theLoaiList.size();
    }

    // ViewHolder class - Giữ tham chiếu đến các View trong mỗi theloai_item
    static class TheLoaiViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTheLoai;
        TextView tvTheLoai;

        public TheLoaiViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTheLoai = itemView.findViewById(R.id.imgTheLoai);
            tvTheLoai = itemView.findViewById(R.id.tvTheLoai);
        }
    }
}