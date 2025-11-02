
void main() {

  Integer[] vals = { 2, 4, 6, 8, 10, 12 };
  Float[] vals2 = { 2.4f, 4.2f, 6.1f, 8.4f, 10.2f, 12.1f };

  var myList = mapArrayToList(vals, e -> e * e);
  System.out.println(myList);

  var myList2 = mapArrayToList(vals2, e -> e * e);
  System.out.println(myList2);
}

<T, R> List<R> mapArrayToList(T[] a, Function<T, R> mapFun) {

  return Arrays.stream(a).map(mapFun).collect(Collectors.toList());
}
