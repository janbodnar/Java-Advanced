
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

// the program reads data from a web page, validates it and 
// calculates sum from them
// uses custom validation method

void main(String[] args) throws IOException {

  String url = "https://test.webcode.me/data.txt";

  Document doc = Jsoup.connect(url).get();

  String content = doc.body().text();
  System.out.println(content);

  String[] vals = content.split(",\\s+");

  int sum = 0;

  for (String val : vals) {

    if (isNumeric(val)) {

      sum += Integer.parseInt(val);
    }
  }

  System.out.println(sum);
}

boolean isNumeric(String val) {

  int len = val.length();

  for (int i = 0; i < len; i++) {

    if (!Character.isDigit(val.charAt(i))) {

      return false;
    }
  }

  return true;
}
