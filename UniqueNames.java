void main() {

    List<String> names = List.of("Martin", "Lucy", "Peter",
            "Martin", "Robert", "Peter");

    System.out.println(unique(names));
    System.out.println(unique2(names));
    System.out.println(unique3(names));
}

List<String> unique(List<String> names) {

    List<String> uniqueNames = new ArrayList<>();

    names.forEach(e -> {

        if (!uniqueNames.contains(e)) {

            uniqueNames.add(e);
        }
    });

    return uniqueNames;
}

List<String> unique2(List<String> names) {

    return names.stream().distinct().toList();

}

List<String> unique3(List<String> names) {

    HashSet<String> uniqueNames = new HashSet<>(names);

    return new ArrayList<String>(uniqueNames);

}
