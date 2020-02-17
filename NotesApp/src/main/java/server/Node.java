package main.java.server;

import java.io.*;
import java.util.*;
import java.lang.Math;

/**
** Generic class for implementing a tree structure.
 */
public class Node<T> {
  private String name;
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

  public void setParent(Node<T> newParent) {
    parent = newParent;
    if (parent != null)
      parent.addChild(this);
    // Level is always determined based on the parent:
    level = (parent == null ? 1 : parent.getLevel() + 1);
  }

  // Define toString()?}
}
