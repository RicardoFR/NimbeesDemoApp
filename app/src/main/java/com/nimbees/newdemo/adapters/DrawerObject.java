package com.nimbees.newdemo.adapters;

/**
 * Created by ricardo on 25/03/15.
 */
public class DrawerObject {

        public int icon;
        public String name;

        public DrawerObject(int icon, String name)
        {
            this.icon = icon;
            this.name = name;
        }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

