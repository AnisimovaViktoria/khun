import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static int countX;
    private static int countY;
    private static int arrSize;
    private static int[] arr;
    private static boolean[] used;
    private static ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>();
    private static int[] mt;


    //читает входные данные
    private static void readGraph() throws FileNotFoundException {
        FileReader reader = new FileReader("in.txt");
        Scanner sc = new Scanner(reader);
        countX = sc.nextInt();
        countY = sc.nextInt();
        sc.nextLine();
        arrSize = sc.nextInt();
        sc.nextLine();
        arr = new int[arrSize];
        int i = 0;
        while (sc.hasNextLine()) {
            while (sc.hasNextInt()) {
                arr[i] = sc.nextInt();
                ++i;
            }
        }
        used = new boolean[countX];
        mt = new int[countY];
        for (int j = 0; j < mt.length; j++) {
            mt[j] = -1;
        }
    }

    private static void convert() {
        for (int i = 1; i <= countX; i++) {
            ArrayList<Integer> array = new ArrayList<>();
            int from = arr[i - 1] - 1;
            int to = arr[i] - 1;
            if (to - from < 0)
                to = arrSize - 1;
            for (int j = from; j < to; j++) {
                array.add(arr[j] - 1);
            }
            adj.add(array);
            if (arr[to] == 32767)
                if (i == countX)
                    break;
                else {
                    for (int k = i; k < countX; k++) {
                        adj.add(new ArrayList<Integer>());
                    }
                }
        }
    }

    private static boolean try_kuhn(int v) {
        if (used[v]) return false;
        used[v] = true;
        for (int i = 0; i < adj.get(v).size(); i++) {
            int to = adj.get(v).get(i);

            if (mt[to] == -1 || try_kuhn(mt[to])) {
                mt[to] = v;
                return true;
            }
        }
        return false;
    }

    private static void refreshUsed() {
        for (int i = 0; i < used.length; i++) {
            used[i] = false;
        }
    }

    private static void out() throws IOException {

        int errV = -1;
        int k = 0;
        FileWriter writer = new FileWriter("out.txt");
        for (int v = 0; v < countX; v++) {
            refreshUsed();
            if (try_kuhn(v) == false)
                errV = v + 1;
        }
        for (int i = 0; i < countY; i++) {
            if (mt[i] != -1) {
                k++;
            }
        }
        //вывод
        if (k == countX) {
            writer.write("Y");
            writer.write("\n");
            for (int j = 0; j < countY; j++) {
                if (mt[j] != -1) {
                    int v1 = mt[j] + 1;
                    int v2 = j + 1;
                    writer.write(v1 + "-" + v2 + "\n");
                }
            }
        } else {
            writer.write("N");
            writer.write("\n");
            writer.write(errV + "");
        }
        writer.flush();
    }

    public static void main(String[] args) throws IOException {
        readGraph();
        convert();
        out();
    }
}

