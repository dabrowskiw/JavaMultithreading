package de.htw_berlin.multithreading.blur.model;

import java.awt.*;

public class ProducerConsumerTwoSyncsBlurrer extends MultithreadedBlurrer {

    private boolean productionDone = false;

    private class BlurringThread extends Thread {
        public void run() {
            BlurTask task = null;
            try {
                while (!tasks.isEmpty() || !productionDone) {
                    synchronized (tasks) {
                        while (tasks.isEmpty()) {
                            tasks.wait();
                        }
                        task = tasks.getFirst();
                        tasks.removeFirst();
                        int[] blurredPixels = getBlurredPixels(task);
                        synchronized(model.getBlurredImage()) {
                            drawBlurredPixels(task, blurredPixels);
                        }
                    }
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
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
            productionDone = false;
            makeTasks(radius, 100);
            productionDone = true;
        }
    }

    public ProducerConsumerTwoSyncsBlurrer(ImageModel model) {
        super(model);
    }

    @Override
    public void blur(int radius) {
        blankImage(Color.black);
        BlurringThread[] threads = new BlurringThread[8];
        for(int numThread = 0; numThread < 8; numThread++) {
            threads[numThread] = new BlurringThread();
        }
        TaskProducer producer = new TaskProducer(radius);
        for(BlurringThread thread : threads) {
            thread.start();
        }
        new Thread(producer).start();
    }

    protected void makeTasks(int radius, int size) {
        synchronized (tasks) {
            tasks.clear();
        }
        for(int i=0; i<model.getSourceImage().getWidth(); i+=size) {
            for(int j=0; j<model.getSourceImage().getHeight(); j+=size) {
                BlurTask task = new BlurTask(i, j, size, size, radius);
                synchronized (model.getBlurredImage()) {
                    markRegion(task, Color.red);
                    synchronized (tasks) {
                        tasks.add(task);
                        tasks.notifyAll();
                    }
                }
            }
        }
    }

}
