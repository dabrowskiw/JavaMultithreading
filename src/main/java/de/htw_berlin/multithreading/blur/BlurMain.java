package de.htw_berlin.multithreading.blur;

import de.htw_berlin.multithreading.blur.control.BlurController;
import de.htw_berlin.multithreading.blur.model.ImageModel;
import de.htw_berlin.multithreading.blur.view.BlurMainFrame;

import javax.swing.*;

public class BlurMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ImageModel model = new ImageModel();
                BlurController controller = new BlurController(model);
                BlurMainFrame mainframe = new BlurMainFrame(model, controller);
                mainframe.setVisible(true);
            }
        });
    }
}
