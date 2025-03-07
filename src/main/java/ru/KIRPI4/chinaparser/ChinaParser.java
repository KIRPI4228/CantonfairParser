package ru.KIRPI4.chinaparser;

import ru.KIRPI4.chinaparser.gui.JoinGui;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class ChinaParser {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        var joinGui = new JoinGui();
    }

    public static void onError(Exception exception) {
        exception.printStackTrace();
        JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
