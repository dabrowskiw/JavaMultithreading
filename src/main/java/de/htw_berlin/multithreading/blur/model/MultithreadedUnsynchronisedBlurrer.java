package de.htw_berlin.multithreading.blur.model;

import java.awt.*;
import java.util.LinkedList;

public class MultithreadedUnsynchronisedBlurrer extends MultithreadedBlurrer {

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
    }

    public MultithreadedUnsynchronisedBlurrer(ImageModel model) {
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
}
