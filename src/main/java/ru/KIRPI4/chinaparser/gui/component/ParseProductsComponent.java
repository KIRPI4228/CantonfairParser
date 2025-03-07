package ru.KIRPI4.chinaparser.gui.component;

import ru.KIRPI4.chinaparser.ChinaParser;
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
import java.util.HashSet;
import java.util.Scanner;

public class ParseProductsComponent extends JComponent implements Accessible, SwingConstants {
    private static final Dimension SIZE = new Dimension(300, 400);


    private final JLabel headerLabel = new JLabel("Parse products");
    private final JButton browseListFileButton = new JButton("Browse urls file list");
    private final JButton parseButton = new JButton("Parse");
    private final JFileChooser openFileChooser = new JFileChooser();
    private final JFileChooser saveFileChooser = new JFileChooser();

    public ParseProductsComponent() {
        setSize(SIZE);

        openFileChooser.setCurrentDirectory(new java.io.File("."));
        openFileChooser.setSelectedFile(new File(""));
        openFileChooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        saveFileChooser.setCurrentDirectory(new java.io.File("."));
        saveFileChooser.setSelectedFile(new File(""));
        saveFileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);


        headerLabel.setSize(getWidth(), 20);
        headerLabel.setLocation(55, 30);

        browseListFileButton.setSize(140, 40);
        browseListFileButton.setFont(new Font("Arial", Font.PLAIN, 11));
        browseListFileButton.setFocusPainted(false);
        browseListFileButton.setLocation(30, headerLabel.getY()+headerLabel.getHeight()+30);
        browseListFileButton.addActionListener(this::onBrowseClick);


        parseButton.setSize(140, 40);
        parseButton.setFont(new Font("Arial", Font.PLAIN, 18));
        parseButton.setFocusPainted(false);
        parseButton.setLocation(30, browseListFileButton.getY()+browseListFileButton.getHeight()+10);
        parseButton.addActionListener(this::onParseClick);

        add(headerLabel);
        add(browseListFileButton);
        add(parseButton);
    }

    private void onBrowseClick(ActionEvent event) {
        openFileChooser.showOpenDialog(this);
    }

    private void onParseClick(ActionEvent event) {
        if (openFileChooser.getSelectedFile() == null) {
            JOptionPane.showMessageDialog(this, "Choose list file please", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        try {
            if (saveFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                var scanner = new Scanner(new FileInputStream(openFileChooser.getSelectedFile()));

                var saveFile = saveFileChooser.getSelectedFile();

                var productsInfoModules = new HashSet<ProductInfoModel>();

                while (scanner.hasNext()) {
                    var parts = scanner.nextLine().split("/");

                    productsInfoModules.add(Client.getProductInfo(parts[parts.length-1].split("\\?")[0]));
                }

                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                }

                var outputStream = new FileOutputStream(saveFile);

                ExcelManager.saveProductsInfo(productsInfoModules, outputStream);

                outputStream.close();

                JOptionPane.showMessageDialog(this, "Successfully parsed", "Confirm", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            ChinaParser.onError(e);
        }
    }
}
