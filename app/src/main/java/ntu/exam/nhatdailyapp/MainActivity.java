package ntu.exam.nhatdailyapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    LinearLayout mainTopBar;

    void TimDieuKhien() {
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

        // Quản lý hiển thị TopBar khi fragment thay đổi
        getSupportFragmentManager().addOnBackStackChangedListener(this::updateTopBar);

        replaceFragmentAndControlTopBar(new TrangChuFragment());

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int mnuItemDuocChonID = item.getItemId();
                if (mnuItemDuocChonID == R.id.mnu_home) {
                    replaceFragmentAndControlTopBar(new TrangChuFragment());
                    return true;
                }
                if (mnuItemDuocChonID == R.id.mnu_saved) {
                    replaceFragmentAndControlTopBar(new DaLuuFragment());
                    return true;
                }
                if (mnuItemDuocChonID == R.id.mnu_genre) {
                    replaceFragmentAndControlTopBar(new TheLoaiFragment());
                    return true;
                }
                return false;
            }
        });
    }

    // Phương thức thay thế Fragment và điều khiển Top Bar (chỉ điều khiển BackStack)
    void replaceFragmentAndControlTopBar(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);

        boolean isMainFragment = (fragment instanceof TrangChuFragment ||
                fragment instanceof DaLuuFragment ||
                fragment instanceof TheLoaiFragment);

        if (fragment instanceof TheLoaiMoiFragment) {
            fragmentTransaction.addToBackStack(null); // Chỉ thêm TheLoaiMoiFragment vào BackStack
        } else if (isMainFragment) {
            // Xoá toàn bộ BackStack khi quay lại Fragment chính
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        updateTopBar(); // Gọi cập nhật lại TopBar sau khi commit
    }

    // Cập nhật trạng thái hiển thị của Top Bar theo Fragment hiện tại
    private void updateTopBar() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (currentFragment instanceof TheLoaiMoiFragment) {
            mainTopBar.setVisibility(View.GONE);
        } else {
            mainTopBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            fragmentManager.executePendingTransactions();
            updateTopBar(); // Đảm bảo TopBar cập nhật chính xác sau khi back
        } else {
            super.onBackPressed();
        }
    }
}
