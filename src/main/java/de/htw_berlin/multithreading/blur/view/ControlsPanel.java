package de.htw_berlin.multithreading.blur.view;

import de.htw_berlin.multithreading.blur.control.BlurController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlsPanel extends JPanel {
    private BlurController controller;
    private JButton blurButton;
    private JComboBox<String> blurMethod;
    private JSpinner radius;

    public ControlsPanel(BlurController controller) {
        this.controller = controller;
        initGUI();
    }

    private void initGUI() {
        add(new JLabel("Method:"));
        blurMethod = new JComboBox<>();
        for(String blurrerName : controller.getBlurrerNames()) {
            blurMethod.addItem(blurrerName);
        }
        add(blurMethod);
        add(new JLabel("Radius:"));
        radius = new JSpinner(new SpinnerNumberModel(3, 1, 100, 1));
        add(radius);
        blurButton = new JButton("Blur!");
        add(blurButton);
        blurButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.blur((String)blurMethod.getSelectedItem(), (Integer)radius.getValue());
            }
        });
    }
}
