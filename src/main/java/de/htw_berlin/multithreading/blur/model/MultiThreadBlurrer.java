package de.htw_berlin.multithreading.blur.model;

import java.awt.*;
import java.util.LinkedList;

public class MultiThreadBlurrer extends AbstractBlurrer {

    private LinkedList<BlurTask> tasks;

    private class BlurringThread extends Thread {
        public void run() {
            BlurTask task = null;
            while(!tasks.isEmpty()) {
                task = tasks.getFirst();
                markRegion(task, Color.red);
                tasks.removeFirst();
                blurPixels(task);
            }
        }

        private void blurPixels(BlurTask task) {
            for (int xpos = task.getX(); xpos < task.getX()+ task.getWidth(); xpos++) {
                for (int ypos = task.getY(); ypos < task.getY()+ task.getHeight(); ypos++) {
                    model.getBlurredImage().setRGB(xpos, ypos, getBlurredPixel(xpos, ypos, task.getRadius()));
                    model.blurredImageChanged();
                }
            }
        }

        private void markRegion(BlurTask task, Color color) {
            // First, make the part to blur red to make the build-up of the blurring visible
            for (int xpos = task.getX(); xpos < task.getX()+task.getWidth(); xpos++) {
                for (int ypos = task.getY(); ypos < task.getY()+task.getHeight(); ypos++) {
                    model.getBlurredImage().setRGB(xpos, ypos, color.getRGB());
                    model.blurredImageChanged();
                }
            }
        }
    }

    public MultiThreadBlurrer(ImageModel model) {
        super(model);
    }

    @Override
    public void blur(int radius) {
        makeTasks(radius, 100);
        blankImage(Color.black);
        BlurringThread[] threads = new BlurringThread[8];
        for(int numThread = 0; numThread < 8; numThread++) {
            threads[numThread] = new BlurringThread();
        }
        for(BlurringThread thread : threads) {
            thread.start();
        }
    }

    private void makeTasks(int radius, int size) {
        tasks = new LinkedList<>();
        for(int i=0; i<model.getSourceImage().getWidth(); i+=size) {
            for(int j=0; j<model.getSourceImage().getHeight(); j+=size) {
                tasks.add(new BlurTask(i, j, size, size, radius));
            }
        }
    }
}
