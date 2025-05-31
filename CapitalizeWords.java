
void main() {

    // works for Latin, more complex languages need a different solution

    List<String> words = List.of("rock", "forest", "sky", "cloud", "water");
    List<String> capitalized = new ArrayList<>();

    for (var word : words) {

        var sb = new StringBuilder(word);

        char c = sb.charAt(0);
        char upperCased = Character.toUpperCase(c);
        sb.setCharAt(0, upperCased);

        capitalized.add(sb.toString());
    }

    System.out.println(capitalized);
}
