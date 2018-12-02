package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;

class Card implements Comparable<Card> {

    @Override
    public int compareTo(Card o) {

        return Comparator.comparing(Card::getValue)
                .thenComparing(Card::getSuit)
                .compare(this, o);
    }

    public enum Suits {
        SPADES,
        CLUBS,
        HEARTS,
        DIAMONDS
    }

    public enum Values {
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE,
    }

    private Suits suit;
    private Values value;

    public Card(Values value, Suits suit) {

        this.value = value;
        this.suit = suit;
    }

    public Values getValue() {
        return value;
    }

    public Suits getSuit() {
        return suit;
    }

    public void showCard() {
        value = getValue();
        suit = getSuit();

        System.out.println(value + " of " + suit);
    }

    @Override
    public String toString() {

        final var sb = new StringBuilder("Card{");
        sb.append("suit=").append(suit);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}

public class ArraysSortObjectsEx {

    public static void main(String[] args) {

        Card[] cards = {
                new Card(Card.Values.KING, Card.Suits.DIAMONDS),
                new Card(Card.Values.FIVE, Card.Suits.HEARTS),
                new Card(Card.Values.ACE, Card.Suits.CLUBS),
                new Card(Card.Values.NINE, Card.Suits.SPADES),
                new Card(Card.Values.JACK, Card.Suits.SPADES),
                new Card(Card.Values.JACK, Card.Suits.DIAMONDS)};

        Arrays.sort(cards);

        for (Card card : cards) {

            System.out.println(card);
        }
    }
}
