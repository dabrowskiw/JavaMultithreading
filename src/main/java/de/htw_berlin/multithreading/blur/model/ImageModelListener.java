package de.htw_berlin.multithreading.blur.model;

public interface ImageModelListener {
    public void sourceImageChanged();
    public void blurredImageChanged();
}
