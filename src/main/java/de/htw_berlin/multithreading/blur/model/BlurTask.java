package de.htw_berlin.multithreading.blur.model;

public class BlurTask {
    private int x;
    private int y;
    private int width;
    private int height;
    private int radius;

    public BlurTask(int x, int y, int width, int height, int radius) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.radius = radius;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
