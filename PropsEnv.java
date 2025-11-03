void main() {

  var path = System.getenv("PATH");

  // Split using the system-specific path separator
  String[] directories = path.split(System.getProperty("path.separator"));

  for (var directory : directories) {

    System.out.println(directory);
  }

  System.out.printf("There are %d items in the PATH variable%n",
      directories.length);

  String os_version = System.getProperty("os.name");
  String java_version = System.getProperty("java.version");
  String java_home = System.getProperty("java.home");

  System.out.println(os_version);
  System.out.println(java_version);
  System.out.println(java_home);
}
