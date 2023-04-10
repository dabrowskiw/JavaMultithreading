package de.htw_berlin.multithreading.blur.model;

public abstract class AbstractBlurrer {
    protected ImageModel model;
    protected int radius;

    public AbstractBlurrer(ImageModel model, int radius) {
        this.model = model;
        this.radius = radius;
    }

    protected AbstractBlurrer() {
    }

    public abstract void blur();
}
