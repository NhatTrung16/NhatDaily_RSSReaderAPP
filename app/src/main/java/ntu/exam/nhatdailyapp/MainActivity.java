package ntu.exam.nhatdailyapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View; // THÊM MỚI: Import View để dùng View.GONE và View.VISIBLE
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment; // THÊM MỚI: Import Fragment
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction; // THÊM MỚI: Import FragmentTransaction

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    LinearLayout mainTopBar;
    void TimDieuKhien(){
        bottomNav = findViewById(R.id.bottomNav);
        mainTopBar = findViewById(R.id.linearLayout3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TimDieuKhien();
        FragmentManager fragmentManager = getSupportFragmentManager();

        replaceFragmentAndControlTopBar(new TrangChuFragment());
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int mnuItemDuocChonID = item.getItemId();
                if (mnuItemDuocChonID == R.id.mnu_home){
                    replaceFragmentAndControlTopBar(new TrangChuFragment());
                    return true;
                }
                if (mnuItemDuocChonID == R.id.mnu_saved){
                    replaceFragmentAndControlTopBar(new DaLuuFragment());
                    return true;
                }
                if (mnuItemDuocChonID == R.id.mnu_genre){
                    replaceFragmentAndControlTopBar(new TheLoaiFragment());
                    return true;
                }
                return false;
            }
        });

    }

    // Phương thức để thay thế Fragment và điều khiển Top Bar
    void replaceFragmentAndControlTopBar(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        if (fragment instanceof TheLoaiMoiFragment) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

        // Logic ẩn/hiện TOÀN BỘ Top Bar của MainActivity bằng cách ẩn/hiện LinearLayout
        if (fragment instanceof TheLoaiMoiFragment) {
            // Nếu Fragment mới là TheLoaiMoiFragment, ẩn Top Bar của MainActivity
            mainTopBar.setVisibility(View.GONE);
        } else {
            // Với các Fragment khác (TrangChu, TheLoai, DaLuu), hiện Top Bar của MainActivity
            mainTopBar.setVisibility(View.VISIBLE);
        }
    }


    // onBackPressed để xử lý việc hiển thị Top Bar khi quay lại
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Kiểm tra xem có Fragment nào trong Back Stack không (ngoại trừ Fragment gốc nếu có)
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(); // Lấy Fragment trước đó ra khỏi Back Stack
            fragmentManager.executePendingTransactions(); // Đảm bảo các thay đổi được thực hiện ngay lập tức

            // Lấy Fragment hiện tại sau khi pop
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragmentContainerView);

            // Kiểm tra Fragment hiện tại để quyết định hiển thị lại Top Bar chính hay không
            // Nếu Fragment hiện tại là TrangChuFragment, TheLoaiFragment, hoặc DaLuuFragment, thì hiện Top Bar
            if (currentFragment instanceof TrangChuFragment ||
                    currentFragment instanceof TheLoaiFragment ||
                    currentFragment instanceof DaLuuFragment) {
                mainTopBar.setVisibility(View.VISIBLE); // Hiển thị Top Bar chính
            } else {
                // Giữ ẩn nếu Fragment hiện tại vẫn là TheLoaiMoiFragment
                // hoặc bất kỳ Fragment nào khác không cần Top Bar chính của MainActivity.
                mainTopBar.setVisibility(View.GONE);
            }
        } else {
            // Nếu không còn Fragment nào trong Back Stack, gọi super.onBackPressed() để thoát ứng dụng
            super.onBackPressed();
        }
    }
}