package fr.usg.islamiclauncher.searcher;

import java.util.List;

import fr.usg.islamiclauncher.KissApplication;
import fr.usg.islamiclauncher.MainActivity;
import fr.usg.islamiclauncher.pojo.Pojo;

/**
 * Returns the list of all applications on the system
 */
public class ApplicationsSearcher extends Searcher {
    public ApplicationsSearcher(MainActivity activity) {
        super(activity);
    }

    @Override
    protected List<Pojo> doInBackground(Void... voids) {
        // Ask for records
        return KissApplication.getDataHandler(activity).getApplications();
    }
}
