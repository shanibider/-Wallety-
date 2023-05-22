package com.example.wallety.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.wallety.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.github.cdimascio.dotenv.Dotenv;

public class MainActivity extends AppCompatActivity {
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //BottomNavigationView handler
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int destinationId = 0;

            switch (item.getItemId()) {
                case R.id.menuTransfer:
                    destinationId = R.id.transferMoneyFragment2;
                    break;
                case R.id.menuTasks:
                    destinationId = R.id.tasksFragment;
                    break;
                case R.id.menuSaving:
                    destinationId = R.id.savingMoneyFragment;
                    break;
                case R.id.menuProfile:
                    destinationId = R.id.stripeActivity;
                    break;
            }

            if (destinationId != 0) {
                navigateToDestination(destinationId);
                return true;
            } else {
                return false;
            }

        });

        //Home button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int destinationId = 0;
                destinationId = R.id.homeFragment;
                if (destinationId != 0) {
                    navigateToDestination(destinationId);
                }
            }
        });
    }

        //Outside onCreate
        private void navigateToDestination(int destinationId) {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
            navHostFragment.getNavController().navigate(destinationId);
        }


//    private  void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.main_navhost, fragment);
//        fragmentTransaction.commit();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navController.popBackStack();
        }
        return (super.onOptionsItemSelected(item));
    }
}
