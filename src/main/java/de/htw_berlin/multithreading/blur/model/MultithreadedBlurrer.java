package de.htw_berlin.multithreading.blur.model;

import java.awt.*;
import java.util.LinkedList;

public abstract class MultithreadedBlurrer extends AbstractBlurrer {

    protected MultithreadedBlurrer(ImageModel model) {
        super(model);
    }
    protected LinkedList<BlurTask> tasks = new LinkedList<>();

    protected void makeTasks(int radius, int size) {
        tasks.clear();
        for(int i=0; i<model.getSourceImage().getWidth(); i+=size) {
            for(int j=0; j<model.getSourceImage().getHeight(); j+=size) {
                tasks.add(new BlurTask(i, j, size, size, radius));
            }
        }
    }


    protected int[] getBlurredPixels(BlurTask task) {
        int[] pixels = new int[task.getWidth()*task.getHeight()];
        int pos=0;
        for (int xpos = task.getX(); xpos < task.getX()+ task.getWidth(); xpos++) {
            for (int ypos = task.getY(); ypos < task.getY()+ task.getHeight(); ypos++) {
                pixels[pos] = getBlurredPixel(xpos, ypos, task.getRadius());
                pos++;
            }
        }
        return pixels;
    }

    protected void drawBlurredPixels(BlurTask task, int[] pixels) {
        int pos=0;
        for (int xpos = task.getX(); xpos < task.getX()+ task.getWidth(); xpos++) {
            for (int ypos = task.getY(); ypos < task.getY()+ task.getHeight(); ypos++) {
                model.getBlurredImage().setRGB(xpos, ypos, pixels[pos]);
                model.blurredImageChanged();
                pos++;
            }
        }
    }

    protected void markRegion(BlurTask task, Color color) {
        // First, make the part to blur red to make the build-up of the blurring visible
        for (int xpos = task.getX()+task.getWidth()-1; xpos >= task.getX(); xpos--) {
            for (int ypos = task.getY()+task.getHeight()-1; ypos >= task.getY(); ypos--) {
                model.getBlurredImage().setRGB(xpos, ypos, color.getRGB());
                model.blurredImageChanged();
            }
        }
    }
}
