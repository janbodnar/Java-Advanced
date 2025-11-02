void main() {

  var data = List.of(1, 2, 3, 4, 5, 6, 7);

  // Consumer<Integer> consumer = (Integer x) -> IO::println(x);
  Consumer<Integer> consumer = IO::println;
  forEach(data, consumer);

  IO.println("--------------------------");

  forEach(data, IO::println);
}

<T> void forEach(List<T> data, Consumer<T> consumer) {

  for (T t : data) {
    consumer.accept(t);
  }
}
