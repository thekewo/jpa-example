package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;

public class Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");

    private static Faker faker = new Faker();

    private static Person randomPerson(){

        Address address = new Address();
        address.setCity(faker.address().city());
        address.setCountry(faker.address().country());
        address.setState(faker.address().state());
        address.setZip(faker.address().zipCode());
        address.setStreetAddress(faker.address().streetAddress());

        Person randomperson = new Person();
        randomperson.setName(faker.name().name());
        randomperson.setDob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        randomperson.setAddress(address);
        randomperson.setEmail(faker.internet().emailAddress());
        randomperson.setGender(faker.options().option(Person.Gender.class));
        randomperson.setProfession(faker.company().profession());

        return randomperson;
    }

    public static void main(String[] args) {

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        for (int i = 0; i < 1000; i++) {
            em.persist(randomPerson());
        }
        em.getTransaction().commit();
        em.createQuery("SELECT p FROM Person p", Person.class).getResultList().forEach(System.out::println);

        em.close();
        emf.close();
    }
}