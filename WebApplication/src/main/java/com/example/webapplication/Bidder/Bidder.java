package com.example.webapplication.Bidder;

import com.example.webapplication.User.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Bidders")
public class Bidder implements Serializable {
    private Integer rating;
    @Id
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "UserId", referencedColumnName = "userId"),
            @JoinColumn(name = "UserAddress", referencedColumnName = "address"),
            @JoinColumn(name = "UserCountry", referencedColumnName = "Country")
    })
    private User user;

    public Bidder() {
    }

    public Bidder(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Bidder{" +
                "rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bidder)) return false;
        Bidder bidder = (Bidder) o;
        return user.equals(bidder.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}