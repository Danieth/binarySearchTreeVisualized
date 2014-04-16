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
/**
 * Person is a class that defines a person's demographic information. Person
 * objects are sorted based upon the last name data field concatenated with the
 * firstname field. http://www.pcs.cnu.edu/~siochi/classes/sp14/270sp14Bst/
 * 
 * @author DanielAckerman
 * @version 1.0
 */
public class Person {

    private String firstName;
    private String lastName;
    private int age;
    private String stateFrom;

    /**
     * Initialize this Person to the given parameters.
     * 
     * @param fName
     *            first name
     * @param lName
     *            last name
     * @param age
     *            age of this person
     * @param state
     *            state where this person was born
     */
    public Person(String fName, String lName, int age, String state) {
        this.firstName = fName;
        this.lastName = lName;
        this.age = age;
        this.stateFrom = state;
    }
    
    public Person(String parse) throws IllegalArgumentException {
        String[] s = parse.split(",");
        if(s.length != 4) {
            throw new IllegalArgumentException();
        }
        try {
            age = Integer.parseInt(s[2]);
        } catch(Exception e) {
            throw new IllegalArgumentException();
        }
        firstName = s[0];
        lastName = s[1];
        stateFrom = s[3];
    }

    /**
     * Get the key used for sorting Persons. The key is the lastname
     * concatenated with the first name. For example if the last name is Smith
     * and the first name is John, then the key is SmithJohn.
     * 
     * @return the key used for sorting Persons
     */
    public String sortKey() {
        return firstName + "" + lastName;
    }

    /**
     * Get all the fields of this Person as a String.
     * 
     * @return <code>fistName + lastName + age + stateFrom;</code>
     */
    public String allFields() {
        return lastName + "," + firstName + "," + age + "," + stateFrom;
    }

    /**
     * Compare the sort key of this Person to the sort key of the Person given
     * as parameter. Return -1, 0, 1 according as this Person's sort key is less
     * than, equal to, or greater than the sort key of the Person given as
     * parameter. The table below illustrates how this works. Note that the
     * comparison is CASE-SENSITIVE.
     * 
     * @param otherPerson
     *            whose key is to be compared
     * @return -1, 0, 1 as described above
     */
    public int compareTo(Person otherPerson) {
        return Math.max(-1,
                Math.min(1, sortKey().compareTo(otherPerson.sortKey())));
    }
}
