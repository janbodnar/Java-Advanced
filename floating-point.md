# Floating-Point Math in Java  

Floating-point numbers are fundamental to scientific computing, graphics, machine  
learning, and countless other domains in Java programming. Understanding their  
representation, limitations, and proper usage is essential for writing robust  
numerical code. This document explores floating-point arithmetic in Java, covering  
the IEEE 754 standard, common pitfalls, precision issues, and best practices.  

Java provides two primitive floating-point types: `float` (32-bit) and `double`  
(64-bit). The `double` type is the default and most commonly used for decimal  
values. While floating-point numbers offer excellent range and performance, they  
cannot represent all decimal values exactly, leading to subtle bugs if not handled  
carefully.  


## IEEE 754 Standard  

Java implements the IEEE 754 standard for floating-point arithmetic, which defines  
how floating-point numbers are stored and calculated. A floating-point number  
consists of three parts: the sign bit, the exponent, and the mantissa (significand).  

| Type     | Sign Bit | Exponent Bits | Mantissa Bits | Total Bits |  
|----------|----------|---------------|---------------|------------|  
| `float`  | 1        | 8             | 23            | 32         |  
| `double` | 1        | 11            | 52            | 64         |  

The formula for the represented value is: `(-1)^sign × 2^(exponent-bias) × 1.mantissa`  

The limited number of bits means many decimal fractions cannot be represented  
exactly in binary floating-point. For example, 0.1 in decimal is a repeating  
fraction in binary, similar to how 1/3 is repeating in decimal.  


## Basic float and double  

The most basic usage of floating-point types demonstrates their declaration and  
simple operations.  

```java
void main() {

    float floatValue = 3.14f;
    double doubleValue = 3.14159265358979;

    IO.println("Float value: " + floatValue);
    IO.println("Double value: " + doubleValue);
    IO.println("Float size: " + Float.BYTES + " bytes");
    IO.println("Double size: " + Double.BYTES + " bytes");
}
```

The `float` type requires an `f` suffix on literals to distinguish from `double`.  
Without the suffix, Java assumes the literal is a `double`. The `float` type  
provides approximately 7 significant decimal digits of precision, while `double`  
provides approximately 15-16 digits.  


## Precision limits  

Floating-point types have inherent precision limits due to their binary  
representation.  

```java
void main() {

    IO.println("Float max value: " + Float.MAX_VALUE);
    IO.println("Float min positive: " + Float.MIN_VALUE);
    IO.println("Float min normal: " + Float.MIN_NORMAL);

    IO.println("Double max value: " + Double.MAX_VALUE);
    IO.println("Double min positive: " + Double.MIN_VALUE);
    IO.println("Double min normal: " + Double.MIN_NORMAL);
}
```

The difference between `MIN_VALUE` and `MIN_NORMAL` relates to denormalized  
numbers. `MIN_VALUE` is the smallest positive value that can be represented,  
while `MIN_NORMAL` is the smallest positive normalized value. Denormalized  
numbers provide gradual underflow but with reduced precision.  


## The classic 0.1 + 0.2 problem  

The most famous floating-point issue demonstrates that simple decimal fractions  
cannot be represented exactly.  

```java
void main() {

    double a = 0.1;
    double b = 0.2;
    double sum = a + b;

    IO.println("0.1 + 0.2 = " + sum);
    IO.println("Expected: 0.3");
    IO.println("Equal to 0.3? " + (sum == 0.3));
    IO.println("Actual sum: " + String.format("%.20f", sum));
}
```

The result is `0.30000000000000004` rather than `0.3` because neither 0.1 nor  
0.2 can be represented exactly in binary floating-point. This is not a bug but  
a fundamental characteristic of binary floating-point representation. The small  
error accumulates during addition.  


## Special values - NaN  

IEEE 754 defines special values including NaN (Not a Number) for undefined  
mathematical operations.  

```java
void main() {

    double nanResult = 0.0 / 0.0;
    double sqrtNegative = Math.sqrt(-1);
    double logNegative = Math.log(-1);

    IO.println("0.0 / 0.0 = " + nanResult);
    IO.println("sqrt(-1) = " + sqrtNegative);
    IO.println("log(-1) = " + logNegative);

    IO.println("Is NaN: " + Double.isNaN(nanResult));
    IO.println("NaN == NaN: " + (nanResult == nanResult));
}
```

NaN has the unusual property of not being equal to itself. This is by design in  
IEEE 754—`NaN == NaN` always returns `false`. Use `Double.isNaN` or `Float.isNaN`  
to check for NaN values. NaN propagates through calculations: any operation  
involving NaN produces NaN.  


## Special values - Infinity  

Positive and negative infinity represent overflow results and mathematical  
infinity.  

```java
void main() {

    double posInf = 1.0 / 0.0;
    double negInf = -1.0 / 0.0;
    double overflow = Double.MAX_VALUE * 2;

    IO.println("1.0 / 0.0 = " + posInf);
    IO.println("-1.0 / 0.0 = " + negInf);
    IO.println("MAX_VALUE * 2 = " + overflow);

    IO.println("Is infinite: " + Double.isInfinite(posInf));
    IO.println("Positive infinity: " + Double.POSITIVE_INFINITY);
    IO.println("Negative infinity: " + Double.NEGATIVE_INFINITY);

    IO.println("Infinity - Infinity = " + (posInf - posInf));
    IO.println("1.0 / Infinity = " + (1.0 / posInf));
}
```

Unlike integer division by zero which throws an exception, floating-point  
division by zero produces infinity. Operations with infinity follow mathematical  
rules: infinity minus infinity produces NaN, and finite values divided by  
infinity produce zero.  


## Signed zero  

IEEE 754 distinguishes between positive zero and negative zero, though they  
compare as equal.  

```java
void main() {

    double posZero = 0.0;
    double negZero = -0.0;

    IO.println("Positive zero: " + posZero);
    IO.println("Negative zero: " + negZero);
    IO.println("+0.0 == -0.0: " + (posZero == negZero));

    IO.println("1.0 / +0.0 = " + (1.0 / posZero));
    IO.println("1.0 / -0.0 = " + (1.0 / negZero));

    IO.println("Compare: " + Double.compare(posZero, negZero));
}
```

While `+0.0 == -0.0` returns `true`, dividing by them produces different  
infinities. `Double.compare` distinguishes between positive and negative zero,  
returning a non-zero value. This behavior is important in mathematical functions  
where the sign of zero carries meaning.  


## Checking for finite values  

Testing whether a value is finite (not NaN or Infinity) is essential for robust  
numerical code.  

```java
void main() {

    double[] values = {
        1.0, Double.NaN, Double.POSITIVE_INFINITY, 
        Double.NEGATIVE_INFINITY, 0.0, -0.0
    };

    for (double v : values) {
        IO.println(v + " -> isNaN: " + Double.isNaN(v) + 
                   ", isInfinite: " + Double.isInfinite(v) +
                   ", isFinite: " + Double.isFinite(v));
    }
}
```

The `Double.isFinite` method returns `true` only if the value is neither NaN nor  
infinity. This is useful for validating inputs and results in numerical  
calculations. Always validate floating-point inputs before using them in  
critical calculations.  


## Equality comparison pitfall  

Direct equality comparison with `==` fails for many floating-point calculations.  

```java
void main() {

    double a = 1.0;
    double b = 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1;

    IO.println("a = " + a);
    IO.println("b = " + String.format("%.20f", b));
    IO.println("a == b: " + (a == b));
    IO.println("Difference: " + Math.abs(a - b));
}
```

Adding 0.1 ten times does not equal exactly 1.0 due to accumulated rounding  
errors. Each addition compounds the representation error. This demonstrates why  
direct equality testing is unreliable for floating-point values computed through  
different paths.  


## Epsilon-based comparison  

Using a tolerance (epsilon) value enables practical floating-point comparisons.  

```java
void main() {

    double a = 1.0;
    double b = 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1;
    double epsilon = 1e-10;

    boolean equal = Math.abs(a - b) < epsilon;
    IO.println("Using epsilon comparison: " + equal);

    double c = 1000000.0;
    double d = c + 0.0000001;
    IO.println("Large values differ? " + (Math.abs(c - d) < epsilon));
}
```

Epsilon comparison works for values near zero but becomes problematic for large  
values. A fixed epsilon may be too large for small values and too small for  
large values. Consider using relative epsilon or `Math.ulp` for more robust  
comparisons.  


## Relative epsilon comparison  

A relative epsilon adapts to the magnitude of the numbers being compared.  

```java
void main() {

    double a = 1000000.1;
    double b = 1000000.2;

    double absoluteEpsilon = 1e-10;
    double relativeEpsilon = 1e-9;

    boolean absoluteEqual = Math.abs(a - b) < absoluteEpsilon;
    boolean relativeEqual = Math.abs(a - b) < relativeEpsilon * Math.max(Math.abs(a), Math.abs(b));

    IO.println("Absolute comparison: " + absoluteEqual);
    IO.println("Relative comparison: " + relativeEqual);
}
```

Relative epsilon scales with the magnitude of the values. For very large numbers,  
the absolute difference can be large even when the relative difference is tiny.  
This approach provides more consistent behavior across different value ranges.  


## Using Math.ulp for comparison  

Unit in the Last Place (ULP) represents the smallest difference between two  
consecutive floating-point values.  

```java
void main() {

    double small = 1.0;
    double large = 1_000_000.0;

    IO.println("ULP of 1.0: " + Math.ulp(small));
    IO.println("ULP of 1,000,000: " + Math.ulp(large));

    double a = 1.0;
    double b = a + Math.ulp(a);
    IO.println("1.0 and next representable: differ by " + (b - a));

    int ulpDifference = 3;
    boolean closeEnough = Math.abs(a - b) <= ulpDifference * Math.ulp(Math.max(a, b));
    IO.println("Within 3 ULPs: " + closeEnough);
}
```

ULP-based comparison is the most precise way to compare floating-point values.  
Two values within a few ULPs of each other are essentially equal for practical  
purposes. The ULP size increases with the magnitude of the number, providing  
automatic scaling.  


## Error accumulation  

Repeated operations accumulate errors, potentially leading to significant  
inaccuracies.  

```java
void main() {

    double sum = 0.0;
    int iterations = 1_000_000;

    for (int i = 0; i < iterations; i++) {
        sum += 0.1;
    }

    double expected = iterations * 0.1;
    IO.println("Accumulated sum: " + sum);
    IO.println("Expected: " + expected);
    IO.println("Error: " + Math.abs(sum - expected));
}
```

Adding 0.1 one million times produces a noticeable error. The error grows with  
each operation because each addition introduces a small rounding error. This is  
particularly problematic in simulations and iterative algorithms that perform  
millions of calculations.  


## Kahan summation algorithm  

The Kahan summation algorithm reduces accumulated errors in floating-point sums.  

```java
void main() {

    int iterations = 1_000_000;

    double naiveSum = 0.0;
    for (int i = 0; i < iterations; i++) {
        naiveSum += 0.1;
    }

    double kahanSum = 0.0;
    double compensation = 0.0;
    for (int i = 0; i < iterations; i++) {
        double y = 0.1 - compensation;
        double t = kahanSum + y;
        compensation = (t - kahanSum) - y;
        kahanSum = t;
    }

    double expected = iterations * 0.1;
    IO.println("Naive sum error: " + Math.abs(naiveSum - expected));
    IO.println("Kahan sum error: " + Math.abs(kahanSum - expected));
}
```

Kahan summation tracks the lost low-order bits and compensates in subsequent  
additions. This algorithm is particularly valuable when summing many small values  
or when values vary greatly in magnitude. Many numerical libraries use this or  
similar compensated summation techniques.  


## Overflow behavior  

Exceeding the maximum representable value causes overflow to infinity.  

```java
void main() {

    double big = Double.MAX_VALUE;
    IO.println("MAX_VALUE: " + big);
    IO.println("MAX_VALUE * 1.1: " + (big * 1.1));
    IO.println("MAX_VALUE + MAX_VALUE: " + (big + big));

    float floatBig = Float.MAX_VALUE;
    IO.println("Float MAX_VALUE * 2: " + (floatBig * 2));

    double result = 1e308 * 1e10;
    IO.println("1e308 * 1e10 = " + result);
}
```

Unlike integer overflow which wraps around, floating-point overflow produces  
infinity. This is often more detectable but can still cause problems if not  
handled. Always check for infinite results when working with potentially large  
values.  


## Underflow behavior  

Values smaller than can be represented become zero or denormalized numbers.  

```java
void main() {

    double tiny = Double.MIN_NORMAL;
    IO.println("MIN_NORMAL: " + tiny);
    IO.println("MIN_NORMAL / 2: " + (tiny / 2));
    IO.println("MIN_NORMAL / 1e20: " + (tiny / 1e20));

    double denormal = Double.MIN_VALUE;
    IO.println("MIN_VALUE (denormal): " + denormal);
    IO.println("MIN_VALUE / 2: " + (denormal / 2));
}
```

Gradual underflow through denormalized numbers helps maintain numerical  
properties but with reduced precision. Operations in the denormalized range  
can be slower on some processors and should be avoided when performance is  
critical.  


## Loss of significance  

Subtracting nearly equal numbers loses significant digits of precision.  

```java
void main() {

    double a = 1.0000000000000001;
    double b = 1.0;

    double diff = a - b;
    IO.println("a = " + a);
    IO.println("b = " + b);
    IO.println("a - b = " + diff);
    IO.println("Expected: 1e-16");

    double large1 = 1234567890.123456;
    double large2 = 1234567890.123455;
    IO.println("Large subtraction: " + (large1 - large2));
}
```

When subtracting two nearly equal large numbers, most significant bits cancel  
out, leaving only the least significant bits which may contain mostly rounding  
errors. This catastrophic cancellation can destroy accuracy in numerical  
algorithms.  


## Associativity failure  

Floating-point addition is not associative due to rounding at each step.  

```java
void main() {

    double a = 1e16;
    double b = -1e16;
    double c = 1.0;

    double result1 = (a + b) + c;
    double result2 = a + (b + c);

    IO.println("(a + b) + c = " + result1);
    IO.println("a + (b + c) = " + result2);
    IO.println("Equal? " + (result1 == result2));
}
```

In exact arithmetic, both expressions equal 1.0. However, `b + c` loses the  
small value `c` because `1e16` is too large to represent the difference of 1.  
This non-associativity means the order of operations matters for floating-point  
accuracy.  


## Float vs Double precision  

Comparing precision between float and double shows significant differences.  

```java
void main() {

    float f = 1.0f / 3.0f;
    double d = 1.0 / 3.0;

    IO.println("Float 1/3: " + String.format("%.20f", f));
    IO.println("Double 1/3: " + String.format("%.20f", d));

    float fSum = 0.0f;
    double dSum = 0.0;
    for (int i = 0; i < 1000; i++) {
        fSum += 0.001f;
        dSum += 0.001;
    }

    IO.println("Float sum: " + fSum);
    IO.println("Double sum: " + dSum);
}
```

Float provides roughly 7 significant decimal digits while double provides about  
15-16. For most applications, double is preferred due to its greater precision.  
Float is mainly useful when memory is constrained or when interacting with APIs  
that require single precision.  


## BigDecimal introduction  

`BigDecimal` provides arbitrary-precision decimal arithmetic, essential for  
financial calculations.  

```java
import java.math.BigDecimal;

void main() {

    var a = new BigDecimal("0.1");
    var b = new BigDecimal("0.2");
    var sum = a.add(b);

    IO.println("BigDecimal 0.1 + 0.2 = " + sum);
    IO.println("Equals 0.3? " + sum.equals(new BigDecimal("0.3")));

    double doubleSum = 0.1 + 0.2;
    IO.println("Double 0.1 + 0.2 = " + doubleSum);
}
```

BigDecimal represents decimal numbers exactly using a scaled integer approach.  
Always use the String constructor to avoid inheriting floating-point  
imprecisions. For financial applications where exact decimal arithmetic is  
required, BigDecimal is the only correct choice.  


## BigDecimal from double pitfall  

Creating BigDecimal from double inherits the double's imprecision.  

```java
import java.math.BigDecimal;

void main() {

    var fromDouble = new BigDecimal(0.1);
    var fromString = new BigDecimal("0.1");

    IO.println("From double: " + fromDouble);
    IO.println("From string: " + fromString);
    IO.println("Equal? " + fromDouble.equals(fromString));

    var correct = BigDecimal.valueOf(0.1);
    IO.println("Using valueOf: " + correct);
}
```

The double constructor captures the actual value stored in the double, including  
its imprecision. Always use the String constructor for exact decimal values.  
`BigDecimal.valueOf` is an alternative that uses the canonical string  
representation of the double.  


## BigDecimal rounding modes  

BigDecimal supports various rounding modes for precise control over calculations.  

```java
import java.math.BigDecimal;
import java.math.RoundingMode;

void main() {

    var value = new BigDecimal("2.5");

    IO.println("UP: " + value.setScale(0, RoundingMode.UP));
    IO.println("DOWN: " + value.setScale(0, RoundingMode.DOWN));
    IO.println("CEILING: " + value.setScale(0, RoundingMode.CEILING));
    IO.println("FLOOR: " + value.setScale(0, RoundingMode.FLOOR));
    IO.println("HALF_UP: " + value.setScale(0, RoundingMode.HALF_UP));
    IO.println("HALF_DOWN: " + value.setScale(0, RoundingMode.HALF_DOWN));
    IO.println("HALF_EVEN: " + value.setScale(0, RoundingMode.HALF_EVEN));

    var negative = new BigDecimal("-2.5");
    IO.println("Negative HALF_UP: " + negative.setScale(0, RoundingMode.HALF_UP));
}
```

`HALF_EVEN` (banker's rounding) minimizes cumulative rounding error over many  
operations by rounding to the nearest even number when equidistant. This is the  
default in IEEE 754 and is recommended for statistical or financial aggregations.  


## BigDecimal division  

Division with BigDecimal requires specifying scale and rounding mode to handle  
non-terminating decimals.  

```java
import java.math.BigDecimal;
import java.math.RoundingMode;

void main() {

    var a = new BigDecimal("10");
    var b = new BigDecimal("3");

    try {
        var result = a.divide(b);
    } catch (ArithmeticException e) {
        IO.println("Exception without scale: " + e.getMessage());
    }

    var result = a.divide(b, 10, RoundingMode.HALF_UP);
    IO.println("10 / 3 with 10 decimal places: " + result);

    var exact = new BigDecimal("10").divide(new BigDecimal("4"));
    IO.println("Exact division 10/4: " + exact);
}
```

Division can produce non-terminating decimals (like 10/3), which cannot be  
represented exactly. Without specifying scale and rounding mode, BigDecimal  
throws ArithmeticException. Always specify these for division unless you know  
the result terminates.  


## Financial calculation example  

Proper handling of currency calculations using BigDecimal.  

```java
import java.math.BigDecimal;
import java.math.RoundingMode;

void main() {

    var price = new BigDecimal("19.99");
    var quantity = new BigDecimal("3");
    var taxRate = new BigDecimal("0.0825");

    var subtotal = price.multiply(quantity);
    var tax = subtotal.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    var total = subtotal.add(tax);

    IO.println("Price: $" + price);
    IO.println("Quantity: " + quantity);
    IO.println("Subtotal: $" + subtotal);
    IO.println("Tax (8.25%): $" + tax);
    IO.println("Total: $" + total);

    double dPrice = 19.99;
    double dTotal = dPrice * 3 * 1.0825;
    IO.println("Double calculation: $" + dTotal);
}
```

Financial applications must use BigDecimal to ensure accuracy. Even small  
rounding errors can accumulate to significant discrepancies in large  
transactions or many operations. Most financial regulations require specific  
rounding behavior that only BigDecimal can guarantee.  


## Math.round and rounding  

Java's built-in rounding functions provide convenient but limited options.  

```java
void main() {

    double value = 2.5;
    IO.println("Math.round(2.5): " + Math.round(value));
    IO.println("Math.round(-2.5): " + Math.round(-2.5));

    IO.println("Math.floor(2.7): " + Math.floor(2.7));
    IO.println("Math.ceil(2.3): " + Math.ceil(2.3));

    double precise = 2.555;
    IO.println("Math.round(2.555 * 100) / 100.0: " + 
               Math.round(precise * 100) / 100.0);
}
```

`Math.round` uses round-half-up for positive numbers but round-half-down for  
negative numbers (towards positive infinity). The multiply-round-divide pattern  
for rounding to decimal places can introduce additional errors. For precise  
rounding, use BigDecimal.  


## DecimalFormat for display  

DecimalFormat controls how floating-point numbers are displayed without changing  
their value.  

```java
import java.text.DecimalFormat;

void main() {

    double value = 1234567.89123;

    var df1 = new DecimalFormat("#,###.##");
    var df2 = new DecimalFormat("0.000");
    var df3 = new DecimalFormat("$#,###.00");
    var df4 = new DecimalFormat("0.###E0");

    IO.println("Pattern #,###.##: " + df1.format(value));
    IO.println("Pattern 0.000: " + df2.format(value));
    IO.println("Pattern $#,###.00: " + df3.format(value));
    IO.println("Scientific: " + df4.format(value));
}
```

DecimalFormat is for display purposes only—it formats a number into a string  
for output. It does not perform precise calculations. Use BigDecimal for  
calculations and DecimalFormat only for final output formatting.  


## Mixing int and double  

Type promotion when mixing integer and floating-point types can cause surprises.  

```java
void main() {

    int a = 7;
    int b = 3;

    double result1 = a / b;
    double result2 = (double) a / b;
    double result3 = a / (double) b;
    double result4 = (double) (a / b);

    IO.println("a / b (int division): " + result1);
    IO.println("(double) a / b: " + result2);
    IO.println("a / (double) b: " + result3);
    IO.println("(double) (a / b): " + result4);
}
```

Integer division occurs before type conversion if both operands are integers.  
`7 / 3` equals `2` as integer division, then converts to `2.0`. Cast at least  
one operand to double before division to get floating-point division.  


## Float to double conversion  

Converting between float and double can introduce unexpected precision.  

```java
void main() {

    float f = 0.1f;
    double d = f;

    IO.println("float 0.1f: " + f);
    IO.println("Promoted to double: " + d);
    IO.println("With precision: " + String.format("%.20f", d));

    double exact = 0.1;
    IO.println("Double 0.1: " + String.format("%.20f", exact));
    IO.println("Are they equal? " + (d == exact));
}
```

When a float is widened to double, the exact bit pattern is preserved and  
extended with zeros. This reveals the actual value stored in the float, which  
differs from the double representation of the same decimal. This can cause  
comparison failures.  


## StrictMath vs Math  

StrictMath guarantees identical results across platforms at potential  
performance cost.  

```java
void main() {

    double value = 0.5;

    IO.println("Math.sin: " + Math.sin(value));
    IO.println("StrictMath.sin: " + StrictMath.sin(value));

    IO.println("Math.exp: " + Math.exp(value));
    IO.println("StrictMath.exp: " + StrictMath.exp(value));

    IO.println("Math.pow(2, 10): " + Math.pow(2, 10));
    IO.println("StrictMath.pow(2, 10): " + StrictMath.pow(2, 10));
}
```

Math methods may use platform-specific optimizations that produce slightly  
different results on different CPUs or JVMs. StrictMath uses software  
implementations that guarantee bit-identical results everywhere. Use StrictMath  
when reproducibility across platforms is required.  


## strictfp modifier  

The `strictfp` modifier ensures strict floating-point semantics for a class or  
method.  

```java
strictfp class StrictCalculation {
    static double calculate(double a, double b) {
        return a * b + a / b;
    }
}

void main() {

    double result = StrictCalculation.calculate(1e308, 1e-308);
    IO.println("Strict result: " + result);

    IO.println("Note: Since Java 17, all floating-point operations");
    IO.println("are strictfp by default (JEP 306)");
}
```

Before Java 17, floating-point operations could use extended precision on some  
platforms, leading to different results. JEP 306 made strictfp the default,  
so modern Java code always uses strict IEEE 754 semantics. The modifier is  
now effectively a no-op but retained for source compatibility.  


## Comparing wrapper objects  

Comparing Float and Double objects has additional pitfalls beyond primitive  
comparison.  

```java
void main() {

    Double a = 0.1 + 0.2;
    Double b = 0.3;

    IO.println("a.equals(b): " + a.equals(b));
    IO.println("a == b: " + (a == b));

    Double c = 1.0;
    Double d = 1.0;
    IO.println("1.0 == 1.0 (objects): " + (c == d));

    Double nanA = Double.NaN;
    Double nanB = Double.NaN;
    IO.println("NaN.equals(NaN): " + nanA.equals(nanB));
    IO.println("NaN == NaN (objects): " + (nanA == nanB));
}
```

Object `==` compares references, not values. `equals` compares values but still  
uses exact comparison. Unlike primitive NaN, wrapper NaN objects can be reference  
equal if cached, and `equals` returns true for two NaN wrappers—contrary to  
primitive behavior.  


## Parsing floating-point strings  

Converting strings to floating-point numbers requires handling parse errors and  
locale issues.  

```java
void main() {

    IO.println("Parse '3.14': " + Double.parseDouble("3.14"));
    IO.println("Parse '-1.5e10': " + Double.parseDouble("-1.5e10"));
    IO.println("Parse 'NaN': " + Double.parseDouble("NaN"));
    IO.println("Parse 'Infinity': " + Double.parseDouble("Infinity"));

    try {
        Double.parseDouble("3,14");
    } catch (NumberFormatException e) {
        IO.println("Cannot parse '3,14' (European format)");
    }

    IO.println("valueOf: " + Double.valueOf("123.456"));
}
```

`parseDouble` and `valueOf` use US locale conventions (period as decimal  
separator). For locale-sensitive parsing, use `NumberFormat`. Scientific  
notation, NaN, and Infinity strings are all valid inputs. Invalid strings throw  
NumberFormatException.  


## Binary representation  

Examining the actual binary representation of floating-point numbers.  

```java
void main() {

    double value = 0.1;
    long bits = Double.doubleToLongBits(value);

    IO.println("Value: " + value);
    IO.println("Bits (hex): " + Long.toHexString(bits));
    IO.println("Bits (binary): " + Long.toBinaryString(bits));

    long sign = (bits >> 63) & 1;
    long exponent = (bits >> 52) & 0x7FF;
    long mantissa = bits & 0xFFFFFFFFFFFFFL;

    IO.println("Sign: " + sign);
    IO.println("Exponent: " + exponent + " (biased), " + (exponent - 1023) + " (actual)");
    IO.println("Mantissa: " + Long.toBinaryString(mantissa));

    double reconstructed = Double.longBitsToDouble(bits);
    IO.println("Reconstructed: " + reconstructed);
}
```

`doubleToLongBits` reveals the actual IEEE 754 representation. The exponent is  
stored with a bias of 1023 for doubles. Understanding the binary representation  
helps explain why certain decimal values cannot be represented exactly and how  
precision decreases with magnitude.  


## Next representable values  

Finding adjacent floating-point values reveals the discrete nature of the  
representation.  

```java
void main() {

    double value = 1.0;
    double next = Math.nextUp(value);
    double prev = Math.nextDown(value);

    IO.println("Value: " + value);
    IO.println("Next representable: " + next);
    IO.println("Previous representable: " + prev);
    IO.println("Gap up: " + (next - value));
    IO.println("Gap down: " + (value - prev));

    double large = 1e15;
    IO.println("Gap at 1e15: " + (Math.nextUp(large) - large));

    double small = 1e-10;
    IO.println("Gap at 1e-10: " + (Math.nextUp(small) - small));
}
```

The gap between representable values increases with magnitude. Near 1.0, the gap  
is about 2.2e-16. Near 1e15, the gap is about 0.125—meaning values cannot be  
represented with sub-integer precision at that magnitude. This explains why  
precision decreases for larger numbers.  


## Subnormal numbers  

Subnormal (denormalized) numbers allow gradual underflow at the cost of  
precision.  

```java
void main() {

    double normal = Double.MIN_NORMAL;
    double subnormal = normal / 2;

    IO.println("MIN_NORMAL: " + normal);
    IO.println("Half of MIN_NORMAL: " + subnormal);
    IO.println("Is subnormal: " + (subnormal < Double.MIN_NORMAL && subnormal > 0));

    double tiny = Double.MIN_VALUE;
    IO.println("MIN_VALUE (smallest subnormal): " + tiny);
    IO.println("MIN_VALUE / 2: " + (tiny / 2));

    IO.println("Subnormal range: [" + Double.MIN_VALUE + ", " + Double.MIN_NORMAL + ")");
}
```

Subnormal numbers have reduced precision because the leading "1" bit is no  
longer implicit. They fill the gap between zero and the smallest normal number,  
preventing abrupt underflow to zero. However, operations with subnormals can be  
significantly slower on some hardware.  


## Fused multiply-add  

FMA performs multiply-add with a single rounding, improving accuracy and  
performance.  

```java
void main() {

    double a = 1.0 + 1e-15;
    double b = 1.0 - 1e-15;
    double c = -1.0;

    double standard = a * b + c;
    double fma = Math.fma(a, b, c);

    IO.println("a * b + c (standard): " + standard);
    IO.println("Math.fma(a, b, c): " + fma);
    IO.println("Expected: " + (1e-30));

    double x = 0.1;
    double y = 10.0;
    double z = -1.0;
    IO.println("Standard: " + (x * y + z));
    IO.println("FMA: " + Math.fma(x, y, z));
}
```

FMA computes `a * b + c` with only one rounding at the end instead of two  
(one for multiplication, one for addition). This can provide better accuracy for  
certain numerical algorithms. Modern CPUs have hardware FMA instructions, making  
it efficient as well.  


## Performance comparison: double vs BigDecimal  

BigDecimal is significantly slower than primitive double for arithmetic.  

```java
import java.math.BigDecimal;
import java.math.MathContext;

void main() {

    int iterations = 1_000_000;

    long startDouble = System.nanoTime();
    double sumDouble = 0.0;
    for (int i = 0; i < iterations; i++) {
        sumDouble += 0.1;
        sumDouble *= 1.001;
    }
    long doubleTime = System.nanoTime() - startDouble;

    long startBigDecimal = System.nanoTime();
    var sumBD = BigDecimal.ZERO;
    var increment = new BigDecimal("0.1");
    var multiplier = new BigDecimal("1.001");
    for (int i = 0; i < iterations; i++) {
        sumBD = sumBD.add(increment);
        sumBD = sumBD.multiply(multiplier, MathContext.DECIMAL64);
    }
    long bdTime = System.nanoTime() - startBigDecimal;

    IO.println("Double time: " + (doubleTime / 1_000_000) + " ms");
    IO.println("BigDecimal time: " + (bdTime / 1_000_000) + " ms");
    IO.println("Ratio: " + (bdTime / doubleTime) + "x slower");
}
```

BigDecimal can be 10-100x slower than primitive double operations. For  
performance-critical numerical computations like graphics or physics, use  
double. Reserve BigDecimal for scenarios where exact decimal arithmetic is  
required, such as financial calculations.  


## Floating-point in loops  

Using floating-point numbers as loop counters can cause unexpected behavior.  

```java
void main() {

    int count1 = 0;
    for (double d = 0.0; d < 1.0; d += 0.1) {
        count1++;
    }
    IO.println("Loop with d += 0.1: " + count1 + " iterations");

    int count2 = 0;
    for (double d = 0.0; d <= 1.0; d += 0.1) {
        count2++;
    }
    IO.println("Loop with d <= 1.0: " + count2 + " iterations");

    IO.println("Better approach:");
    int count3 = 0;
    for (int i = 0; i < 10; i++) {
        double d = i * 0.1;
        count3++;
    }
    IO.println("Integer loop counter: " + count3 + " iterations");
}
```

Accumulated rounding errors in loop counters can cause off-by-one errors or  
even infinite loops. The last iteration may or may not execute depending on  
accumulated error. Use integer loop counters and calculate the floating-point  
value from the integer when needed.  


## Floating-point in HashMap keys  

Using Float or Double as HashMap keys is problematic due to comparison issues.  

```java
void main() {

    var map = new HashMap<Double, String>();

    map.put(0.1 + 0.2, "calculated");
    map.put(0.3, "literal");

    IO.println("Map size: " + map.size());
    IO.println("Get 0.3: " + map.get(0.3));
    IO.println("Get 0.1+0.2: " + map.get(0.1 + 0.2));

    IO.println("Keys: " + map.keySet());

    Map<Double, String> nanMap = new HashMap<>();
    nanMap.put(Double.NaN, "NaN value");
    IO.println("Get NaN: " + nanMap.get(Double.NaN));
}
```

`0.1 + 0.2` and `0.3` have different hash codes because they are not equal.  
This results in two separate entries. NaN keys work because Double.equals treats  
all NaNs as equal, contrary to primitive comparison. Avoid using floating-point  
values as map keys unless you understand these implications.  


## Floating-point in switch  

Floating-point values cannot be used in switch statements, requiring alternatives.  

```java
void main() {

    double value = 1.5;

    if (Math.abs(value - 1.0) < 0.01) {
        IO.println("Approximately one");
    } else if (Math.abs(value - 1.5) < 0.01) {
        IO.println("Approximately one and a half");
    } else if (Math.abs(value - 2.0) < 0.01) {
        IO.println("Approximately two");
    }

    int scaled = (int) Math.round(value * 2);
    String result = switch (scaled) {
        case 2 -> "One";
        case 3 -> "One and a half";
        case 4 -> "Two";
        default -> "Other";
    };
    IO.println("Switch result: " + result);
}
```

Java prohibits float and double in switch statements because exact equality  
comparison is unreliable. Use if-else with epsilon comparison, or scale to  
integers if appropriate. The integer scaling approach works when the floating-  
point values represent discrete choices.  


## Arrays and floating-point  

Working with arrays of floating-point numbers has performance considerations.  

```java
void main() {

    double[] prices = {19.99, 29.99, 9.99, 49.99, 14.99};

    double sum = 0;
    for (double price : prices) {
        sum += price;
    }
    IO.println("Sum (loop): " + sum);

    double streamSum = java.util.Arrays.stream(prices).sum();
    IO.println("Sum (stream): " + streamSum);

    double average = java.util.Arrays.stream(prices).average().orElse(0);
    IO.println("Average: " + average);

    double max = java.util.Arrays.stream(prices).max().orElse(0);
    IO.println("Maximum: " + max);

    java.util.Arrays.sort(prices);
    IO.println("Sorted: " + java.util.Arrays.toString(prices));
}
```

Primitive double arrays are more memory-efficient than Double object arrays.  
Stream operations on double arrays use DoubleStream which avoids boxing  
overhead. For large numerical computations, primitive arrays significantly  
outperform collections of wrapper objects.  


## Statistical accuracy considerations  

Computing statistical measures requires attention to numerical stability.  

```java
void main() {

    double[] data = {1e9, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    double sum = 0;
    for (double d : data) {
        sum += d;
    }
    double mean = sum / data.length;

    double varianceSum = 0;
    for (double d : data) {
        varianceSum += (d - mean) * (d - mean);
    }
    double variance = varianceSum / data.length;

    IO.println("Mean: " + mean);
    IO.println("Variance: " + variance);

    double[] normalized = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    sum = 0;
    for (double d : normalized) sum += d;
    mean = sum / normalized.length;
    varianceSum = 0;
    for (double d : normalized) varianceSum += (d - mean) * (d - mean);
    IO.println("Normalized variance: " + (varianceSum / normalized.length));
}
```

When data values vary widely in magnitude, subtracting the mean can cause loss  
of significance. For more accurate variance calculation, use Welford's online  
algorithm or other numerically stable methods. Libraries like Apache Commons  
Math provide robust statistical functions.  


## Random floating-point numbers  

Random number generation with floating-point has its own characteristics.  

```java
import java.util.Random;

void main() {

    var random = new Random(42);

    IO.println("Random doubles [0, 1):");
    for (int i = 0; i < 5; i++) {
        IO.println("  " + random.nextDouble());
    }

    IO.println("Random in range [10, 20):");
    for (int i = 0; i < 5; i++) {
        double value = 10 + random.nextDouble() * 10;
        IO.println("  " + value);
    }

    IO.println("Gaussian distribution:");
    for (int i = 0; i < 5; i++) {
        IO.println("  " + random.nextGaussian());
    }
}
```

`nextDouble` returns values in [0, 1) with uniform distribution. The range is  
half-open: it includes 0 but excludes 1. Random floating-point values still  
have the same representation limitations as other floating-point numbers—not  
all values in the range are equally likely to appear.  


## Guidelines for choosing float, double, or BigDecimal  

Selecting the appropriate type depends on the use case requirements.  

```java
import java.math.BigDecimal;
import java.math.RoundingMode;

void main() {

    IO.println("=== Use float for: ===");
    IO.println("- Graphics (GPU APIs often use float)");
    IO.println("- Large arrays where memory matters");
    float[] vertices = {1.0f, 2.0f, 3.0f};
    IO.println("Vertex example: " + vertices[0]);

    IO.println("\n=== Use double for: ===");
    IO.println("- General scientific computing");
    IO.println("- Physics simulations");
    IO.println("- Most decimal calculations");
    double gravity = 9.80665;
    IO.println("Gravity: " + gravity + " m/s²");

    IO.println("\n=== Use BigDecimal for: ===");
    IO.println("- Financial calculations");
    IO.println("- When exact decimal representation is required");
    IO.println("- When specific rounding modes matter");
    var price = new BigDecimal("99.99");
    var tax = price.multiply(new BigDecimal("0.08")).setScale(2, RoundingMode.HALF_UP);
    IO.println("Tax on $99.99 at 8%: $" + tax);
}
```

Float saves memory but has limited precision—use only when necessary. Double is  
the default choice for most numerical work, offering good precision and  
performance. BigDecimal is essential when exact decimal arithmetic matters, such  
as in financial software, despite its performance cost.  


## Summary and Best Practices  

Key guidelines for robust floating-point programming in Java:  

| Situation | Recommendation |  
|-----------|----------------|  
| Financial calculations | Always use `BigDecimal` with String constructor |  
| Equality comparison | Use epsilon or ULP-based comparison |  
| Loop counters | Use integers, compute float from index |  
| HashMap keys | Avoid float/double, or normalize values |  
| Scientific computing | Use double, consider numerical stability |  
| Graphics/GPU | Use float for memory and API compatibility |  
| Cross-platform consistency | Use StrictMath or strictfp (pre-Java 17) |  
| Accumulating sums | Consider Kahan summation or BigDecimal |  
| Display formatting | Use DecimalFormat, not for calculations |  
| Parsing user input | Handle NumberFormatException, validate result |  

Understanding floating-point limitations prevents subtle bugs. The IEEE 754  
representation cannot exactly represent most decimal fractions, leading to  
precision issues that accumulate through calculations. Choosing the right type  
and comparison method for each use case ensures both correctness and performance.  

Always validate floating-point inputs for NaN and infinity. Be aware of the  
magnitude of your values—precision decreases as magnitude increases. When exact  
decimal arithmetic is required, accept the performance cost of BigDecimal rather  
than trying to work around floating-point limitations.  
