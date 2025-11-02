
void main() {

  var words = List.of("rock", "sky", "blue", "ocean", "falcon");
  var vals = List.of(2, 3, 4, 5, 6, 7, 8);

  var e1 = getRandomElement(words);
  IO.println(e1);

  var e2 = getRandomElement(vals);
  IO.println(e2);
}

<T> T getRandomElement(List<T> list) {

  var random = new Random();
  int idx = random.nextInt(list.size());
  return list.get(idx);
}
