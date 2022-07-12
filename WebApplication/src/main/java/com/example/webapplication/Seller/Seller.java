
package com.example.webapplication.Seller;

import com.example.webapplication.User.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Sellers")
public class Seller implements Serializable {
    private int rating;

    @Id
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "SellerId", referencedColumnName = "userId"),
            @JoinColumn(name = "SellerAdrress", referencedColumnName = "address"),
            @JoinColumn(name = "SellerCountry", referencedColumnName = "country"),
    })

    private User user;

    public Seller() {
    }

    public Seller(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "rating=" + rating +
                '}';
    }
}

