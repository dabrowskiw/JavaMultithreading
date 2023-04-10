package de.htw_berlin.multithreading.blur.control;

import de.htw_berlin.multithreading.blur.model.ImageModel;

import java.util.Collection;

public class BlurController {
    private ImageModel model;

    public BlurController(ImageModel model) {
        this.model = model;
    }

    public void blur(String name, int radius) {
        model.runBlurrer(name, radius);
    }

    /**
     * Returns the list of avaialble blurrers (to be passed to blur as name).
     * @return
     */
    public Collection<String> getBlurrerNames() {
        return model.getBlurrerNames();
    }
}
