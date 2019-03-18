package com.example.module_usage.bean;

public class MoreStyleBean {
    private String title;
    private int leftImage;
    private int rightImage;

    public MoreStyleBean(String title, int leftImage, int rightImage) {
        this.title = title;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLeftImage() {
        return leftImage;
    }

    public void setLeftImage(int leftImage) {
        this.leftImage = leftImage;
    }

    public int getRightImage() {
        return rightImage;
    }

    public void setRightImage(int rightImage) {
        this.rightImage = rightImage;
    }
}
