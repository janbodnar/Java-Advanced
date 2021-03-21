import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// sorting integers by creating a copy of the original 
// list, which is intact

public class ListSortIntegers2 {

    public static void main(String[] args) {

        List<Integer> vals = Arrays.asList(5, -4, 0, 2, -1, 4, 7, 6, 1, -1, 3, 8, -2);

        System.out.println("Ascending order");

        var sorted1 = vals.stream().sorted().toList();
        System.out.println(sorted1);

        System.out.println("-------------------------------");

        System.out.println("Descending order");

        var sorted2 = vals.stream().sorted(Comparator.reverseOrder()).toList();
        System.out.println(sorted2);

        System.out.println("-------------------------------");

        System.out.println("Original order");
        System.out.println(vals);
    }
}
