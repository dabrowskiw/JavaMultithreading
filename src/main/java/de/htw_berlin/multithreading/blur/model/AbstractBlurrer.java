package de.htw_berlin.multithreading.blur.model;

import java.awt.*;

public abstract class AbstractBlurrer {
    protected ImageModel model;

    public AbstractBlurrer(ImageModel model) {
        this.model = model;
    }

    protected AbstractBlurrer() {
    }

    public abstract void blur(int radius);

    /**
     * Gets the blurred value for a single pixel around the given image coordinates.
     * @param x X coordinate of pixel to blur
     * @param y Y coordinate of pixel to blur
     * @param radius Radius of the blur effect
     * @return argb int with the color values of the blurred pixel
     */
    protected int getBlurredPixel(int x, int y, int radius) {
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
        return (colorsums[0]/numpixels) | ((colorsums[1]/numpixels) << 8) | ((colorsums[2]/numpixels) << 16) | ((colorsums[3]/numpixels) << 24) ;
    }

    protected void blankImage(Color color) {
        Graphics g = model.getBlurredImage().getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, model.getBlurredImage().getWidth(), model.getBlurredImage().getHeight());
    }
}
