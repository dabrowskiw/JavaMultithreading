package de.htw_berlin.multithreading.blur.model;

import java.awt.*;
import java.util.LinkedList;

public class MultiThreadPopBlurrer extends MultithreadedBlurrer {

    private LinkedList<BlurTask> tasks = new LinkedList<>();

    private class BlurringThread extends Thread {
        public void run() {
            BlurTask task = null;
            while(!tasks.isEmpty()) {
                task = tasks.pop();
                markRegion(task, Color.red);
                int[] blurredPixels = getBlurredPixels(task);
                drawBlurredPixels(task, blurredPixels);
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

}
