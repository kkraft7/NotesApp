package server;

import java.util.*;

/**
** Generic class for implementing a tree structure.
 */
public class Node<T> {
  private final String name;
  private Node<T> parent;
  private int level;
  private ArrayList<Node<T>> children = new ArrayList<>();

  /**
   * ** Constructor for non-root node.
   */
  public Node(String name, Node<T> parent) {
    this.name = name;
    children = new ArrayList<Node<T>>();
    setParent(parent);
  }

  /**
   ** Constructor for root node.
   */
  public Node(String name) {
    this(name, null);
  }

  public int getLevel() { return level; }
  public String getName() { return name; }

  public Node<T> getParent() { return parent; }
  public Node<T> getChild(int index) { return children.get(index); }
  public ArrayList<Node<T>> getChildren() {
    return children;
  }

  public void addChild(Node<T> child) {
    children.add(child);
  }

  public void move(Node<T> newParent) {
    setParent(newParent);
  }

  // The level is set here (outside of the constructor) to support changing the parent node
  public void setParent(Node<T> newParent) {
    parent = newParent;
    if (parent != null) {
      parent.addChild(this);
      level = parent.getLevel() + 1;
    }
    else {
      level = 1;
    }
  }

  // Define toString()?}
}
