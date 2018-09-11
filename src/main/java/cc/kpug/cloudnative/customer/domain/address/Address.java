package cc.kpug.cloudnative.customer.domain.address;

import cc.kpug.cloudnative.customer.domain.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String street1;

    private String street2;

    private String city;

    private String state;

    private Integer zipCode;

    private String country;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    public Address(final String street1,
                   final String street2,
                   final String city,
                   final String state,
                   final Integer zipCode,
                   final String country,
                   final AddressType addressType) {
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.addressType = addressType;
    }

}
