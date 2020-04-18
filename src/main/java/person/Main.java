package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class Main {

    static Faker faker = new Faker();
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");


    private static Person randomPerson(){

        Address address = new Address();
        address.setCountry(faker.address().country());
        address.setState(faker.address().state());
        address.setCity(faker.address().city());
        address.setStreetAddress(faker.address().streetAddress());
        address.setZip(faker.address().zipCode());

        Person person = new Person();
        person.setName(faker.name().name());

        Date date = faker.date().birthday();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        person.setDob(localDate);

        person.setGender(faker.options().option(Person.Gender.class));

        person.setAddress(address);

        person.setEmail(faker.internet().emailAddress());

        person.setProfession(faker.company().profession());

        return person;
    }

    public static void addPeople(int number){
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for (int i = 0; i < number; i++) {
                em.persist(randomPerson());
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static List<Person> listPeople(){
        EntityManager em = emf.createEntityManager();
        return em.createQuery("SELECT p FROM Person p",Person.class).getResultList();
    }


    public static void main(String[] args) {
        addPeople(10);
        listPeople().forEach(System.out::println);
        emf.close();
    }



}
