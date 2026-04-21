package miku.united_as_one.genesis.client.render.luminous;

public enum GenesisEffect {
    RAINBOW("rainbow"),
    BLACK_RED("black_red"),
    BLUE_WHITE("blue_white");

    private final String id;

    GenesisEffect(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}