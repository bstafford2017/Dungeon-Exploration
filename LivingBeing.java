/*
 * CSCI 161 - L01
 * Benjamin Stafford
 */
public class LivingBeing {
	
	protected String name;
	protected double currentHP;
	protected double maxHP;
	protected int attack;
	
	public LivingBeing(){

	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getCurrentHP(){
		return currentHP;
	}
	
	public void setCurrentHP(double currentHP){
		this.currentHP = currentHP;
	}
	
	public double getMaxHP() {
		return maxHP;
	}
	
	public void setMaxHP(double maxHP) {
		this.maxHP = maxHP;
	}
	
	public int getAttack(){
		return attack;
	}

	public void setAttack(int attack){
		this.attack = attack;
	}
	
}
