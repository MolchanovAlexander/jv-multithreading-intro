package core.basesyntax;

public class Example {
  public static void main(String[] args) {
    new Thread(() -> System.out.println("Hello mates!")).run();
  }
}