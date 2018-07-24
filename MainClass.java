import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*
 * CSCI 161 - L01
 * Benjamin Stafford
 * Lab 9
 * 4/2/18
 * This program focuses on creating a linked list from scratch. It also features 
 * a new mode called Dungeon Exploration.
 */

public class MainClass {

	public static void main(String[] args) {

		// Initialize fighter
		Fighter fighter;

		// Initialize array
		ArrayList<Monster> array = new ArrayList<>();
											
		// Initialize sc
		Scanner sc = new Scanner(System.in);
		
		// Initialize lastMonster 
		int lastMonster = -1;

		// Original file location
		// ** CAN/SHOULD BE ALTERED IF DOWNLOADED **
		String address = "C:\\Users\\Benjamin\\Dropbox\\UND Courses\\Spring 2018\\CSCI 161\\Program09\\MONSTERLIST.txt";

		// Loads from file
		int numberOfEntries = numberOfEntries(address);
		open(address, array, numberOfEntries, false);
		
		// NotEnoughMonsterException
		while(true) {
			try {
				monsterException(array);
				break;
			} catch (NotEnoughMonsterException e) {
				System.out.println("Not enough monsters in file. Try again.");
				System.out.println("Enter a new file name: ");
				address = sc.nextLine();
			}
		}
		
		// Initialize extraArray
		double[] extraArray = null;

		// Character menu
		System.out.println("------CHARACTER MENU------");
		System.out.println("1. Create a brawler\n2. Create a fighter\nEnter response: ");
		int charResponse;
		while(true) {
			try {
				charResponse = sc.nextInt();
				if (charResponse == 1) {
					fighter = new Brawler();
					fighterMenu(sc, fighter);
					System.out.println("Would you like to play Dungeon Exploration? (Y/N)");
					char miniResponse = sc.next().charAt(0);
					while(true) {
						if(miniResponse == 'Y' || miniResponse == 'y') {
							dungeonExploration(sc, fighter, array, numberOfEntries, address, lastMonster);
						} else if(miniResponse == 'N' || miniResponse == 'n') {
							break;
						} else {
							System.out.println("Invalid response. Try again\nEnter response: ");
						}
					}
					break;
				} else if (charResponse == 2) {
					fighter = new Fighter();
					fighterMenu(sc, fighter);
					System.out.println("Would you like to play Dungeon Exploration? (Y/N)");
					char miniResponse = sc.next().charAt(0);
					while(true) {
						if(miniResponse == 'Y' || miniResponse == 'y') {
							dungeonExploration(sc, fighter, array, numberOfEntries, address, lastMonster);
						} else if(miniResponse == 'N' || miniResponse == 'n') {
							break;
						} else {
							System.out.println("Invalid response. Try again\nEnter response: ");
						}
					}
					break;
				} else {
					throw new WrongChoiceException("Invalid response. Try again.\nEnter response: ");
				}
			} catch (WrongChoiceException e) {
				System.out.println("Invalid response. Try again.\nEnter response: ");
				continue;
			}
		}

		// Print menu before loop
		printMenu(numberOfEntries);
		
		// For WrongChoiceException
		int userInput = sc.nextInt();
		while(true) {
			try {
				if(userInput > 10 || userInput < 0) {
					throw new WrongChoiceException("Invalid response. Try again.\nEnter response: ");
				} else {
					break;
				}
			} catch(WrongChoiceException e) {
				System.out.println("Invalid response. Try again.\nEnter response: ");
				userInput = sc.nextInt();
				continue;
			}
		}

		// Continuous loop for entering responses
		while (true) {
			if(userInput == 1) {
				fighterMenu(sc, fighter);
			}
			else if(userInput == 2) {
				searchAndChange(array, sc);
			}
			else if(userInput == 3) {
				 lastMonster = attack(array, sc, fighter, address, lastMonster, false);
			}
			else if(userInput == 4) {
				setupForSearch(array, sc, extraArray, numberOfEntries);
			}
			else if(userInput == 5) {
				displayAll(array, fighter);
			}
			else if(userInput == 6) {
		
				// Load file
				System.out.print("Enter the name of the file you want to load: ");
				address = sc.nextLine();
				address = sc.nextLine();
				numberOfEntries = load(array, sc, address, numberOfEntries);
			}
			else if(userInput == 7) {

				// Save file
				System.out.print("Enter the name of the file you want to save to: ");
				String tempAddress = sc.nextLine();
				tempAddress = sc.nextLine();
				save(array, sc, tempAddress, false);
			}
			else if (userInput == 8) {
				open(address, array, numberOfEntries, false);
				System.out.println("Array has been reloaded!");
			}
			else if(userInput == 9) {
				setupForSort(array, sc, extraArray, numberOfEntries);
			}
			else if (userInput == 10) {
				
				sc.close();
				System.exit(0);
			}
			else {
				try {
					throw new WrongChoiceException("Invalid input.\nEnter respose: ");
				} catch(WrongChoiceException e) {
					System.out.println("Invalid input.\nEnter respose: ");
					userInput = sc.nextInt();
				}
			}

			// Reprints menu for next loop
			printMenu(numberOfEntries);

			// Resets userInput for next response
			userInput = sc.nextInt();
			while(true) {
				try {
					if(userInput > 10 || userInput < 0) {
						throw new WrongChoiceException("Invalid response. Try again.\nEnter response: ");
					} else {
						break;
					}
				} catch(WrongChoiceException e) {
					System.out.println("Invalid response. Try again.\nEnter response: ");
					userInput = sc.nextInt();
					continue;
				}
			}
		}
	}

	public static void printMenu(int numberOfEntries) {
		System.out.println("------------------MENU------------------");
		System.out.println("There are " + numberOfEntries + " Monsters.");
		System.out.println("1. Edit fighter");
		System.out.println("2. Edit a monster");
		System.out.println("3. Attack a monster");
		System.out.println("4. Search for a monster");
		System.out.println("5. Display all");
		System.out.println("6. Load from save");
		System.out.println("7. Save to file");
		System.out.println("8. Reload array");
		System.out.println("9. Sort array");
		System.out.println("10. Exit Program");
		System.out.print("Enter response: ");
	}

	public static void open(String address, ArrayList<Monster> array, int numberOfEntries, boolean DE) {

		// Initialize file and scanners
		File file = null;
		Scanner sc = null;

		// For FileNotFound Exception
		try {
			file = new File(address);
			sc = new Scanner(file);
		} catch (Exception e) {
			System.out.println("File not found");
		}

		
		if(DE) {			

			// Remove all in linked list
			Dungeon.removeAll();
			
			// Read in DE data from file
			for(int i = 0; i < numberOfEntries; i++) {
				String line = sc.nextLine();
				String[] split = line.split("/"); 
				boolean bool;
				if(split[0].equals("true")) {
					bool = true;
				} else {
					bool = false;
				}
				boolean flag = false;
				for(int j = 0; j < array.size(); j++) {
					if(array.get(j).getName().equals(split[2])) {
						Dungeon.addBack(bool, Integer.parseInt(split[1]), array.get(i));
						System.out.println(bool + " " + split[1] + " " + array.get(i).getName());
						flag = true;
						break;
					}
				}
				if(flag == false) {
					Monster mon = new Monster();
					mon.setName(split[2]);
					mon.setCurrentHP(Double.parseDouble(split[3]));
					mon.setMaxHP(0);
					mon.setAttack(Integer.parseInt(split[4]));
					mon.setEXP();
					Dungeon.addBack(bool, Integer.parseInt(split[1]), mon);
					System.out.println(bool + " " + split[1] + " " + array.get(i).getName());
				}
			}
		} else {
			// Checks if array is empty
			// Else overwrites current ArrayList
			if (array.size() == 0) {

				// Get data from file to array of Monsters
				for (int j = 0; j < numberOfEntries; j++) {
					
					// Get line by line
					String line = sc.nextLine();
				
					// Checks if there is an empty space in data file
					if (line.equals("")) {
						continue;
					}

					// Set arg
					String[] arg = line.split("/");

					// Declare Monster in array
					array.add(new Monster());

					// Parsing data
					array.get(j).setName(arg[0]);
					array.get(j).setCurrentHP(Double.parseDouble(arg[1]));
					array.get(j).setAttack(Integer.parseInt(arg[2]));
					array.get(j).setEXP();
				}
			}

			else {
				for (int i = 0; i < array.size(); i++) {

					// Get response
					String line = sc.nextLine();

					// Checks if there is an empty space in data file
					if (line.equals("")) {
						continue;
					}

					// Set arg
					String[] arg = line.split("/");

					// Parsing data
					array.get(i).setName(arg[0]);
					array.get(i).setCurrentHP(Double.parseDouble(arg[1]));
					array.get(i).setAttack(Integer.parseInt(arg[2]));
					array.get(i).setEXP();
				}
			}
		}

		// Closings
		sc.close();
	}

	public static int numberOfEntries(String address) {

		// Initialize file and scanners
		File file = null;
		Scanner newSC = null;

		// For FileNotFound Exception
		try {
			file = new File(address);
			newSC = new Scanner(file);
		} catch (Exception e) {
			System.out.println("File not found");
		}

		// Get number of entries
		int numberOfEntries = 0;
		while (newSC.hasNextLine()) {
			String line = newSC.nextLine();

			// Checks if there is an empty space in data file
			if (line.equals("")) {
				continue;
			}

			numberOfEntries++;
		}

		return numberOfEntries;
	}

	public static void linearSearch(double[] array, ArrayList<Monster> monsterArray, Scanner sc) {

		// Sets response and flag
		System.out.print("Enter the monster's value you want to find: ");
		double valueToFind = sc.nextDouble();
		boolean flag = false;
		boolean running = true;

		// System for checking if value is in the list
		while (running) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == valueToFind) {
					System.out.printf("\nPosition:   %d\n", i + 1);
					System.out.printf("Name:       %s\n", monsterArray.get(i).getName());
					System.out.printf("Health:     %.2f\n", monsterArray.get(i).getCurrentHP());
					System.out.printf("Attack:     %d\n", monsterArray.get(i).getAttack());
					System.out.printf("EXP:        %d\n", monsterArray.get(i).getEXP());

					flag = true;
					running = false;
				}
			}
			if (flag == false) {
				System.out.println("There is no monster with this value.");
				break;
			}
		}
	}

	public static void binarySearch(double[] array, ArrayList<Monster> monsterArray, Scanner sc) {

		// Sort array before binary search
		array = bubbleSort(array, monsterArray);

		System.out.print("Enter the monster's value you want to find: ");
		double valueToFind = sc.nextDouble();
		boolean flag = false;

		// Establish high and low variables
		int high = array.length;
		int low = 0;

		while (high >= low) {

			// Recalculate mid
			int mid = (high + low) / 2;

			// Since sorted, checks for duplicates around matching value
			if (array[mid] == valueToFind) {
				System.out.printf("\nPosition:   %d\n", mid + 1);
				System.out.printf("Name:       %s\n", monsterArray.get(mid).getName());
				System.out.printf("Health:     %.2f\n", monsterArray.get(mid).getCurrentHP());
				System.out.printf("Attack:     %d\n", monsterArray.get(mid).getAttack());
				System.out.printf("EXP:        %d\n", monsterArray.get(mid).getEXP());

				// Checks for matching values to the right
				for (int i = mid + 1; i < array.length; i++) {
					if (array[mid] == array[i]) {
						System.out.printf("\nPosition:   %d\n", i + 1);
						System.out.printf("Name:       %s\n", monsterArray.get(i).getName());
						System.out.printf("Health:     %.2f\n", monsterArray.get(i).getCurrentHP());
						System.out.printf("Attack:     %d\n", monsterArray.get(i).getAttack());
						System.out.printf("EXP:        %d\n", monsterArray.get(i).getEXP());
					}

					// Breaks out of loop if no more matching value to the right
					if (array[mid] != array[i]) {
						break;
					}
				}

				// Checks for matching values to the left
				for (int i = mid - 1; i > 0; i--) {
					if (array[mid] == array[i]) {
						System.out.printf("\nPosition:   %d\n", i + 1);
						System.out.printf("Name:       %s\n", monsterArray.get(i).getName());
						System.out.printf("Health:     %f\n", monsterArray.get(i).getCurrentHP());
						System.out.printf("Attack:     %d\n", monsterArray.get(i).getAttack());
						System.out.printf("EXP:        %d\n", monsterArray.get(i).getEXP());
					}

					// Breaks out of loop if no more matching value to the left
					if (array[mid] != array[i]) {
						break;
					}
				}

				flag = true;
				break;

			} else if (array[mid] > valueToFind) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		if (flag == false)
			System.out.println("There is no monster with this value.");

	}

	public static void searchAndChange(ArrayList<Monster> array, Scanner sc) {

		// Sets response and flag
		System.out.print("Enter a monster's name to search for: ");
		String response = sc.nextLine();
		response = sc.nextLine();
		boolean flag = false;
		boolean running = true;

		// System for checking if value is in the list
		while (running) {
			for (int i = 0; i < array.size(); i++) {

				if (response.equals(array.get(i).getName())) {

					// Set new name
					System.out.print("Enter a new name for " + array.get(i).getName() + ": ");
					String name = sc.nextLine();
					// name = sc.nextLine();
					array.get(i).setName(name);

					// Set new health
					System.out.print("Enter a new health for " + array.get(i).getName() + ": ");
					double health = sc.nextDouble();
					array.get(i).setCurrentHP(health);

					// Set new attack
					System.out.print("Enter a new attack for " + array.get(i).getName() + ": ");
					int attack = sc.nextInt();
					array.get(i).setAttack(attack);
					
					// Set new EXP
					array.get(i).setEXP();

					flag = true;
					running = false;
				}
			}
			running = false;
		}
		if (flag == false) {
			System.out.println("Invalid monster name. Please try again.");
		}
	}

	public static int attack(ArrayList<Monster> array, Scanner sc, Fighter fighter, String address, int lastMonster, boolean DE) {

		String response = "";
		if(DE) {
			Room current = Dungeon.getStartRoom();
			while(current.nextRoom != null) {
				if(current.isPlayer() == true) {
					response = current.getHostMon().getName();
					break;
				} else {
					current = current.nextRoom;
				}
			}
		} else {
			// Searching for monster to attack
			System.out.print("Enter a monster's name to search for: ");
			System.out.println();
			response = sc.nextLine();
			response = sc.nextLine();
		}
		
		boolean flag = false;
		boolean running = true;
		
		// System for checking if value is in the list
		while (running) {
			for (int i = 0; i < array.size(); i++) {
				if (response.equals(array.get(i).getName())) {
					if (array.get(i).getCurrentHP() > 0) {
			
						// Set rand for attack back
						Random rand = new Random();
						int hit = rand.nextInt(2);
						int firstAttack = rand.nextInt(2);

						// Check if Fighter is of type Brawler
						if (fighter instanceof Brawler) {

							// *For holding a grudge*
							// Checking if lastMonster equals i
							if(lastMonster == i) {
								
								// Fighter attack complete
								double health = array.get(i).getCurrentHP();
								health -= fighter.getAttack()*0.50 + fighter.getAttack();
								array.get(i).setCurrentHP(health);
								System.out.println(fighter.getName() + " attacked " + array.get(i).getName() + " for "
										+ (fighter.getAttack()*0.50 + fighter.getAttack()));
							} 
							else {
								
								// Fighter attack complete
								double health = array.get(i).getCurrentHP();
								health -= fighter.getAttack();
								array.get(i).setCurrentHP(health);
								System.out.println(fighter.getName() + " attacked " + array.get(i).getName() + " for "
										+ fighter.getAttack());
							}

							// Verifies if Monster is dead
							if (array.get(i).getCurrentHP() <= 0) {

								// Give fighter EXP
								if (!fighter.getName().isEmpty()) {
									System.out.println(array.get(i).getName() + " is dead. " + fighter.getName()
											+ " gained " + array.get(i).getEXP() + " EXP!");
									fighter.setCurrentEXP(array.get(i).getEXP()*3 + fighter.getCurrentEXP());
								}
								
								// Change room for DE
								if(DE) {	
									Dungeon.move();
									//attack(array,sc,fighter,address,i,true);
									
								} else {
									// Reset monster in array
									int numberOfEntries = numberOfEntries(address);
									load(array,sc,address,numberOfEntries);
								}
							} else {

								// Monster attack complete
								if (hit == 0) {
									double change = fighter.getCurrentHP()
											- (array.get(i).getAttack() - fighter.getDefense());
									if (change >= fighter.getCurrentHP()) {
										fighter.setCurrentHP(fighter.getCurrentHP());
										System.out.println(fighter.getName() + "\'s defense blocked "
												+ array.get(i).getName() + "\'s attack");
									} else {
										fighter.setCurrentHP(change);
										System.out.println(array.get(i).getName() + " attacked " + fighter.getName()
												+ " for " + (array.get(i).getAttack()-fighter.getDefense()));
									}
								} else if (hit == 1) {
									System.out.println(array.get(i).getName() + "\'s attack missed!");
								}

								// DeadFighterIsDeadException
								try {
									fighterException(fighter);
								} catch(DeadFighterIsDeadException e) {
									System.out.println("Fighter is dead.");
									String[] args = {""};
									main(args);
								}

								// Display health
								System.out.println(
										"---" + array.get(i).getName() + "\'s health: " + array.get(i).getCurrentHP());
								System.out.println("---" + fighter.getName() + "\'s health: " + fighter.getCurrentHP());
								
								// Set lastMonster
								lastMonster = i;
							}
						}
						// Check if Fighter is of type Fighter
						else {
							// Fighter attacks first
							if (firstAttack == 0) {

								// Fighter attack complete
								double health = array.get(i).getCurrentHP();
								health -= fighter.getAttack();
								array.get(i).setCurrentHP(health);
								System.out.println(fighter.getName() + " attacked " + array.get(i).getName() + " for "
										+ fighter.getAttack());

								// Verifies if Monster is dead
								if (array.get(i).getCurrentHP() <= 0) {

									// Give fighter EXP
									if (!fighter.getName().isEmpty()) {
										System.out.println(array.get(i).getName() + " is dead. " + fighter.getName()
												+ " gained " + array.get(i).getEXP() + " EXP!");
										fighter.setCurrentEXP(array.get(i).getEXP()*3 + fighter.getCurrentEXP());
									}
									
									// Change room for DE
									if(DE) {				
										Dungeon.move();
										//attack(array,sc,fighter,address,i,true);
									} else {
										// Reset monster in array
										int numberOfEntries = numberOfEntries(address);
										load(array,sc,address,numberOfEntries);
									}
								} else {

									// Monster attack complete
									if (hit == 0) {
										double change = fighter.getCurrentHP()
												- (array.get(i).getAttack() - fighter.getDefense());
										if (change >= fighter.getCurrentHP()) {
											fighter.setCurrentHP(fighter.getCurrentHP());
											System.out.println(fighter.getName() + "\'s defense blocked "
													+ array.get(i).getName() + "\'s attack");
										} else {
											fighter.setCurrentHP(change);
											System.out.println(array.get(i).getName() + " attacked " + fighter.getName()
													+ " for " + (array.get(i).getAttack()-fighter.getDefense()));
										}
									} else if (hit == 1) {
										System.out.println(array.get(i).getName() + "\'s attack missed!");
									}

									// DeadFighterIsDeadException
									try {
										fighterException(fighter);
									} catch(DeadFighterIsDeadException e) {
										System.out.println("Fighter is dead.");
										String[] args = {""};
										main(args);
									}

									// Display health
									System.out.println(
											"---" + array.get(i).getName() + "\'s health: " + array.get(i).getCurrentHP());
									System.out
											.println("---" + fighter.getName() + "\'s health: " + fighter.getCurrentHP());
								}
							}
							// Monster attacks first
							else if (firstAttack == 1) {

								// Monster attack complete
								if (hit == 0) {
									double change = fighter.getCurrentHP()
											- (array.get(i).getAttack() - fighter.getDefense());
									if (change >= fighter.getCurrentHP()) {
										fighter.setCurrentHP(fighter.getCurrentHP());
										System.out.println(fighter.getName() + "\'s defense blocked "
												+ array.get(i).getName() + "\'s attack");
									} else {
										fighter.setCurrentHP(change);
										System.out.println(array.get(i).getName() + " attacked " + fighter.getName()
												+ " for " + (array.get(i).getAttack()-fighter.getDefense()));
									}
								} else if (hit == 1) {
									System.out.println(array.get(i).getName() + "\'s attack missed!");
								}

								// DeadFighterIsDeadException
								try {
									fighterException(fighter);
								} catch(DeadFighterIsDeadException e) {
									System.out.println("Fighter is dead.");
									String[] args = {""};
									main(args);
								}

								// Fighter attack complete
								double health = array.get(i).getCurrentHP();
								health -= fighter.getAttack();
								array.get(i).setCurrentHP(health);
								System.out.println(fighter.getName() + " attacked " + array.get(i).getName() + " for "
										+ fighter.getAttack());

								// Verifies if Monster is dead
								if (array.get(i).getCurrentHP() <= 0) {

									// Give fighter EXP
									if (!fighter.getName().isEmpty()) {
										System.out.println(array.get(i).getName() + " is dead. " + fighter.getName()
												+ " gained " + array.get(i).getEXP() + " EXP!");
										fighter.setCurrentEXP(array.get(i).getEXP()*3 + fighter.getCurrentEXP());
									}
									
									// Change room for DE
									if(DE) {				
										Dungeon.move();
										//attack(array,sc,fighter,address,i,true);
									} else {
										// Reset monster in array
										int numberOfEntries = numberOfEntries(address);
										load(array,sc,address,numberOfEntries);
									}
								} else {

									// Display health
									System.out.println(
											"---" + array.get(i).getName() + "\'s health: " + array.get(i).getCurrentHP());
									System.out
											.println("---" + fighter.getName() + "\'s health: " + fighter.getCurrentHP());
								}
							}
						}
					}
					flag = true;
					running = false;
				}
			}
			if (flag == false) {
				System.out.print("Invalid monster name\nEnter a new name to search for: ");
				response = sc.nextLine();
			}
		}
		
		return lastMonster;
	}

	public static void save(ArrayList<Monster> array, Scanner sc, String saveAddress, boolean DE) {

		File file = null;
		PrintWriter pw = null;
		try {
			file = new File(saveAddress);
			pw = new PrintWriter(file);
		} catch (Exception e) {
			System.out.println("File Not Found.");
		}

		if(DE) {
			// Save DE data to file
			Room current = Dungeon.getStartRoom();
			while(current != null) {
				pw.println(current.isPlayer() + "/" + current.getRoomNo() + "/" + current.getHostMon().getName()
						+ "/" + current.getHostMon().getCurrentHP() + "/" + current.getHostMon().getAttack());
				current = current.nextRoom;
			}
		} else {
			// Save monster data to file
			for (Monster m : array) {
				pw.print(m.getName() + "/" + m.getCurrentHP() + "/" + m.getAttack() + "/" + m.getEXP() + "\n");
			}
		}

		// Closings
		pw.close();
	}

	public static int load(ArrayList<Monster> array, Scanner sc, String address, int numberOfEntries) {

		// Clears array to re-fill ArrayList
		array.clear();
		numberOfEntries = numberOfEntries(address);
		open(address, array, numberOfEntries, false);
		return numberOfEntries;
	}

	public static void setupForSearch(ArrayList<Monster> array, Scanner sc, double[] extraArray, int numberOfEntries) {
		
		// Gets search type from user
		System.out.print(
				"Which search method would you like to use?\n1. Linear search\n2. Binary search\nEnter Response: ");
		int searchResponse = sc.nextInt();

		// Declare extraArray size
		extraArray = new double[numberOfEntries];

		while(true) {
			if (searchResponse == 1) {
				System.out.print(
						"Which attribute would you like to search in?\n1. Health\n2. Attack\n3. EXP\nEnter response: ");
				int response1 = sc.nextInt();
				if (response1 == 1) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getCurrentHP();
					}
					linearSearch(extraArray, array, sc);
					break;
				} else if (response1 == 2) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getAttack();
					}
					linearSearch(extraArray, array, sc);
					break;
				} else if (response1 == 3) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getEXP();
					}
					linearSearch(extraArray, array, sc);
					break;
				} else {
					try {
						throw new WrongChoiceException("Invalid response. Try again.\nEnter response: ");
					} catch (WrongChoiceException e) {
						System.out.println("Invalid response. Please try again.");
					}
				}
			}
	
			// Setup for binary search
			else if (searchResponse == 2) {
				System.out.print(
						"Which attribute would you like to search in?\n1. Health\n2. Attack\n3. EXP\nEnter response: ");
				int response2 = sc.nextInt();
				if (response2 == 1) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getCurrentHP();
					}
					binarySearch(extraArray, array, sc);
					break;
				} else if (response2 == 2) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getAttack();
					}
					binarySearch(extraArray, array, sc);
					break;
				} else if (response2 == 3) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getEXP();
					}
					binarySearch(extraArray, array, sc);
					break;
				} else {
					try {
						throw new WrongChoiceException("Invalid response. Try again.\nEnter response: ");
					} catch (WrongChoiceException e) {
						System.out.println("Invalid response. Please try again.");
					}
				}
			}
			
			else {
				try {
					throw new WrongChoiceException("Invalid response. Try again.\nEnter response: ");
				} catch (WrongChoiceException e) {
					System.out.println("Invalid response. Please try again.\nEnter response: ");
					searchResponse = sc.nextInt();
				}
			}
		}
	}

	public static void setupForSort(ArrayList<Monster> array, Scanner sc, double[] extraArray, int numberOfEntries) {

		// Type of sorting used
		System.out.print(
				"Which sort would you like to use?\n1. Selection sort\n2. Bubble sort\n3. Quick sort\nEnter response: ");
		int sortResponse = sc.nextInt();

		// Declare extraArray size
		extraArray = new double[numberOfEntries];

		
		while(true) {
			if (sortResponse == 1) {
	
				// Sort Health, Sort, Attack, or EXP
				System.out.println("What would you like to bubble sort?\n1. Health\n2. Attack\n3. EXP\nEnter response: ");
				int response1 = sc.nextInt();
				if (response1 == 1) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getCurrentHP();
					}
					extraArray = bubbleSort(extraArray, array);
					break;
				} else if (response1 == 2) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getAttack();
					}
					extraArray = bubbleSort(extraArray, array);
					break;
				} else if (response1 == 3) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getEXP();
					}
					extraArray = bubbleSort(extraArray, array);
					break;
				} else {
					try {
						throw new WrongChoiceException("Invalid response. Try again.\nEnter response: ");
					} catch (WrongChoiceException e) {
						System.out.println("Invalid response. Please try again.");
					}
				}
			}
	
			else if (sortResponse == 2) {
	
				// Sort Health, Sort, Attack, or EXP
				System.out
						.println("What would you like to selection sort?\n1. Health\n2. Attack\n3. EXP\nEnter response: ");
				int response2 = sc.nextInt();
				if (response2 == 1) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getCurrentHP();
					}
					extraArray = selectionSort(extraArray, array);
					break;
				} else if (response2 == 2) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getAttack();
					}
					extraArray = selectionSort(extraArray, array);
					break;
				} else if (response2 == 3) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getEXP();
					}
					extraArray = selectionSort(extraArray, array);
					break;
				} else {
					try {
						throw new WrongChoiceException("Invalid response. Try again.\nEnter response: ");
					} catch (WrongChoiceException e) {
						System.out.println("Invalid response. Please try again.");
					}
				}
			}
	
			else if (sortResponse == 3) {
	
				// Sort Health, Sort, Attack, or EXP
				System.out
						.println("What would you like to selection sort?\n1. Health\n2. Attack\n3. EXP\nEnter response: ");
				int response3 = sc.nextInt();
	
				int low = 0;
				int high = extraArray.length - 1;
	
				if (response3 == 1) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getCurrentHP();
					}
					extraArray = quickSort(extraArray, low, high, array);
					break;
				} else if (response3 == 2) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getAttack();
					}
					extraArray = quickSort(extraArray, low, high, array);
					break;
				} else if (response3 == 3) {
					for (int i = 0; i < array.size(); i++) {
						if (array.get(i).getName().equals("temp"))
							continue;
						extraArray[i] = array.get(i).getEXP();
					}
					extraArray = quickSort(extraArray, low, high, array);
					break;
				} else {
					try {
						throw new WrongChoiceException("Invalid response. Try again.\nEnter response: ");
					} catch (WrongChoiceException e) {
						System.out.println("Invalid response. Please try again.");
					}
				}
			}
			
			else {
				try {
					throw new WrongChoiceException("Invalid response. Try again.\nEnter response: ");
				} catch (WrongChoiceException e) {
					System.out.println("Invalid response. Please try again.\nEnter response: ");
					sortResponse = sc.nextInt();
				}
			}
		}

		System.out.println("Sort was successful!");
	}

	public static void fighterMenu(Scanner sc, Fighter fighter) {

		// Create fighter Display
		System.out.println("-------FIGHTER MENU-------");
		System.out.println("1. Manually create fighter\n2. Generate default fighter\nEnter response: ");
		int fighterInput = sc.nextInt();

		while (true) {
			if (fighterInput == 1) {

				// Set fighter name
				System.out.println("Enter a name: ");
				String name = sc.nextLine();
				name = sc.nextLine();
				fighter.setName(name);

				// Set fighter health
				System.out.println("Enter a health: ");
				double health = sc.nextDouble();
				fighter.setCurrentHP(health);

				// // Set fighter attack
				System.out.println("Enter an attack: ");
				int attack = sc.nextInt();
				fighter.setAttack(attack);

				// Set fighter level
				System.out.println("Enter a level: ");
				int level = sc.nextInt();
				fighter.setLevel(level);

				// Set fighter EXP
				System.out.println("Enter a EXP: ");
				int EXP = sc.nextInt();
				fighter.setCurrentEXP(EXP);

				// Set fighter defense
				System.out.println("Enter a defense: ");
				int defense = sc.nextInt();
				fighter.setDefense(defense);
				break;
			} else if (fighterInput == 2) {

				if(fighter instanceof Fighter) {
					// Default Fighter
					fighter.setName("Fighter 1");
					fighter.setCurrentHP(100);
					fighter.setAttack(5);
					fighter.setLevel(1);
					fighter.setCurrentEXP(0);
					fighter.setDefense(1);
					break;
				} else {
					// Default Brawler
					fighter.setName("Brawler 1");
					fighter.setCurrentHP(100);
					fighter.setAttack(5);
					fighter.setLevel(1);
					fighter.setCurrentEXP(0);
					fighter.setDefense(1);
					break;
				}
			} else {
				System.out.println("Invalid response.");
				fighterInput = sc.nextInt();
			}
		}
	}

	public static void displayAll(ArrayList<Monster> array, Fighter fighter) {

		// Display all monsters (formatted)
		System.out.println("---------------ALL MONSTERS-----------------");
		System.out.printf("#   Name                Health    Attack    EXP\n");
		int q = 0;
		for (Monster m : array) {

			if (m.getName().equals("temp"))
				continue;

			// For aligning name evenly
			if (q < 9) {
				System.out.printf("%d.  %-20s%-10.2f%-10d%-7d\n", q + 1, m.getName(), m.getCurrentHP(), m.getAttack(),
						m.getEXP());
			} else {
				System.out.printf("%d. %-20s%-10.2f%-10d%-7d\n", q + 1, m.getName(), m.getCurrentHP(), m.getAttack(),
						m.getEXP());
			}
			q++;
		}

		// Display fighter info
		System.out.println();
		System.out.println("-----------------------------ALL FIGHTERS---------------------------------");
		System.out.printf("#   Name                Health    Attack    EXP    Level    Defense    Max EXP\n");
		System.out.printf("%d.  %-20s%-10.2f%-10d%-7d%-9d%-11d%-10d\n", 1, fighter.getName(), fighter.getCurrentHP(),
				fighter.getAttack(), fighter.getCurrentEXP(), fighter.getLevel(), fighter.getDefense(),
				fighter.getMaxEXP());
		System.out.println();
	}

	public static double[] selectionSort(double[] array, ArrayList<Monster> monsterArray) {

		int min;

		for (int j = 0; j < array.length - 1; j++) {

			min = j;
			for (int i = j + 1; i < array.length; i++) {
				if (array[i] < array[min]) {
					min = i;
				}
			}
			if (min != j) {
				// Swap double array
				double temp = array[j];
				array[j] = array[min];
				array[min] = temp;

				// Swap monsterArray
				Monster monsterTemp = monsterArray.get(j);
				monsterArray.set(j, monsterArray.get(min));
				monsterArray.set(min, monsterTemp);
			}
		}

		return array;
	}

	public static double[] bubbleSort(double[] array, ArrayList<Monster> monsterArray) {

		for (int i = 0; i < array.length - 1; i++) {
			boolean swap = false;
			for (int j = 0; j < array.length - 1; j++) {
				if (array[j] > array[j + 1]) {

					// Swap double array
					double temp = array[j + 1];
					array[j + 1] = array[j];
					array[j] = temp;

					// Swap monsterArray
					Monster monsterTemp = monsterArray.get(j + 1);
					monsterArray.set(j + 1, monsterArray.get(j));
					monsterArray.set(j, monsterTemp);

					swap = true;
				}
			}
			if (swap == false) {
				break;
			}
		}

		return array;
	}

	public static double[] quickSort(double[] array, int low, int high, ArrayList<Monster> monsterArray) {

		// video: https://www.youtube.com/watch?v=MZaf_9IZCrc

		if (low < high) {
			int pivot = partition(array, low, high, monsterArray);
			quickSort(array, low, pivot - 1, monsterArray);
			quickSort(array, pivot + 1, high, monsterArray);
		}

		return array;
	}

	public static int partition(double[] array, int low, int high, ArrayList<Monster> monsterArray) {

		// Sets pivot and i counter
		double pivot = array[high];
		int i = low - 1;

		for (int j = low; j < high; j++) {
			if (array[j] <= pivot) {
				i++;

				// Swap double array
				double temp = array[i];
				array[i] = array[j];
				array[j] = temp;

				// Swap monsterArray
				Monster monsterTemp = monsterArray.get(i);
				monsterArray.set(i, monsterArray.get(j));
				monsterArray.set(j, monsterTemp);
			}
		}

		// Swap double array
		double temp = array[i + 1];
		array[i + 1] = array[high];
		array[high] = temp;

		// Swap monsterArray
		Monster monsterTemp = monsterArray.get(i + 1);
		monsterArray.set(i + 1, monsterArray.get(high));
		monsterArray.set(high, monsterTemp);

		return i + 1;
	}
	
	public static void monsterException(ArrayList<Monster> array) throws NotEnoughMonsterException {
		
		int count = 0;
		for(Monster m : array) {
			if(m.getName().equals("temp")) {
				continue;
			}
			count++;
		}
		
		if(count < 10) {
			throw new NotEnoughMonsterException("Not enought monsters in file.");
		}
	}
	
	public static void fighterException(Fighter fighter) throws DeadFighterIsDeadException {
		
		if(fighter.getCurrentHP() <= 0.0) {
			throw new DeadFighterIsDeadException("Fighter is dead.");
		}
	}
	
	public static void wrongChoiceException() throws DeadFighterIsDeadException {
		throw new DeadFighterIsDeadException("Wrong choice.");
	}
	
	public static void dungeonExploration(Scanner sc, Fighter fighter, ArrayList<Monster> array, int numberOfEntries, String address, int lastMonster) {
				
		//Dungeon dungeon = new Dungeon();
		
		for(int i = 0; i < numberOfEntries; i++) {
			if(i == 0) {
				Dungeon.addFront(true, i+1, array.get(i));
			} else {
				Dungeon.addBack(false, i+1, array.get(i));
			}
		}
		Dungeon.print();
		printDEMenu();
		int response = sc.nextInt();
		
		while(true) {
			if(response == 1) {
				attack(array, sc, fighter, address, lastMonster, true); 
			} else if(response == 2) {
				Dungeon.print();
			} else if(response == 3) {
				System.out.println("What file do you want to load?");
				String saveAddress = sc.nextLine();
				saveAddress = sc.nextLine();
				open(saveAddress, array, numberOfEntries, true);
			} else if(response == 4) {
				System.out.println("What file do you want to save to?");
				String saveAddress = sc.nextLine();
				 saveAddress = sc.nextLine();
				save(array, sc, saveAddress, true);
			} else if(response == 5) {
				System.out.println("----CREATE A MONSTER----");
				System.out.println("Enter a name: ");
				String name = sc.nextLine();
				name = sc.nextLine();
				System.out.println("Enter a health: ");
				double currentHP = sc.nextDouble();
				System.out.println("Enter a max health: ");
				double maxHP = sc.nextDouble();
				System.out.println("Enter an attack: ");
				int attack = sc.nextInt();
				
				Monster monster = new Monster();
				monster.setName(name);
				monster.setCurrentHP(currentHP);
				monster.setMaxHP(maxHP);
				monster.setAttack(attack);
				monster.setEXP();

				System.out.println("Where would you like to put this new room?");
				int index = sc.nextInt();
				if(index > Dungeon.getSize()) {
					Dungeon.addBack(false, Dungeon.getSize()+1, monster);
				} else if(index < 0) {
					Dungeon.addFront(false, 1, monster);
				} else {
					Dungeon.insert(false, index, monster, index);
				}
				
				// For changing room numbers
				int i = 0;
				Room current = Dungeon.getStartRoom();
				while(current != null) {
					current.setRoomNo(i+1);
					i++;
					current = current.nextRoom;
				}
								
			} else if(response == 6) {
				System.out.println("What element would you like to remove?");
				int index = sc.nextInt();
				if(index == 1) {
					Dungeon.removeFront();
				} else if(index == Dungeon.getSize()) {
					Dungeon.removeBack();
				} else {
					Dungeon.remove(index);
				}
				
				// For changing room numbers
				int i = 0;
				Room current = Dungeon.getStartRoom();
				while(current != null) {
					current.setRoomNo(i+1);
					i++;
					current = current.nextRoom;
				}
				
			} else {
				try {
					throw new WrongChoiceException("Invalid input.\nEnter respose: ");
				} catch(WrongChoiceException e) {
					System.out.println("Invalid input.\nEnter respose: ");
					response = sc.nextInt();
				}
			}
			printDEMenu();
			response = sc.nextInt();
		}
	
	}
	
	public static void printDEMenu() {
		System.out.println("------DUNGEON EXPLORATION------");
		System.out.println("1. Start Dungeon Crawling");
		System.out.println("2. View the Dungeon");
		System.out.println("3. Load Dungeon");
		System.out.println("4. Save Dungeon");
		System.out.println("5. Add Room (at Location)");
		System.out.println("6. Delete Room (at Location)");
		System.out.print("Enter response: ");
	}
}