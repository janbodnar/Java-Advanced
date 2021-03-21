package com.zetcode;

import java.util.Comparator;
import java.util.List;

// sorting a list of objects by implementing a Comparable and 
// internal compareTo method

class Card implements Comparable<Card> {

    @Override
    public int compareTo(Card o) {

        return Comparator.comparing(Card::getRank)
                .thenComparing(Card::getSuit)
                .compare(this, o);
    }

    public enum Suit {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES
    }

    public enum Rank {
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

    private Suit suit;
    private Rank rank;

    public Card(Rank rank, Suit suit) {

        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public void showCard() {

        rank = getRank();
        suit = getSuit();

        System.out.println(rank + " of " + suit);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Card{");
        sb.append("suit=").append(suit);
        sb.append(", rank=").append(rank);
        sb.append('}');
        return sb.toString();
    }
}

public class ListSortCards2 {

    public static void main(String[] args) {

        var cards = List.of(
                new Card(Card.Rank.KING, Card.Suit.DIAMONDS),
                new Card(Card.Rank.FIVE, Card.Suit.HEARTS),
                new Card(Card.Rank.ACE, Card.Suit.CLUBS),
                new Card(Card.Rank.NINE, Card.Suit.SPADES),
                new Card(Card.Rank.JACK, Card.Suit.SPADES),
                new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));

        var sorted = cards.stream().sorted().toList();
        sorted.forEach(System.out::println);
    }
}
