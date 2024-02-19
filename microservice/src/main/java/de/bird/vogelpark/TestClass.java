package de.bird.vogelpark;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class TestClass {
    @GetMapping("/data/")
    public Person getData() {
        return new Person("Bob", "Mueller");
    }


    public record  Person(
            String vorname,
            String nachname) { }
}
