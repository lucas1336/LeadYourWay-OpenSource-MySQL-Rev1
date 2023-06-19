package com.upc.leadyourway.service;

import com.upc.leadyourway.model.Rent;

import java.util.List;

public interface RentService {
    public abstract Rent create(Rent rent);
    public abstract Rent getById(Long rent_id);
    public abstract Rent update(Long rent_id, Rent rent);
    public abstract void delete(Long rent_id);

    public abstract List<Rent> getByLenderId(Long lender_id);
    public abstract List<Rent> getByRenterId(Long renter_id);
    public abstract List<Rent> getByBicycleId(Long bicycle_id);
    public abstract List<Rent> getByLenderIdAndRentStatus(Long lender_id, String rent_status);
    public abstract List<Rent> getByRenterIdAndRentStatus(Long renter_id, String rent_status);
}
