package ntu.exam.nhatdailyapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DaLuuFragment extends Fragment {

    RecyclerView recyclerView;
    ArticleAdapter adapter;

    public DaLuuFragment() {}

    public void filterArticles(String query) {
        if (adapter != null) {
            adapter.filter(query); // Gọi phương thức filter của ArticleAdapter
        } else {
            Log.e(TAG, "Adapter is null. Cannot filter articles.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_da_luu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerView_daluu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy danh sách bài viết đã lưu từ SharedPreferences
        List<Article> list = SavedArticle.getSavedArticles(requireContext());

        // Khởi tạo adapter và truyền danh sách bài đã lưu.
        // Adapter sẽ tự sao chép danh sách này vào originalArticles của nó.
        adapter = new ArticleAdapter(requireContext(), new ArrayList<>(list)); // Tạo ArrayList mới để truyền

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Luôn tải lại từ SavedArticle để đảm bảo dữ liệu mới nhất
        List<Article> list = SavedArticle.getSavedArticles(requireContext());
        if (adapter != null) {
            // Cập nhật dữ liệu trong adapter (sẽ đồng bộ cả articles và originalArticles bên trong adapter)
            adapter.updateData(new ArrayList<>(list));
        } else {
            Log.e(TAG, "Adapter is null in onResume. This shouldn't happen.");
            // Trường hợp này có thể xảy ra nếu onResume được gọi trước onViewCreated
            // Tốt nhất là khởi tạo adapter trong onViewCreated và chỉ update ở đây.
            // Hoặc kiểm tra và khởi tạo lại nếu null.
            recyclerView = getView().findViewById(R.id.recyclerView_daluu);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ArticleAdapter(requireContext(), new ArrayList<>(list));
            recyclerView.setAdapter(adapter);
        }
        // QUAN TRỌNG: Thêm dòng này để xử lý focus của SearchView
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).resetSearchView();
            Log.d(TAG, "SearchView đã được reset trong onResume của DaLuuFragment.");
        }
    }
}