
package com.example.webapplication.Seller;

import com.example.webapplication.Bid.Bid;
import com.example.webapplication.User.User;
import com.example.webapplication.Auction.Auction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name="Sellers")
public class Seller implements Serializable {
    private int rating;
    /*@Id
    @OneToOne
    @JoinColumn(name = "SellerId", referencedColumnName = "userId")
    private User user;*/

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "seller" ,cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    // we create to auction-table a new column named:Seller_ID which refers to Seller.sellerId column
    //@JoinColumn(name = "Seller_ID", referencedColumnName = "SellerId")
    @JsonManagedReference
    private List<Auction> sellersAuctions = new ArrayList<>();

    public Seller() {
    }

    public Seller(int rating, Long id) {
        this.id=id;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "rating=" + rating +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seller)) return false;
        Seller seller = (Seller) o;
        return getRating() == seller.getRating() && user.equals(seller.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRating(), user);
    }

}

