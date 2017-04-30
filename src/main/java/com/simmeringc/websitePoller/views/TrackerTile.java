/**
 * Created by Conner on 4/29/17.
 */

package com.simmeringc.websitePoller.views;

import static com.simmeringc.websitePoller.views.SystemLog.systemLogOpeningBrowser;
import static com.simmeringc.websitePoller.views.SystemLog.systemLogOpeningMailClient;
import static com.simmeringc.websitePoller.views.SystemLog.systemLogUriFailed;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.awt.Desktop; //flagged for old API usage

public class TrackerTile extends JPanel implements ActionListener {

    private String url;
    private String email;
    private int trackerNumber;
    private Timer timer;

    public TrackerTile(String url, String email, String threshold, String interval, int trackerNumber) {
        this.url = url;
        this.email = email;
        this.trackerNumber = trackerNumber;
        this.timer = new Timer((Integer.parseInt(interval) * 1000), this);

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

        JButton urlButton = new JButton();
        urlButton.setText("<HTML>URL: <FONT color=\"#000099\"><U>" + url + "</U></FONT></HTML>");
        urlButton.setHorizontalAlignment(SwingConstants.LEFT);
        urlButton.setBorderPainted(false);
        urlButton.setOpaque(false);
        urlButton.setBackground(Color.WHITE);
        urlButton.addActionListener(new UrlButtonListener());
        c.gridx = 0;
        c.gridy = 0;
        add(urlButton, c);

        JButton emailButton = new JButton();
        emailButton.setText("<HTML>Email: <FONT color=\"#000099\"><U>" + email + "</U></FONT></HTML>");
        emailButton.setHorizontalAlignment(SwingConstants.LEFT);
        emailButton.setBorderPainted(false);
        emailButton.setOpaque(false);
        emailButton.setBackground(Color.WHITE);
        emailButton.addActionListener(new EmailButtonListener());
        c.gridx = 0;
        c.gridy = 1;
        add(emailButton, c);

        JButton thresholdText = new JButton();
        thresholdText.setText("<HTML>Threshold: <U>" + threshold + "</U></FONT></HTML>");
        thresholdText.setHorizontalAlignment(SwingConstants.LEFT);
        thresholdText.setBorderPainted(false);
        thresholdText.setOpaque(false);
        thresholdText.setBackground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 2;
        add(thresholdText, c);

        JButton intervalText = new JButton();
        intervalText.setText("<HTML>Interval: <U>" + interval + "</U></FONT></HTML>");
        intervalText.setHorizontalAlignment(SwingConstants.LEFT);
        intervalText.setBorderPainted(false);
        intervalText.setOpaque(false);
        intervalText.setBackground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 3;
        add(intervalText, c);

        JButton changePercentage = new JButton();
        changePercentage.setText("<HTML>Diff %: <FONT color=\"#CC1100\"><U>" + MainWindow.webPollerList.get(trackerNumber).getPercentDiff() + "</U></FONT></HTML>");
        changePercentage.setHorizontalAlignment(SwingConstants.LEFT);
        changePercentage.setBorderPainted(false);
        changePercentage.setOpaque(false);
        changePercentage.setBackground(Color.WHITE);
        c.gridx = 2;
        c.gridy = 2;
        add(changePercentage, c);

        ImageIcon pollingSpinner = new ImageIcon("src/main/java/com/simmeringc/websitePoller/resources/spinner.gif");
        c.weightx = 0.0;
        c.gridx = 3;
        c.gridy = 3;
        add(new JLabel("Polling...", pollingSpinner, JLabel.CENTER), c);
    }

    //update percent diff every interval
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == timer) {
            System.out.println(MainWindow.webPollerList.get(trackerNumber).getPercentDiff());
            repaint();// this will call at every 1 second
        }
    }

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
}
