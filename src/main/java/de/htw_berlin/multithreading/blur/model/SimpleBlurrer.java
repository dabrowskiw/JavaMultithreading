package de.htw_berlin.multithreading.blur.model;

import java.awt.*;

public class SimpleBlurrer extends AbstractBlurrer {

    public SimpleBlurrer(ImageModel model) {
        super(model);
    }

    @Override
    public void blur(int radius) {
        for(int xpos = 0; xpos < model.getSourceImage().getWidth(); xpos++) {
            for(int ypos = 0; ypos < model.getSourceImage().getHeight(); ypos++) {
                model.getBlurredImage().setRGB(xpos, ypos, getBlurredPixel(xpos, ypos, radius));
                model.blurredImageChanged();
            }
        }
    }
}
