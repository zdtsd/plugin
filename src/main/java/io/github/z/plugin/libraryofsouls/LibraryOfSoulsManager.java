package io.github.z.plugin.libraryofsouls;

public class LibraryOfSoulsManager {

    private static LibraryOfSoulsManager instance;

    private final SoulDatabase mDatabase;
    private final LibraryOfSoulsAPI mAPI;

    public LibraryOfSoulsManager() {
        mDatabase = new SoulDatabase();
        mAPI = new LibraryOfSoulsAPI(mDatabase);
        instance = this;
    }

    public static LibraryOfSoulsManager getInstance() {
        return instance;
    }

    public void shutdown() {
        mDatabase.close();
    }
}
