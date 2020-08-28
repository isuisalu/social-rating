package org.example.rating.calculator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.example.rating.api.Person;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@EqualsAndHashCode
@RedisHash("rating")
public class PersonSocialRating {
    @Id
    private Person person;
    private double rating;

    public PersonSocialRating(final Person person, final double rating) {
       this.person = person;
       this.rating = rating;
    }
}
