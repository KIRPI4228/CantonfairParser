package ru.KIRPI4.chinaparser.gui;

import ru.KIRPI4.chinaparser.http.Client;
import ru.KIRPI4.chinaparser.http.models.ImageCheckModel;
import ru.KIRPI4.chinaparser.http.models.ResultAuthenticationModel;
import ru.KIRPI4.chinaparser.http.models.ResultImageCheckModel;
import ru.KIRPI4.chinaparser.http.models.ResultLoginModel;

import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

public class JoinGui extends JFrame {
    private static final String HEADER = "Join";
    private static final Dimension SIZE = new Dimension(350, 500);

    private final JLabel imageCheckBackgroundImage;
    private final JLabel imageCheckBlockImage;

    private final JTextField emailTextField = new JTextField("email");
    private final JTextField passwordTextField = new JTextField("password");

    private final JButton submitButton = new JButton("Join");
    private final JSlider imageCheckSlider = new JSlider(0, 310, 0);

    private final String imageCheckKey;


    public JoinGui(ImageCheckModel imageCheckModel) {
        super(HEADER);

        this.imageCheckKey = imageCheckModel.data.id;

        this.imageCheckBackgroundImage = new JLabel(new ImageIcon(Base64.getDecoder().decode(imageCheckModel.data.backgroundImage)));
        this.imageCheckBlockImage = new JLabel(new ImageIcon(Base64.getDecoder().decode(imageCheckModel.data.blockImage)));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SIZE);
        setResizable(false);


        submitButton.setSize(getSize().width-31, 45);
        submitButton.setLocation(8, getSize().height-submitButton.getSize().height-40);
        submitButton.addActionListener(this::onSubmitClick);

        imageCheckSlider.setOrientation(JSlider.HORIZONTAL);
        imageCheckSlider.setSize(getSize().width-15, 45);
        imageCheckSlider.setLocation(0, submitButton.getLocation().y - imageCheckSlider.getSize().height);
        imageCheckSlider.addChangeListener(this::onSliderChanged);

        this.imageCheckBackgroundImage.setSize(310, 155);

        this.imageCheckBlockImage.setSize(45, 45);
        this.imageCheckBlockImage.setLocation(0, imageCheckModel.data.blockY);

        this.imageCheckBackgroundImage.add(add(imageCheckBlockImage));

        emailTextField.setSize(getSize().width, 45);
        emailTextField.setLocation(0, imageCheckBackgroundImage.getSize().height+20);
        passwordTextField.setSize(getSize().width, 45);
        passwordTextField.setLocation(0, imageCheckBackgroundImage.getSize().height+emailTextField.getSize().height+40);


        add(emailTextField);
        add(passwordTextField);
        add(imageCheckBackgroundImage);
        add(submitButton);
        add(imageCheckSlider);

        setLayout(null);



        setVisible(true);
    }

    private void onSliderChanged(ChangeEvent event) {
        imageCheckBlockImage.setLocation(imageCheckSlider.getValue(), imageCheckBlockImage.getLocation().y);
    }

    private void onSubmitClick(ActionEvent event) {
        try {
            var imageCheckResult = Client.submitImageCheck(imageCheckBlockImage.getLocation().x, imageCheckKey);

            if (!imageCheckResult.isSuccess) {
                System.out.println("2");
                return;
            }

            var authenticationResult = Client.submitAuthentication(emailTextField.getText(), passwordTextField.getText(), imageCheckResult.sliderKeySuccess, Client.getRsaPublicKey().key);

            if (!authenticationResult.isSuccess) {
                System.out.println("3");
                return;
            }

            var loginResult = Client.login(authenticationResult.data.code);

            System.out.println(loginResult.accessToken);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }


        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
