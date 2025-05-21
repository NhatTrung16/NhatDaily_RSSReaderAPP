package ntu.exam.nhatdailyapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TrangChuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);

        RecyclerView rvArticles = view.findViewById(R.id.recyclerView);
        rvArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvArticles.setHasFixedSize(true);

        // Adapter rỗng tránh lỗi No adapter attached
        rvArticles.setAdapter(new ArticleAdapter(getContext(), new ArrayList<>()));

        // Sau đó load dữ liệu
        new ReadRSS(getContext(), rvArticles).execute("https://vnexpress.net/rss/thoi-su.rss");

        return view;
    }
}