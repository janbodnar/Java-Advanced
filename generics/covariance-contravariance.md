## Covariance & Contravariance


## Covariance & Contravariance table

| Feature | Covariance (Upper Bound) | Contravariance (Lower Bound) |
| :--- | :--- | :--- |
| **Common Name** | "Extends Wildcard" | "Super Wildcard" |
| **Syntax** | `? extends T` | `? super T` |
| **Example** | `List<? extends Number>` | `List<? super Integer>` |
| **What It Means** | A list of *some unknown type* that is `T` or a **subtype** of `T`. | A list of *some unknown type* that is `T` or a **supertype** of `T`. |
| **Type of Bound** | **Upper Bound** (`T` is the *highest* class in the hierarchy you can guarantee). | **Lower Bound** (`T` is the *lowest* class in the hierarchy you can guarantee). |
| **Valid List Types** | `List<Number>`, `List<Integer>`, `List<Double>` | `List<Integer>`, `List<Number>`, `List<Object>` |
| **Can you READ? (Get)** | **Yes.** You are guaranteed to get an object that is at least of type `T`. | **No** (safely). You can only be sure you're getting an `Object`, as you don't know how high up the superclass chain the list's type goes. |
| **Can you WRITE? (Add)** | **No** (except `null`). You don't know the *specific* subtype. A `List<? extends Number>` could be a `List<Integer>`, so you can't add a `Double`. | **Yes.** You can safely add any object of type `T` (or its subtypes). A `List<Integer>`, `List<Number>`, or `List<Object>` can all safely accept an `Integer`. |
| **PECS Mnemonic** | **P**roducer **E**xtends | **C**onsumer **S**uper |
| **Primary Use Case** | When you are **reading** items *from* a generic structure (it *produces* items for you). | When you are **writing** items *to* a generic structure (it *consumes* items from you). |



## Wildcards with upper bounds

This example shows using wildcard types with upper bounds to accept lists of  
subclasses.  

```java
abstract class Shape {

  abstract void draw();
}

class Rectangle extends Shape {

  @Override
  void draw() {
    IO.println("Drawing rectangle");
  }
}

class Circle extends Shape {

  @Override
  void draw() {
    IO.println("Drawing circle");
  }
}

// Generic wildcard example
void main() {

  List<Rectangle> shapes1 = new ArrayList<>();
  shapes1.add(new Rectangle());

  List<Circle> shapes2 = new ArrayList<>();
  shapes2.add(new Circle());
  shapes2.add(new Circle());

  drawShapes(shapes1);
  drawShapes(shapes2);
}

void drawShapes(List<? extends Shape> lists) {

  for (Shape s : lists) {
    s.draw();
  }
}
```

The `drawShapes` method uses a bounded wildcard `? extends Shape` to accept  
lists of any type that extends `Shape`. This demonstrates covariance - the  
method can accept `List<Rectangle>`, `List<Circle>`, or `List<Shape>`. The  
wildcard makes the method more flexible than `List<Shape>` alone, which would  
not accept lists of subtypes. This is useful for read-only operations where  
you need to process elements polymorphically without adding new elements.  
