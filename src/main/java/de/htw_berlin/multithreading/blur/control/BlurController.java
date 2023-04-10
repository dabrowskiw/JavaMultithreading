package de.htw_berlin.multithreading.blur.control;

import de.htw_berlin.multithreading.blur.model.ImageModel;
import de.htw_berlin.multithreading.blur.model.SimpleBlurrer;

public class BlurController {
    private ImageModel model;

    public BlurController(ImageModel model) {
        this.model = model;
    }

    public void blur() {
        model.runBlurrer(new SimpleBlurrer(model, 3));
    }
}
