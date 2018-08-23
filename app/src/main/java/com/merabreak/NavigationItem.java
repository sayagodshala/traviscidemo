package com.merabreak;

import java.util.ArrayList;
import java.util.List;

public class NavigationItem {
    private int icon;
    private NavigationItemType navigationItemType;
    private List<NavigationItem> items = new ArrayList<>();

    public NavigationItem() {
    }

    public NavigationItem(int icon, NavigationItemType navigationItemType) {
        this.icon = icon;
        this.navigationItemType = navigationItemType;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public NavigationItemType getNavigationItemType() {
        return navigationItemType;
    }

    public void setNavigationItemType(NavigationItemType navigationItemType) {
        this.navigationItemType = navigationItemType;
    }

    public List<NavigationItem> getItems() {
        return items;
    }

    public void setItems(List<NavigationItem> items) {
        this.items = items;
    }

    public boolean isHeader() {
        return items.size() > 0;
    }
}
