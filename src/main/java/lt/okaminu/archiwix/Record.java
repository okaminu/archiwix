package lt.okaminu.archiwix;

import org.jetbrains.annotations.NotNull;

public final class Record {

    private final String id;
    private final String title;
    private final String content;
    private final int views;
    private final int timestamp;

    public Record(@NotNull String id) {
        this(id, "", "");
    }

    public Record(@NotNull String id, @NotNull String title) {
        this(id, title, "");
    }

    public Record(@NotNull String id, @NotNull String title, @NotNull String content) {
        this(id, title, content, 0);
    }

    public Record(@NotNull String id, @NotNull String title, @NotNull String content, int views) {
        this(id, title, content, views, 0);
    }

    public Record(@NotNull String id, @NotNull String title, @NotNull String content, int views, int timestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.views = views;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getViews() {
        return views;
    }

    public int getTimestamp() {
        return timestamp;
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
