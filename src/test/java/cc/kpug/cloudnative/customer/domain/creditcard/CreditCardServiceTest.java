package cc.kpug.cloudnative.customer.domain.creditcard;

import cc.kpug.cloudnative.customer.CustomerApplication;
import cc.kpug.cloudnative.customer.domain.account.Account;
import cc.kpug.cloudnative.customer.domain.address.Address;
import cc.kpug.cloudnative.customer.domain.address.AddressType;
import cc.kpug.cloudnative.customer.domain.customer.Customer;
import cc.kpug.cloudnative.customer.domain.customer.CustomerRepository;
import java.util.UUID;
import junit.framework.AssertionFailedError;
import org.hibernate.LazyInitializationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerApplication.class)
@ActiveProfiles(profiles = "local")
@Rollback
public class CreditCardServiceTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Transactional
    public void customerTest() {
        final String accountNumber = UUID.randomUUID().toString();
        final Account account = new Account(accountNumber);
        final Customer customer = new Customer("Steve", "Jobs", "sjobs@apple.com", account);
        final CreditCard creditCard = new CreditCard("1234567890", CreditCardType.VISA);
        customer.getAccount().getCreditCards().add(creditCard);

        final String street1 = "One Infinity Loop";
        final String city = "Cupertino";
        final String state = "DC";
        final int zipCode = 20500;
        final String country = "United States";
        final AddressType addressType = AddressType.SHIPPING;
        final Address address = new Address(street1, null, city, state, zipCode, country, addressType);
        customer.getAccount().getAddresses().add(address);

        customerRepository.save(customer);

        final Customer persistedResult = customerRepository.findById(customer.getId())
            .orElseThrow(() -> new AssertionFailedError("there's supposed to be a matching record!"));
        Assert.assertNotNull(persistedResult.getAccount());
        Assert.assertNotNull(persistedResult.getCreatedAt());
        Assert.assertNotNull(persistedResult.getLastModified());
        Assert.assertTrue(persistedResult.getAccount().getAddresses().stream()
                              .anyMatch(add -> add.getStreet1().equalsIgnoreCase(street1)));

        final Customer searchResult = customerRepository.findByEmailContaining(customer.getEmail())
            .orElseThrow(() -> new AssertionFailedError("there's supposed to be a matching record!"));
        Assert.assertEquals(accountNumber, searchResult.getAccount().getAccountNumber());
    }

    @Test(expected = LazyInitializationException.class)
    public void lazyInitializationTest() {
        final String accountNumber = UUID.randomUUID().toString();
        final Account account = new Account(accountNumber);
        final Customer customer = new Customer("Elon", "Musk", "elon.musk@teslamotors.com", account);
        final CreditCard creditCard = new CreditCard("1234567890", CreditCardType.VISA);
        customer.getAccount().getCreditCards().add(creditCard);

        final String street1 = "3500 Deer Creek Road";
        final String city = "Palo Alto";
        final String state = "CA";
        final int zipCode = 94304;
        final String country = "United States";
        final AddressType addressType = AddressType.SHIPPING;
        final Address address = new Address(street1, null, city, state, zipCode, country, addressType);
        customer.getAccount().getAddresses().add(address);

        customerRepository.save(customer);

        customerRepository.findById(customer.getId())
            .orElseThrow(() -> new AssertionFailedError("there's supposed to be a matching record!"))
            .getAccount()
            .getAddresses().stream().forEach(addr -> {});
    }
}