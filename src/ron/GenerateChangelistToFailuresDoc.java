package ron;

import io.ProgressTracker;
import io.Tsv;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Map;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class GenerateChangelistToFailuresDoc {
  public static void main(String[]args)throws Exception {
    String basedir = "/Users/ry23/Dropbox/cmu-sfdc/ron_mallet/";

    String changelists_tsv = basedir+"brokenby.txt";
    String outfile = basedir + "changelist_to_failures_doc.txt";
    Tsv brokenby = new Tsv(changelists_tsv);
    Multimap<Integer, Integer> changelist_to_failures = ArrayListMultimap.create();
    for (String[] row : brokenby.rows()) {
      int test_id = Integer.parseInt(row[0]);
      for (String cl : row[1].split("\\|")) {
        int cl_id = Integer.parseInt(cl);
        changelist_to_failures.put(cl_id, test_id);
      }
    }
    try (BufferedWriter out = new BufferedWriter(new FileWriter(outfile));
        ProgressTracker pt = new ProgressTracker(null, "write", -1, "changelists", "test failures")) {

      for (Map.Entry<Integer, Collection<Integer>> e_failure_count : changelist_to_failures.asMap().entrySet()) {
        int changelist_id = e_failure_count.getKey();
        out.write(Integer.toString(changelist_id));
        int x = 0;
        for (int test_id : e_failure_count.getValue()) {
          out.write('\t');
          out.write(Integer.toString(test_id));
          x++;
        }
        out.write('\n');;
        pt.advise(0, 1, x);
      }
    }
  }
}
