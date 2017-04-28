/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.view;

import com.simmeringc.websitePoller.model.Cache;
import com.simmeringc.websitePoller.model.Node;
import static com.simmeringc.websitePoller.common.URLify.buildURL;
import static com.simmeringc.websitePoller.controller.PageRequester.getHTML;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainWindow {

    //swing references
    JFrame frame;
    JLabel urlFormLabel, emailFormLabel;
    JPanel inputPanel, logPanel, trackerPanel;
    JTextField urlForm, emailForm;
    JButton enterButton, helpButton;
    JScrollPane scroll;

    //instantiate systemLog helper
    SystemLog systemLog = new SystemLog();

    //instantiate cache model
    Cache cache = new Cache();

    public static void main(String[] args) {
        //take the menu bar off the JFrame
        System.setProperty("apple.laf.useScreenMenuBar", "false");

        //set the name of the application menu item
        System.setProperty("apple.awt.application.name", "SnapLogic Website Poller");

        //build the GUI
        new MainWindow().buildGUI();
    }

    public void buildGUI() {

        //create frame - set frame parameters
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(809, 601);
        frame.setTitle("SnapLogic Website Poller");
        frame.setResizable(false);

        //create panel component containers
        inputPanel = new JPanel();
        logPanel = new JPanel();
        trackerPanel = new JPanel();

        //component: URL input form
        urlFormLabel = new JLabel("URL:");
        urlForm = new JTextField(16);
        urlForm.addKeyListener(new FormListener());

        //component: email input form
        emailFormLabel = new JLabel("email:");
        emailForm = new JTextField(16);
        emailForm.addKeyListener(new FormListener());

        //component: enter button
        enterButton = new JButton("Enter");
        enterButton.addActionListener(new EnterButtonListener());

        //component: help button
        helpButton = new JButton("Help");
        helpButton.addActionListener(new HelpButtonListener());

        //append components to inputPanel
        inputPanel.add(urlFormLabel);
        inputPanel.add(urlForm);
        inputPanel.add(emailFormLabel);
        inputPanel.add(emailForm);
        inputPanel.add(enterButton);
        inputPanel.add(helpButton);

        //component: text-scroll box inheriting from systemLog
        scroll = new JScrollPane(systemLog.getSystemLogTextArea());

        //append components to logPanel - show tutorial
        logPanel.add(scroll);
        systemLog.help();

        //append panels to frame - swing border layout
        frame.getContentPane().add(BorderLayout.SOUTH, inputPanel);
        frame.getContentPane().add(BorderLayout.CENTER, logPanel);
        frame.getContentPane().add(BorderLayout.NORTH, trackerPanel);
        frame.setVisible(true);
    }

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

    class EnterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String url = buildURL(urlForm.getText());
            String html = null;
            urlForm.setText(url);
            try {
                html = getHTML(url);
                systemLog.htmlGetSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Node page = new Node(url, html);
            cache.put(url, page);
            System.out.println(cache.getPageMap());
        }
    }

    class HelpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            systemLog.help();
        }
    }
}



