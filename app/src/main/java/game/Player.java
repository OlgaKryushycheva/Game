package game;

public class Player {
    private final String name;
    private final boolean ai;

    public Player(String name) {
        this(name, false);
    }

    private Player(String name, boolean ai) {
        this.name = name;
        this.ai = ai;
    }

    public static Player ai() {
        return new Player("ШІ", true);
    }

    public String getName() {
        return name;
    }

    public boolean isAi() {
        return ai;
    }
}
