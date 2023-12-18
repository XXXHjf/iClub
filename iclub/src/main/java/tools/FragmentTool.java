package tools;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentTool {
    /**
     *向Activity的指定占位符里添加一个判断
     * @param containerId  片段要插入的占位符组件的ID
     * @param fragment  要添加的片段
     * @param fragmentManager  片段管理，直接传入getSupportFragmentManager()即可
     */
    public static void addFragment(int containerId, Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(containerId, fragment);
        transaction.commit();
    }

    /**
     *替换片段
     * @param containerId  新替换的片段要插入的占位符组件的ID
     * @param fragment  新替换的片段
     * @param fragmentManager  片段管理，直接传入getSupportFragmentManager()即可
     */
    public static void replaceFragment(int containerId, Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }

    /**
     *显示一个片段
     * @param fragment  要显示的片段
     * @param fragmentManager  片段管理，直接传入getSupportFragmentManager()即可
     */
    public static void showFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.show(fragment);
        transaction.commit();
    }

    /**
     *隐藏一个片段
     * @param fragment  要隐藏的片段
     * @param fragmentManager  片段管理，直接传入getSupportFragmentManager()即可
     */
    public static void hideFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

}
