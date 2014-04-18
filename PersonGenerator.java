import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/* Clarifications:
 * This code is all the work of Daniel Ackerman. I used no other code but
 * my own in the process of creating this solution. No one else, to the best of
 * my efforts, and to the extent of my knowledge, has viewed this work except
 * for myself, and my professors. I have collaborated with no one, received help
 * from no one, and talked to no one about this solution. All code here is the
 * individual work of Daniel Ackerman.
 * 
 * Copyright (c) <2013> <Daniel Ackerman>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above clarifications, copyright notice, and this permission
 * notice shall be included in all copies of the Software.
 *
 */

public class PersonGenerator {
    
    public static void main(String[] args) {
        PersonGenerator ps = new PersonGenerator();
        for(int i = 0; i < 100; i++) {
            System.out.println(ps.generateRandomPerson().allFields());
        }
    }
    
    private HashSet<String> used;
    private HashMap<String,String> states;
    private ArrayList<String> firstNames;
    private ArrayList<String> lastNames;
    
    public PersonGenerator() {
        states = new HashMap<String,String>();
        generateStates();
        firstNames = new ArrayList<String>();
        generateFirstNames();
        lastNames = new ArrayList<String>();
        generateLastNames();
        used = new HashSet<String>();
    }
    
    private void generateFirstNames() {
        firstNames.add("Michael");
        firstNames.add("James");
        firstNames.add("John");
        firstNames.add("Robert");
        firstNames.add("David");
        firstNames.add("William");
        firstNames.add("Mary");
        firstNames.add("Richard");
        firstNames.add("Joseph");
        firstNames.add("Christopher");
        firstNames.add("Thomas");
        firstNames.add("Daniel");
        firstNames.add("Charles");
        firstNames.add("Matthew");
        firstNames.add("Jennifer");
        firstNames.add("Patricia");
        firstNames.add("Linda");
        firstNames.add("Mark");
        firstNames.add("Elizabeth");
        firstNames.add("Barbara");
        firstNames.add("Anthony");
        firstNames.add("Steven");
        firstNames.add("Brian");
        firstNames.add("Joshua");
        firstNames.add("Andrew");
        firstNames.add("Kevin");
        firstNames.add("Paul");
        firstNames.add("Susan");
        firstNames.add("Donald");
        firstNames.add("Jessica");
        firstNames.add("Kenneth");
        firstNames.add("Timothy");
        firstNames.add("Jason");
        firstNames.add("Lisa");
        firstNames.add("Karen");
        firstNames.add("Jeffrey");
        firstNames.add("Sarah");
        firstNames.add("Ronald");
        firstNames.add("Nancy");
        firstNames.add("Ryan");
        firstNames.add("Edward");
        firstNames.add("George");
        firstNames.add("Eric");
        firstNames.add("Sandra");
        firstNames.add("Ashley");
        firstNames.add("Nicholas");
        firstNames.add("Kimberly");
        firstNames.add("Gary");
        firstNames.add("Michelle");
        firstNames.add("Amanda");
        firstNames.add("Stephen");
        firstNames.add("Margaret");
        firstNames.add("Donna");
        firstNames.add("Jonathan");
        firstNames.add("Jacob");
        firstNames.add("Melissa");
        firstNames.add("Scott");
        firstNames.add("Justin");
        firstNames.add("Carol");
        firstNames.add("Stephanie");
        firstNames.add("Deborah");
        firstNames.add("Brandon");
        firstNames.add("Emily");
        firstNames.add("Betty");
        firstNames.add("Rebecca");
        firstNames.add("Larry");
        firstNames.add("Laura");
        firstNames.add("Cynthia");
        firstNames.add("Sharon");
        firstNames.add("Gregory");
        firstNames.add("Amy");
        firstNames.add("Kathleen");
        firstNames.add("Angela");
        firstNames.add("Dorothy");
        firstNames.add("Patrick");
        firstNames.add("Brenda");
        firstNames.add("Benjamin");
        firstNames.add("Pamela");
        firstNames.add("Nicole");
    }
    
    private void generateLastNames() {
        lastNames.add("Samuel");
        lastNames.add("Johnson");
        lastNames.add("Williams");
        lastNames.add("Brown");
        lastNames.add("Jones");
        lastNames.add("Miller");
        lastNames.add("Davis");
        lastNames.add("Garcia");
        lastNames.add("Rodriguez");
        lastNames.add("Wilson");
        lastNames.add("Martinez");
        lastNames.add("Anderson");
        lastNames.add("Taylor");
        lastNames.add("Thomas");
        lastNames.add("Hernandez");
        lastNames.add("Moore");
        lastNames.add("Martin");
        lastNames.add("Jackson");
        lastNames.add("Thompson");
        lastNames.add("White");
        lastNames.add("Lopez");
        lastNames.add("Lee");
        lastNames.add("Gonzalez");
        lastNames.add("Harris");
        lastNames.add("Clark");
        lastNames.add("Lewis");
        lastNames.add("Robinson");
        lastNames.add("Walker");
        lastNames.add("Perez");
        lastNames.add("Hall");
        lastNames.add("Young");
        lastNames.add("Allen");
        lastNames.add("Sanchez");
        lastNames.add("Wright");
        lastNames.add("King");
        lastNames.add("Scott");
        lastNames.add("Green");
        lastNames.add("Baker");
        lastNames.add("Adams");
        lastNames.add("Nelson");
        lastNames.add("Hill");
        lastNames.add("Ramirez");
        lastNames.add("Campbell");
        lastNames.add("Mitchell");
        lastNames.add("Roberts");
        lastNames.add("Carter");
        lastNames.add("Phillips");
        lastNames.add("Evans");
        lastNames.add("Turner");
        lastNames.add("Torres");
        lastNames.add("Parker");
        lastNames.add("Collins");
        lastNames.add("Edwards");
        lastNames.add("Stewart");
        lastNames.add("Flores");
        lastNames.add("Morris");
        lastNames.add("Nguyen");
        lastNames.add("Murphy");
        lastNames.add("Rivera");
        lastNames.add("Cook");
        lastNames.add("Rogers");
        lastNames.add("Morgan");
        lastNames.add("Peterson");
        lastNames.add("Cooper");
        lastNames.add("Reed");
        lastNames.add("Bailey");
        lastNames.add("Bell");
        lastNames.add("Gomez");
        lastNames.add("Kelly");
        lastNames.add("Howard");
        lastNames.add("Ward");
        lastNames.add("Cox");
        lastNames.add("Diaz");
        lastNames.add("Richardson");
        lastNames.add("Wood");
        lastNames.add("Watson");
        lastNames.add("Brooks");
        lastNames.add("Bennett");
        lastNames.add("Gray");
    }

    private void generateStates() {
        states.put("Alabama","AL");
        states.put("Missouri","MO");
        states.put("Alaska","AK");
        states.put("Montana","MT");
        states.put("Arizona","AZ");
        states.put("Nebraska","NE");
        states.put("Arkansas","AR");
        states.put("Nevada","NV");
        states.put("California","CA");
        states.put("NewHampshire","NH");
        states.put("Colorado","CO");
        states.put("NewJersey","NJ");
        states.put("Connecticut","CT");
        states.put("NewMexico","NM");
        states.put("Delaware","DE");
        states.put("NewYork","NY");
        states.put("NorthCarolina","NC");
        states.put("Florida","FL");
        states.put("NorthDakota","ND");
        states.put("Georgia","GA");
        states.put("Ohio","OH");
        states.put("Hawaii","HI");
        states.put("Oklahoma","OK");
        states.put("Idaho","ID");
        states.put("Oregon","OR");
        states.put("Illinois","IL");
        states.put("Pennsylvania","PA");
        states.put("Indiana","IN");
        states.put("RhodeIsland","RI");
        states.put("Iowa","IA");
        states.put("SouthCarolina","SC");
        states.put("Kansas","KS");
        states.put("SouthDakota","SD");
        states.put("Kentucky","KY");
        states.put("Tennessee","TN");
        states.put("Louisiana","LA");
        states.put("Texas","TX");
        states.put("Maine","ME");
        states.put("Utah","UT");
        states.put("Maryland","MD");
        states.put("Vermont","VT");
        states.put("Massachusetts","MA");
        states.put("Virginia","VA");
        states.put("Michigan","MI");
        states.put("Washington","WA");
        states.put("Minnesota","MN");
        states.put("WestVirginia","WV");
        states.put("Mississippi","MS");
        states.put("Wisconsin","WI");
    }
    
    public Person generateRandomPerson() {
        Collections.shuffle(firstNames);
        Collections.shuffle(lastNames);
        String firstName = firstNames.get(0);
        String lastName = lastNames.get(0);
        while(used.contains(firstName+lastName)) {
            Collections.shuffle(firstNames);
            Collections.shuffle(lastNames);
            firstName = firstNames.get(0);
            lastName = lastNames.get(0);
        }
        used.add(firstName + lastName);
        String state = (String)(states.values().toArray()[(int) (Math.random()*states.size())]);
        int age = (int) Math.rint(((Math.log(Math.random()*100+1)*2250/101+20) % 97)+3);
        return new Person(firstName, lastName, age, state);
    }
}
