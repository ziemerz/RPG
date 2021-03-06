/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg;

import java.util.Random;
//import java.util.Scanner;

/**
 *
 * @author mzhje_000
 */
public class RPG {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        
        Builder build = new Builder();
        Player player = new Player();
        String command;
        String value = "";
        int gold = 0;
        String error;
        boolean playing = false;
        Room currentRoom;
        currentRoom = build.getRoom(0);
        player.setCurWeapon(new Weapon("Fists", "You have no weapon - but you can punch the monsters in the face with your BROOOOOO-FIST!", 1, 2));
        Random rnd = new Random();
        int maxDmg = player.getCurWeapon().getMaxDmg();
        int minDmg = player.getCurWeapon().getMinDmg();
        IO controller = new IO();
        Enemy enemy;
        Room prevRoom = null;
        boolean bound = false;
        
        
        controller.writeLine("What's your name young adventurer?");
        if(!playing){
            player.setName(controller.read());
            playing = true;
            controller.write("You find yourself in a dark room. Only wearing some dirty clothes. \n"
                + "On the floor is some useful items, pick them up using the take command. \n" 
                + "Use the help command if you get stuck, and good luck");
        }
        while(playing){
            String input = controller.read();
            String[] data = input.split(" ");

            if(data.length == 2){
                command = data[0];
                value = data[1];
            }else{
                command = data[0];
            }
            
            
            
            switch(command){
                case "help":
                    controller.writeLine("-go");
                    controller.writeFormatLine("south");
                    controller.writeFormatLine("north");
                    controller.writeFormatLine("east");
                    controller.writeFormatLine("west");
                    controller.writeFormatLine("back");
                    
                    controller.write("-take");
                    controller.write("-use");
                    controller.writeFormatLine("potion");
                    controller.writeFormatLine("key");
                    controller.writeFormatLine("poison");
                    controller.write("-look");
                    controller.write("-attack");
                    controller.write("-inventory");
                    controller.write("-quit");
                    break;
                case "info":
                    controller.writeLine("Name: " + player.getName());
                    controller.write("Level: " + player.getLevel());
                    controller.write("HP: " + player.getCurHP() + " / " + player.getMaxHP());
                    controller.write("Equipped weapon: " + player.getCurWeapon() + ", " + player.getCurWeapon().getDesc());
                    controller.write("Damage: " + minDmg + " - " + maxDmg);
                    controller.write("Valuables in gold: " + gold);
                            
                    break;
                case "go":
                    error = "I can't go that way!";
                    

                    switch (value) {
                        case "south":
                            if(currentRoom.getSouth() != null && !bound){
                                prevRoom = currentRoom;
                                currentRoom = currentRoom.getSouth();
                                controller.writeLine(currentRoom.getRoomDesc());
                            }else{
                                controller.writeLine(error);
                            }
                            break;
                        case "north":
                            if(currentRoom.getNorth() != null && !bound){
                                prevRoom = currentRoom;
                                currentRoom = currentRoom.getNorth();
                                controller.writeLine(currentRoom.getRoomDesc());
                            }else{
                                controller.writeLine(error);
                            }
                            break;
                        case "east":
                            if(currentRoom.getEast() != null && !bound){
                                prevRoom = currentRoom;
                                currentRoom = currentRoom.getEast();
                                controller.writeLine(currentRoom.getRoomDesc());
                            }else{
                                controller.writeLine(error);
                            }
                            break;
                        case "west":
                            if(currentRoom.getWest() != null && !bound){
                                prevRoom = currentRoom;
                                currentRoom = currentRoom.getWest();
                                controller.writeLine(currentRoom.getRoomDesc());
                            }else{
                                controller.writeLine(error);
                            }
                            break;
                    
                        case "back":
                            if(bound){
                                controller.writeLine("You flee from the enemy and end up in the previous room.");
                                currentRoom = prevRoom;
                                bound = false;
                            }
                            break;
                        default:
                            controller.writeLine("You want to go where?");
                            break;
                    }
                    if(currentRoom.getItems() != null && !bound){
                        controller.write("I see some items that might be useful in this room");
                    }
                if(currentRoom.getEnemy() != null){
                    bound = true;
                    enemy = currentRoom.getEnemy();
                    controller.writeLine("A " + enemy.getName() + " appears. " + enemy.getDescription());
                    int enemyDmg = enemy.getDmgActual();
                    player.setCurHP(player.getCurHP()-enemyDmg);
                    controller.write("The " + enemy.getName() + " hits you for: " + enemyDmg);
                    controller.write("Your current HP: " + player.getCurHP());
                }else{
                    enemy = null;
                }
                break;
            
                case "take":
                    if(!bound){
                        if(currentRoom.getItems() != null){
                            for (Item item : currentRoom.getItems()) {
                                player.addItem(item);
                            }

                            currentRoom.removeItems();
                        }else{
                            controller.writeLine("There's nothing to pick up in this room");
                        }
                            for(int i = 0; i < player.getInventory().size(); i++){
                                Item item = player.getInventory().get(i);
                                if(item instanceof Weapon){
                                    Weapon weapon = (Weapon) item;
                                    if(weapon.getMaxDmg() > player.getCurWeapon().getMaxDmg()){
                                        player.setCurWeapon(weapon);
                                        minDmg = player.getCurWeapon().getMinDmg();
                                        maxDmg = player.getCurWeapon().getMaxDmg();

                                    }
                                }
                            }
                            for(int i = 0; i < player.getInventory().size(); i++){
                                Item item = player.getInventory().get(i);
                                if(item instanceof Valuables){
                                    Valuables valuables = (Valuables) item;
                                    gold += valuables.getValue();
                                }
                            }
                    }else{
                        controller.write("I can't pick up anything. A monster is in the way");
                    }
                    
                    if(currentRoom.getRoomNumber() == 20){
                        controller.writeLine("You have won the game!");
                        
                        controller.writeLine("Your accomplishments: ");
                        controller.writeLine("Your inventory:");
                            for(Item item : player.getInventory()){
                                controller.write(item);
                            }
                            controller.writeLine("Name: " + player.getName());
                            controller.write("Level: " + player.getLevel());
                            controller.write("HP: " + player.getCurHP() + " / " + player.getMaxHP());
                            controller.write("Equipped weapon: " + player.getCurWeapon() + ", " + player.getCurWeapon().getDesc());
                            controller.write("Damage: " + minDmg + " - " + maxDmg);
                            controller.write("Valuables in gold: " + gold);

                    }
                    break;
                case "look":
                    controller.writeLine(currentRoom.getRoomDesc());
                    break;
                case "use":
                    switch(value){
                        case "potion":
                            
                            if(player.getInventory().toString().contains("Potion") 
                                    && player.getCurHP() != player.getMaxHP()){
                                for(Item item : player.getInventory()){
                                    controller.write(item.getName());
                                    if(item.getName().equals("Potion")){
                                    if(item instanceof Consumable){
                                        Consumable consumable = (Consumable) item;
                                        player.setCurHP(player.getMaxHP());
                                        player.getInventory().remove(consumable);
                                        controller.writeLine("Jeg brugte en potion yay!");
                                        break;
                                    }
                                    }
                                }
                            }else{
                                controller.writeLine("You already have full HP."
                                        + " No reason to use a potion!");
                            }
                            break;
                        case "key":
                            if (player.getInventory().toString().contains("DoomKey") 
                                    && currentRoom.getRoomNumber() == 14){
                                        controller.writeLine("The DoomKey opens the door");
                                        build.useKey(14, 15, "north");
                            }
                            else if (player.getInventory().toString().contains("GoldKey") 
                                    && currentRoom.getRoomNumber() == 16){
                                        controller.writeLine("The GoldKey opens the door");
                                        build.useKey(16, 17, "north");
                            }else{
                                controller.writeLine("No doors in this room fit your key");
                            }
                            break;
                        case "poison":
                            minDmg += 5;
                            maxDmg += 5;
                            break;
                    }
                    break;
                case "attack":   
                    if(currentRoom.getEnemy() != null){
                        enemy = currentRoom.getEnemy();
                        int enemyDmg = enemy.getDmgActual();
                        player.setCurHP(player.getCurHP()-enemyDmg);
                        int playerDmg = rnd.nextInt((maxDmg + 1) - minDmg) + minDmg;
                       currentRoom.getEnemy().setHP(playerDmg);    
                        controller.writeLine("The " + enemy.getName() + " hit you for: " + enemyDmg);
                        controller.write("You dealt " + playerDmg + " damage to the " + enemy.getName());
                        controller.write("Your HP: " + player.getCurHP());
                        controller.write("Monster HP: " + enemy.getHP());
                        if(enemy.getHP() <= 0){
                            currentRoom.removeEnemy();
                            controller.writeLine("You killed the " + enemy.getName());
                            player.setLevel(1);
                            player.setMaxHP(10);
                            player.setCurHP(player.getLevelHP());
                            bound = false;
                        }
                    }else{
                        controller.writeLine("There's nothing to attack here");
                    }
                    
                    break;
                case "inventory":
                    controller.writeLine("Your inventory:");
                    for(Item item : player.getInventory()){
                        controller.write(item);
                    }
                    break;
                case "quit":
                    controller.writeLine("Goodbye noob...");
                    playing = false;
                    break;
                case "y":
                    if(player.getCurHP() <= 0){
                        build = new Builder();
                        player = new Player();
                        gold = 0;
                        currentRoom = build.getRoom(0);
                        player.setCurWeapon(new Weapon("Fists", "You have no weapon - but you can punch the monsters in the face with your BROOOOOO-FIST!", 1, 2));
                        prevRoom = null;
                        bound = false;
                        player.resetInventory();
                        player.resetHP();
                        controller.writeLine("What's your name young adventurer?");

                        player.setName(controller.read());
                        controller.write("You find yourself in a dark room. Only wearing some dirty clothes. \n"
                            + "On the floor is some useful items, pick them up using the take command. \n" 
                            + "Use the help command if you get stuck, and good luck");
                    }
                    break;
                case "n":
                    if(player.getCurHP() <= 0){
                        controller.writeLine("Your accomplishments: ");
                        controller.writeLine("Your inventory:");
                            for(Item item : player.getInventory()){
                                controller.write(item);
                            }
                            controller.writeLine("Name: " + player.getName());
                            controller.write("Level: " + player.getLevel());
                            controller.write("HP: " + player.getCurHP() + " / " + player.getMaxHP());
                            controller.write("Equipped weapon: " + player.getCurWeapon() + ", " + player.getCurWeapon().getDesc());
                            controller.write("Damage: " + minDmg + " - " + maxDmg);
                            controller.write("Valuables in gold: " + gold);

                        playing = false;
                    }
                    break;
            }
            if(player.getCurHP() <= 0){
                controller.writeLine("You've died. Game over! Press 'y' if you want to continue playing. Else press 'n' to quit");
            }
        }
        
    }
    
}
