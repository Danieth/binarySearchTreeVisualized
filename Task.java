public class Task {

    public final static String DELETE = "delete";
    public final static String INSERT = "insert";
    public final static String FIND = "find";
    public final static String FIND_FOR_DELETE = "findForDelete";
    public final static String FIND_SUCCESSOR = "findSuccessor";
    public final static String MIN = "min";
    public final static String MAX = "max";
    public final static String PREORDER = "preOrder";
    public final static String INORDER = "inOrder";
    public final static String POSTORDER = "postOrder";
    private final String type;
    private final Object[] args;

    public Task(String type, Object... args) {
        this.type = type;
        if (args == null) {
            this.args = new Object[0];
        } else {
            this.args = args;
        }
    }

    @SuppressWarnings("ConstantConditions")
    public int execute(BstObjPanel bst) {
        Person person = null;
        if (args.length > 0) {
            person = (Person) args[0];
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
            case PREORDER:
                if (node == bst.treeShape.root || (node != null && node.equals(bst.treeShape.root))) {
                    if (node == null) {
                        bst.buffer.clear();
                        bst.addToBuffer("The tree is empty, so you cannot do a pre-order scan");
                        break;
                    } else {
                        bst.buffer.clear();
                        bst.addToBuffer("Beginning pre-order scan of list,");
                    }
                }
                node.select(1000);
                bst.addToBuffer(node.getVal().toString());
                if (node.getLkid() != null) {
                    bst.addTaskToFront(new Task(PREORDER, null, node.getLkid()));
                }
                if (node.getRkid() != null) {
                    bst.addTaskToFront(new Task(PREORDER, null, node.getRkid()));
                }
                break;
            case INORDER:
                if (node == bst.treeShape.root || (node != null && node.equals(bst.treeShape.root))) {
                    if (node == null) {
                        bst.buffer.clear();
                        bst.addToBuffer("The tree is empty, so you cannot do an in-order scan");
                        break;
                    } else {
                        bst.buffer.clear();
                        bst.addToBuffer("Beginning in-order scan of list,");
                    }
                }
                if (parentNode == null) {
                    if (node.getLkid() != null) {
                        bst.addTaskToFront(new Task(INORDER, null, node.getLkid()));
                    }
                    node.select(100);
                    bst.addTaskToFront(new Task(INORDER, null, null, node));
                    if (node.getRkid() != null) {
                        bst.addTaskToFront(new Task(INORDER, null, node.getRkid()));
                    }
                    return 100;
                } else {
                    bst.addToBuffer(parentNode.getVal() + "");
                    parentNode.select(1000);
                }
                break;
            case POSTORDER:
                if (node == bst.treeShape.root || (node != null && node.equals(bst.treeShape.root))) {
                    if (node == null) {
                        bst.buffer.clear();
                        bst.addToBuffer("The tree is empty, so you cannot do a post-order scan");
                        break;
                    } else {
                        bst.buffer.clear();
                        bst.addToBuffer("Beginning post-order scan of list,");
                    }
                }
                if (parentNode == null) {
                    node.select(100);
                    bst.addTaskToFront(new Task(POSTORDER, null, null, node));
                    if (node.getLkid() != null) {
                        bst.addTaskToFront(new Task(POSTORDER, null, node.getLkid()));
                    }
                    if (node.getRkid() != null) {
                        bst.addTaskToFront(new Task(POSTORDER, null, node.getRkid()));
                    }
                    return 100;
                } else {
                    bst.addToBuffer(parentNode.getVal() + "");
                    parentNode.select(1000);
                }
                break;
            case DELETE:
                if (bst.treeShape.root == null) {
                    bst.addToBuffer("(Find For Delete) The tree is empty, so you cannot delete " + person);
                    bst.addTaskArgument(new Object[]{null});
                    break;
                } else if (parentNode == null && node == null && bst.treeShape.root != null && bst.treeShape.root
                        .getVal().compareTo(person) == 0) {
                    bst.addToBuffer("(Find For Delete) Finding " + person);
                    bst.addToBuffer("(Find For Delete) " + person + " is the root node, so we will find the in order successor, and delete it");
                    bst.addTaskToFront(new Task(DELETE, person));
                    bst.addTaskToFront(new Task(FIND_SUCCESSOR, null, bst.treeShape.root));
                    break;
                } else if (bst.getTaskArgumentsSize() == 0) {
                    bst.addTaskToFront(this);
                    if (node == null || parentNode == null) {
                        bst.addTaskToFront(new Task(FIND_FOR_DELETE, person));
                        bst.addToBuffer("(Find For Delete) Finding " + person);
                    } else {
                        bst.addTaskToFront(new Task(FIND_FOR_DELETE, person, node, parentNode));
                        bst.addToBuffer("(Find For Delete) Finding " + person + " from " + parentNode.getVal());
                    }
                } else if (bst.getTaskArgumentsSize() == 2) { // We have the result of Find for delete
                    node = (NodeShape) bst.getNextTaskArgument();
                    parentNode = (NodeShape) bst.getNextTaskArgument();
                    if (node == null && parentNode == null) {
                        bst.addToBuffer("(Delete) Cannot delete " + person + " because the person cannot be found");
                    } else if (node != null && parentNode != null) {
                        // node was found, so delete it
                        bst.addToBuffer("(Delete) Found the node containing " + person + " so now we will search for the in-order successor");
                        node.select();

                        // add the arguments to the queue
                        bst.addTaskArgument(node, parentNode);

                        // find the successor
                        bst.addTaskToFront(new Task(DELETE, person));
                        bst.addTaskToFront(new Task(FIND_SUCCESSOR, null, node));
                    } else if (node != null && parentNode == null) {
                        bst.addToBuffer("(Find For Delete) Found the root node containing " + person);
                        bst.treeShape.root = null;
                        bst.treeShape.delete();
                        bst.addToBuffer("(Delete) Deleted " + person);
                    } else {
                        bst.addToBuffer("(Delete) We could not find the person");
                    }
                } else if (bst.getTaskArgumentsSize() == 3) { // We found the in order successor
                    node = (NodeShape) bst.getNextTaskArgument();
                    parentNode = (NodeShape) bst.getNextTaskArgument();
                    NodeShape inOrderSuccessorNode = (NodeShape) bst.getNextTaskArgument();
                    //bst.addTaskToFront(newTask);

                    if (inOrderSuccessorNode == null) {
                        if (parentNode != null) {
                            bst.addToBuffer("(Deleting) " + person + " has no in order successor, so we delete it from the parent node, " + parentNode
                                    .getVal());
                            if (parentNode.getLkid() != null && parentNode.getLkid().getVal()
                                    .compareTo(node.getVal()) == 0) {
                                parentNode.setLkid(null);
                                bst.treeShape.delete();
                                break;
                            } else if (parentNode.getRkid() != null && parentNode.getRkid().getVal()
                                    .compareTo(node.getVal()) == 0) {
                                parentNode.setRkid(null);
                                bst.treeShape.delete();
                                break;
                            }
                        } else {
                            bst.addToBuffer("(Deleting) The BST root is the parent node to this node.");
                            bst.treeShape.delete();
                            bst.treeShape.root = null;
                        }
                    } else if (inOrderSuccessorNode != null) {
                        bst.addToBuffer("(Deleting) Found the in-order successor, so now we will swap the values, and delete " + person);
                        node.select(1000);
                        inOrderSuccessorNode.select(1000);
                        node.setVal(inOrderSuccessorNode.getVal());
                        inOrderSuccessorNode.setVal(person);
                        int compare = node.getVal().compareTo(person);
                        NodeShape newChild;
                        if (compare > 0) {
                            newChild = (NodeShape) node.getLkid();
                        } else {
                            newChild = (NodeShape) node.getRkid();
                        }
                        bst.addTaskToFront(new Task(DELETE, person, newChild, node));
                    }
                }
                break;
            case FIND_FOR_DELETE:
                // In the case of infinite loop, we descend until child is null, and then we return null for both parent and node
                // If the node is found, we return the node and it's parent.
                // If the tree is initially empty, we return a single null;

                if (node == null) {
                    node = bst.treeShape.root;
                }
                if (person != null) {
                    node.select(100);
                    int compare = node.getVal().compareTo(person);
                    if (compare == 0) {
                        bst.addToBuffer("(Find For Delete) Found " + person + " for delete");
                        bst.addTaskArgument(node);
                        bst.addTaskArgument(parentNode);
                        break;
                    } else if (compare < 0) {
                        bst.addToBuffer("(Find For Delete) " + node.getVal()
                                .toString() + " < " + person + " so we will continue down the right tree.");
                        NodeShape n = (NodeShape) node.getLkid();
                        if (n == null) {
                            bst.addTaskArgument(new Object[]{null});
                            bst.addTaskArgument(new Object[]{null});
                            bst.addToBuffer("(Find For Delete) Right tree is empty. Therefore, " + person + " is not in this tree.");
                        } else {
                            bst.addTaskToFront(new Task(FIND_FOR_DELETE, person, n, node));
                        }
                    } else {
                        bst.addToBuffer("(Find For Delete) " + node.getVal()
                                .toString() + " > " + person + " so we will continue down the left tree.");
                        NodeShape n = (NodeShape) node.getRkid();
                        if (n == null) {
                            bst.addTaskArgument(new Object[]{null});
                            bst.addTaskArgument(new Object[]{null});
                            bst.addToBuffer("(Find For Delete) Left tree is empty. Therefore, " + person + " is not in this tree.");
                        } else {
                            bst.addTaskToFront(new Task(FIND_FOR_DELETE, person, n, node));
                        }
                    }
                }
                break;
            case FIND_SUCCESSOR:
                if (node == null) {
                    bst.addToBuffer("(Find InOrder Successor) This node doesn't exist, so we will do nothing.");
                } else if (node.getRkid() != null) {
                    bst.addTaskToFront(new Task(MIN, null, node.getRkid()));
                    bst.addToBuffer("(Find InOrder Successor) This node has a left child, so we will go to the left tree and find the maximum value");
                } else if (node.getLkid() != null) {
                    bst.addTaskToFront(new Task(MAX, null, node.getLkid()));
                    bst.addToBuffer("(Find InOrder Successor) This node has a right child, so we will go to the right tree and find the maximum value");
                } else {
                    bst.addTaskArgument(new Object[]{null});
                    bst.addToBuffer("(Find InOrder Successor) This node has no children, so there is no InOrder successor");
                }
                break;
            case MIN:
                if (node != null && node.getLkid() == null) { // Found
                    bst.addToBuffer("(Found Min) Because " + node
                            .getVal() + " has no right child, it is the maximum value of this tree");
                    bst.addTaskArgument(node);
                } else if (node != null) {
                    bst.addToBuffer("(Searching For Min) Because " + node
                            .getVal() + " has a left child, we will go to the right child and continue searching for the minumum value");
                    node.select(100);
                    bst.addTaskToFront(new Task(MIN, null, node.getLkid()));
                    return 100;
                }
                break;
            case MAX:
                if (node != null && node.getRkid() == null) { // Found
                    bst.addToBuffer("(Found Max) Because " + node
                            .getVal() + " has no right child, it is the maximum value of this tree");
                    bst.addTaskArgument(node);
                } else if (node != null) {
                    bst.addToBuffer("(Searching For Max) Because " + node
                            .getVal() + " has a left child, we will go to the left child and continue searching for the maximum value");
                    node.select(100);
                    bst.addTaskToFront(new Task(MAX, null, node.getRkid()));
                    return 100;
                }
                break;
            case INSERT:
                if (bst.treeShape.root == null) {
                    bst.buffer.clear();
                    bst.addToBuffer("The tree is empty, so you cannot insert " + person);
                }
                if (node == bst.treeShape.root || node == null) {
                    node = bst.treeShape.root;
                    bst.buffer.clear();
                    bst.addToBuffer("Inserting " + person + " into the list");
                }
                if (bst.getTaskArgumentsSize() == 1) { // actual insertion
                    bst.treeShape.insert();
                    if ((boolean) bst.getNextTaskArgument()) {
                        bst.addToBuffer("(Insertion) The left tree was empty, so this node will point to the new node containing " + person);
                        node.setRkid(new NodeShape(person, null, null));
                        if (bst.isSelectingOn()) {
                            ((NodeShape) node.getRkid()).select(1000);
                        }
                    } else {
                        bst.addToBuffer("(Insertion) The right tree was empty, so this node will point to the new node containing " + person);
                        node.setLkid(new NodeShape(person, null, null));
                        if (bst.isSelectingOn()) {
                            ((NodeShape) node.getLkid()).select(1000);
                        }
                    }
                    break;
                } else {
                    if (node == null) {
                        bst.treeShape.insert();
                        bst.addToBuffer("(Insertion) Because the root of the BST was not pointing to anything, it will now point to " + person);
                        bst.treeShape.root = new NodeShape(person, null, null);
                        if (bst.isSelectingOn()) {
                            bst.treeShape.root.select(1000);
                        }
                        break;
                    } else {
                        if (bst.isSelectingOn()) {
                            node.select(1000);
                        }
                        int compare = node.getVal().compareTo(person);
                        if (compare < 0) {
                            bst.addToBuffer("(Comparison) " + node.getVal()
                                    .toString() + " < " + person + " so we will continue down the right tree");
                            NodeShape n = (NodeShape) node.getLkid();
                            if (n == null) {
                                bst.addTaskArgument(false);
                                bst.addTaskToFront(new Task(INSERT, person, node));
                            } else {
                                bst.addTaskToFront(new Task(INSERT, person, n));
                            }
                        } else if (compare > 0) {
                            bst.addToBuffer("(Comparison) " + node.getVal()
                                    .toString() + " > " + person + " so we will continue down the left tree");
                            NodeShape n = (NodeShape) node.getRkid();
                            if (n == null) {
                                bst.addTaskArgument(true);
                                bst.addTaskToFront(new Task(INSERT, person, node));
                            } else {
                                bst.addTaskToFront(new Task(INSERT, person, n));
                            }
                        } else {
                            //The node is equal. In the real world, the node is considered greater.
                            bst.addToBuffer("(Comparison) " + node.getVal()
                                    .toString() + " == " + person + " so the list will not be modified");
                            break;
                        }
                    }
                }
                break;
            case FIND:
                if (bst.treeShape.root == null) {
                    bst.buffer.clear();
                    bst.addToBuffer("(Find) The tree is empty, so you cannot find " + person);
                    break;
                }
                if (node == bst.treeShape.root || node == null) {
                    node = bst.treeShape.root;
                    bst.buffer.clear();
                    bst.addToBuffer("(Find) Finding " + person);
                }
                if (person != null) {
                    node.select(1000);
                    int compare = node.getVal().compareTo(person);
                    if (compare == 0) {
                        bst.addToBuffer("Found " + person);
                    } else if (compare < 0) {
                        bst.addToBuffer("(Comparison) " + node.getVal()
                                .toString() + " < " + person + " so we will continue down the right tree");
                        NodeShape n = (NodeShape) node.getLkid();
                        if (n == null) {
                            bst.addTaskArgument(false);
                            bst.addToBuffer("Right tree is empty. The person we are looking for is not in this tree.");
                        } else {
                            bst.addTaskToFront(new Task(FIND, person, n));
                        }
                    } else {
                        bst.addToBuffer("(Comparison) " + node.getVal()
                                .toString() + " > " + person + " so we will continue down the left tree");
                        NodeShape n = (NodeShape) node.getRkid();
                        if (n == null) {
                            bst.addTaskArgument(false);
                            bst.addToBuffer("Left tree is empty. The person we are looking for is not in this tree.");
                        } else {
                            bst.addTaskToFront(new Task(FIND, person, n));
                        }
                    }
                }
                break;
        }
        return 1000;
    }
}
