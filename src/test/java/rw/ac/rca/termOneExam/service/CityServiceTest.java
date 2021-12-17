package rw.ac.rca.termOneExam.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.repository.ICityRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {

    @Mock
    private ICityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    public void getById_success(){
        City city = new City(101,"Kigali",24, 75.2);
        when(cityRepository.findById(101L)).thenReturn(Optional.of(city));

        assertEquals(city.getId(), cityService.getById(101L).get().getId());
        assertEquals(city.getName(), cityService.getById(101L).get().getName());
    }

    @Test
    public void getById_notFound(){
        when(cityRepository.findById(200L)).thenReturn(Optional.empty());
        assertFalse(cityService.getById(200L).isPresent());
    }

    @Test
    public void getAll_withSomeElements(){
        when(cityRepository.findAll()).thenReturn(Arrays.asList(
                new City(101,"Kigali",24, 75.2),
                new City(102,"Musanze",18, 64.4)
        ));
        assertEquals(102, cityService.getAll().get(1).getId());
    }

    @Test
    public void getAll_empty(){
        when(cityRepository.findAll()).thenReturn(new ArrayList<City>());
        assertEquals(0, cityService.getAll().size());
    }

    @Test
    public void existsByName_success(){
        when(cityRepository.existsByName("Musanze")).thenReturn(true);
        when(cityRepository.existsByName("Burera")).thenReturn(true);

        assertTrue(cityService.existsByName("Musanze"));
        assertTrue(cityService.existsByName("Burera"));
    }

    @Test
    public void existsByName_failure(){
        when(cityRepository.existsByName("Nyabihu")).thenReturn(false);
        when(cityRepository.existsByName("Kamonyi")).thenReturn(false);

        assertFalse(cityService.existsByName("Nyabihu"));
        assertFalse(cityService.existsByName("Kamonyi"));
    }

    @Test
    public void save(){
        City city = new City(101,"Kigali",24, 75.2);
        CreateCityDTO dto = new CreateCityDTO("Kigali", 24);
        when(cityRepository.save(any(City.class))).thenReturn(city);
        assertEquals(city.getName(), cityService.save(dto).getName());
    }
}
