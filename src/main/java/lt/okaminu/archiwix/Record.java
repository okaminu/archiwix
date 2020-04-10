package lt.okaminu.archiwix;


import org.jetbrains.annotations.NotNull;

public final class Record {

    private final String id;

    public Record(@NotNull String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return id.equals(((Record) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
