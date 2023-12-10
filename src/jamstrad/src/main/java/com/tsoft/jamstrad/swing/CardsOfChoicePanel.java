package com.tsoft.jamstrad.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CardsOfChoicePanel extends JPanel implements CardsInOrderListener {
    private CardsInOrderPanel orderPanel;
    private List<CardsInOrderPanel.Card> cardsOfChoice;
    private Map<CardsInOrderPanel.Card, CardsOfChoicePanel.CardOfChoiceAction> cardsActionMap;

    public CardsOfChoicePanel(List<CardsInOrderPanel.Card> cardsOfChoice, CardsInOrderPanel orderPanel) {
        this(cardsOfChoice, orderPanel, 1, 0);
    }

    public CardsOfChoicePanel(List<CardsInOrderPanel.Card> cardsOfChoice, CardsInOrderPanel orderPanel, int rows, int cols) {
        super(new GridLayout(rows, cols));
        this.orderPanel = orderPanel;
        this.cardsOfChoice = cardsOfChoice;
        this.cardsActionMap = new HashMap();
        this.buildUI();
        orderPanel.addListener(this);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        Map<CardsInOrderPanel.Card, CardsOfChoicePanel.CardOfChoiceAction> map = this.getCardsActionMap();
        if (enabled) {
            List<CardsInOrderPanel.Card> cardsSelected = this.getOrderPanel().getCardsInOrder();
            Iterator var5 = this.getCardsOfChoice().iterator();

            while(var5.hasNext()) {
                CardsInOrderPanel.Card card = (CardsInOrderPanel.Card)var5.next();
                ((CardsOfChoicePanel.CardOfChoiceAction)map.get(card)).setEnabled(!cardsSelected.contains(card));
            }
        } else {
            Iterator var7 = this.getCardsOfChoice().iterator();

            while(var7.hasNext()) {
                CardsInOrderPanel.Card card = (CardsInOrderPanel.Card)var7.next();
                ((CardsOfChoicePanel.CardOfChoiceAction)map.get(card)).setEnabled(false);
            }
        }

    }

    private void buildUI() {
        Iterator var2 = this.getCardsOfChoice().iterator();

        while(var2.hasNext()) {
            CardsInOrderPanel.Card card = (CardsInOrderPanel.Card)var2.next();
            CardsOfChoicePanel.CardOfChoiceAction action = new CardsOfChoicePanel.CardOfChoiceAction(card);
            JButton button = new JButton(action);
            this.add(button);
            this.getCardsActionMap().put(card, action);
        }

    }

    public void cardAddedToPanel(CardsInOrderPanel panel, CardsInOrderPanel.Card card) {
        CardsOfChoicePanel.CardOfChoiceAction action = (CardsOfChoicePanel.CardOfChoiceAction)this.getCardsActionMap().get(card);
        if (action != null) {
            action.setEnabled(false);
        }

    }

    public void cardRemovedFromPanel(CardsInOrderPanel panel, CardsInOrderPanel.Card card) {
        CardsOfChoicePanel.CardOfChoiceAction action = (CardsOfChoicePanel.CardOfChoiceAction)this.getCardsActionMap().get(card);
        if (action != null) {
            action.setEnabled(true);
        }

    }

    public void cardsRearrangedInPanel(CardsInOrderPanel panel) {
    }

    public void cardsChangedInPanel(CardsInOrderPanel panel) {
    }

    public List<CardsInOrderPanel.Card> getCardsOfChoice() {
        return this.cardsOfChoice;
    }

    private Map<CardsInOrderPanel.Card, CardsOfChoicePanel.CardOfChoiceAction> getCardsActionMap() {
        return this.cardsActionMap;
    }

    private CardsInOrderPanel getOrderPanel() {
        return this.orderPanel;
    }

    private class CardOfChoiceAction extends AbstractAction {
        private CardsInOrderPanel.Card card;

        public CardOfChoiceAction(CardsInOrderPanel.Card card) {
            super(card.getLabel(), card.getIcon());
            this.card = card;
        }

        public void actionPerformed(ActionEvent event) {
            CardsOfChoicePanel.this.getOrderPanel().addCard(this.getCard());
        }

        public CardsInOrderPanel.Card getCard() {
            return this.card;
        }
    }
}
