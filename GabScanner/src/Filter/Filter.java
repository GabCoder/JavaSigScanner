package Filter;

import java.util.List;

/**
 * @author GabCode
 * @version 1.2
 */

public class Filter {
    public static void zeroKiller(List<List<Byte>> toFilter) {
        step: for (int i = 0; i < toFilter.size(); i++) {
            //System.out.println(i + "/" + toFilter.size());
            for (int j = 0; j < toFilter.get(i).size(); ++j) {
                if (toFilter.get(i).get(j) != 0) {
                    continue step;
                }
            }

            toFilter.remove(i);
            i = 0;
        }
    }

    public static void sameKiller(List<List <Byte>> toFilter) {
        for (int i = 0; i < toFilter.size(); ++i) {

            for (int j = (i + 1); j < toFilter.size(); ++j) {
                if (toFilter.get(i).equals(toFilter.get(j))) {
                    toFilter.remove(j);
                    j--;
                }
            }
        }
    }
}
