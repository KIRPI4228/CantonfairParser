package ru.KIRPI4.chinaparser.gui;

import ru.KIRPI4.chinaparser.gui.component.ParseProductsComponent;

import javax.swing.*;
import java.awt.*;

public class MainGui extends JFrame {
    private static final String HEADER = "Parser";
    private static final Dimension SIZE = new Dimension(400, 300);

    private final ParseProductsComponent parseProductsComponent = new ParseProductsComponent();

    public MainGui() {
        super(HEADER);
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        setSize(SIZE);
        setResizable(false);


        add(parseProductsComponent);

        setLayout(null);
        setVisible(true);
    }
}
