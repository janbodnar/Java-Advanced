// the program throws a NullPointerException
// we should swap the sides: "rock".equals(word)
void main() {

    var words = Arrays.asList("wood", "forest", "falcon", null, "sky", "rock");

    for (var word : words) {

        if (word.equals("rock")) {

            System.out.println("The list contains rock");
        }
    }
}
