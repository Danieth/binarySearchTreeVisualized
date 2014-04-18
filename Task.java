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

public class Task {

    private final static String DELETE = "delete";
    private final static String INSERT = "insert";
    private final static String FIND = "find";
    private final static String FIND_FOR_DELETE = "findForDelete";
    private final static String FIND_SUCCESSOR = "findSuccessor";
    private final static String MIN = "min";
    private final static String MAX = "max";
    private final static String DISPLAY = "display";
    private final String type;
    private final Object[] args;

    public Task(String type, Object... args) {
        this.type = type.toLowerCase();
        if (args == null) {
            this.args = new Object[0];
        } else {
            this.args = args;
        }
    }

    public void exectute(BstObjPanel bst) {
        String message = null;
        Person person = null;
        if (args.length > 0) {
            if (args[0] instanceof Person) {
                person = (Person) args[0];
            } else if (args[0] instanceof String) {
                message = (String) args[0];
            }
        }
        NodeShape node = null;
        if (args.length > 1) {
            node = (NodeShape) args[1];
        }
        NodeShape parentNode = null;
        if (args.length > 2) {
            parentNode = (NodeShape) args[2];
        }

        switch (type) {
            case DELETE:
                if (bst.getTaskArgumentsSize() == 0) {
                    bst.addTaskToFront(this);
                    bst.addTaskToFront(new Task(FIND_FOR_DELETE, person));
                    bst.addTaskToFront(new Task(DISPLAY, "Searching for "
                            + person + " to delete them"));
                } else if (bst.getTaskArgumentsSize() == 2) { // We have the node with the person
                    node = (NodeShape) bst.getNextTaskArgument();
                    parentNode = (NodeShape) bst.getNextTaskArgument();
                    if (node == null) {
                        // the node was not found
                        bst.addTaskToFront(new Task(DISPLAY,
                                "Could not find the person " + person));
                    } else {
                        // node was found, so delete it

                        // re-add the arguments to the queue
                        bst.addTaskArgument(node, parentNode);

                        // find the successor
                        bst.addTaskToFront(new Task(DELETE, person));
                        bst.addTaskToFront(new Task(FIND_SUCCESSOR, null, node));
                        bst.addTaskToFront(new Task(DISPLAY,
                                "Found the node containing " + person + " so now we will search for the in-order successor"));
                    }
                } else if (bst.getTaskArgumentsSize() == 3) { // We found the in order successor
                    node = (NodeShape) bst.getNextTaskArgument();
                    parentNode = (NodeShape) bst.getNextTaskArgument();
                    TreeNode inOrderSuccessorNode = (TreeNode) bst.getNextTaskArgument();
                    //bst.addTaskToFront(newTask);

                    if (inOrderSuccessorNode == null) {
                        bst.addTaskToFront(new Task(DISPLAY,
                                "The value has no in order successor, so we delete the reference to it from the parent"));
                        if (parentNode != null) {
                            if (parentNode.getLkid() != null && parentNode.getLkid().getVal()
                                    .compareTo(node.getVal()) == 0) {
                                parentNode.setLkid(null);
                            } else if (parentNode.getRkid() != null && parentNode.getRkid().getVal()
                                    .compareTo(node.getVal()) == 0) {
                                parentNode.setRkid(null);
                            }
                        } else {
                            // extreme edge case, the node is the first node, and it is barren,
                            // therefore, we could simply go to bst and tell it to delete the
                            // reference to it, but that seems a bit shady
                            bst.treeShape.root = null;

                            // Either refactor code to include reference to the bst, or delete the shady way
                        }
                    } else if (inOrderSuccessorNode != null) {
                        bst.addTaskToFront(new Task(DISPLAY,
                                "Found the in-order successor, so now we will swap the values, and delete the in order successor node"));
                        node.setVal(inOrderSuccessorNode.getVal());
                        inOrderSuccessorNode.setVal(node.getVal());
                        bst.addTaskArgument(DELETE, person, node, parentNode);
                    }
                }
                break;
            case FIND_FOR_DELETE:
                //TODO special case of find. Finds the value and adds it with bst.addTaskArgument(foundNode)
                
                break;
            case FIND_SUCCESSOR:
                //TODO finds the in order successor from the node given, add it with bst.addTaskArgument(inOrderSuccessor)
                if (node != null && node.getRkid() != null) {
                    bst.addTaskToFront(new Task(MIN, null, node.getRkid()));
                    bst.addTaskToFront(new Task(DISPLAY, "This node has a right child, so we will go to the right tree and find the minimum value"));
                } else if (node != null && node.getLkid() != null) {
                    bst.addTaskToFront(new Task(MAX, null, node.getLkid()));
                    bst.addTaskToFront(new Task(DISPLAY, "This node has a left child, so we will go to the left tree and find the maximum value"));
                }
                break;
            case MIN:
                if (node != null && node.getLkid() == null) { // Found
                    bst.addTaskArgument(node);
                } else if (node != null) {
                    bst.addTaskToFront(new Task(MIN, null, node.getLkid()));
                }
                break;
            case MAX:
                if (node != null && node.getRkid() == null) { // Found
                    bst.addTaskArgument(node);
                } else if (node != null) {
                    bst.addTaskToFront(new Task(MAX, null, node.getRkid()));
                }
                break;
            case INSERT:
                if(node == null) {
                    node = bst.treeShape.root;
                }
                if(bst.getTaskArgumentsSize() == 1) {
                    bst.treeShape.insert();
                    if((boolean)bst.getNextTaskArgument()) {
                        node.setRkid(new NodeShape(person,null,null));
                    } else {
                        node.setLkid(new NodeShape(person,null,null));
                    }
                    return;
                } else {
                    if(node == null) {
                        bst.treeShape.insert();
                        bst.treeShape.root = new NodeShape(person, null, null);
                        return;
                    } else {
                        node.select(1000);
                        if(node.getVal().compareTo(person) < 0) {
                            NodeShape n = (NodeShape)node.getLkid();
                            if(n == null) {
                                bst.addTaskArgument(false);
                                bst.addTaskToFront(new Task(INSERT, person, node));
                            } else {
                                bst.addTaskToFront(new Task(INSERT, person, n));
                            }
                        } else if(node.getVal().compareTo(person) > 0) {
                            NodeShape n = (NodeShape)node.getRkid();
                            if(n == null) {
                                bst.addTaskArgument(true);
                                bst.addTaskToFront(new Task(INSERT, person, node));
                            } else {
                                bst.addTaskToFront(new Task(INSERT, person, n));
                            }
                        } else {
                            //Problem!
                        }
                    }
                }
                break;
            case FIND:

                break;
        }
    }
    // Tasks
    /*
     * TODO build task class
     * 
     * Other Important Tasks
     * 
     * Print results Modify Shapes change color of shape delete a shape
     */

}
