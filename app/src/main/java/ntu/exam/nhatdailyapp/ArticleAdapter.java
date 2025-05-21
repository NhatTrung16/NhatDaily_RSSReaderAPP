package ntu.exam.nhatdailyapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private static final String TAG = "ArticleAdapter";
    Context context;
    ArrayList<Article> articles;

    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.land_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);

        // Ghi log để debug
        Log.d(TAG, "Vị trí: " + position);
        Log.d(TAG, "Tiêu đề: " + article.getTitle());
        Log.d(TAG, "URL hình ảnh: " + article.getImageUrl());

        // Hiển thị tiêu đề và ngày
        holder.titleText.setText(article.getTitle());
        holder.dateText.setText(article.getPubDate());

        // Xử lý hiển thị hình ảnh
        String imageUrl = article.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.d(TAG, "Đang tải hình ảnh từ: " + imageUrl);

            // Thêm các tùy chọn để cải thiện việc tải hình ảnh
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            // Tải hình ảnh với Glide
            Glide.with(context.getApplicationContext())
                    .load(imageUrl)
                    .apply(options)
                    .into(holder.imageView);
        } else {
            Log.d(TAG, "Không có URL hình ảnh, sử dụng placeholder");
            holder.imageView.setImageResource(R.drawable.placeholder);
        }

        // Thiết lập sự kiện click để mở bài báo
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArticleDetail_Activity.class);
            intent.putExtra("link", article.getLink());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, dateText;
        ImageView imageView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.textViewCation);
            dateText = itemView.findViewById(R.id.textViewCation2);
            imageView = itemView.findViewById(R.id.imageViewLand);
        }
    }
}