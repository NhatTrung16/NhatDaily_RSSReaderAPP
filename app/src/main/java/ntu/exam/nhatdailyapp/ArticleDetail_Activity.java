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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ArticleDetail_Activity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    ImageButton ibtnBack,ibtnSave;

    ConstraintLayout topBar;

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
}