# Enum type 

The enum type is built-in Java data type that defines a fixed set of named constants.   
The set of constants cannot be changed afterwards. Variables having an enum type can  
be assigned any of the enumerators as a value.

The enum type provides a more robust and readable way to handle constant values compared   
to using primitive data types like integers. Enemerations enforce type safety by guaranteeing  
that the variable can only hold one of the predefined values.

We should use enum types any time we need to represent a fixed set of constants. There are   
many natural enum types such as the planets in our solar system, seasons, or days of week.   
These are data sets where we know all possible values at compile time.


## Simple

```java
enum Day {

    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

void main() {

    Day day = Day.MONDAY;

    if (day == Day.MONDAY) {

        System.out.println("It is Monday");
    }

    System.out.println(day);

    for (Day d : Day.values()) {

        System.out.println(d);
    }
}
```

## Custom constant values

```java
enum Season {

    SPRING(10),
    SUMMER(20),
    AUTUMN(30),
    WINTER(40);

    private int value;

    private Season(int value) {
        this.value = value;
    }

    public int getValue() {

        return value;
    }
}

void main() {

    for (Season season : Season.values()) {

        System.out.println(STR."\{season} \{season.getValue()}");
    }
}
```


## Custom method

```java
import java.util.Random;

public enum Coin {
    HEADS,
    TAILS;

    public static Coin toss() {

        var rand = new Random();
        int rdx = rand.nextInt(Coin.values().length);
        return Coin.values()[rdx];
    }
}

void main() {

    for (int i = 1; i <= 15; i++) {

        System.out.print(STR."\{Coin.toss()} ");
    }
}
```

## Switch expression

```java
import java.util.List;

enum Day {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday
}

boolean isWeekend(Day d) {

    return switch (d) {
        case Day.Monday, Day.Tuesday, Day.Wednesday, Day.Thursday, Day.Friday -> false;
        case Day.Saturday, Day.Sunday -> true;
    };
}

void main() {

    var days = List.of(Day.Monday, Day.Tuesday, Day.Wednesday, Day.Thursday,
            Day.Friday, Day.Saturday, Day.Sunday);

    for (var e : days) {

        if (isWeekend(e)) {
            System.out.print("weekend ");
        } else {
            System.out.print("weekday ");
        }
    }
}
```


```java
import java.util.Random;

void main() {

    Season season = Season.randomSeason();

    String msg = switch (season) {

        case Season.SPRING -> "Spring";
        case Season.SUMMER -> "Summer";
        case Season.AUTUMN -> "Autumn";
        case Season.WINTER -> "Winter";
    };

    System.out.println(msg);
}

enum Season {
    SPRING,
    SUMMER,
    AUTUMN,
    WINTER;

    public static Season randomSeason() {
        
        var random = new Random();
        int ridx = random.nextInt(Season.values().length);
        return Season.values()[ridx];
    }
}
```


