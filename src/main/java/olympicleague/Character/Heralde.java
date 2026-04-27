package Character;

public class Heralde extends GameCharacter {
    public Heralde() {
        super("Heralde", "Legends say Heralde strangled a lion...", 1800, 450);

        this.addSkill(new Skill("Lion's Strike", 1, "220 Base Dmg. Cost: 90MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 90) return "Not enough mana!";
                source.useMana(90);
                return "LION'S STRIKE deals " + target.takeDamage(source.randomDamage(220, 20)) + " damage!";
            }
        });

        this.addSkill(new Skill("Iron Hide", 3, "Reduce damage by 30% for 2 turns. Cost: 100MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 100) return "Not enough mana!";
                source.useMana(100);
                source.setDefenseBonus(0.7);
                source.setStatusEffectTurns(2);
                return "IRON HIDE activates! Damage reduced by 30% for 2 turns!";
            }
        });

        this.addSkill(new Skill("Thunder Wrath", 6, "400 True Dmg. Cost: 180MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 180) return "Not enough mana!";
                source.useMana(180);
                return "THUNDER WRATH calls Zeus' lightning! Dealt " + target.takeTrueDamage(400) + " True Damage!";
            }
        });
    }
}