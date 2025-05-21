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

import java.text.ParseException; // Import này
import java.text.SimpleDateFormat; // Import này
import java.util.ArrayList;
import java.util.Date; // Import này
import java.util.Locale; // Import này
import java.util.concurrent.TimeUnit; // Import này
import java.util.Calendar; // Import này để kiểm tra hôm qua/hôm nay

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

        // Hiển thị tiêu đề
        holder.titleText.setText(article.getTitle());

        // --- Bắt đầu xử lý định dạng ngày tháng ---
        String pubDateString = article.getPubDate();
        // Định dạng đầu vào của pubDate từ RSS: EEE, dd MMM yyyy HH:mm:ss Z
        SimpleDateFormat parser = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        try {
            Date pubDate = parser.parse(pubDateString);
            String formattedDate;

            long diffMillis = System.currentTimeMillis() - pubDate.getTime();

            // Kiểm tra nếu là "vừa xong"
            if (diffMillis < TimeUnit.MINUTES.toMillis(1)) {
                formattedDate = "Vừa xong";
            }
            // Kiểm tra nếu là "X phút trước"
            else if (diffMillis < TimeUnit.HOURS.toMillis(1)) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis);
                formattedDate = minutes + " phút trước";
            }
            // Kiểm tra nếu là "X giờ trước"
            else if (diffMillis < TimeUnit.DAYS.toMillis(1)) {
                long hours = TimeUnit.MILLISECONDS.toHours(diffMillis);
                formattedDate = hours + " giờ trước";
            }
            // Kiểm tra nếu là "Hôm qua"
            else if (isYesterday(pubDate)) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                formattedDate = "Hôm qua, " + timeFormat.format(pubDate);
            }
            // Mặc định hiển thị ngày cụ thể (vd: 21/05/2025)
            else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                formattedDate = dateFormat.format(pubDate);
            }
            holder.dateText.setText(formattedDate);

        } catch (ParseException e) {
            Log.e(TAG, "Lỗi phân tích cú pháp ngày: " + pubDateString, e);
            // Fallback: Nếu không thể phân tích, hiển thị chuỗi gốc
            holder.dateText.setText(pubDateString);
        }
        // --- Kết thúc xử lý định dạng ngày tháng ---

        // Xử lý hiển thị hình ảnh
        String imageUrl = article.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.d(TAG, "Đang tải hình ảnh từ: " + imageUrl);

            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(context.getApplicationContext())
                    .load(imageUrl)
                    .apply(options)
                    .into(holder.imageView);
        } else {
            Log.d(TAG, "Không có URL hình ảnh, sử dụng placeholder");
            holder.imageView.setImageResource(R.drawable.placeholder);
        }

        // CLick bài báo thì intent qua detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArticleDetail_Activity.class);
            intent.putExtra("link", article.getLink());
            context.startActivity(intent);
        });
    }

    // Helper method để kiểm tra xem một ngày có phải là "hôm qua" không
    private boolean isYesterday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1); // Trừ đi 1 ngày

        // Đặt cả hai ngày về 00:00:00 để so sánh chỉ ngày
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        yesterday.set(Calendar.MILLISECOND, 0);

        Calendar articleDate = Calendar.getInstance();
        articleDate.setTime(date);
        articleDate.set(Calendar.HOUR_OF_DAY, 0);
        articleDate.set(Calendar.MINUTE, 0);
        articleDate.set(Calendar.SECOND, 0);
        articleDate.set(Calendar.MILLISECOND, 0);

        return articleDate.equals(yesterday);
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