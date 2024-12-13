package ru.KIRPI4.chinaparser.gui.component;

import javax.swing.*;
import java.awt.*;
import java.util.Base64;

public class ImageComponent extends JLabel {
    public ImageComponent() {

    }

    public ImageComponent(Image image) {
        super(new ImageIcon(image));
    }

    public ImageComponent(String base64) {
        super(new ImageIcon(Base64.getDecoder().decode(base64)));
    }
}
