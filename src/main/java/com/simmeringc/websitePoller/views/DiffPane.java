/**
 * Created by simmeringc on 4/30/17.
 *
 * new frame thread displays comprehensive diff between webpage changes
 */

package com.simmeringc.websitePoller.views;;

import javax.swing.*;
import java.util.LinkedList;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import static java.lang.System.setProperty;

public class DiffPane extends JFrame implements Runnable {

    private String oldHtml;
    private String newHtml;
    private DiffMatchPatch dmp;

    public DiffPane(String url, String oldHtml, String newHtml) {
        this.oldHtml = oldHtml;
        this.newHtml = newHtml;
        this.dmp = new DiffMatchPatch();

        //take the menu bar off the JFrame
        setProperty("apple.laf.useScreenMenuBar", "true");

        //set the name of the application menu item
        setProperty("apple.awt.application.name", "Java WebPoller Diff");

        setTitle("Java WebPoller Diff: " + url);
    }

    public void run() {
        try {
            JEditorPane editorPane = new JEditorPane("text/html", getDiff());
            JScrollPane scrollPane = new JScrollPane(editorPane);
            add(scrollPane);
            setVisible(true);
            setSize(600,600);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getDiff() {
        LinkedList diffs =  dmp.diffMain(oldHtml, newHtml);
        String html = dmp.diffPrettyHtml(diffs);
        return html;
    }
}


