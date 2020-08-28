package org.example.rating.calculator;

import org.example.rating.api.Person;
import org.springframework.data.repository.CrudRepository;

public interface RatingsRepo extends CrudRepository<PersonSocialRating, Person> {
}
