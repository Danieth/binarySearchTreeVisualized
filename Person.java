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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Person is a class that defines a person's demographic information. Person objects are sorted based upon the last name
 * data field concatenated with the firstname field. http://www.pcs.cnu.edu/~siochi/classes/sp14/270sp14Bst/
 *
 * @author DanielAckerman
 * @version 1.0
 */
public class Person {

    private static final int SHORT_LENGTH_CAP = 10;
    private static final int LONG_LENGTH_CAP = 14;
    static final LinkedHashMap<String, String> STATE_MAP = new LinkedHashMap<>();

    static {
        STATE_MAP.put("Alabama", "AL");
        STATE_MAP.put("Missouri", "MO");
        STATE_MAP.put("Alaska", "AK");
        STATE_MAP.put("Montana", "MT");
        STATE_MAP.put("Arizona", "AZ");
        STATE_MAP.put("Nebraska", "NE");
        STATE_MAP.put("Arkansas", "AR");
        STATE_MAP.put("Nevada", "NV");
        STATE_MAP.put("California", "CA");
        STATE_MAP.put("New Hampshire", "NH");
        STATE_MAP.put("Colorado", "CO");
        STATE_MAP.put("New Jersey", "NJ");
        STATE_MAP.put("Connecticut", "CT");
        STATE_MAP.put("New Mexico", "NM");
        STATE_MAP.put("Delaware", "DE");
        STATE_MAP.put("New York", "NY");
        STATE_MAP.put("North Carolina", "NC");
        STATE_MAP.put("Florida", "FL");
        STATE_MAP.put("North Dakota", "ND");
        STATE_MAP.put("Georgia", "GA");
        STATE_MAP.put("Ohio", "OH");
        STATE_MAP.put("Hawaii", "HI");
        STATE_MAP.put("Oklahoma", "OK");
        STATE_MAP.put("Idaho", "ID");
        STATE_MAP.put("Oregon", "OR");
        STATE_MAP.put("Illinois", "IL");
        STATE_MAP.put("Pennsylvania", "PA");
        STATE_MAP.put("Indiana", "IN");
        STATE_MAP.put("Rhode Island", "RI");
        STATE_MAP.put("Iowa", "IA");
        STATE_MAP.put("South Carolina", "SC");
        STATE_MAP.put("Kansas", "KS");
        STATE_MAP.put("South Dakota", "SD");
        STATE_MAP.put("Kentucky", "KY");
        STATE_MAP.put("Tennessee", "TN");
        STATE_MAP.put("Louisiana", "LA");
        STATE_MAP.put("Texas", "TX");
        STATE_MAP.put("Maine", "ME");
        STATE_MAP.put("Utah", "UT");
        STATE_MAP.put("Maryland", "MD");
        STATE_MAP.put("Vermont", "VT");
        STATE_MAP.put("Massachusetts", "MA");
        STATE_MAP.put("Virginia", "VA");
        STATE_MAP.put("Michigan", "MI");
        STATE_MAP.put("Washington", "WA");
        STATE_MAP.put("Minnesota", "MN");
        STATE_MAP.put("West Virginia", "WV");
        STATE_MAP.put("Mississippi", "MS");
        STATE_MAP.put("Wisconsin", "WI");
    }

    private String shortFirstName;
    private String longFirstName;
    private String firstName;
    private String shortLastName;
    private String longLastName;
    private String lastName;
    private int age;
    private String shortStateName;
    private String longStateName;
    private int stateFrom;

    /**
     * Initialize this Person to the given parameters.
     *
     * @param fName first name
     * @param lName last name
     * @param age   age of this person
     * @param state state where this person was born
     */
    public Person(String fName, String lName, int age, int state) {
        this.firstName = fName;
        this.shortFirstName = (fName.length() > SHORT_LENGTH_CAP) ? fName.substring(0, SHORT_LENGTH_CAP) : fName;
        this.longFirstName = (fName.length() > LONG_LENGTH_CAP) ? fName.substring(0, LONG_LENGTH_CAP) : fName;
        this.lastName = lName;
        this.shortLastName = (lName.length() > SHORT_LENGTH_CAP) ? lName.substring(0, SHORT_LENGTH_CAP) : lName;
        this.longLastName = (lName.length() > LONG_LENGTH_CAP) ? lName.substring(0, LONG_LENGTH_CAP) : lName;
        this.age = age;
        this.stateFrom = state;
        String[] keyValuePair = getKeyValuePairAtIndex(stateFrom);
        this.shortStateName = keyValuePair[1];
        this.longStateName = keyValuePair[0];
    }

    public Person(String parse) throws IllegalArgumentException {
        String[] s = parse.split(",");
        if (s.length != 4) {
            throw new IllegalArgumentException();
        }
        try {
            age = Integer.parseInt(s[2]);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        try {
            stateFrom = Integer.parseInt(s[3]);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        if (stateFrom < 0 || stateFrom > STATE_MAP.size()) {
            throw new IllegalArgumentException();
        }
        firstName = s[0];
        lastName = s[1];
    }

    private String[] getKeyValuePairAtIndex(int index) {
        int i = 0;
        for (Map.Entry<String, String> entry : STATE_MAP.entrySet()) {
            if (i == index) {
                return new String[]{entry.getKey(), entry.getValue()};
            }
            i++;
        }
        return new String[]{"", ""};
    }

    /**
     * Get the key used for sorting Persons. The key is the lastname concatenated with the first name. For example if
     * the last name is Smith and the first name is John, then the key is SmithJohn.
     *
     * @return the key used for sorting Persons
     */
    public String sortKey() {
        return firstName + "" + lastName;
    }

    /**
     * Get all the fields of this Person as a String with a capped length.
     *
     * @return <code>fistName + lastName + age + stateFrom;</code>
     */
    public String allFields() {
        return shortFirstName + "," + shortLastName + "," + age + "," + shortStateName;
    }

    /**
     * Get all the fields of this Person as a String with a higher capped length.
     * @return <code>fistName + lastName + age + stateFrom;</code>
     */
    public String allLongFields() {
        return longFirstName + "," + longLastName + "," + age + "," + longStateName;
    }

    /**
     * Compare the sort key of this Person to the sort key of the Person given as parameter. Return -1, 0, 1 according
     * as this Person's sort key is less than, equal to, or greater than the sort key of the Person given as parameter.
     * The table below illustrates how this works. Note that the comparison is CASE-SENSITIVE.
     *
     * @param otherPerson whose key is to be compared
     * @return -1, 0, 1 as described above
     */
    public int compareTo(Person otherPerson) {
        return Math.max(-1,
                Math.min(1, sortKey().compareTo(otherPerson.sortKey())));
    }

}
