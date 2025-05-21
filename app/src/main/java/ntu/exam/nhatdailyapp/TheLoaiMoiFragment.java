package ntu.exam.nhatdailyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView; // Bạn đã import TextView nhưng không dùng trong code này
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TheLoaiMoiFragment extends Fragment {
    private static final String TAG = "TheLoaiMoiFragment";

    private String rssFeedUrl;
    private String fragmentTitle;

    RecyclerView recyclerViewArticles;
    // ArticleAdapter articleAdapter; // Không cần khởi tạo ở đây nếu ReadRSS tự thiết lập
    ArrayList<Article> articles;
    ImageButton ibtnBack;

    // Factory method để tạo instance của Fragment và truyền dữ liệu
    public static TheLoaiMoiFragment newInstance(String rssFeedUrl, String title) {
        TheLoaiMoiFragment fragment = new TheLoaiMoiFragment();
        Bundle args = new Bundle();
        args.putString("rss_feed_url", rssFeedUrl);
        args.putString("fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rssFeedUrl = getArguments().getString("rss_feed_url");
            fragmentTitle = getArguments().getString("fragment_title");
            Log.d(TAG, "Đã nhận RSS URL: " + rssFeedUrl + ", Tiêu đề: " + fragmentTitle);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_the_loai_moi, container, false);

        ibtnBack = view.findViewById(R.id.ibtnBack);

        // Thiết lập sự kiện click cho nút Back
        ibtnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                // Sử dụng getParentFragmentManager để đảm bảo quay lại đúng Fragment gốc
                // và MainActivity có thể xử lý việc hiển thị lại Top Bar
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewArticles = view.findViewById(R.id.recyclerView_theloaimoi);

        // Bắt đầu tải bài báo nếu có URL RSS
        if (rssFeedUrl != null && getContext() != null) {
            // Gọi lớp ReadRSS
            new ReadRSS(getContext(), recyclerViewArticles).execute(rssFeedUrl);
            Log.d(TAG, "Đang gọi ReadRSS để tải: " + rssFeedUrl);
        } else {
            Log.e(TAG, "RSS Feed URL hoặc Context là null. Không thể tải bài báo.");
        }
    }
}