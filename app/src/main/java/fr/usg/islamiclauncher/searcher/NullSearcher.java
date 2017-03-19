package fr.usg.islamiclauncher.searcher;

import java.util.ArrayList;
import java.util.List;

import fr.usg.islamiclauncher.MainActivity;
import fr.usg.islamiclauncher.pojo.Pojo;

/**
 * Retrieve pojos from history
 */
public class NullSearcher extends Searcher {

    public NullSearcher(MainActivity activity) {
        super(activity);
    }

    @Override
    protected List<Pojo> doInBackground(Void... voids) {
        return new ArrayList<>();
    }
}
