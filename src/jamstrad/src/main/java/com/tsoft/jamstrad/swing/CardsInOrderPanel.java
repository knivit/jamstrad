package com.tsoft.jamstrad.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class CardsInOrderPanel extends JPanel {
    public static int CENTER_ALIGNMENT = 0;
    public static int LEFT_ALIGNMENT = 1;
    public static int RIGHT_ALIGNMENT = 2;
    private int cardsAlignment;
    private int cardsSpacing;
    private int cardsMinimumWidth;
    private int cardsMaximumWidth;
    private int panelMinimumWidth;
    private int panelHeight;
    private double cardsTransparencyWhileMoving;
    private String emptyMessage;
    private Color emptyMessageColor;
    private java.util.List<CardsInOrderPanel.CardComponent> cardComponentsInOrder;
    private java.util.List<CardsInOrderListener> cardsListeners;

    public CardsInOrderPanel(int minimumWidth, int height) {
        this(minimumWidth, height, LEFT_ALIGNMENT);
    }

    public CardsInOrderPanel(int minimumWidth, int height, int cardsAlignment) {
        super((LayoutManager)null, true);
        this.cardsAlignment = cardsAlignment;
        this.cardsSpacing = 10;
        this.cardsMinimumWidth = 100;
        this.cardsMaximumWidth = 400;
        this.panelMinimumWidth = minimumWidth;
        this.panelHeight = height;
        this.cardsTransparencyWhileMoving = 0.2;
        this.emptyMessageColor = Color.GRAY;
        this.cardComponentsInOrder = new Vector();
        this.cardsListeners = new Vector();
        this.sizing();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        Iterator var3 = this.getCardComponentsInOrder().iterator();

        while(var3.hasNext()) {
            CardsInOrderPanel.CardComponent comp = (CardsInOrderPanel.CardComponent)var3.next();
            comp.setEnabled(enabled);
        }

    }

    public void addListener(CardsInOrderListener listener) {
        this.getCardsListeners().add(listener);
    }

    public void removeListener(CardsInOrderListener listener) {
        this.getCardsListeners().remove(listener);
    }

    public synchronized boolean hasCard(CardsInOrderPanel.Card card) {
        return this.findComponentFor(card) != null;
    }

    public synchronized void addCard(CardsInOrderPanel.Card card) {
        if (this.getCardsAlignment() == RIGHT_ALIGNMENT) {
            this.addCardAtBegin(card);
        } else {
            this.addCardAtEnd(card);
        }

    }

    public synchronized void addCardAtBegin(CardsInOrderPanel.Card card) {
        if (!this.hasCard(card)) {
            this.addCardComponent(new CardsInOrderPanel.CardComponent(card), 0);
        }

    }

    public synchronized void addCardAtEnd(CardsInOrderPanel.Card card) {
        if (!this.hasCard(card)) {
            this.addCardComponent(new CardsInOrderPanel.CardComponent(card), this.getCardsCount());
        }

    }

    public synchronized void removeCard(CardsInOrderPanel.Card card) {
        CardsInOrderPanel.CardComponent comp = this.findComponentFor(card);
        if (comp != null) {
            this.removeCardComponent(comp);
        }

    }

    public synchronized void clear() {
        while(!this.isEmpty()) {
            this.removeCardComponent((CardsInOrderPanel.CardComponent)this.getCardComponentsInOrder().get(this.getCardsCount() - 1));
        }

    }

    private void addCardComponent(CardsInOrderPanel.CardComponent comp, int index) {
        this.getCardComponentsInOrder().add(index, comp);

        for(int i = 0; i < this.getCardsCount(); ++i) {
            ((CardsInOrderPanel.CardComponent)this.getCardComponentsInOrder().get(i)).setIndex(i);
        }

        comp.setEnabled(this.isEnabled());
        this.add(comp);
        this.validate();
        Iterator var4 = this.getCardsListeners().iterator();

        while(var4.hasNext()) {
            CardsInOrderListener listener = (CardsInOrderListener)var4.next();
            listener.cardAddedToPanel(this, comp.getCard());
        }

    }

    private void removeCardComponent(CardsInOrderPanel.CardComponent comp) {
        this.getCardComponentsInOrder().remove(comp);

        for(int i = comp.getIndex(); i < this.getCardsCount(); ++i) {
            ((CardsInOrderPanel.CardComponent)this.getCardComponentsInOrder().get(i)).setIndex(i);
        }

        this.remove(comp);
        this.validate();
        Iterator var3 = this.getCardsListeners().iterator();

        while(var3.hasNext()) {
            CardsInOrderListener listener = (CardsInOrderListener)var3.next();
            listener.cardRemovedFromPanel(this, comp.getCard());
        }

    }

    private CardsInOrderPanel.CardComponent findComponentFor(CardsInOrderPanel.Card card) {
        Iterator var3 = this.getCardComponentsInOrder().iterator();

        while(var3.hasNext()) {
            CardsInOrderPanel.CardComponent comp = (CardsInOrderPanel.CardComponent)var3.next();
            if (comp.getCard().equals(card)) {
                return comp;
            }
        }

        return null;
    }

    public synchronized void validate() {
        super.validate();
        this.sizing();
        this.layoutCards();
        this.repaint();
    }

    private void sizing() {
        int width = Math.max(this.getCardsLineupWidth(), this.getPanelMinimumWidth());
        int height = this.getPanelHeight();
        Dimension size = new Dimension(width, height);
        this.setSize(size);
        this.setPreferredSize(size);
    }

    private int getCardsLineupWidth() {
        int width = (this.getCardsCount() - 1) * this.getCardsSpacing();

        CardsInOrderPanel.CardComponent comp;
        for(Iterator var3 = this.getCardComponentsInOrder().iterator(); var3.hasNext(); width += comp.getWidth()) {
            comp = (CardsInOrderPanel.CardComponent)var3.next();
        }

        return width;
    }

    private void layoutCards() {
        Insets insets = this.getInsets();
        int x = 0;
        int y = insets.top;
        int align = this.getCardsAlignment();
        if (align == LEFT_ALIGNMENT) {
            x = insets.left;
        } else if (align == RIGHT_ALIGNMENT) {
            x = this.getWidth() - insets.right - this.getCardsLineupWidth();
        } else if (align == CENTER_ALIGNMENT) {
            x = (this.getWidth() - this.getCardsLineupWidth()) / 2;
        }

        CardsInOrderPanel.CardComponent comp;
        Iterator var6;
        for(var6 = this.getCardComponentsInOrder().iterator(); var6.hasNext(); x += comp.getWidth() + this.getCardsSpacing()) {
            comp = (CardsInOrderPanel.CardComponent)var6.next();
            comp.setLocation(x, y);
        }

        var6 = this.getCardsListeners().iterator();

        while(var6.hasNext()) {
            CardsInOrderListener listener = (CardsInOrderListener)var6.next();
            listener.cardsChangedInPanel(this);
        }

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.isEmpty()) {
            String msg = this.getEmptyMessage();
            if (msg != null) {
                FontMetrics fm = g.getFontMetrics();
                int x = (this.getWidth() - fm.stringWidth(msg)) / 2;
                int y = (this.getHeight() + fm.getAscent()) / 2;
                g.setColor(this.getEmptyMessageColor());
                g.drawString(msg, x, y);
            }
        }

    }

    private synchronized void rearrangeCardsWithMovingCard(CardsInOrderPanel.CardComponent comp) {
        int x = comp.getCenterX();
        int i = comp.getIndex();
        boolean rearranged = false;
        if (i > 0 && x < ((CardsInOrderPanel.CardComponent)this.getCardComponentsInOrder().get(i - 1)).getCenterX()) {
            this.cardShiftingLeft(comp);
            rearranged = true;
        } else if (i < this.getCardsCount() - 1 && x > ((CardsInOrderPanel.CardComponent)this.getCardComponentsInOrder().get(i + 1)).getCenterX()) {
            this.cardShiftingRight(comp);
            rearranged = true;
        }

        if (rearranged) {
            Iterator var6 = this.getCardsListeners().iterator();

            while(var6.hasNext()) {
                CardsInOrderListener listener = (CardsInOrderListener)var6.next();
                listener.cardsRearrangedInPanel(this);
            }
        }

    }

    private void cardShiftingLeft(CardsInOrderPanel.CardComponent comp) {
        java.util.List<CardsInOrderPanel.CardComponent> cards = this.getCardComponentsInOrder();
        int x = comp.getCenterX();
        int i = comp.getIndex();

        int j;
        for(j = i - 1; j > 0 && x < ((CardsInOrderPanel.CardComponent)cards.get(j - 1)).getCenterX(); --j) {
        }

        int x0 = ((CardsInOrderPanel.CardComponent)cards.get(j)).getX();
        cards.add(j, (CardsInOrderPanel.CardComponent)cards.remove(i));

        for(int k = j; k <= i; ++k) {
            CardsInOrderPanel.CardComponent cardK = (CardsInOrderPanel.CardComponent)cards.get(k);
            cardK.setIndex(k);
            if (cardK != comp) {
                cardK.setLocation(x0, cardK.getY());
            }

            x0 += cardK.getWidth() + this.getCardsSpacing();
        }

    }

    private void cardShiftingRight(CardsInOrderPanel.CardComponent comp) {
        java.util.List<CardsInOrderPanel.CardComponent> cards = this.getCardComponentsInOrder();
        int x = comp.getCenterX();
        int i = comp.getIndex();

        int j;
        for(j = i + 1; j < this.getCardsCount() - 1 && x > ((CardsInOrderPanel.CardComponent)cards.get(j + 1)).getCenterX(); ++j) {
        }

        int x1 = ((CardsInOrderPanel.CardComponent)cards.get(j)).getX() + ((CardsInOrderPanel.CardComponent)cards.get(j)).getWidth() - 1;
        cards.add(j, (CardsInOrderPanel.CardComponent)cards.remove(i));

        for(int k = j; k >= i; --k) {
            CardsInOrderPanel.CardComponent cardK = (CardsInOrderPanel.CardComponent)cards.get(k);
            cardK.setIndex(k);
            if (cardK != comp) {
                cardK.setLocation(x1 - cardK.getWidth() + 1, cardK.getY());
            }

            x1 -= cardK.getWidth() + this.getCardsSpacing();
        }

    }

    private boolean isInRemovalArea(CardsInOrderPanel.CardComponent comp) {
        int x = comp.getCenterX();
        if (x >= 0 && x <= this.getWidth()) {
            int y = comp.getCenterY();
            return y < 0 || y > this.getHeight();
        } else {
            return false;
        }
    }

    public synchronized boolean isEmpty() {
        return this.getCardComponentsInOrder().isEmpty();
    }

    public synchronized int getCardsCount() {
        return this.getCardComponentsInOrder().size();
    }

    public synchronized java.util.List<CardsInOrderPanel.Card> getCardsInOrder() {
        java.util.List<CardsInOrderPanel.Card> cards = new Vector(this.getCardsCount());
        Iterator var3 = this.getCardComponentsInOrder().iterator();

        while(var3.hasNext()) {
            CardsInOrderPanel.CardComponent comp = (CardsInOrderPanel.CardComponent)var3.next();
            cards.add(comp.getCard());
        }

        return cards;
    }

    private java.util.List<CardsInOrderPanel.CardComponent> getCardComponentsInOrder() {
        return this.cardComponentsInOrder;
    }

    public int getCardsAlignment() {
        return this.cardsAlignment;
    }

    public void setCardsAlignment(int alignment) {
        this.cardsAlignment = alignment;
        this.revalidate();
    }

    public int getCardsSpacing() {
        return this.cardsSpacing;
    }

    public void setCardsSpacing(int spacing) {
        this.cardsSpacing = spacing;
        this.revalidate();
    }

    public int getCardsMinimumWidth() {
        return this.cardsMinimumWidth;
    }

    public void setCardsMinimumWidth(int width) {
        this.cardsMinimumWidth = width;
        this.revalidate();
    }

    public int getCardsMaximumWidth() {
        return this.cardsMaximumWidth;
    }

    public void setCardsMaximumWidth(int width) {
        this.cardsMaximumWidth = width;
        this.revalidate();
    }

    public int getPanelMinimumWidth() {
        return this.panelMinimumWidth;
    }

    public void setPanelMinimumWidth(int width) {
        this.panelMinimumWidth = width;
        this.revalidate();
    }

    public int getPanelHeight() {
        return this.panelHeight;
    }

    public void setPanelHeight(int panelHeight) {
        this.panelHeight = panelHeight;
        this.revalidate();
    }

    public double getCardsTransparencyWhileMoving() {
        return this.cardsTransparencyWhileMoving;
    }

    public void setCardsTransparencyWhileMoving(double transparency) {
        if (!(transparency < 0.0) && !(transparency > 1.0)) {
            this.cardsTransparencyWhileMoving = transparency;
        } else {
            throw new IllegalArgumentException("Transparency level must be between 0 and 1: " + transparency);
        }
    }

    public String getEmptyMessage() {
        return this.emptyMessage;
    }

    public void setEmptyMessage(String message) {
        this.emptyMessage = message;
        if (this.isEmpty()) {
            this.repaint();
        }

    }

    public Color getEmptyMessageColor() {
        return this.emptyMessageColor;
    }

    public void setEmptyMessageColor(Color color) {
        this.emptyMessageColor = color;
        if (this.isEmpty()) {
            this.repaint();
        }

    }

    private List<CardsInOrderListener> getCardsListeners() {
        return this.cardsListeners;
    }

    public static class Card {
        private String label;
        private Icon icon;
        private int horizontalAlignment;
        private Color backgroundColor;

        public Card(String label, Color backgroundColor) {
            this(label, (Icon)null, backgroundColor);
        }

        public Card(String label, Icon icon, Color backgroundColor) {
            this(label, icon, 0, backgroundColor);
        }

        public Card(String label, Icon icon, int horizontalAlignment, Color backgroundColor) {
            this.label = label;
            this.icon = icon;
            this.horizontalAlignment = horizontalAlignment;
            this.backgroundColor = backgroundColor == null ? Color.WHITE : backgroundColor;
        }

        public String toString() {
            return this.getLabel();
        }

        public int hashCode() {
            return this.getLabel().hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (this.getClass() != obj.getClass()) {
                return false;
            } else {
                CardsInOrderPanel.Card other = (CardsInOrderPanel.Card)obj;
                if (this.getLabel() == null) {
                    if (other.getLabel() != null) {
                        return false;
                    }
                } else if (!this.getLabel().equals(other.getLabel())) {
                    return false;
                }

                return true;
            }
        }

        public String getLabel() {
            return this.label;
        }

        public Icon getIcon() {
            return this.icon;
        }

        public int getHorizontalAlignment() {
            return this.horizontalAlignment;
        }

        public Color getBackgroundColor() {
            return this.backgroundColor;
        }
    }

    private class CardComponent extends JLabel implements MouseMotionListener, MouseListener {
        private CardsInOrderPanel.Card card;
        private int index;
        private boolean dragged;
        private Point locationBeforeDragging;
        private Point mouseAnchorLocation;
        private float[] rgbaComps = new float[4];

        public CardComponent(CardsInOrderPanel.Card card) {
            super(card.getLabel(), card.getIcon(), card.getHorizontalAlignment());
            this.setBackground(card.getBackgroundColor());
            this.setOpaque(true);
            this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
            this.sizing();
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.card = card;
        }

        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            this.setBackground(enabled ? this.getCard().getBackgroundColor() : null);
        }

        private void sizing() {
            Insets insets = CardsInOrderPanel.this.getInsets();
            int width = Math.min(Math.max(this.getPreferredSize().width, CardsInOrderPanel.this.getCardsMinimumWidth()), CardsInOrderPanel.this.getCardsMaximumWidth());
            int height = CardsInOrderPanel.this.getPanelHeight() - insets.top - insets.bottom;
            Dimension size = new Dimension(width, height);
            this.setSize(size);
            this.setPreferredSize(size);
        }

        private void setBackgroundTransparency(double transparency) {
            this.getCard().getBackgroundColor().getRGBColorComponents(this.rgbaComps);
            this.rgbaComps[3] = (float)(1.0 - transparency);
            this.setBackground(new Color(this.rgbaComps[0], this.rgbaComps[1], this.rgbaComps[2], this.rgbaComps[3]));
        }

        public void mouseClicked(MouseEvent event) {
            if (this.isEnabled() && event.getButton() != 1) {
                synchronized(CardsInOrderPanel.this) {
                    CardsInOrderPanel.this.removeCardComponent(this);
                }
            }

        }

        public void mouseDragged(MouseEvent event) {
            if (this.isEnabled()) {
                if (!this.isDragged()) {
                    this.setDragged(true);
                    this.setLocationBeforeDragging(this.getLocation());
                    this.setMouseAnchorLocation(event.getLocationOnScreen());
                    this.setCursor(Cursor.getPredefinedCursor(13));
                    this.setBackgroundTransparency(CardsInOrderPanel.this.getCardsTransparencyWhileMoving());
                    CardsInOrderPanel.this.setComponentZOrder(this, 0);
                }

                Point p0 = this.getLocationBeforeDragging();
                Point m0 = this.getMouseAnchorLocation();
                int dx = event.getXOnScreen() - m0.x;
                int dy = event.getYOnScreen() - m0.y;
                this.setLocation(p0.x + dx, p0.y + dy);
                CardsInOrderPanel.this.rearrangeCardsWithMovingCard(this);
            }

        }

        public void mouseReleased(MouseEvent event) {
            if (this.isEnabled() && this.isDragged()) {
                this.setDragged(false);
                this.setBackgroundTransparency(0.0);
                this.setCursor(Cursor.getDefaultCursor());
                synchronized(CardsInOrderPanel.this) {
                    if (CardsInOrderPanel.this.isInRemovalArea(this)) {
                        CardsInOrderPanel.this.removeCardComponent(this);
                    } else {
                        CardsInOrderPanel.this.layoutCards();
                    }
                }
            }

        }

        public void mouseMoved(MouseEvent event) {
        }

        public void mouseEntered(MouseEvent event) {
        }

        public void mouseExited(MouseEvent event) {
        }

        public void mousePressed(MouseEvent event) {
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (CardsInOrderPanel.this.isInRemovalArea(this)) {
                FontMetrics fm = g.getFontMetrics();
                int w = fm.charWidth('x');
                int x = (this.getWidth() - w) / 2;
                g.setColor(this.getBackground().darker());
                g.fillOval(x - 3, 3, w + 5, w + 5);
                g.fillOval(x - 3, this.getHeight() - w - 9, w + 5, w + 5);
                g.setColor(Color.WHITE);
                g.drawString("x", x, fm.getAscent());
                g.drawString("x", x, this.getHeight() - 6);
            }

        }

        public int getCenterX() {
            return this.getX() + this.getWidth() / 2;
        }

        public int getCenterY() {
            return this.getY() + this.getHeight() / 2;
        }

        public CardsInOrderPanel.Card getCard() {
            return this.card;
        }

        private int getIndex() {
            return this.index;
        }

        private void setIndex(int index) {
            this.index = index;
        }

        private boolean isDragged() {
            return this.dragged;
        }

        private void setDragged(boolean dragged) {
            this.dragged = dragged;
        }

        private Point getLocationBeforeDragging() {
            return this.locationBeforeDragging;
        }

        private void setLocationBeforeDragging(Point location) {
            this.locationBeforeDragging = location;
        }

        private Point getMouseAnchorLocation() {
            return this.mouseAnchorLocation;
        }

        private void setMouseAnchorLocation(Point location) {
            this.mouseAnchorLocation = location;
        }
    }
}
