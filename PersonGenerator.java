import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonGenerator {

    private Set<String> used;
    private List<String> firstNames;
    private List<String> lastNames;

    public PersonGenerator() {
        firstNames = new ArrayList<>();
        generateFirstNames();
        lastNames = new ArrayList<>();
        generateLastNames();
        used = new HashSet<>();
    }

    public static void main(String[] args) {
        PersonGenerator ps = new PersonGenerator();
        for (int i = 0; i < 100; i++) {
            System.out.println(ps.generateRandomPerson().allFields());
        }
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

    public Person generateRandomPerson() {
        Collections.shuffle(firstNames);
        Collections.shuffle(lastNames);
        String firstName = firstNames.get(0);
        String lastName = lastNames.get(0);
        while (used.contains(firstName + lastName)) {
            Collections.shuffle(firstNames);
            Collections.shuffle(lastNames);
            firstName = firstNames.get(0);
            lastName = lastNames.get(0);
        }
        used.add(firstName + lastName);
        String state = Person.STATE_MAP.keySet().toArray()[(int) (Math.random()*Person.STATE_MAP.size())].toString();
        int age = (int) Math.rint(((Math.log(Math.random() * 100 + 1) * 2250 / 101 + 20) % 97) + 3);
        return new Person(firstName, lastName, age, state);
    }
    
    public Person generateMedianPerson() {
        Collections.sort(firstNames);
        Collections.sort(lastNames);
        String firstName = firstNames.get(firstNames.size()/2);
        String lastName = lastNames.get(lastNames.size()/2);
        if(used.contains(firstName + lastName)) {
            int i = 1;
            while(used.contains(firstName + lastName)) {
                firstName = firstNames.get((int) (firstNames.size()/2+i*Math.rint(2*(Math.random()-.5))));
                lastName = lastNames.get((int) (lastNames.size()/2+i*Math.rint(2*(Math.random()-.5))));
                i++;
            }
        }
        used.add(firstName + lastName);
        String state = Person.STATE_MAP.keySet().toArray()[(int) (Math.random()*Person.STATE_MAP.size())].toString();
        int age = (int) Math.rint(((Math.log(Math.random() * 100 + 1) * 2250 / 101 + 20) % 97) + 3);
        return new Person(firstName, lastName, age, state);
    }

    public void reset() {
        used.clear();
    }
}
