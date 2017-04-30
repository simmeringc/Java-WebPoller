/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.controllers;

import static com.simmeringc.websitePoller.views.SystemLog.systemLogHtmlGetFailed;

import org.jsoup.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class WebRequester {

    private static InputStream is = null;
    private static boolean success = false;

    public static String getHtml(String url) throws Exception {
        String html = "";
        try {
            html = Jsoup.connect(url).get().html();
            if (!html.toString().equals("")) {
                success = true;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!success) {
            try {
                String str;
                StringBuilder htmlBuilder = new StringBuilder();
                URL urlObj = new URL(url);
                is = urlObj.openStream();  // connecting stream, throws IOException
                BufferedReader br = new BufferedReader(new InputStreamReader(is)); // chaining stream
                while ((str = br.readLine()) != null) {
                    htmlBuilder.append(str);
                }
                if (!htmlBuilder.toString().equals("")) {
                    html = htmlBuilder.toString();
                    success = true;
                }
            }
            catch (Exception e) {
                systemLogHtmlGetFailed(url);
                throw new Exception("HTML get failed");
            }
            finally {
                try {
                    if (is != null) is.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace(System.out);
                }
            }
        }
        return html;
    }
}

