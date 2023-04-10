package de.htw_berlin.multithreading.blur.model;

import java.awt.*;
import java.util.LinkedList;

public class MultiThreadPopBlurrer extends AbstractBlurrer {

    private LinkedList<BlurTask> tasks;

    private class BlurringThread extends Thread {
        public void run() {
            while(!tasks.isEmpty()) {
                BlurTask task = tasks.pop();
                // First, make the part to blur red to make the build-up of the blurring visible
                Graphics g = model.getBlurredImage().getGraphics();
                g.setColor(Color.red);
                g.fillRect(task.getX(), task.getY(), task.getWidth(), task.getHeight());
                model.blurredImageChanged();
                for (int xpos = task.getX(); xpos < task.getX()+task.getWidth(); xpos++) {
                    for (int ypos = task.getY(); ypos < task.getY()+task.getHeight(); ypos++) {
                        model.getBlurredImage().setRGB(xpos, ypos, getBlurredPixel(xpos, ypos, task.getRadius()));
                        model.blurredImageChanged();
                    }
                }
            }
        }
    }

    public MultiThreadPopBlurrer(ImageModel model) {
        super(model);
    }

    @Override
    public void blur(int radius) {
        makeTasks(radius, 100);
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
