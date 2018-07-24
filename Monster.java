/*
 * CSCI 161 - L01
 * Benjamin Stafford
 */
public class Monster extends LivingBeing {
	
	private String name;
	private int EXP;
	
	public Monster(){
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getEXP() {
		return EXP;
	}
	
	public void setEXP() {
		EXP = (int)((2*attack)+(currentHP/5));
	}
	
}
