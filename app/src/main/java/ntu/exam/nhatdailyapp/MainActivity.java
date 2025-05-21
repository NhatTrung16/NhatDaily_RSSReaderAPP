package ntu.exam.nhatdailyapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    void TimDieuKhien(){
        bottomNav = findViewById(R.id.bottomNav);
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
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int mnuItemDuocChonID = item.getItemId();
                if (mnuItemDuocChonID == R.id.mnu_home){
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, new TrangChuFragment())
                            .commit();
                    return true;

                }
                if (mnuItemDuocChonID == R.id.mnu_saved){
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, new DaLuuFragment())
                            .commit();
                    return true;

                }
                if (mnuItemDuocChonID == R.id.mnu_genre){
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, new TheLoaiFragment())
                            .commit();
                    return true;

                }
                return false;
            }

        });

    }
}