package site.metacoding.white.util;

class 엄마 extends 인간 {
    public void say() {
        System.out.println("나는 엄마");
    }

    public void has() {
        System.out.println("아들");
    }
}

class 인간 {
    public void say() {
        System.out.println("나는 인간");
    }
}

public class classTest {
    public static void main(String[] args) {
        엄마 person1 = new 엄마();
        인간 person2 = new 인간();

        인간 person3 = person1;
        person3.say();
        // person3.has();
    }
}
