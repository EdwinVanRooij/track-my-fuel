package io.github.edwinvanrooij.trackmyfuel;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;

import static io.github.edwinvanrooij.trackmyfuel.util.Config.KEY_RECORD;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public final static int MODIFY_RECORD = 1;

    public final static int RESULT_UPDATE = 100;
    public final static int RESULT_DELETE = 101;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.et_total)
    EditText total;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.listview)
    ListView listview;

    @BindString(R.string.inside)
    String inside;
    @BindString(R.string.average)
    String average;
    @BindString(R.string.outside)
    String outside;

    RecordAdapter mAdapter;
    RecordDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initNavigationDrawer();

        initSpinner();
        initListView();
        mAdapter.addAll(getAllRecords());
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
        menu.findItem(R.id.nav_drawer_lists).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_list));
        menu.findItem(R.id.nav_drawer_products).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_content_paste));
        menu.findItem(R.id.nav_drawer_friends).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_supervisor_account));
        menu.findItem(R.id.nav_drawer_suggestion).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_lightbulb_outline));
        menu.findItem(R.id.nav_drawer_bug).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_bug_report));

        // Last group
        menu.findItem(R.id.nav_drawer_settings).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_settings));
        menu.findItem(R.id.nav_drawer_logout).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_exit_to_app));

        View header = navigationView.getHeaderView(0);

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
        mDbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_drawer_home:
//                setDefaultListFragment();
                break;
            case R.id.nav_drawer_lists:
//                setFragment(MyListsFragment.class, false);
//                fab.show();
                break;
            case R.id.nav_drawer_products:
//                setFragment(MyProductsFragment.class, false);
//                fab.show();
                break;
            case R.id.nav_drawer_friends:
//                setFragment(FriendsFragment.class, false);
//                fab.show();
                break;
            case R.id.nav_drawer_suggestion:
//                setFragment(SuggestionFragment.class, false);
//                fab.hide();
                break;
            case R.id.nav_drawer_bug:
//                setFragment(BugFragment.class, false);
//                fab.hide();
                break;
            case R.id.nav_drawer_settings:
//                We don't need a fab in settings
//                setFragment(SettingsFragment.class, false);
//                fab.hide();
                break;
            case R.id.nav_drawer_logout:
//                logOut();
                break;
            default:
                Toast.makeText(this, "Could not determine which drawer item was clicked", Toast.LENGTH_SHORT).show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initListView() {
        ArrayList<Record> data = new ArrayList<>();
        mAdapter = new RecordAdapter(this, data);

        listview.setAdapter(mAdapter);
    }

    @OnItemLongClick(R.id.listview)
    boolean onItemLongClick(int position) {
        Record r = mAdapter.getItem(position);
        Toast.makeText(this, String.format("Clicked %s", r), Toast.LENGTH_SHORT).show();

        startActivityForResult(new Intent(this, ModifyRecordActivity.class).putExtra(KEY_RECORD, Parcels.wrap(r)), MODIFY_RECORD);
        return true;
    }

    @OnClick(R.id.btn_add)
    void add() {
        int km = Integer.valueOf(total.getText().toString());

        Record.Type type = null;

        String spinnerSelection = spinner.getSelectedItem().toString();
        if (Objects.equals(spinnerSelection, inside)) {
            type = Record.Type.INSIDE;
        } else if (Objects.equals(spinnerSelection, average)) {
            type = Record.Type.AVERAGE;
        } else if (Objects.equals(spinnerSelection, outside)) {
            type = Record.Type.OUTSIDE;
        } else {
            Toast.makeText(this,
                    "Could not determine spinner selection, it equals none of the accepted types: {}"
                    , Toast.LENGTH_SHORT).show();
        }

        Record record = new Record(km, type);

        record = addToDb(record);
        mAdapter.add(record);
    }

    private void refreshListview() {
        mAdapter.clear();
        mAdapter.addAll(getAllRecords());
    }

    private boolean deleteFromDb(Record record) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        return db.delete(RecordContract.Record.TABLE_NAME, RecordContract.Record.COLUMN_RECORD_ID + "=" + record.getId(), null) > 0;
    }

    private void updateFromDb(Record record) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(RecordContract.Record.COLUMN_RECORD_KM, record.getKm());
        cv.put(RecordContract.Record.COLUMN_RECORD_TYPE, record.getType().ordinal());

        db.update(RecordContract.Record.TABLE_NAME, cv, RecordContract.Record.COLUMN_RECORD_ID + "=" + record.getId(), null);
    }

    private Record addToDb(Record record) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(RecordContract.Record.COLUMN_RECORD_KM, record.getKm());
        values.put(RecordContract.Record.COLUMN_RECORD_TYPE, record.getType().ordinal());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(RecordContract.Record.TABLE_NAME, null, values);

        Record newRecord = new Record((int) newRowId, record.getKm(), record.getType());

        System.out.println(String.format("Returning new record; %s", newRecord));

        return newRecord;
    }

    private void initSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the mAdapter to the spinner
        spinner.setAdapter(adapter);
    }

    private List<Record> getAllRecords() {
        List<Record> resultList = new ArrayList<>();

        mDbHelper = new RecordDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(String.format("select * from %s", RecordContract.Record.TABLE_NAME), null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Integer id = cursor.getInt(cursor.getColumnIndex(RecordContract.Record.COLUMN_RECORD_ID));
                Integer km = cursor.getInt(cursor.getColumnIndex(RecordContract.Record.COLUMN_RECORD_KM));
                Integer type = cursor.getInt(cursor.getColumnIndex(RecordContract.Record.COLUMN_RECORD_TYPE));

                Record record = new Record(id, km, Record.Type.values()[type]);
                resultList.add(record);

                cursor.moveToNext();
            }
        }
        cursor.close();

        return resultList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MODIFY_RECORD) {
            if (resultCode == RESULT_UPDATE) {

                Record record = Parcels.unwrap(data.getParcelableExtra(KEY_RECORD));
                updateFromDb(record);
                Toast.makeText(this, String.format("Updated: %s", record.toString()), Toast.LENGTH_SHORT).show();
                refreshListview();

            } else if (resultCode == RESULT_DELETE) {

                Record record = Parcels.unwrap(data.getParcelableExtra(KEY_RECORD));
                deleteFromDb(record);
                Toast.makeText(this, String.format("Deleted: %s", record.toString()), Toast.LENGTH_SHORT).show();
                refreshListview();

            }
        }
    }
}
