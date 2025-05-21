package ntu.exam.nhatdailyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;

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
    SearchView searchView;

    void TimDieuKhien() {
        bottomNav = findViewById(R.id.bottomNav);
        mainTopBar = findViewById(R.id.linearLayout3);
        searchView = findViewById(R.id.searchView);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("MainActivity", "Search submitted: " + query);
                handleSearch(query);
                searchView.clearFocus(); // Ẩn bàn phím khi submit
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("MainActivity", "Search text changed: " + newText);
                handleSearch(newText);
                return true;
            }
        });

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

    private void handleSearch(String query) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (currentFragment instanceof TrangChuFragment) {
            ((TrangChuFragment) currentFragment).filterArticles(query);
        } else if (currentFragment instanceof DaLuuFragment) {
            ((DaLuuFragment) currentFragment).filterArticles(query);
        }
    }

    // Phương thức để reset SearchView từ Fragment
    public void resetSearchView() {
        searchView.setQuery("", false); // Xóa query text, false: không kích hoạt listener
        searchView.clearFocus(); // Bỏ focus cho SearchView
        // Ẩn bàn phím ảo
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
