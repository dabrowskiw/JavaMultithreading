package de.htw_berlin.multithreading.blur.model;

import java.awt.*;

public class SingleThreadBlurrer extends AbstractBlurrer {

    private class BlurringThread extends Thread {
        private int radius;

        public BlurringThread(int radius) {
            this.radius = radius;
        }

        public void run() {
            // First, clear the image to black to make the build-up of the blurring visible
            Graphics g = model.getBlurredImage().getGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, model.getBlurredImage().getWidth(), model.getBlurredImage().getHeight());
            model.blurredImageChanged();
            for(int xpos = 0; xpos < model.getSourceImage().getWidth(); xpos++) {
                for(int ypos = 0; ypos < model.getSourceImage().getHeight(); ypos++) {
                    model.getBlurredImage().setRGB(xpos, ypos, getBlurredPixel(xpos, ypos, radius));
                    model.blurredImageChanged();
                }
            }
        }
    }

    public SingleThreadBlurrer(ImageModel model) {
        super(model);
    }

    @Override
    public void blur(int radius) {
        BlurringThread thread = new BlurringThread(radius);
        thread.start();
    }
}
