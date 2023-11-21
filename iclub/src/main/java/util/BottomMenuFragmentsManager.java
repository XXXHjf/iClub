package util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import com.example.icluub.R;


public class BottomMenuFragmentsManager {
    public void addFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = fragment.getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_fragments, fragment, tag);
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = fragment.getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_fragments, fragment, tag);
        transaction.commit();
    }

    public void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = fragment.getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.show(fragment);
        transaction.commit();
    }

    public void hideFragment(Fragment fragment) {
        FragmentManager fragmentManager = fragment.getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }
}
