void main() {

  var path = System.getenv("PATH");

  // Split using the system-specific path separator
  String[] directories = path.split(System.getProperty("path.separator"));

  for (var directory : directories) {

    IO.println(directory);
  }

  IO.println(String.format("There are %d items in the PATH variable%n",
      directories.length));

  String os_version = System.getProperty("os.name");
  String user_name = System.getProperty("user.name");
  String java_version = System.getProperty("java.version");
  String java_home = System.getProperty("java.home");

  IO.println(os_version);
  IO.println(user_name);
  IO.println(java_version);
  IO.println(java_home);
}
