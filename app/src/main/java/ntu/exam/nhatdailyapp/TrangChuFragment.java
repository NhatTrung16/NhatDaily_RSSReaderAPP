package ntu.exam.nhatdailyapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;

public class TrangChuFragment extends Fragment {

    RecyclerView rvArticles;
    ArticleAdapter articleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);

        rvArticles = view.findViewById(R.id.recyclerView); // Ánh xạ RecyclerView
        rvArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvArticles.setHasFixedSize(true);

        // Khởi tạo this.articleAdapter VÀ thiết lập nó cho RecyclerView
        // Truyền một ArrayList rỗng ban đầu, dữ liệu sẽ được cập nhật sau
        articleAdapter = new ArticleAdapter(getContext(), new ArrayList<>());
        rvArticles.setAdapter(articleAdapter);

        // Load dữ liệu
        // Quan trọng: Override onPostExecute để cập nhật dữ liệu của articleAdapter
        if (getContext() != null) {
            new ReadRSS(getContext(), null) { // Truyền null cho RecyclerView vì Fragment tự quản lý adapter
                @Override
                protected void onPostExecute(ArrayList<Article> result) {
                    if (result != null) {
                        if (articleAdapter != null) {
                            // Gọi updateData của ArticleAdapter để cập nhật cả danh sách hiển thị và danh sách gốc
                            articleAdapter.updateData(result);
                            Log.d(TAG, "Đã tải thành công " + result.size() + " bài báo và cập nhật adapter.");
                        } else {
                            Log.e(TAG, "articleAdapter is null after RSS load in onPostExecute. This should not happen.");
                        }
                    } else {
                        Log.e(TAG, "Không có dữ liệu bài báo từ RSS.");
                    }
                }
            }.execute("https://vnexpress.net/rss/thoi-su.rss"); // URL RSS của bạn
        }

        return view;
    }
    public void filterArticles(String query) {
        if (articleAdapter != null) {
            articleAdapter.filter(query); // Gọi phương thức filter của ArticleAdapter
            Log.d(TAG, "Đã gọi filterArticles với query: " + query);
        } else {
            Log.e(TAG, "articleAdapter is null. Cannot filter articles."); // Log này sẽ không xuất hiện nữa
        }
    }

    // QUAN TRỌNG: Thêm onResume để xử lý focus của SearchView
    @Override
    public void onResume() {
        super.onResume();
        // Kiểm tra nếu Activity là MainActivity và gọi phương thức resetSearchView
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).resetSearchView();
            Log.d(TAG, "SearchView đã được reset trong onResume của TrangChuFragment.");
        }
    }
}