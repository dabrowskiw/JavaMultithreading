package de.htw_berlin.multithreading.blur.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class ImageModel {
    private BufferedImage sourceImage;
    private BufferedImage blurredImage;
    private LinkedList<ImageModelListener> listeners;

    public ImageModel() {
        sourceImage = loadSourceImage(new File("res/kitten.png"));
        blurredImage = makeBlurredImage();
        listeners = new LinkedList<>();
    }

    /**
     * Runs the given blurrer to blur the source image and notifies image listeners.
     * @param blurrer
     */
    public void runBlurrer(AbstractBlurrer blurrer) {
        blurrer.blur();
        blurredImageChanged();
    }

    /**
     * Add listener to be notified of changes in blurred or source image content.
     * @param listener
     */
    public void addImageListener(ImageModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove all occurrences of this listener from the listeners list.
     * @param listener
     */
    public void removeImageListener(ImageModelListener listener) {
        while(listeners.remove(listener));
    }

    /**
     * Notify registered listeners of change in blurred image content.
     */
    public void blurredImageChanged() {
        for(ImageModelListener listener : listeners) {
            listener.blurredImageChanged();
        }
    }

    private BufferedImage makeBlurredImage() {
        BufferedImage res = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = res.createGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, res.getWidth(), res.getHeight());
        return res;
    }

    private BufferedImage loadSourceImage(File image) {
        try {
            return ImageIO.read(image);
        } catch(IOException e) {
            System.err.println("Could not load " + image.getAbsolutePath() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return makeErrorImage(image.getAbsolutePath());
    }

    private BufferedImage makeErrorImage(String filepath) {
        BufferedImage errorImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphics = errorImage.createGraphics();
        imageGraphics.setColor(Color.red);
        imageGraphics.fillRect(0, 0, 500, 500);
        imageGraphics.setColor(Color.black);
        imageGraphics.drawString("Error: Could not load input image from:", 10, 40);
        int linelength = 60;
        for(int pos=0; pos<filepath.length(); pos += linelength) {
            imageGraphics.drawString(filepath.substring(pos, Math.min(filepath.length(), pos+linelength)), 10, 40 +20*(1 + pos / linelength));
        }
        return errorImage;
    }

    public BufferedImage getSourceImage() {
        return sourceImage;
    }

    public BufferedImage getBlurredImage() {
        return blurredImage;
    }
}
