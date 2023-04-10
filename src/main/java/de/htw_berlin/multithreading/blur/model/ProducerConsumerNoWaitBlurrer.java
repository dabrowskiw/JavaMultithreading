package de.htw_berlin.multithreading.blur.model;

import java.awt.*;

public class ProducerConsumerNoWaitBlurrer extends MultithreadedBlurrer {

    private class BlurringThread extends Thread {
        public void run() {
            BlurTask task = null;
            while(!tasks.isEmpty()) {
                synchronized (tasks) {
                    task = tasks.getFirst();
                    tasks.removeFirst();
                    int[] blurredPixels = getBlurredPixels(task);
                    drawBlurredPixels(task, blurredPixels);
                }
            }
        }
    }

    private class TaskProducer implements Runnable {
        private int radius;

        public TaskProducer(int radius) {
            this.radius = radius;
        }

        @Override
        public void run() {
            makeTasks(radius, 100);
        }
    }

    public ProducerConsumerNoWaitBlurrer(ImageModel model) {
        super(model);
    }

    @Override
    public void blur(int radius) {
        blankImage(Color.black);
        TaskProducer producer = new TaskProducer(radius);
        new Thread(producer).start();
        BlurringThread[] threads = new BlurringThread[8];
        for(int numThread = 0; numThread < 8; numThread++) {
            threads[numThread] = new BlurringThread();
        }
        for(BlurringThread thread : threads) {
            thread.start();
        }
    }

    protected void makeTasks(int radius, int size) {
        synchronized (tasks) {
            tasks.clear();
        }
        for(int i=0; i<model.getSourceImage().getWidth(); i+=size) {
            for(int j=0; j<model.getSourceImage().getHeight(); j+=size) {
                BlurTask task = new BlurTask(i, j, size, size, radius);
                markRegion(task, Color.red);
                synchronized (tasks) {
                    tasks.add(task);
                }
            }
        }
    }

}
