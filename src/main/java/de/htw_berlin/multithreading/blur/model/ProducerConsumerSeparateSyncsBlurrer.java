package de.htw_berlin.multithreading.blur.model;

import java.awt.*;

public class ProducerConsumerSeparateSyncsBlurrer extends MultithreadedBlurrer {

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
                        tasks.notifyAll();
                    }
                    int[] blurredPixels = getBlurredPixels(task);
                    synchronized(model.getBlurredImage()) {
                        drawBlurredPixels(task, blurredPixels);
                    }
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void blurPixels(BlurTask task) {
        for (int xpos = task.getX(); xpos < task.getX()+ task.getWidth(); xpos++) {
            for (int ypos = task.getY(); ypos < task.getY()+ task.getHeight(); ypos++) {
                int blurredPixel = getBlurredPixel(xpos, ypos, task.getRadius());
                synchronized (model.getBlurredImage()) {
                    model.getBlurredImage().setRGB(xpos, ypos, blurredPixel);
                }
                model.blurredImageChanged();
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
            productionDone = true;
        }
    }

    public ProducerConsumerSeparateSyncsBlurrer(ImageModel model) {
        super(model);
    }

    @Override
    public void blur(int radius) {
        blankImage(Color.black);
        BlurringThread[] threads = new BlurringThread[8];
        productionDone = false;
        TaskProducer producer = new TaskProducer(radius);
        for(int numThread = 0; numThread < 8; numThread++) {
            threads[numThread] = new BlurringThread();
        }
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
                synchronized (tasks) {
                    tasks.add(task);
                    tasks.notifyAll();
                    synchronized (model.getBlurredImage()) {
                        markRegion(task, Color.red);
                    }
                }
            }
        }
    }

}
