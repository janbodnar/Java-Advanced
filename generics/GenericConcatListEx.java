
void main() {

  var vals1 = List.of(1, 2, 3);
  var vals2 = List.of(4, 5, 6);
  var vals3 = List.of(7, 8, 9);

  var vals = concatenate(vals1, vals2, vals3);

  IO.println(vals);

  var words1 = List.of("pen", "paper", "ink");
  var words2 = List.of("sky", "cloud", "wind");
  var words3 = List.of("tree", "forest", "wood");

  var words = concatenate(words1, words2, words3);

  IO.println(words);
}

@SafeVarargs
final <T> List<T> concatenate(List<T>... lists) {
  return new ArrayList<>() {
    {

      for (List<T> mylist : lists) {

        addAll(mylist);
      }
    }
  };
}
