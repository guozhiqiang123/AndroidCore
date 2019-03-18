package com.gzq.androidcore.bean;

public class MainMenuBean {
    private String title;
    private int logo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public MainMenuBean(String title, int logo) {
        this.title = title;
        this.logo = logo;
    }
}
