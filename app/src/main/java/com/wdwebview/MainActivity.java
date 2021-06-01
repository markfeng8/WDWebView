package com.wdwebview;

import com.wdweblib.bean.TabListBean;
import com.wdweblib.ui.WDMainActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends WDMainActivity {

    @Override
    protected ArrayList<TabListBean> tabConfig() {
        ArrayList<TabListBean> list=new ArrayList<>();
        TabListBean tabOne=new TabListBean();
        tabOne.setHasNavigation(true);
        tabOne.setDialog(false);
        tabOne.setSelectedUrl("www.123.jpg");
        tabOne.setUnselectedUrl("www.456.jpg");
        tabOne.setTabName("shouye");
        List<TabListBean.TabPageBean> tabPageOne=new ArrayList<>();
        tabPageOne.add(new TabListBean.TabPageBean("one","http://10.2.98.138:8889/"));
        tabOne.setTabPage(tabPageOne);
        list.add(tabOne);

        TabListBean tabTwo=new TabListBean();
        tabTwo.setHasNavigation(true);
        tabTwo.setDialog(false);
        tabTwo.setSelectedUrl("www.123.jpg");
        tabTwo.setUnselectedUrl("www.456.jpg");
        tabTwo.setTabName("geren");
        List<TabListBean.TabPageBean> tabPageTwo=new ArrayList<>();
        tabPageTwo.add(new TabListBean.TabPageBean("two","https://www.baidu.com/"));
        tabPageTwo.add(new TabListBean.TabPageBean("title","https://www.baidu.com/"));
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


}