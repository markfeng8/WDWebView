package com.wdwebview;

import android.content.Intent;
import android.print.PrintManager;

import androidx.annotation.Nullable;

import com.wdweblib.bean.TabListBean;
import com.wdweblib.ui.WDMainActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends WDMainActivity {

    @Override
    protected ArrayList<TabListBean> tabConfig() {

        ArrayList<TabListBean> list = new ArrayList<>();
        TabListBean tabOne = new TabListBean();
        tabOne.setHasNavigation(true);
        tabOne.setDialog(false);
        tabOne.setSelectedUrl(R.mipmap.ic_search_black);
        tabOne.setUnselectedUrl("www.456.jpg");
        tabOne.setTabName("首页");
        List<TabListBean.TabPageBean> tabPageOne = new ArrayList<>();
        tabPageOne.add(new TabListBean.TabPageBean("one", "http://10.2.98.138:8888/"));
        tabOne.setTabPage(tabPageOne);
        list.add(tabOne);

        TabListBean tabTwo = new TabListBean();
        tabTwo.setHasNavigation(true);
        tabTwo.setDialog(false);
        tabTwo.setSelectedUrl("www.123.jpg");
        tabTwo.setUnselectedUrl("www.456.jpg");
        tabTwo.setTabName("个人");
        List<TabListBean.TabPageBean> tabPageTwo = new ArrayList<>();
        tabPageTwo.add(new TabListBean.TabPageBean("one", "http://10.2.98.138:8888/"));
        tabPageTwo.add(new TabListBean.TabPageBean("two", "https://www.baidu.com/"));
        tabTwo.setTabPage(tabPageTwo);
        list.add(tabTwo);

        return list;
    }

    @Override
    protected String selectedTextColor() {
        return "#51da8a";
    }

    @Override
    protected String unselectedTextColor() {
        return "#636363";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}