import FileIO.IO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GabCode
 * @version 1.0
 * @since 2.0
 */

public class DiffSearcher {
    private static File source = new File("resultOfScan1.txt");
    private static File secondary = new File("resultOfScan2.txt");

    private static File resFile = new File("diff.txt");

    private static List<List<Byte>> list = new ArrayList<>();
    private static List<List<Byte>> _list = new ArrayList<>();
    public static void main(String[] args) {
        System.out.println("Parsing bytes from files...");
        IO.parseBytes(source, secondary, list, _list);
        System.out.println("Parsed!");
        System.out.println("Algorithm started...");
        List<List<Byte>> result = uniqSigsFilter(list, _list);
        System.out.println("Algorithm ended!");
        System.out.println("Writing to file...");
        try {
            IO.resultToFile(resFile, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Ended!");
    }

    private static List<List<Byte>> uniqSigsFilter(List<List<Byte>> list, List<List<Byte>> _list) {
        List<List<Byte>> res = new ArrayList<>();
        tag: for (int i = 0; i < list.size(); ++i) {

            for (int j = 0; j < _list.size(); ++j) {
                if (list.get(i).equals(_list.get(j))) {
                    continue tag;
                }
            }
            res.add(list.get(i));
        }

        return res;
    }
}
