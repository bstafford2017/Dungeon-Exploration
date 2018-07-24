import java.util.Random;

/*
 * CSCI 161 - L01
 * Benjamin Stafford
 */
public class Fighter extends LivingBeing {
	
	protected int level;
	protected int currentEXP;
	protected int defense;
	protected int maxEXP;
	
	public Fighter() {

	}
	
	public int getLevel() {
		return this.level;
	}
	
	public int getCurrentEXP() {
		return this.currentEXP;
	}
	
	public int getDefense() {
		return this.defense;
	}
	
	public int getMaxEXP() {
		return this.maxEXP;
	}
	
	public void setLevel(int level) {
		this.level = level;
		maxEXP = 40+(this.level*10);
	}
	
	public void setCurrentEXP(int currentEXP) {		
		levelUp(currentEXP);
	}
	
	public void setDefense(int defense) {
		this.defense = defense;
	}
	
	public void levelUp(int currentEXP) {
		if(currentEXP >= maxEXP) {
			this.level = this.level + 1;
			this.currentEXP = 0;
			maxEXP = 40+(this.level*10);
			
			// Create rand object
			Random rand = new Random();
			
			// Add to health, attack, and defense
			int health = rand.nextInt(6);
			health += 4;
			this.setCurrentHP(health + this.getCurrentHP());
			
			int attack = rand.nextInt(6);
			attack += 2;
			this.setAttack(attack + this.getAttack());
			
			int defense = rand.nextInt(5);
			this.setDefense(defense + this.getDefense());
			System.out.println(this.getName() + " has leveled up to level " + this.getLevel() + "!");
			System.out.println("You have added +" + health + " to your health");
			System.out.println("You have added +" + attack + " to your attack");
			System.out.println("You have added +1 to your defense");
		} else {
			this.currentEXP = currentEXP;
		}
	}
	
}
