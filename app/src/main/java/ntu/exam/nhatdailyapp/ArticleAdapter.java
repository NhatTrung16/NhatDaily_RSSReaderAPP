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

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
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
        Log.d("ArticleAdapter", "Image URL: " + article.getImageUrl());
        holder.titleText.setText(article.getTitle());
        holder.dateText.setText(article.getPubDate());

        String imageUrl = article.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            // Mở bài báo khi click
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(article.getLink()));
            context.startActivity(i);
        });
        Glide.with(context)
                .load(article.getImageUrl())
                .placeholder(R.drawable.placeholder) // tạo sẵn ảnh "placeholder" nếu muốn
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
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

