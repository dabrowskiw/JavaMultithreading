package de.htw_berlin.multithreading.blur.view;

import de.htw_berlin.multithreading.blur.control.BlurController;
import de.htw_berlin.multithreading.blur.model.ImageModel;

import javax.swing.*;
import java.awt.*;

public class BlurMainFrame extends JFrame {
    private BlurredPanel blurred;
    private ImageModel model;
    private BlurController controller;

    public BlurMainFrame(ImageModel model, BlurController controller) {
        this.model = model;
        this.controller = controller;
        initGUI();
        setVisible(true);
    }

    private void initGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 500));
        GridBagConstraints c = new GridBagConstraints();
        getContentPane().setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        getContentPane().add(new JLabel(new ImageIcon(model.getSourceImage())), c);

        blurred = new BlurredPanel(model);
        model.addImageListener(blurred);
        c.gridx ++;
        getContentPane().add(blurred, c);
        c.gridy ++;
        c.gridx = 0;
        c.gridwidth = 2;
        getContentPane().add(new ControlsPanel(controller), c);
        pack();
    }

}
