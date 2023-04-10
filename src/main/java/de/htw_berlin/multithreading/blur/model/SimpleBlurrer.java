package de.htw_berlin.multithreading.blur.model;

import java.awt.*;

public class SimpleBlurrer extends AbstractBlurrer {

    public SimpleBlurrer(ImageModel model, int radius) {
        super(model, radius);
    }

    @Override
    public void blur() {
        for(int xpos = 0; xpos < model.getSourceImage().getWidth(); xpos++) {
            for(int ypos = 0; ypos < model.getSourceImage().getHeight(); ypos++) {
                model.getBlurredImage().setRGB(xpos, ypos, getBlurredPixel(xpos, ypos));
//                model.getBlurredImage().setRGB(xpos, ypos, model.getSourceImage().getRGB(xpos, ypos));
//                model.getBlurredImage().setRGB(xpos, ypos, Color.red.getRGB());
                model.blurredImageChanged();
            }
        }
    }

    /**
     * Gets the blurred value for a single pixel around the given image coordinates.
     * @param x X coordinate of pixel to blur
     * @param y Y coordinate of pixel to blur
     * @return argb int with the color values of the blurred pixel
     */
    protected int getBlurredPixel(int x, int y) {
        int[] colorsums = new int[4];
        int numpixels = 0;
        // Math.max and Math.min to avoid referencing pixels outside the image
        for(int xref = Math.max(0, x-radius); xref < Math.min(model.getSourceImage().getWidth(), x+radius); xref++) {
            for (int yref = Math.max(0, y - radius); yref < Math.min(model.getSourceImage().getHeight(), y + radius); yref++) {
                colorsums[0] += (model.getSourceImage().getRGB(xref, yref) & 0xff); // blue pixel value
                colorsums[1] += (model.getSourceImage().getRGB(xref, yref) & 0xff00) >> 8; // green pixel value
                colorsums[2] += (model.getSourceImage().getRGB(xref, yref) & 0xff0000) >> 16; // red pixel value
                colorsums[3] += (model.getSourceImage().getRGB(xref, yref) & 0xff000000) >> 24; // alpha pixel value
                numpixels++;
            }
        }
//        return (colorsums[0] / numpixels) | ((colorsums[1]/numpixels) << 8) | ((colorsums[2]/numpixels) << 16);
        return (colorsums[0]/numpixels) | ((colorsums[1]/numpixels) << 8) | ((colorsums[2]/numpixels) << 16) | ((colorsums[3]/numpixels) << 24) ;
    }
}
