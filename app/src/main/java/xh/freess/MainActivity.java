package xh.freess;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import xh.freess.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements FreeSSFragment.FreeSSFragListener {

    public static final String TAG = "MainActivity";
    public static final int DURATION = 500;

    ActivityMainBinding binding;
//    private FabMenu fabMenu;
    private String currentSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);


        binding.fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return super.onPrepareMenu(navigationMenu);
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                FreeSSFragment fragment = (FreeSSFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                switch (menuItem.getItemId()) {
                    case R.id.menu_site:
                        fragment.changeSite();
                        break;
                    case R.id.menu_output:
                        fragment.accountToJsonAll();
                        break;
                    case R.id.menu_copy_all:
                        fragment.copyAll();
                        break;
                    default:
                        super.onMenuItemSelected(menuItem);
                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSiteTag(String tag) {
    }
}
