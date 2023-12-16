/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jemu.core.device.floppy;

import java.awt.*;
import java.net.URL;
import java.awt.event.*;
import javax.swing.*;

import jemu.settings.Settings;

/**
 * @author Markus
 */
public class virtualDrive extends JFrame implements MouseListener, MouseMotionListener {

    public int oldtrackpos, trackpos;

    protected boolean init = true;
    protected int oldY = 10;

    private URL dhead = getClass().getResource("/image/drive_head.png");
    private Image drivehead = getToolkit().getImage(dhead);
    private URL driv = getClass().getResource("/image/drive.png");
    private Image drivebase = getToolkit().getImage(driv);

    private static boolean showIt, hideIt;
    private int multiplier = 2;

    public static Color LED_OFF = new Color(0x90, 0x00, 0x00);

    public void update() {
        if (init) {
            int x = Integer.parseInt(Settings.get(Settings.FLOPPYX, "0"));
            int y = Integer.parseInt(Settings.get(Settings.FLOPPYY, "0"));
            multiplier = Integer.parseInt(Settings.get(Settings.FLOPPYZOOM, "1"));
            this.setLocation(x, y);
            this.addMouseMotionListener(this);
            this.addMouseListener(this);
            rePaint();
            init = false;
        }
        if (showIt && !this.isVisible())
            this.setVisible(true);
        if (hideIt && this.isVisible()) {
            this.setVisible(false);
        }
        repaint();
    }

    public static void Show() {
        showIt = true;
        hideIt = false;
    }

    public static void Hide() {
        hideIt = true;
        showIt = false;
    }

    @Override
    public void paint(Graphics g) {
        if (oldtrackpos != trackpos) {
            trackpos = (trackpos % 42) / 2;
            trackpos = trackpos * multiplier;
        }
        oldtrackpos = trackpos;
        g.drawImage(drivebase, 0, 0, drivebase.getWidth(this) * multiplier, drivebase.getHeight(this) * multiplier, this);
        g.drawImage(drivehead, 35 * multiplier, 22 * multiplier + trackpos, drivehead.getWidth(this) * multiplier, drivehead.getHeight(this) * multiplier, this);
        if (jemu.ui.Display.ledOn)
            g.setColor(Color.RED);
        else
            g.setColor(LED_OFF);
        g.fillRect(6 * multiplier, 158 * multiplier, 8 * multiplier, 2 * multiplier);
    }

    @Override
    public void mouseMoved(MouseEvent me) {

    }

    @Override
    public void mouseDragged(MouseEvent me) {
        int x = me.getXOnScreen();
        int y = me.getYOnScreen();
        int xf = this.getWidth() / 2;
        int yf = this.getHeight() / 2;
        this.setLocation(x - xf, y - yf);
        Settings.set(Settings.FLOPPYX, "" + (x - xf));
        Settings.set(Settings.FLOPPYY, "" + (y - yf));
    }

    protected void rePaint() {
        System.out.println("Zoomfactor is: " + this.multiplier);
        this.setSize(98 * this.multiplier, 160 * this.multiplier);
        this.oldY = this.getHeight();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0) {
                if (multiplier > 1) {
                    multiplier--;
                    Settings.set(Settings.FLOPPYZOOM, "" + (multiplier));
                    rePaint();
                }

            } else {
                if (multiplier < 4) {
                    multiplier++;
                    Settings.set(Settings.FLOPPYZOOM, "" + (multiplier));
                    rePaint();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}