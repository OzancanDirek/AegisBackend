package org.example.Repository;

import org.example.Model.Adresses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Adresses, Integer>
{
    boolean existsByCityAndDistrictAndStreet(String city, String district, String street);
}

