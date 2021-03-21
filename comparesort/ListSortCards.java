package com.zetcode;

import java.util.Comparator;
import java.util.List;

// sorting a list of objects by external comparator

public class ListSortCards {

    public static void main(String[] args) {

        var cards = List.of(
                new Card(Rank.KING, Suit.DIAMONDS),
                new Card(Rank.FIVE, Suit.HEARTS),
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.JACK, Suit.SPADES),
                new Card(Rank.JACK, Suit.DIAMONDS));

        var sorted = cards.stream().sorted(CardComparator.build()).toList();
        sorted.forEach(System.out::println);
    }
}

enum Rank {
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

enum Suit {
    CLUBS,
    DIAMONDS,
    HEARTS,
    SPADES
}

record Card(Rank rank, Suit suit) {
}

class CardComparator implements Comparator<Card> {

    static CardComparator build() {
        return new CardComparator();
    }

    @Override
    public int compare(Card o1, Card o2) {
        return Comparator.comparing(Card::rank)
                .thenComparing(Card::suit)
                .compare(o1, o2);
    }
}
