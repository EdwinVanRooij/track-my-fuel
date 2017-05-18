package io.github.edwinvanrooij.trackmyfuel.util;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.edwinvanrooij.trackmyfuel.R;
import io.github.edwinvanrooij.trackmyfuel.Record;
import me.evrooij.groceries.interfaces.ContainerActivity;

import static io.github.edwinvanrooij.trackmyfuel.util.Config.KEY_RECORD;
import static io.github.edwinvanrooij.trackmyfuel.util.Config.THREADPOOL_MAINACTIVITY_SIZE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ContainerActivity {

    public final static int MODIFY_RECORD = 1;

    public final static int RESULT_UPDATE = 100;
    public final static int RESULT_DELETE = 101;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    BaseFragment mCurrentFragment;

    private ExecutorService threadPool;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initNavigationDrawer();

        threadPool = Executors.newFixedThreadPool(THREADPOOL_MAINACTIVITY_SIZE);

        setDefaultListFragment();
    }

    public void setDefaultListFragment() {
        setFragment(MainFragment.class, false);
        navigationView.setCheckedItem(R.id.nav_drawer_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setFragment(Class fragmentClass, boolean addToStack) {
        try {
            BaseFragment fragment = (BaseFragment) fragmentClass.newInstance();
//            Bundle bundle = new Bundle();
//            bundle.putParcelable(KEY_ACCOUNT, Parcels.wrap(thisAccount));
//            fragment.setArguments(bundle);
            mCurrentFragment = fragment;

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            // Add this transaction to the back stack
            if (addToStack) {
                ft.addToBackStack(null);
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the navigation drawer, including variable usage to set account name and email in the header
     */
    private void initNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();

        // First group
        menu.findItem(R.id.nav_drawer_home).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_home));

        // Communication
//        menu.findItem(R.id.nav).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_list));
//        menu.findItem(R.id.nav_drawer_products).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_content_paste));
//        menu.findItem(R.id.nav_drawer_friends).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_supervisor_account));
//        menu.findItem(R.id.nav_drawer_suggestion).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_lightbulb_outline));
//        menu.findItem(R.id.nav_drawer_bug).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_bug_report));

        // Last group
        menu.findItem(R.id.nav_drawer_settings).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_settings));

//        View header = navigationView.getHeaderView(0);
//
//        TextView tvName = (TextView) header.findViewById(R.id.nav_header_name);
//        TextView tvEmail = (TextView) header.findViewById(R.id.nav_header_email);
//        tvName.setText(thisAccount.getUsername());
//        tvEmail.setText(thisAccount.getEmail());
//
//        header.setOnClickListener(v -> {
//            drawer.closeDrawer(GravityCompat.START);
//            setFragment(MyProfileFragment.class, true);
//            fab.hide();
//        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_drawer_home:
                setDefaultListFragment();
                break;
            case R.id.nav_drawer_all_records:
//                setFragment(MyListsFragment.class, false);
//                fab.show();
                break;
            case R.id.nav_drawer_bill:
                setFragment(BillFragment.class, false);
//                fab.show();
                break;
            case R.id.nav_drawer_payment:
//                setFragment(FriendsFragment.class, false);
//                fab.show();
                break;
            case R.id.nav_drawer_my_records:
//                setFragment(SuggestionFragment.class, false);
//                fab.hide();
                break;
            case R.id.nav_drawer_settings:
//                We don't need a fab in settings
//                setFragment(SettingsFragment.class, false);
//                fab.hide();
                break;
            default:
                Toast.makeText(this, "Could not determine which drawer item was clicked", Toast.LENGTH_SHORT).show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurrentFragment.onContainterActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void executeRunnable(@NotNull Runnable runnable) {
        threadPool.execute(runnable);
    }

    @Override
    public void setActionBarTitle(@NotNull String title) {
        runOnUiThread(() -> toolbar.setTitle(title));
    }

    @Override
    public void startActivityForResult(@NotNull Object obj) {
        startActivityForResult(new Intent(this, ModifyRecordActivity.class).putExtra(KEY_RECORD, Parcels.wrap(obj)), MainActivity.MODIFY_RECORD);
    }
}
