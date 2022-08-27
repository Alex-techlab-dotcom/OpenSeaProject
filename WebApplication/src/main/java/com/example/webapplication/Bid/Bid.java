package com.example.webapplication.Bid;

import com.example.webapplication.Auction.Auction;
import com.example.webapplication.Bidder.Bidder;
import com.example.webapplication.Seller.Seller;
import com.example.webapplication.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Bid implements Serializable {
   /* @Id
    @OneToOne
    @JoinColumn(name = "BidderUserId", referencedColumnName = "user_id")
    private Bidder bidder;*/
    @Id
    @SequenceGenerator(
           name= "user_sequence", sequenceName = "user_sequence",allocationSize = 1
    )
    @GeneratedValue( strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    @Column(name="bid_id")
    private Long bid_id;

  /*  @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "user_id")*/
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bidder_id", referencedColumnName = "bidder_id")
    private Bidder bidder;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime localBidDateTime;
    private String BidderAddress;
    private String BidderCountry;
    private Integer moneyAmount;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "item_id" /*nullable = false*/)
    private Auction auction;

    public Bid() {
    }

    public Bid(LocalDateTime localBidDateTime, String bidderAddress, String bidderCountry, Integer moneyAmount,Long itemId) {
        //this.bidder = bidder;
        this.localBidDateTime = localBidDateTime;
        BidderAddress = bidderAddress;
        BidderCountry = bidderCountry;
        this.moneyAmount = moneyAmount;
    }


}
