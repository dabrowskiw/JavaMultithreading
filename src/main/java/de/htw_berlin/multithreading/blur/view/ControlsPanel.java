package de.htw_berlin.multithreading.blur.view;

import de.htw_berlin.multithreading.blur.control.BlurController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlsPanel extends JPanel {
    private BlurController controller;
    private JButton blurButton;

    public ControlsPanel(BlurController controller) {
        this.controller = controller;
        initGUI();
    }

    private void initGUI() {
        blurButton = new JButton("Blur!");
        add(blurButton);
        blurButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.blur();
            }
        });
    }
}
