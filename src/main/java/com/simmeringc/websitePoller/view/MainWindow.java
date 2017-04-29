/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.view;

import com.simmeringc.websitePoller.controller.WebPoller;
import com.simmeringc.websitePoller.model.Cache;
import com.simmeringc.websitePoller.model.Node;
import static com.simmeringc.websitePoller.view.InputVerifier.verifyInput;
import static com.simmeringc.websitePoller.controller.WebRequester.getHtml;
import static com.simmeringc.websitePoller.view.SystemLog.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URI;
import java.awt.Desktop; //flagged for old API usage
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainWindow {

    //swing scope references
    JFrame frame;
    JLabel urlFormLabel, emailFormLabel, thresholdFormLabel, pollIntervalFormLabel;
    JPanel inputPanel, logPanel, trackerPanel, trackerTile;
    JTextField urlForm, emailForm, thresholdForm, pollIntervalForm;
    JButton enterButton, helpButton;
    JScrollPane systemLogPanel, trackerScrollContainer;

    //website counter
    public static int trackerNumber = 0;

    //instantiate systemLog systemLogHelper
    SystemLog systemLog = new SystemLog();

    //instantiate cache model
    Cache cache = new Cache();

    public static void main(String[] args) {
        //take the menu bar off the JFrame
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        //set the name of the application menu item
        System.setProperty("apple.awt.application.name", "SnapLogic Website WebPoller");

        //build the GUI
        new MainWindow().buildGUI();
    }

    public void buildGUI() {

        //create frame - set frame parameters
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(809, 601);
        frame.setTitle("SnapLogic Website WebPoller");
        frame.setResizable(false);

        //create panel component containers
        inputPanel = new JPanel();
        logPanel = new JPanel();

        //trackerPanel holds trackerTiles inside the trackerScrollContainer panel
        trackerPanel = new JPanel();
        trackerPanel.setLayout(new BoxLayout(trackerPanel, BoxLayout.Y_AXIS));
        trackerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.GRAY), "Websites being tracked: " + 0 + " "));
        trackerScrollContainer = new JScrollPane(trackerPanel);
        trackerScrollContainer.setPreferredSize(new Dimension(500, 400));
        trackerScrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        trackerScrollContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //component: URL input form
        urlFormLabel = new JLabel("URL:");
        urlForm = new JTextField(12);
        urlForm.addKeyListener(new FormListener());

        //component: email input form
        emailFormLabel = new JLabel("Email:");
        emailForm = new JTextField(12);
        emailForm.addKeyListener(new FormListener());

        //component: change-threshold input form
        thresholdFormLabel = new JLabel("Threshold (%):");
        thresholdForm = new JTextField(2);
        thresholdForm.addKeyListener(new FormListener());

        //component: poll-interval input form
        pollIntervalFormLabel = new JLabel("Interval (s):");
        pollIntervalForm = new JTextField(2);
        pollIntervalForm.addKeyListener(new FormListener());

        //component: enter button
        enterButton = new JButton("Enter");
        enterButton.addActionListener(new EnterButtonListener());

        //component: systemLogHelp button
        helpButton = new JButton("Help");
        helpButton.addActionListener(new SystemLogHelpButtonListener());

        //append components to inputPanel
        inputPanel.add(urlFormLabel);
        inputPanel.add(urlForm);
        inputPanel.add(emailFormLabel);
        inputPanel.add(emailForm);
        inputPanel.add(thresholdFormLabel);
        inputPanel.add(thresholdForm);
        inputPanel.add(pollIntervalFormLabel);
        inputPanel.add(pollIntervalForm);
        inputPanel.add(enterButton);
        inputPanel.add(helpButton);

        //component: scroll panel inheriting from systemLog
        systemLogPanel = new JScrollPane(systemLog.getSystemLogTextArea());

        //append components to logPanel - show tutorial
        logPanel.add(systemLogPanel);
        systemLogHelp();

        //append panels to frame
        frame.getContentPane().add(BorderLayout.SOUTH, inputPanel);
        frame.getContentPane().add(BorderLayout.CENTER, logPanel);
        frame.getContentPane().add(BorderLayout.NORTH, trackerScrollContainer);
        frame.setVisible(true);
    }

    //trackerScrollContainer counter redraw
    public void incrementTrackerPanelCounter() {
        trackerNumber++;
        trackerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.GRAY), "Websites being tracked: " + trackerNumber + " "));
    }

    public void createPoller() {
        int thresholdPercent = Integer.parseInt(thresholdForm.getText());
        long interval = Long.parseLong(pollIntervalForm.getText());

        WebPoller pollerRunnable = new WebPoller(urlForm.getText(), trackerNumber, thresholdPercent);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(pollerRunnable, 0, interval, TimeUnit.SECONDS);
    }

    //method: called from EnterButtonListener to create and append TrackerTile to TrackerPanel in GUI
    public void addTrackerTile() {
        incrementTrackerPanelCounter();
        createPoller();

        trackerTile = new JPanel();
        trackerTile.setPreferredSize(new Dimension(760, 90));
        trackerTile.setMaximumSize(new Dimension(760, 90));
        trackerTile.setMinimumSize(new Dimension(760, 90));
        trackerTile.setBorder(BorderFactory.createEtchedBorder());
        trackerTile.setBackground(Color.LIGHT_GRAY);

        JButton urlButton = new JButton();
        urlButton.setText("<HTML>URL: <FONT color=\"#000099\"><U>"+ urlForm.getText() +"</U></FONT></HTML>");
        urlButton.setHorizontalAlignment(SwingConstants.LEFT);
        urlButton.setBorderPainted(false);
        urlButton.setOpaque(false);
        urlButton.setBackground(Color.WHITE);
        urlButton.addActionListener(new UrlButtonListener());

        JButton emailButton = new JButton();
        emailButton.setText("<HTML>Email: <FONT color=\"#000099\"><U>"+ emailForm.getText() +"</U></FONT></HTML>");
        emailButton.setHorizontalAlignment(SwingConstants.LEFT);
        emailButton.setBorderPainted(false);
        emailButton.setOpaque(false);
        emailButton.setBackground(Color.WHITE);
        emailButton.addActionListener(new EmailButtonListener());

        JButton thresholdText = new JButton();
        thresholdText.setText("<HTML>Threshold: <U>"+ thresholdForm.getText() +"</U></FONT></HTML>");
        thresholdText.setHorizontalAlignment(SwingConstants.LEFT);
        thresholdText.setBorderPainted(false);
        thresholdText.setOpaque(false);
        thresholdText.setBackground(Color.WHITE);

        JButton intervalText = new JButton();
        intervalText.setText("<HTML>Interval: <U>"+ pollIntervalForm.getText() +"</U></FONT></HTML>");
        intervalText.setHorizontalAlignment(SwingConstants.LEFT);
        intervalText.setBorderPainted(false);
        intervalText.setOpaque(false);
        intervalText.setBackground(Color.WHITE);

        trackerTile.add(urlButton);
        trackerTile.add(emailButton);
        trackerTile.add(thresholdText);
        trackerTile.add(intervalText);

        JSeparator jSeparator = new JSeparator();
        jSeparator.setMaximumSize(new Dimension(5, 5));
        jSeparator.setMinimumSize(new Dimension(5, 5));

        trackerPanel.add(trackerTile);
        trackerPanel.add(jSeparator);

        frame.validate();
        frame.repaint();
    }

    //inner-class: form listener on form fields allows user to hit the enter key to send input to the application
    class FormListener implements KeyListener {
        public void keyPressed(KeyEvent event) {
            if (event.getKeyCode()==KeyEvent.VK_ENTER){
                enterButton.doClick();
            }
        }
        public void keyReleased(KeyEvent event) {
            //abstract method stub
        }
        public void keyTyped(KeyEvent event) {
            //abstract method stub
        }
    }

    //inner-class: primary call to action
    class EnterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String urlFromText = urlForm.getText();
            String url = urlFromText;
            String emailFormText = emailForm.getText();
            String changeThresholdText = thresholdForm.getText();
            String pollIntervalText = pollIntervalForm.getText();
            String html = null;

            try {
                verifyInput(urlFromText, emailFormText, changeThresholdText, pollIntervalText);
                try {
                    html = getHtml(url);
                    systemLogHtmlGetSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Node page = new Node(html);
            cache.put(url, page);
            addTrackerTile();
        }
    }

    //inner-class: displays help intro message
    class SystemLogHelpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            systemLogHelp();
        }
    }

    //inner-class: opens webpage for TrackerTile URL
    class UrlButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            //flagged for old API usage
            if(Desktop.isDesktopSupported())
            {
                try {
                    Desktop.getDesktop().browse(new URI(urlForm.getText()));
                    systemLogOpeningBrowser();
                } catch (Exception ex) {
                    systemLogUriFailed();
                    ex.printStackTrace();
                }
            }
        }
    }

    //inner-class: opens webpage for TrackerTile email
    class EmailButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            //flagged for old API usage
            if(Desktop.isDesktopSupported())
            {
                try {
                    Desktop.getDesktop().mail(new URI("mailto:" + emailForm.getText()));
                    systemLogOpeningMailClient();
                } catch (Exception ex) {
                    systemLogUriFailed();
                    ex.printStackTrace();
                }
            }
        }
    }
}



