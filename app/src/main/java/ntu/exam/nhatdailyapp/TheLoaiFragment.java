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

        theLoaiList.add(new TheLoai("Thế Giới", R.drawable.ic_thegioi, "https://vnexpress.net/rss/the-gioi.rss"));
        theLoaiList.add(new TheLoai("Kinh Doanh", R.drawable.ic_kinhdoanh, "https://vnexpress.net/rss/kinh-doanh.rss"));
        theLoaiList.add(new TheLoai("Khoa Học", R.drawable.ic_khoahoc, "https://vnexpress.net/rss/khoa-hoc.rss"));
        theLoaiList.add(new TheLoai("Giải Trí", R.drawable.ic_giaitri, "https://vnexpress.net/rss/giai-tri.rss"));
        theLoaiList.add(new TheLoai("Thể Thao", R.drawable.ic_thethao, "https://vnexpress.net/rss/the-thao.rss"));
        theLoaiList.add(new TheLoai("Pháp Luật", R.drawable.ic_phapluat, "https://vnexpress.net/rss/phap-luat.rss"));
        theLoaiList.add(new TheLoai("Giáo Dục", R.drawable.ic_giaoduc, "https://vnexpress.net/rss/giao-duc.rss"));
        theLoaiList.add(new TheLoai("Sức Khỏe", R.drawable.ic_suckhoe, "https://vnexpress.net/rss/suc-khoe.rss"));
        theLoaiList.add(new TheLoai("Đời Sống", R.drawable.ic_doisong, "https://vnexpress.net/rss/doi-song.rss"));
        theLoaiList.add(new TheLoai("Du Lịch", R.drawable.ic_dulich, "https://vnexpress.net/rss/du-lich.rss"));

        theLoaiAdapter = new TheLoaiAdapter(getContext(), theLoaiList, this);
        recyclerViewTheLoai.setAdapter(theLoaiAdapter);
    }

    @Override
    public void onTheLoaiClick(TheLoai theLoai) {
        // Tạo instance của TheLoaiMoiFragment
        TheLoaiMoiFragment theLoaiMoiFragment = TheLoaiMoiFragment.newInstance(theLoai.getRssFeedUrl(), theLoai.getName());

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragmentAndControlTopBar(theLoaiMoiFragment); // Đã sửa tên phương thức
        }
    }
}