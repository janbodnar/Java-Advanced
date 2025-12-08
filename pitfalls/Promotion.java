void main() {

  byte x = 10;
  byte y = 20;

  var res = (Object) (x + y);

  IO.println(res);
  IO.println(res.getClass().getName());
}
