package ru.KIRPI4.chinaparser.gui;

import ru.KIRPI4.chinaparser.ChinaParser;
import ru.KIRPI4.chinaparser.gui.component.ImageCheckComponent;
import ru.KIRPI4.chinaparser.http.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class JoinGui extends JFrame {
    private static final String HEADER = "Join";
    private static final Dimension SIZE = new Dimension(326, 500);

    private final JTextField emailTextField = new JTextField();
    private final JLabel emailLabel = new JLabel("email:");
    private final JTextField passwordTextField = new JTextField();
    private final JLabel passwordLabel = new JLabel("password:");

    private final JButton submitButton = new JButton("Join");

    private ImageCheckComponent imageCheck = new ImageCheckComponent();


    public JoinGui() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        super(HEADER);

        setSize(SIZE);
        setResizable(false);


        submitButton.setSize(getSize().width-31, 45);
        submitButton.setLocation(8, getSize().height-submitButton.getSize().height-40);
        submitButton.addActionListener(this::onSubmitClick);

        emailLabel.setSize(getSize().width, 15);
        emailLabel.setLocation(5, imageCheck.getSize().height+20);
        emailTextField.setSize(getSize().width, 35);
        emailTextField.setLocation(0, emailLabel.getLocation().y+emailLabel.getSize().height+5);

        passwordLabel.setSize(getSize().width, 15);
        passwordLabel.setLocation(5, emailTextField.getLocation().y+emailTextField.getSize().height+20);
        passwordTextField.setSize(getSize().width, 35);
        passwordTextField.setLocation(0, passwordLabel.getLocation().y+passwordLabel.getSize().height+5);


        add(emailTextField);
        add(emailLabel);
        add(passwordTextField);
        add(passwordLabel);
        add(imageCheck);
        add(submitButton);

        setLayout(null);
        setVisible(true);
    }

    private void refreshImageCheck() {
        try {
            imageCheck.setVisible(false);
            remove(imageCheck);
            imageCheck = new ImageCheckComponent();
            add(imageCheck);
        } catch (Exception e) {
            ChinaParser.onError(e);
        }
    }

    private void onSubmitClick(ActionEvent event) {
        try {
            var imageCheckResult = Client.submitImageCheck(imageCheck.getValue(), imageCheck.getId());

            if (!imageCheckResult.isSuccess) {
                refreshImageCheck();
                return;
            }

            var authenticationResult = Client.submitAuthentication(emailTextField.getText(), passwordTextField.getText(), imageCheckResult.sliderKeySuccess, Client.getRsaPublicKey().key);

            if (!authenticationResult.isSuccess) {
                refreshImageCheck();
                return;
            }

            Client.login(authenticationResult.data.code);

            new MainGui();

        } catch (Exception e) {
            ChinaParser.onError(e);
            return;
        }


        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
