package ntu.exam.nhatdailyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ArticleDetail_Activity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    ImageButton ibtnBack,ibtnSave;
    ConstraintLayout topBar;

    private Article currentArticle; // Thêm biến để giữ đối tượng Article hiện tại
    private boolean isArticleSaved = false; // Trạng thái lưu của bài báo

    void TimDieuKhien(){
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        ibtnBack = findViewById(R.id.ibtnBack);
        ibtnSave = findViewById(R.id.ibtnSave);
        topBar = findViewById(R.id.topBar);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TimDieuKhien();

        // 1. Nhận đối tượng Article từ Intent
        currentArticle = (Article) getIntent().getSerializableExtra("article"); // Nhận đối tượng Article
        if (currentArticle != null) {
            Log.d("ArticleDetail", "Bài báo được nhận: " + currentArticle.getTitle() + " - " + currentArticle.getLink());
            webView.loadUrl(currentArticle.getLink()); // Tải link của bài báo
        } else {
            Log.e("ArticleDetail", "Không nhận được đối tượng Article.");
            // Fallback nếu không nhận được Article, vẫn thử tải link nếu có
            String link = getIntent().getStringExtra("link");
            if (link != null) {
                webView.loadUrl(link);
            } else {
                Toast.makeText(this, "Không có dữ liệu bài báo để hiển thị.", Toast.LENGTH_LONG).show();
                finish(); // Đóng Activity nếu không có gì để hiển thị
            }
        }

        // 2. Cập nhật trạng thái nút Save ban đầu
        updateSaveButtonState();

        // 3. Xử lý sự kiện click cho nút Save
        ibtnSave.setOnClickListener(v -> {
            if (currentArticle != null) {
                if (isArticleSaved) {
                    // Bài báo đã được lưu, bây giờ là bỏ lưu
                    SavedArticle.removeArticle(this, currentArticle);
                    Toast.makeText(this, "Đã xóa bài báo khỏi danh sách đã lưu", Toast.LENGTH_SHORT).show();
                } else {
                    // Bài báo chưa được lưu, bây giờ là lưu
                    SavedArticle.saveArticle(this, currentArticle);
                    Toast.makeText(this, "Đã lưu bài báo", Toast.LENGTH_SHORT).show();
                }
                updateSaveButtonState(); // Cập nhật lại icon sau khi thay đổi trạng thái
            } else {
                Toast.makeText(this, "Không thể lưu/bỏ lưu bài báo (dữ liệu rỗng).", Toast.LENGTH_SHORT).show();
            }
        });

        ibtnBack.setOnClickListener(v -> finish());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        String link = getIntent().getStringExtra("link");
        Log.d("ArticleLink", "Link received: " + link);
        if (link != null) {
            webView.loadUrl(link);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        // Cuộn xuống: ẩn thanh điều hướng
                        topBar.animate().translationY(-topBar.getHeight()).setDuration(200).start();
                    } else if (scrollY < oldScrollY) {
                        // Cuộn lên: hiện lại
                        topBar.animate().translationY(0).setDuration(200).start();
                    }
                }
            });
        }

    }
    // Phương thức để cập nhật icon của nút Save
    private void updateSaveButtonState() {
        if (currentArticle != null) {
            isArticleSaved = SavedArticle.isSaved(this, currentArticle);
            if (isArticleSaved) {
                // Đã lưu: hiển thị icon "đã lưu"
                ibtnSave.setImageResource(R.drawable.ic_daluu);
            } else {
                // Chưa lưu: hiển thị icon "chưa lưu"
                ibtnSave.setImageResource(R.drawable.savedx64); // Sử dụng icon save ban đầu
            }
        }
    }
}