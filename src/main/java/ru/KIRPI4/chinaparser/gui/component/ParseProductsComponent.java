package ru.KIRPI4.chinaparser.gui.component;

import ru.KIRPI4.chinaparser.ExcelManager;
import ru.KIRPI4.chinaparser.http.Client;
import ru.KIRPI4.chinaparser.http.models.ProductInfoModel;

import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class ParseProductsComponent extends JComponent implements Accessible, SwingConstants {
    private static final Dimension SIZE = new Dimension(300, 400);


    private final JLabel headerLabel = new JLabel("Parse products");
    private final JButton browseListFileButton = new JButton("Browse urls file list");
    private final JButton parseButton = new JButton("Parse");
    private final JFileChooser fileChooser = new JFileChooser();

    private File listFile;

    public ParseProductsComponent() {
        setSize(SIZE);

        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setSelectedFile(new File(""));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);


        headerLabel.setSize(getWidth(), 20);
        headerLabel.setLocation(55, 30);

        browseListFileButton.setSize(140, 40);
        browseListFileButton.setFont(new Font("Airal", 0, 11));
        browseListFileButton.setFocusPainted(false);
        browseListFileButton.setLocation(30, headerLabel.getY()+headerLabel.getHeight()+30);
        browseListFileButton.addActionListener(this::onBrowseClick);


        parseButton.setSize(140, 40);
        parseButton.setFont(new Font("Airal", 0, 18));
        parseButton.setFocusPainted(false);
        parseButton.setLocation(30, browseListFileButton.getY()+browseListFileButton.getHeight()+10);
        parseButton.addActionListener(this::onParseClick);

        add(headerLabel);
        add(browseListFileButton);
        add(parseButton);
    }

    private void onBrowseClick(ActionEvent event) {
        if (fileChooser.showOpenDialog(this) == JFileChooser.OPEN_DIALOG) {
            listFile = fileChooser.getSelectedFile();
        }
    }

    private void onParseClick(ActionEvent event) {
        if (listFile == null) {
            JOptionPane.showMessageDialog(this, "Choose list file please", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        try {
            if (fileChooser.showOpenDialog(this) == JFileChooser.OPEN_DIALOG) {
                var saveFile = fileChooser.getSelectedFile();

                var scanner = new Scanner(new FileInputStream(listFile));

                var productsInfoModules = new HashSet<ProductInfoModel>();

                while (scanner.hasNext()) {
                    var parts = scanner.nextLine().split("/");

                    productsInfoModules.add(Client.getProductInfo(parts[parts.length-1].split("\\?")[0]));
                }

                var outputStream = new FileOutputStream(saveFile);

                ExcelManager.saveProductsInfo(productsInfoModules, outputStream);

                outputStream.close();

                JOptionPane.showMessageDialog(this, "Successfully parsed", "Confirm", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
