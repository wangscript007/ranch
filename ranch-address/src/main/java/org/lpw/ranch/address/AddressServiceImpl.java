package org.lpw.ranch.address;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(AddressModel.NAME + ".service")
public class AddressServiceImpl implements AddressService {
    @Inject
    private AddressDao addressDao;
}
