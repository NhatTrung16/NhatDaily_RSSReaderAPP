package ntu.exam.nhatdailyapp;

import android.os.Bundle;
import androidx.annotation.NonNull; // Import này cần thiết cho @NonNull
import androidx.annotation.Nullable; // Import này cần thiết cho @Nullable
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager; // Import này cần thiết cho GridLayoutManager
import androidx.recyclerview.widget.RecyclerView; // Import này cần thiết cho RecyclerView
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast; // Dùng tạm để test click

import java.util.ArrayList; // Import này cần thiết cho ArrayList

public class TheLoaiFragment extends Fragment implements TheLoaiAdapter.OnTheLoaiClickListener {

    private RecyclerView recyclerViewTheLoai;
    private TheLoaiAdapter theLoaiAdapter;
    private ArrayList<TheLoai> theLoaiList;

    public TheLoaiFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_the_loai, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewTheLoai = view.findViewById(R.id.recycler_view_the_loai);
        recyclerViewTheLoai.setLayoutManager(new GridLayoutManager(getContext(), 2));

        theLoaiList = new ArrayList<>();

        theLoaiList.add(new TheLoai("Thời sự", R.drawable.ic_thoisu, "https://vnexpress.net/rss/thoi-su.rss"));
        theLoaiList.add(new TheLoai("Thế giới", R.drawable.ic_thegioi, "https://vnexpress.net/rss/the-gioi.rss"));
        theLoaiList.add(new TheLoai("Kinh doanh", R.drawable.ic_kinhdoanh, "https://vnexpress.net/rss/kinh-doanh.rss"));
        theLoaiList.add(new TheLoai("Khoa học", R.drawable.ic_khoahoc, "https://vnexpress.net/rss/khoa-hoc.rss"));
        theLoaiList.add(new TheLoai("Giải trí", R.drawable.ic_giaitri, "https://vnexpress.net/rss/giai-tri.rss"));
        theLoaiList.add(new TheLoai("Thể thao", R.drawable.ic_thethao, "https://vnexpress.net/rss/the-thao.rss"));
        theLoaiList.add(new TheLoai("Pháp luật", R.drawable.ic_phapluat, "https://vnexpress.net/rss/phap-luat.rss"));
        theLoaiList.add(new TheLoai("Giáo dục", R.drawable.ic_giaoduc, "https://vnexpress.net/rss/giao-duc.rss"));
        theLoaiList.add(new TheLoai("Sức khỏe", R.drawable.ic_suckhoe, "https://vnexpress.net/rss/suc-khoe.rss"));
        theLoaiList.add(new TheLoai("Đời sống", R.drawable.ic_doisong, "https://vnexpress.net/rss/doi-song.rss"));
        // ... Thêm các thể loại khác của bạn vào đây

        theLoaiAdapter = new TheLoaiAdapter(getContext(), theLoaiList, this);
        recyclerViewTheLoai.setAdapter(theLoaiAdapter);
    }

    @Override
    public void onTheLoaiClick(TheLoai theLoai) {
        Toast.makeText(getContext(), "Bạn đã chọn: " + theLoai.getName() + "\nRSS Feed: " + theLoai.getRssFeedUrl(), Toast.LENGTH_SHORT).show();

        // Ở đây, bạn sẽ thực hiện chuyển đổi Fragment để hiển thị danh sách bài báo của thể loại đã chọn.
        // Ví dụ: TrangChuFragment newsListFragment = new TrangChuFragment();
        // Bundle bundle = new Bundle();
        // bundle.putString("rss_feed_url", theLoai.getRssFeedUrl());
        // newsListFragment.setArguments(bundle);

        // getParentFragmentManager().beginTransaction()
        //      .replace(R.id.fragment_container, newsListFragment)
        //      .addToBackStack(null)
        //      .commit();
    }
}