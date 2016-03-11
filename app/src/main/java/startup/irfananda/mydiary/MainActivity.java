package startup.irfananda.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private String dateNow;
    private int dd,mm,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Calendar datenow = Calendar.getInstance();
        dd = datenow.get(Calendar.DAY_OF_MONTH);
        // Month is 0 based, just add 1
        mm = datenow.get(Calendar.MONTH) + 1;
        yy = datenow.get(Calendar.YEAR);
        // set current date into textview
        dateNow = String.valueOf(new StringBuilder()
                .append(yy).append("-").append(mm).append("-").append(dd));

        fragment = new DiaryFragment(dateNow);

        changeFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_diaries) {
            fragment = new DiaryFragment(dateNow);
        } else if (id == R.id.nav_overview) {
            fragment = new OverViewFragment();
        } else if (id == R.id.nav_rewards) {
            fragment = new RewardFragment();
        } else if (id == R.id.nav_notes) {
            fragment = new NoteFragment();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this,SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_About) {
            fragment = new NoteFragment();
        } else if (id == R.id.nav_LogOut) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }

        changeFragment(fragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }
}
