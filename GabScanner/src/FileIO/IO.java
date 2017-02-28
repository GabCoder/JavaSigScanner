package FileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GabCode
 * @version 1.0
 */
public class IO {
    public static void resultToFile(File writeTo, List<List<Byte>> bytesResult) throws IOException {
        List <Byte> biggerArray = new ArrayList<>();
        Files.write(writeTo.toPath(), ("").getBytes());

        while (bytesResult.size() != 0) {
            int index = 0;
            for (int i = 0; i < bytesResult.size(); i++) {
                if (bytesResult.get(i).size() > biggerArray.size()) {
                    biggerArray = bytesResult.get(i);
                    index = i;
                }
            }

            bytesResult.remove(index);

            Files.write(writeTo.toPath(), (biggerArray.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
            biggerArray.clear();
        }
    }

    public static void parseFromFile(File file, List<List<Byte>> list) {
        if (!file.exists()) throw new IllegalArgumentException(file.getName() + " not found :(");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String s;

            for (int i = 0; (s = reader.readLine()) != null; i++) {
                s = s.replace(" ", "");
                s = s.substring(1,s.length() - 1);
                //System.out.println(s);
                String[] stringBytes = s.split(",");

                list.add(new ArrayList<>());
                for (String stringByte : stringBytes) {
                    list.get(i).add(Byte.parseByte(stringByte));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseBytes(File res, File _res, List<List<Byte>> list, List<List<Byte>> _list) {
        IO.parseFromFile(res, list);
        IO.parseFromFile(_res, _list);
    }
}
