import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gabCode.
 * @version 1.2
 */
public class Searcher {
    private static File target = new File("app1.exe");
    private static File _target = new File("app2.exe");

    private static File resFile = new File("resultOfScan.txt"); // must be in root catalog
    private static final int START_GROUP = 3; // minimum length of signature

    private static List<List <Byte>> sigs = new ArrayList<>();

    public static void main(String[] args) {
        if (!target.isFile() || !_target.isFile()) throw new IllegalArgumentException("isn't file!"); // simple check
        byte[] targetBytes = null;
        byte[] _targetBytes = null;

        try {
            targetBytes = Files.readAllBytes(target.toPath()); // reading all bytes of first app
            _targetBytes = Files.readAllBytes(_target.toPath()); // reading all bytes of second app
        } catch (IOException e) {
            e.printStackTrace();
        }

        long start = System.nanoTime();
        sigSearch(targetBytes, _targetBytes); // signature searching
        System.out.println("Scan completed...");

        System.out.println("Filtering...");
        zeroKiller(sigs); // removing "zero"-arrays (signatures)

        System.out.println("Writing results to file / " + resFile.getName());
        try {
            resultToFile(resFile, sigs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long finish = System.nanoTime();
        System.out.println("All operations completed! Same signatures has been copied to " + resFile.getName() + " | Time: " + (finish - start) + " nanosec.");
    }

    private static void zeroKiller(List<List <Byte>> toFilter) {
        step: for (int i = 0; i < toFilter.size(); i++) {
            System.out.println(i + "/" + toFilter.size());
            for (int j = 0; j < toFilter.get(i).size(); ++j) {
                if (toFilter.get(i).get(j) != 0) {
                    continue step;
                }
            }

            toFilter.remove(i);
            i = 0;
        }
    }

    private static void sigSearch(byte[] targetBytes, byte[] _targetBytes) {
        for (int offset = 0; offset < (targetBytes.length - START_GROUP); ++offset) {
            System.out.println("Scanner: " + ( (float)offset / (targetBytes.length - START_GROUP) * 100) + "%");
            List<Byte> targetComb = new ArrayList<>();

            for (int i = 0; i < START_GROUP; ++i) {
                targetComb.add(targetBytes[offset + i]);
            }

            step: for (int _offset = 0; _offset < (_targetBytes.length - START_GROUP); ++_offset) {
                List<Byte> confirmedBytes = new ArrayList<>();

                for (int i = 0; i < START_GROUP; ++i) {
                    if (_targetBytes[_offset + i] == targetComb.get(i)) {
                        confirmedBytes.add(_targetBytes[_offset + i]);
                    } else {
                        confirmedBytes.clear();
                        continue step;
                    }
                }

                boolean close = false;
                int toAdd = 0;
                for (int extra = 0; true; extra++) {
                    try {
                        if (_targetBytes[_offset + START_GROUP + extra] == targetBytes[offset + START_GROUP + extra]) {
                            confirmedBytes.add(_targetBytes[_offset + START_GROUP + extra]);
                            toAdd++;
                        } else {
                            break;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        close = true;
                        break;
                    }
                }
                offset += START_GROUP + toAdd;

                sigs.add(new ArrayList<>());

                for (byte confirmedByte : confirmedBytes) {
                    sigs.get(sigs.size() - 1).add(confirmedByte);
                }

                if(close) return;
            }
        }
    }

    private static void resultToFile(File writeTo, List<List <Byte>> bytesResult) throws IOException {
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
}