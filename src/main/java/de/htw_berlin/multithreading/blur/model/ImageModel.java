package de.htw_berlin.multithreading.blur.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class ImageModel {
    private BufferedImage sourceImage;
    private BufferedImage blurredImage;
    private LinkedList<ImageModelListener> listeners;
    private HashMap<String, AbstractBlurrer> blurrers;

    public ImageModel() {
        sourceImage = loadSourceImage(new File("res/kitten.png"));
        blurredImage = makeBlurredImage();
        listeners = new LinkedList<>();
        blurrers = new HashMap<>();
        blurrers.put("Simple non-threaded blurrer", new SimpleBlurrer(this));
        blurrers.put("Single-threaded blurrer", new SingleThreadBlurrer(this));
        blurrers.put("Multi-threaded blurrer", new MultiThreadBlurrer(this));
        blurrers.put("Multi-threaded synchronized blurrer", new MultiThreadSynchronisedBlurrer(this));
        blurrers.put("Multi-threaded blurrer (using pop)", new MultiThreadPopBlurrer(this));
    }

    /**
     * Returns the list of avaialble blurrers (to be passed to runBlurrer as name). Names are taken from the internal blurrers list (initialized in Contructor).
     * @return
     */
    public Collection<String> getBlurrerNames() {
        return blurrers.keySet();
    }

    /**
     * Runs the given blurrer (for name: see getBlurrerNames()) to blur the source image and notifies image listeners.
     * @param name
     * @param radius
     */
    public void runBlurrer(String name, int radius) {
        blurrers.get(name).blur(radius);
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
