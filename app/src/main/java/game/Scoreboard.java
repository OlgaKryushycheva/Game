package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scoreboard {
    private final Path storagePath;
    private final Map<String, Integer> totals = new HashMap<>();

    public Scoreboard(Path storagePath) {
        this.storagePath = storagePath;
        load();
    }

    public void recordScore(String playerName, int delta) {
        if (delta <= 0) {
            return;
        }
        totals.merge(playerName, delta, Integer::sum);
        save();
    }

    public List<ScoreEntry> top(int limit) {
        List<ScoreEntry> entries = new ArrayList<>();
        totals.forEach((name, score) -> entries.add(new ScoreEntry(name, score)));
        entries.sort(Comparator.comparingInt(ScoreEntry::score).reversed());
        return entries.size() > limit ? entries.subList(0, limit) : entries;
    }

    private void load() {
        if (!Files.exists(storagePath)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(storagePath)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(";", 2);
                if (parts.length == 2) {
                    try {
                        totals.put(parts[0], Integer.parseInt(parts[1]));
                    } catch (NumberFormatException ignored) {
                        // skip malformed line
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Не вдалося завантажити таблицю результатів: " + e.getMessage());
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : totals.entrySet()) {
            lines.add(entry.getKey() + ";" + entry.getValue());
        }
        try {
            if (storagePath.getParent() != null) {
                Files.createDirectories(storagePath.getParent());
            }
            Files.write(storagePath, lines);
        } catch (IOException e) {
            System.out.println("Не вдалося зберегти таблицю результатів: " + e.getMessage());
        }
    }
}
