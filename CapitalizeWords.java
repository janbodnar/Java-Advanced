
void main() {

    // works for Latin, more complex languages need a different solution

    List<String> words = List.of("rock", "forest", "sky", "cloud", "water");

    System.out.println(capitalize(words));
    System.out.println(capitalize2(words));
}

List<String> capitalize(List<String> words) {

    List<String> capitalized = new ArrayList<>();

    for (var word : words) {

        var sb = new StringBuilder(word);

        char c = sb.charAt(0);
        char upperCased = Character.toUpperCase(c);
        sb.setCharAt(0, upperCased);

        capitalized.add(sb.toString());
    }

    return capitalized;
}

List<String> capitalize2(List<String> words) {

    return words.stream()
            .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
            .toList();

}
