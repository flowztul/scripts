package burp;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.AWTEvent;
import javax.swing.*;


class MyItemListener implements ItemListener {

    BurpExtender extender;

    MyItemListener(BurpExtender extender) {
        this.extender = extender;
    }

    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.DESELECTED){
            this.extender.disable();
        } else {
            this.extender.enable();
        }
    }
}

class MainEventListener implements AWTEventListener {

    BurpExtender extender;

    MainEventListener(BurpExtender extender) {
        this.extender = extender;
    }

    public void eventDispatched(AWTEvent event) {

        Object source = event.getSource();

        if(source instanceof JFrame) {
            JFrame frame = (JFrame) source;
            JRootPane rootpane = frame.getRootPane();
            JMenuBar menubar = rootpane.getJMenuBar();

            menubar.add(Box.createHorizontalGlue());

            JMenu submenu = new JMenu("BurpExtender");

            JCheckBoxMenuItem checkbox;

            checkbox = new JCheckBoxMenuItem("Enable Burp Extender");
            checkbox.addItemListener(new MyItemListener(this.extender));
            checkbox.setSelected(true);
            submenu.add(checkbox);

            menubar.add(submenu);

            this.extender.removeEventListener();
        };
    }

}

public class BurpExtender {

    MainEventListener eventlistener;
    Boolean enabled = true;

    BurpExtender() {
        this.addEventListener();
    }

    void addEventListener() {
        this.eventlistener = new MainEventListener(this);
        java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(this.eventlistener, 0xffffffff);
    }
    
    void removeEventListener() {
        java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener(this.eventlistener);
    }

    void enable() {
        this.enabled = true;
        System.out.println("Burp Extender enabled");
    }

    void disable() {
        this.enabled = false;
        System.out.println("Burp Extender disabled");
    }

    public void setCommandLineArgs(String[] args) {
    }
    
    public byte[] processProxyMessage(
            int messageReference,
            boolean messageIsRequest,
            String remoteHost,
            int remotePort,
            boolean serviceIsHttps,
            String httpMethod,
            String url,
            String resourceType,
            String statusCode,
            String responseContentType,
            byte[] message,
            int[] action) {
        if(this.enabled) {
            System.out.println(url);
        }
        return message;
    }

    public void registerExtenderCallbacks(burp.IBurpExtenderCallbacks callbacks) {
    }

    public void applicationClosing() {
    }
    
    public void processHttpMessage(
            String toolName, 
            boolean messageIsRequest, 
            IHttpRequestResponse messageInfo) throws Exception {
        if(this.enabled) {
            System.out.println(messageInfo.getUrl());
        }
    }
    
    public void newScanIssue(IScanIssue issue) {
    }

}
