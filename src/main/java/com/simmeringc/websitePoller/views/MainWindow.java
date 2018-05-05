/**
 * Created by simmeringc on 4/28/17.
 *
 * main thread and parent GUI
 */

package com.simmeringc.websitePoller.views;

import com.simmeringc.websitePoller.controllers.WebPoller;
import com.simmeringc.websitePoller.controllers.WebRequester;

import static com.simmeringc.websitePoller.views.InputVerifier.verifyInput;
import static com.simmeringc.websitePoller.views.SystemLog.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.*;

public class MainWindow {

    //save first HTML get() from validation get()
    private String oldHtml;

    //website counter
    public static int trackerNumber = 0;

    //keep track of threads that are executing runnable in WebPoller
    public static ArrayList<ScheduledExecutorService> executorThreads = new ArrayList<>();

    //keep track of TrackerTiles
    public static ArrayList<TrackerTile> trackerTiles = new ArrayList<>();

    //instantiate systemLog systemLogHelper
    private SystemLog systemLog = new SystemLog();

    //swing scope references
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu debug, dummyData;
    private JMenuItem showThreads, googleFill, redditNewFill, redditCSCareerQuestionsFill, stackOverflowHot, gitHubRawText, hackerNewsNew;
    private JLabel urlFormLabel, emailFormLabel, thresholdFormLabel, pollIntervalFormLabel;
    public JPanel inputPanel, logPanel, trackerPanel;
    private JTextField urlForm, emailForm, thresholdForm, pollIntervalForm;
    private JButton enterButton, helpButton;
    private JScrollPane systemLogPanel, trackerScrollContainer;

    public static void main(String[] args) {
        //take the menu bar off the JFrame
        System.setProperty("apple.laf.useScreenMenuBar", "True");

        //set the name of the application menu item
        System.setProperty("apple.awt.application.name", "Simmeringc WebPoller");

        //build the GUI
        new MainWindow().buildGUI();
    }

    public void buildGUI() {

        //create frame - set frame parameters
        frame = new JFrame();

        //create menu bar dialog
        menuBar = new JMenuBar();
        debug = new JMenu("Debug");
        dummyData = new JMenu("Dummy data");

        showThreads = new JMenuItem("Show threads");
        showThreads.addActionListener(new ShowThreadsListener());

        stackOverflowHot = new JMenuItem("https://stackoverflow.com/?tab=hot");
        stackOverflowHot.addActionListener(new StackOverflowHotListener());

        hackerNewsNew = new JMenuItem("https://news.ycombinator.com/newest");
        hackerNewsNew.addActionListener(new HackerNewsNewListener());

        googleFill = new JMenuItem("https://www.google.com");
        googleFill.addActionListener(new GoogleFillListener());

        gitHubRawText = new JMenuItem("https://github.com/simmeringc/archive-simmeringc.github.io");
        gitHubRawText.addActionListener(new GitHubRawTextListener());

        dummyData.add(stackOverflowHot);
        dummyData.add(hackerNewsNew);
        dummyData.add(googleFill);
        dummyData.add(gitHubRawText);
        debug.add(dummyData);
        debug.add(showThreads);
        menuBar.add(debug);

        //create panel component containers
        inputPanel = new JPanel();
        logPanel = new JPanel();

        //trackerPanel holds trackerTiles inside the trackerScrollContainer panel
        trackerPanel = new JPanel();
        trackerPanel.setLayout(new BoxLayout(trackerPanel, BoxLayout.Y_AXIS));
        trackerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.GRAY), "Tracking " + 0 + " websites"));
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
        helpButton.addActionListener(new HelpButtonListener());

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

        //append panels to frame - frame parameters
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(809, 601);
        frame.setTitle("Simmeringc WebPoller");
        frame.setResizable(false);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.SOUTH, inputPanel);
        frame.getContentPane().add(BorderLayout.CENTER, logPanel);
        frame.getContentPane().add(BorderLayout.NORTH, trackerScrollContainer);
        frame.setVisible(true);
    }

    public void reDraw() {
        frame.validate();
        frame.repaint();
    }

    public String getUrlFormText() {
        return urlForm.getText();
    }

    public String getEmailFormText() {
        return emailForm.getText();
    }

    public String getThresholdFormText() {
        return thresholdForm.getText();
    }

    public String getPollIntervalFormText() {
        return pollIntervalForm.getText();
    }

    //trackerCounter increment-redraw, decrement in TrackerTile
    public void incrementTrackerPanelCounter() {
        trackerNumber++;
        if (trackerNumber == 1) {
            trackerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.GRAY), "Tracking " + trackerNumber + " website"));
        } else {
            trackerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.GRAY), "Tracking " + trackerNumber + " websites"));
        }
    }

    //create new thread for interval content analysis
    public WebPoller createPoller() {
        double thresholdPercent = Double.parseDouble(getThresholdFormText());
        long interval = Long.parseLong(getPollIntervalFormText());
        WebPoller pollerRunnable = new WebPoller(getUrlFormText(), getEmailFormText(), oldHtml, thresholdPercent, trackerNumber);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executorThreads.add(executor);
        executor.scheduleAtFixedRate(pollerRunnable, 0, interval, TimeUnit.SECONDS);

        return pollerRunnable;
    }

    //method: called from EnterButtonListener to create and append TrackerTile to TrackerPanel in GUI
    public void addTrackerTile() {
        JSeparator jSeparator = new JSeparator();
        jSeparator.setMaximumSize(new Dimension(5, 5));
        jSeparator.setMinimumSize(new Dimension(5, 5));
        WebPoller pollerRunnable = createPoller();
        TrackerTile trackerTile = new TrackerTile(pollerRunnable, trackerPanel, jSeparator, trackerNumber, getUrlFormText(), getEmailFormText(), getThresholdFormText(), getPollIntervalFormText());
        trackerTiles.add(trackerTile);
        trackerPanel.add(trackerTile);
        trackerPanel.add(jSeparator);
        incrementTrackerPanelCounter();
        reDraw();
        systemLogTrackerStarted(getUrlFormText());
    }

    public static Image getImage(final String pathAndFileName) {
        final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
        return Toolkit.getDefaultToolkit().getImage(url);
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

    //inner-class: show active threads in system log
    class ShowThreadsListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            for (ScheduledExecutorService thread : executorThreads) {
                systemLogShowThread(thread);
            }
        }
    }

    //inner-class: input quick test data
    class StackOverflowHotListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            urlForm.setText("https://stackoverflow.com/?tab=hot");
            emailForm.setText("connersimmering@gmail.com");
            thresholdForm.setText("5");
            pollIntervalForm.setText("5");
        }
    }

    //inner-class: input quick test data
    class HackerNewsNewListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            urlForm.setText("https://news.ycombinator.com/newest");
            emailForm.setText("connersimmering@gmail.com");
            thresholdForm.setText("5");
            pollIntervalForm.setText("5");
        }
    }

    //inner-class: input quick test data
    class GoogleFillListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            urlForm.setText("https://www.google.com");
            emailForm.setText("connersimmering@gmail.com");
            thresholdForm.setText("5");
            pollIntervalForm.setText("5");
        }
    }

    //inner-class: input quick test data
    class GitHubRawTextListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            urlForm.setText("https://github.com/simmeringc/archive-simmeringc.github.io");
            emailForm.setText("connersimmering@gmail.com");
            thresholdForm.setText("5");
            pollIntervalForm.setText("5");
        }
    }

    //inner-class: primary call to action
    class EnterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String url = getUrlFormText();
            String email = getEmailFormText();
            String threshold = getThresholdFormText();
            String interval = getPollIntervalFormText();
            try {
                verifyInput(url, email, threshold, interval);
                try {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Future getHtml = executor.submit(new WebRequester(url));
                    oldHtml = getHtml.get().toString();
                    addTrackerTile();
                    systemLogHtmlGetSuccessful(url);
                } catch (Exception ex) {
                    systemLogHtmlGetFailedTryAgain();
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //inner-class: displays help intro message
    class HelpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            systemLogHelp();
        }
    }
}



