package com.spring.aoce;

import com.spring.aoce.repository.ComputerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AoceApplicationTests {
    @Autowired
    private ComputerRepository computerRepository;

    @Test
    void createEquipmentWithCharacteristics() {
//        Equipment equipment = new Equipment(null, "serialNumber1");
//        equipmentRepository.create(equipment);
//        assertFalse(equipmentRepository.findAll().isEmpty());
//        Computer computer = new Computer("serialNumber2", "cpu");
//        computerRepository.create(computer);
//        assertEquals(1, computerRepository.findAll().size());
//        assertEquals(2, equipmentRepository.findAll().size());
    }
}
