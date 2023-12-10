package com.tsoft.jamstrad.swing;

public interface CardsInOrderListener {
    void cardAddedToPanel(CardsInOrderPanel var1, CardsInOrderPanel.Card var2);

    void cardRemovedFromPanel(CardsInOrderPanel var1, CardsInOrderPanel.Card var2);

    void cardsRearrangedInPanel(CardsInOrderPanel var1);

    void cardsChangedInPanel(CardsInOrderPanel var1);
}
