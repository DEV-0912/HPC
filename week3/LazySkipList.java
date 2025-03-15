// LazySkipList.java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Iterator;
import java.util.NoSuchElementException;
/** @param <T> type of list items */
public final class LazySkipList<T> implements Iterable<T> {
  static final int MAX_LEVEL = 32;
  int randomSeed = ((int) System.currentTimeMillis()) | 0x010000;
  final Node<T> head = new Node<T>(Integer.MIN_VALUE);
  final Node<T> tail = new Node<T>(Integer.MAX_VALUE);
  public LazySkipList() {
    for (int i = 0; i < head.next.length; i++) {
      head.next[i] = tail;
    }
  }
  private int randomLevel() {
    int x = randomSeed;
    x ^= x << 13;
    x ^= x >>> 17;
    randomSeed = x ^= x << 5;
    if ((x & 0x8001) != 0) // test highest and lowest bits
      return 0;
    int level = 1;
    while (((x >>>= 1) & 1) != 0) ++level;
    return Math.min(level,MAX_LEVEL-1);
  }
  
  boolean add(T x) {
    int topLevel = randomLevel();
    Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1];
    Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL + 1];
    while (true) {
      int lFound = find(x, preds, succs);
      if (lFound != -1) {
        Node<T> nodeFound = succs[lFound];
        if (!nodeFound.marked) { // not marked
          while (!nodeFound.fullyLinked) {} // can be marked only if fully linked
          return false; // so its unmarked and fully linked
        }
        continue;
      }
      int highestLocked = -1;
      try {
        Node<T> pred, succ;
        boolean valid = true;
        for (int level = 0; valid && (level <= topLevel); level++) {
          pred = preds[level];
          succ = succs[level];
          pred.lock.lock();
          highestLocked = level;
          valid = !pred.marked && !succ.marked && pred.next[level]==succ;
        }
        if (!valid) continue;
        Node<T> newNode = new Node(x, topLevel);
        // first link succs
        for (int level = 0; level <= topLevel; level++) 
          newNode.next[level] = succs[level];
        // then link next fields of preds
        for (int level = 0; level <= topLevel; level++) 
          preds[level].next[level] = newNode;
        newNode.fullyLinked = true;
        return true;
      } finally {
        for (int level = 0; level <= highestLocked; level++) {
          preds[level].unlock();
        }
      }
    }
  }
  boolean contains(T x) {
    Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1];
    Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL + 1];
    int lFound = find(x, preds, succs);
    return (lFound != -1
        && succs[lFound].fullyLinked
        && !succs[lFound].marked);
  }
  
  boolean remove(T x) {
    Node<T> victim = null;
    boolean isMarked = false;
    int topLevel = -1;
    Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1];
    Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL + 1];
    while (true) {
      int lFound = find(x, preds, succs);
      if (lFound != -1) victim = succs[lFound]; // found node
      if (isMarked ||
          (lFound != -1 && // rest of test if found node
           (victim.fullyLinked // found linked marked node?
           && victim.topLevel == lFound 
           && !victim.marked))) { 
        if (!isMarked) {
          topLevel = victim.topLevel;
                    victim.lock.lock();
          if (victim.marked) {
            victim.lock.unlock();
            return false;
          }
          victim.marked = true;
          isMarked = true;
        }
        int highestLocked = -1;
        try {
          Node<T> pred, succ;
          boolean valid = true;
          for (int level = 0; valid && (level <= topLevel); level++) {
            pred = preds[level];
            pred.lock.lock();
            highestLocked = level;
            valid = !pred.marked && pred.next[level]==victim; 
          } 
          if (!valid) continue;
          for (int level = topLevel; level >= 0; level--) {
            preds[level].next[level] = victim.next[level];
          }
          victim.lock.unlock();
          return true;
        } finally {
          for (int i = 0; i <= highestLocked; i++) {
            preds[i].unlock();
          }
        }
      } else return false;
    }
  }

  
  int find(T x, Node<T>[] preds, Node<T>[] succs) {
    int v = x.hashCode();
    int lFound = -1;
    Node<T> pred = head;
    for (int level = MAX_LEVEL; level >= 0; level--) {
      Node<T> curr = pred.next[level];
      while (v > curr.key) {
        pred = curr; curr = pred.next[level];
      }
      if (lFound == -1 && v == curr.key) {
        lFound = level;
      }
      preds[level] = pred;
      succs[level] = curr;
    }
    return lFound;
  }
  
  // not thread safe!
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      Node<T> cursor = head;
      public boolean hasNext() {
        return cursor.next[0] != tail;
      }
      public T next() {
        cursor = cursor.next[0];
        if (cursor == null)
          throw new NoSuchElementException();
        return cursor.item;
      }
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
  private static final class Node<T> {
    final Lock lock = new ReentrantLock();
    final T item;
    final int key;
    final Node<T>[] next;
    volatile boolean marked = false;
    volatile boolean fullyLinked = false;
    private int topLevel;
    // constructor for sentinel nodes
    public Node(int key) {
      this.item = null;
      this.key = key;
      next = new Node[MAX_LEVEL + 1];
      topLevel = MAX_LEVEL;
    }
    // constructor for regular nodes
    public Node(T x, int height) {
      item = x;
      key = x.hashCode();
      next = new Node[height + 1];
      topLevel = height;
    }
    public void lock() {
      lock.lock();
    }
    /**
     *  Unlock skip list.
     */
    public void unlock() {
      lock.unlock();
    }
  }
}
