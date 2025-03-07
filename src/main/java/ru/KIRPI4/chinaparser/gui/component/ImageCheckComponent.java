package ru.KIRPI4.chinaparser.gui.component;

import lombok.Getter;
import ru.KIRPI4.chinaparser.http.Client;
import ru.KIRPI4.chinaparser.http.models.ImageCheckModel;

import javax.accessibility.Accessible;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;


public class ImageCheckComponent extends JComponent implements SwingConstants, Accessible {
    private static final Dimension BACKGROUND_SIZE = new Dimension(310, 155);
    private static final Dimension BLOCK_SIZE = new Dimension(45, 45);

    private final JSlider slider = new JSlider();
    private final ImageComponent blockImage;
    private final ImageComponent backgroundImage;

    @Getter
    private final String id;


    public ImageCheckComponent(ImageCheckModel model) {
        id = model.data.id;

        setSize(BACKGROUND_SIZE.width, BACKGROUND_SIZE.height + slider.getSize().height + 30);

        blockImage = new ImageComponent(model.data.blockImage);
        blockImage.setSize(BLOCK_SIZE);
        blockImage.setLocation(0, model.data.blockY);

        backgroundImage = new ImageComponent(model.data.backgroundImage);
        backgroundImage.setSize(BACKGROUND_SIZE);
        backgroundImage.add(blockImage);

        slider.setMinimum(0);
        slider.setMaximum(getWidth() - blockImage.getWidth());
        slider.setValue(slider.getMinimum());
        slider.setOrientation(JSlider.HORIZONTAL);
        slider.setSize(getWidth()-getBlockImageHalf()*2, 45);
        slider.setLocation(getBlockImageHalf(), backgroundImage.getHeight());
        slider.addChangeListener(this::onSliderChanged);

        add(backgroundImage);
        add(slider);
    }

    public ImageCheckComponent() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        this(Client.getImageCheckData());
    }

    public int getValue() {
        return slider.getValue();
    }

    private void onSliderChanged(ChangeEvent event) {
        blockImage.setLocation(getValue(), blockImage.getLocation().y);
    }

    private int getBlockImageHalf() {
        return blockImage.getWidth()/2-10;
    }
}
