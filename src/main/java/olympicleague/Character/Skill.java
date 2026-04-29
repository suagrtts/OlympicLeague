package character;

public abstract class Skill {
    private final String name;
    private final int maxCooldown;
    private int currentCooldown;
    private final String description;

    public Skill(String name, int maxCooldown, String description) {
        this.name = name;
        this.maxCooldown = maxCooldown;
        this.currentCooldown = 0;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCurrentCooldown() { return currentCooldown; }
    public boolean isReady() { return currentCooldown == 0; }

    public void putOnCooldown() { this.currentCooldown = maxCooldown; }
    public void reduceCooldown() { if (currentCooldown > 0) currentCooldown--; }
    public void resetCooldown() { this.currentCooldown = 0; }

    // Returns the battle log text to be printed by the UI
    public abstract String execute(GameCharacter source, GameCharacter target);
}