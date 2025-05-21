package ntu.exam.nhatdailyapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DaLuuFragment extends Fragment {

    RecyclerView recyclerView;
    ArticleAdapter adapter;
    ArrayList<Article> savedArticles;

    public DaLuuFragment() {}

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
        savedArticles = new ArrayList<>(list);

        adapter = new ArticleAdapter(requireContext(), savedArticles);
        recyclerView.setAdapter(adapter);
    }

    // (Tuỳ chọn) nếu bạn muốn cập nhật lại khi quay lại Fragment
    @Override
    public void onResume() {
        super.onResume();
        List<Article> list = SavedArticle.getSavedArticles(requireContext());
        savedArticles.clear();
        savedArticles.addAll(list);
        adapter.notifyDataSetChanged();
    }
}