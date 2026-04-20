package ligaolympica.character;
import java.util.*;

public class SirKhai extends GameCharacter{
    Scanner scan = new Scanner(System.in);
    public SirKhai(){
        super("SirKhai", """
                GOAT, The one who controls all, including your mom.
                Call him daddy.""",
                99999, 99999, "Skill 1: Tip of Ballpen - Gives you 1.0 if your mom is chopped. Deals emotional damage", "No need", "Null");
    }

    @Override
    public void skill1(GameCharacter target){
        if(skill1Cooldown > 0){
            return;
        }else{
            typewriter("\nSir Khai decides if ur cooked or nah", 20);
        }

        if(this.mana > 0){
            this.useMana(0);
            this.skill1Cooldown = 0;

            int baseDamage = 1000000;
            int damage = randomDamage(baseDamage, 20);

            typewriter("Sir Khai gives you 1.0, now kys", 20);
            typewriter("Dealt " + damage + " to " + target.getName() + "!", 20);
            target.takeDamage(damage);
        }else{
            typewriter("Not enough mana!", 20);
        }
    }

    @Override
    public void takeTurn(GameCharacter target){
        typewriter("Choose a skill for " + name + ": ", 20);
        typewriter("1) Tip of Ballpen: deals emotional damage", 20);
        typewriter("0) Escape Battle", 20);

        boolean validChoice = false;
        while(!validChoice){
            try{
                int choice;
                System.out.print("Enter the number of your choice: ");
                choice = scan.nextInt();
                switch(choice){
                    case 1 -> {
                        skill1(target);
                        validChoice = true;
                    }
                    case 0 -> {
                        typewriter(name + " Has more urgent matters to attend to, your mom!", 10);
                        this.hasEscaped = true;
                        validChoice = true;
                        return;
                    }
                    default -> {
                        typewriter("Invalid choice. Get a job", 20);
                        scan.nextLine();
                    }
                }
            }catch(InputMismatchException e){
                typewriter("Choose 1 skill thats it, whats wrong wit you", 20);
                scan.nextLine();
            }
        }
    }
}
