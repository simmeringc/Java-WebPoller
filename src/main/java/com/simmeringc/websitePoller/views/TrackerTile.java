/**
 * Created by Conner on 4/29/17.
 *
 * trackerTile GUI with a finniky gridBagLayout
 */

package com.simmeringc.websitePoller.views;

import com.simmeringc.websitePoller.controllers.WebPoller;

import static com.simmeringc.websitePoller.views.MainWindow.executerThreads;
import static com.simmeringc.websitePoller.views.MainWindow.getImage;
import static com.simmeringc.websitePoller.views.SystemLog.*;
import static org.apache.commons.lang3.StringUtils.abbreviate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.awt.Desktop; //flagged for old API usage

public class TrackerTile extends JPanel {

    private String url;
    private String email;
    private String threshold;
    private int trackerNumber;
    private WebPoller poller;

    private JPanel trackerPanel, trackerTile;
    private JSeparator jSeparator;
    private JButton urlButton, emailButton, thresholdText, intervalText, diffButton, terminateButton;
    private JCheckBox enableEmailAlerts;

    public TrackerTile(WebPoller poller, JPanel trackerPanel, JSeparator jSeparator, int trackerNumber, String url, String email, String threshold, String interval) {
        this.url = url;
        this.email = email;
        this.threshold = threshold;
        this.trackerNumber = trackerNumber;
        this.poller = poller;
        this.trackerPanel = trackerPanel;
        this.trackerTile = this;
        this.jSeparator = jSeparator;

        new Timer((Integer.parseInt(interval) * 1000), taskPerformer).start();

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(760, 90));
        setMaximumSize(new Dimension(760, 90));
        setMinimumSize(new Dimension(760, 90));
        setBorder(BorderFactory.createEtchedBorder());
        setBackground(Color.LIGHT_GRAY);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.1;
        c.weightx = 0.5;

        urlButton = new JButton();
        urlButton.setText("<HTML>URL: <FONT color=\"#000099\"><U>" + abbreviate(url, 40) + "</U></FONT></HTML>");
        urlButton.setHorizontalAlignment(SwingConstants.LEFT);
        urlButton.setBorderPainted(false);
        urlButton.setOpaque(false);
        urlButton.setBackground(Color.WHITE);
        urlButton.setToolTipText(url);
        urlButton.addActionListener(new UrlButtonListener());
        c.gridx = 0;
        c.gridy = 0;
        add(urlButton, c);

        emailButton = new JButton();
        emailButton.setText("<HTML>Email: <FONT color=\"#000099\"><U>" + abbreviate(email, 40) + "</U></FONT></HTML>");
        emailButton.setHorizontalAlignment(SwingConstants.LEFT);
        emailButton.setBorderPainted(false);
        emailButton.setOpaque(false);
        emailButton.setBackground(Color.WHITE);
        emailButton.setToolTipText(email);
        emailButton.addActionListener(new EmailButtonListener());
        c.gridx = 0;
        c.gridy = 1;
        add(emailButton, c);

        thresholdText = new JButton();
        thresholdText.setText("<HTML>Threshold: <U>" + abbreviate(threshold, 5) + "%</U></FONT></HTML>");
        thresholdText.setHorizontalAlignment(SwingConstants.LEFT);
        thresholdText.setBorderPainted(false);
        thresholdText.setOpaque(false);
        thresholdText.setBackground(Color.WHITE);
        thresholdText.setToolTipText(threshold + " percent");
        c.gridx = 0;
        c.gridy = 2;
        add(thresholdText, c);

        intervalText = new JButton();
        intervalText.setText("<HTML>Interval: <U>" + abbreviate(interval, 5) + "s</U></FONT></HTML>");
        intervalText.setHorizontalAlignment(SwingConstants.LEFT);
        intervalText.setBorderPainted(false);
        intervalText.setOpaque(false);
        intervalText.setBackground(Color.WHITE);
        intervalText.setToolTipText(interval + " seconds");
        c.gridx = 0;
        c.gridy = 3;
        add(intervalText, c);

        diffButton = new JButton();
        diffButton.setText("<HTML>Diff: <FONT color=\"#3CB371\" size=14px><U>" + 0.00 + "%</U></FONT></HTML>");
        diffButton.setToolTipText(String.valueOf(poller.getPreProcessedPercentDiff() + "% diff, click to view"));
        diffButton.addActionListener(new DiffButtonListener());
        c.gridx = 2;
        c.gridy = 3;
        add(diffButton, c);

        terminateButton = new JButton();
        terminateButton.setText("Terminate");
        terminateButton.setToolTipText(String.valueOf("end this tracker"));
        terminateButton.addActionListener(new TerminateButtonListener());
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 0.1;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        add(terminateButton, c);

        enableEmailAlerts = new JCheckBox("Enable Email Alerts");
        enableEmailAlerts.setSelected(true);
        enableEmailAlerts.setToolTipText(String.valueOf("check to enable email alerts"));
        c.gridx = 1;
        c.gridy = 3;
        add(enableEmailAlerts, c);

        ImageIcon pollingSpinner = new ImageIcon(getImage("animations/spinner.gif"));
        c.weightx = 0.0;
        c.gridx = 3;
        c.gridy = 3;
        add(new JLabel("Polling...", pollingSpinner, JLabel.CENTER), c);
    }

    public boolean emailAlertsEnabled() {
        return enableEmailAlerts.isSelected();
    }

    //update percent diff every interval
    ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if (poller.getPercentDiff() > poller.getThresholdPercent()) {
                diffButton.setText("<HTML>Diff: <FONT color=\"#CC1100\" size=14px><U>" + poller.getPercentDiff() + "%</U></FONT></HTML>");
                diffButton.setToolTipText(String.valueOf(poller.getPreProcessedPercentDiff() + "% diff, click to view"));
            } else {
                diffButton.setToolTipText(String.valueOf(poller.getPreProcessedPercentDiff() + "% diff, click to view"));
                diffButton.setText("<HTML>Diff: <FONT color=\"#3CB371\" size=14px><U>" + poller.getPercentDiff() + "%</U></FONT></HTML>");
            }
        }
    };

    //inner-class: opens webpage for TrackerTile URL
    class UrlButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            //flagged for old API usage
            if(Desktop.isDesktopSupported())
            {
                try {
                    Desktop.getDesktop().browse(new URI(url));
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
                    Desktop.getDesktop().mail(new URI("mailto:" + email));
                    systemLogOpeningMailClient();
                } catch (Exception ex) {
                    systemLogUriFailed();
                    ex.printStackTrace();
                }
            }
        }
    }

    //inner-class: displays text diff in new pane | oldHtml -> newHtml
    class DiffButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            systemLogCreatingDiff(url);
            new Thread(new DiffPane(url, poller.getOldHtml(), poller.getNewHtml())).start();
        }
    }

    //inner-class: terminates tracker-thread and removes TrackerTile
    class TerminateButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            executerThreads.get(trackerNumber).shutdown();
            trackerPanel.remove(jSeparator);
            trackerPanel.remove(trackerTile);
            decrementTrackerPanelCounter();
            trackerPanel.repaint();
            systemLogTerminateTracker(url);
        }
    }

    //trackerCounter decrement-redraw, increment in MainWindow
    public void decrementTrackerPanelCounter() {
        MainWindow.trackerNumber--;
        if (trackerNumber == 1) {
            trackerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.GRAY), "Tracking " + trackerNumber + " website"));
        } else {
            trackerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.GRAY), "Tracking " + trackerNumber + " websites"));
        }
    }
}
