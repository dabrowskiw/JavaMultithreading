package de.htw_berlin.multithreading.blur.view;

import de.htw_berlin.multithreading.blur.model.ImageModel;
import de.htw_berlin.multithreading.blur.model.ImageModelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BlurredPanel extends JPanel implements ImageModelListener {
    private ImageModel model;

    public BlurredPanel(ImageModel model) {
        this.model = model;
        setPreferredSize(new Dimension(model.getBlurredImage().getWidth(), model.getBlurredImage().getHeight()));
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(model.getBlurredImage(), 0, 0, null);
    }

    @Override
    public void sourceImageChanged() {

    }

    @Override
    public void blurredImageChanged() {
        repaint();
    }
}
