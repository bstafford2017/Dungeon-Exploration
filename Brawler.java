import java.util.Random;

/*
 * CSCI 161 - L01
 * Benjamin Stafford
 */
public class Brawler extends Fighter {
	
	private String name;
	
	public Brawler (){
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getCurrentEXP() {
		return this.currentEXP;
	}
	
	public void setCurrentEXP(int currentEXP) {		
		levelUp(currentEXP);
	}
	
	public void levelUp(int currentEXP) {
		if(currentEXP >= maxEXP) {
			this.level = this.level + 1;
			this.currentEXP = 0;
			maxEXP = 40+(this.level*10);
			
			// Create rand object
			Random rand = new Random();
			
			// Add to health, attack, and defense
			int health = rand.nextInt(7);
			health += 5;
			this.setCurrentHP(health + this.getCurrentHP());
			
			int attack = rand.nextInt(6);
			attack += 4;
			this.setAttack(attack + this.getAttack());
			
			this.setDefense(1 + this.getDefense());
			System.out.println(this.getName() + " has leveled up to level " + this.getLevel() + "!");
			System.out.println("You have added +" + health + " to your health");
			System.out.println("You have added +" + attack + " to your attack");
			System.out.println("You have added +1 to your defense");
		} else {
			this.currentEXP = currentEXP;
		}
	}
}
