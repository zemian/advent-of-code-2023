package aoc2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static aoc2023.Day7.HandType.FIVE_OF_A_KIND;
import static aoc2023.Day7.HandType.FOUR_OF_A_KIND;
import static aoc2023.Day7.HandType.FULL_HOUSE;
import static aoc2023.Day7.HandType.HIGH_CARD;
import static aoc2023.Day7.HandType.ONE_PAIR;
import static aoc2023.Day7.HandType.THREE_OF_A_KIND;
import static aoc2023.Day7.HandType.TWO_PAIR;
import static aoc2023.Utils.assertEquals;

public class Day7 {
    public static void main(String[] args) {
        var program = new Day7();
        if (args.length > 0 && args[0].equals("test")) {
            program.runTests();
            System.out.println("Tests passed.");
        } else {
            program.runMain("src/aoc2023/Day7-input2.txt");
        }
    }

    private Long runMain(String fileName) {
        System.out.println("Processing input: " + fileName);
        var hands = new ArrayList<Hand>();
        var lines = Utils.readLines(fileName);
        for (String line : lines) {
            var parts = line.trim().split(" ");
            hands.add(new Hand(parts[0], Integer.parseInt(parts[1])));
        }

        Collections.sort(hands);

        var sum = 0L;
        var maxSize = hands.size();
        for (int i = 0; i < maxSize; i++) {
            var hand = hands.get(i);
            long rank = maxSize - i;
            System.out.println(hand.getCards() + ", rank: " + rank + ", bet: " + hand.getBet());
            sum += (hand.getBet() * rank);
        }

        return sum;
    }

    public enum HandType {
        // The natural order of these enum can be used directly (see compareTo()).
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD;
    }

    public static class Hand implements Comparable<Hand> {
        String cards;

        String[] cardsAry;
        int bet;

        HandType type;

        public static Map<String, Integer> CARD_ORDER = new HashMap<>();
        static {
            CARD_ORDER.put("2", 2);
            CARD_ORDER.put("3", 3);
            CARD_ORDER.put("4", 4);
            CARD_ORDER.put("5", 5);
            CARD_ORDER.put("6", 6);
            CARD_ORDER.put("7", 7);
            CARD_ORDER.put("8", 8);
            CARD_ORDER.put("9", 9);
            CARD_ORDER.put("T", 10);
            CARD_ORDER.put("J", 11);
            CARD_ORDER.put("Q", 12);
            CARD_ORDER.put("K", 13);
            CARD_ORDER.put("A", 14);
        }

        public Hand(String cards) {
            this(cards, 0);
        }

        public Hand(String cards, int bet) {
            this.cards = cards;
            this.cardsAry = cards.split("");
            this.bet = bet;
        }

        public String[] getCardsAry() {
            return this.cardsAry;
        }

        public int getBet() {
            return bet;
        }

        private HandType calcType() {
            var freq = new HashMap<String, Integer>();
            for (var c : cardsAry) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }
            var maxCount = freq.values().stream().max(Integer::compareTo).get();
            if (maxCount == 5) {
                return FIVE_OF_A_KIND;
            } else if (maxCount == 4) {
                return FOUR_OF_A_KIND;
            } else if (maxCount == 3) {
                if (freq.size() == 2) {
                    return FULL_HOUSE;
                }
                return THREE_OF_A_KIND;
            } else if (maxCount == 2) {
                if (freq.size() == 3) {
                    return TWO_PAIR;
                }
                return ONE_PAIR;
            }
            return HIGH_CARD;
        }

        public HandType getType() {
            if (this.type == null) {
                this.type = this.calcType();
            }
            return this.type;
        }

        public String getCards() {
            return cards;
        }

        private int compare(Hand a, Hand b) {
            int typeRet = a.getType().compareTo(b.getType()); // Ascending order (HighCard or Lowest Hand first)
            if (typeRet == 0) {
                var cardsA = a.getCardsAry();
                var cardsB = b.getCardsAry();
                for (int i = 0; i < cardsA.length; i++) {
                    var ca = cardsA[i];
                    var cb = cardsB[i];
                    var cardRet = Integer.compare(CARD_ORDER.get(cb), CARD_ORDER.get(ca)); // Descending order (A first)
                    if (cardRet != 0) {
                        return cardRet;
                    }
                }
                return 0;
            }
            return typeRet;
        }

        public int compareTo(Hand b) {
            return this.compare(this, b);
        }
    }

    private void runTests() {
        testHandType();
        testCompareHand();
        testMain();
    }

    private void testCompareHand() {
        assertEquals(new Hand("32T5K").compareTo(new Hand("32T5K")) == 0, true);
        assertEquals(new Hand("23456").compareTo(new Hand("23457")) > 0, true);
        assertEquals(new Hand("23457").compareTo(new Hand("23456")) < 0, true);
    }

    private void testHandType() {
        assertEquals(new Hand("32T5K").getType(), HIGH_CARD);
        assertEquals(new Hand("32T3K").getType(), ONE_PAIR);
        assertEquals(new Hand("3TT3K").getType(), TWO_PAIR);
        assertEquals(new Hand("33T3K").getType(), THREE_OF_A_KIND);
        assertEquals(new Hand("3KK3K").getType(), FULL_HOUSE);
        assertEquals(new Hand("3KKKK").getType(), FOUR_OF_A_KIND);
        assertEquals(new Hand("KKKKK").getType(), FIVE_OF_A_KIND);

        assertEquals(FIVE_OF_A_KIND.compareTo(FOUR_OF_A_KIND), -1);
        assertEquals(FOUR_OF_A_KIND.compareTo(FULL_HOUSE), -1);
        assertEquals(FULL_HOUSE.compareTo(THREE_OF_A_KIND), -1);
        assertEquals(THREE_OF_A_KIND.compareTo(TWO_PAIR), -1);
        assertEquals(TWO_PAIR.compareTo(ONE_PAIR), -1);
        assertEquals(ONE_PAIR.compareTo(HIGH_CARD), -1);
        assertEquals(HIGH_CARD.compareTo(HIGH_CARD), 0);
        assertEquals(HIGH_CARD.compareTo(ONE_PAIR), 1);
    }

    private void testMain() {
        Long sum = runMain("src/aoc2023/Day7-input1.txt");
        assertEquals(sum, 6440L);

        sum = runMain("src/aoc2023/Day7-input2.txt");
        assertEquals(sum, 248569531L);
    }
}
