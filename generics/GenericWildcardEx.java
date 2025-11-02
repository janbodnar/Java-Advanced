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
