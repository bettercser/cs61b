//package deque;
//
//import org.junit.Test;
//
//import java.util.Comparator;
//
//import static org.junit.Assert.*;
//import static org.junit.Assert.assertEquals;
//
//
//public class MaxArrayDequeTest {
//
//    private class IntegerComparator<T> implements Comparator<T>{
//
//        @Override
//        public int compare(T o1, T o2) {
//            return (int)o1 - (int)o2;
//        }
//    }
//    private class Person{
//        String name;
//        int age;
//        public Person(String name, int age){
//
//            this.name = name;
//            this.age = age;
//
//        }
//
//        @Override
//        public boolean equals(Object o){
//            if(this == o) return true;
//            if(o instanceof Person){
//                Person otherPerson = (Person)o;
//                if(age != otherPerson.age) return false;
//                return name.equals(otherPerson.name);
//            }
//
//            return false;
//        }
//    }
//    private class AgeComparator implements Comparator<Person>{
//        @Override
//        public int compare(Person o1, Person o2) {
//            return o1.age - o2.age;
//        }
//    }
//    @Test
//    public void Integertest(){
//
//        MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<Integer>(new IntegerComparator<Integer>());
//
//        for (int i = 0; i < 1000000; i++) {
//            lld1.addLast(i);
//        }
//
//        assertEquals("the max should be fuck same", 999999, lld1.max(), 0.0);
//
//    }
//
//    @Test
//    public void Agetest(){
//        MaxArrayDeque<Person> lld1 = new MaxArrayDeque<Person>(new AgeComparator());
//
//        for(int i = 0; i < 1000000; i++){
//            lld1.addLast(new Person(""+i, i));
//        }
//
//        assertEquals("the max age person shuold be fuck same", new Person("" + 999999, 999999), lld1.max());
//    }
//
//}
