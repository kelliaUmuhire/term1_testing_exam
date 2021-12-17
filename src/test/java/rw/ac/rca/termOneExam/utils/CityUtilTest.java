package rw.ac.rca.termOneExam.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.repository.ICityRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CityUtilTest {


    //test for temperature and cities using data from memory database

    @Autowired
    private ICityRepository cityRepository;

    @Test
    public void findAll_checkNoMoreThan40(){
        List<City> cities = cityRepository.findAll();
        for(City city:cities){
            assertTrue(Double.compare(40d, city.getWeather()) > 0);
        }
    }

    @Test
    public void findAll_checkNoLessThan10(){
        List<City> cities = cityRepository.findAll();
        for(City city:cities){
            assertTrue(Double.compare(city.getWeather(), 10d) > 0);
        }
    }

    @Test
    public void findAll_containsMusanzeAndKigali(){
        List<City> cities = cityRepository.findAll();
        assertTrue(cities.stream().anyMatch(c -> c.getName().equals("Musanze")));
        assertTrue(cities.stream().anyMatch(c -> c.getName().equals("Kigali")));
    }

    @Test
    public void testSpying(){
        ArrayList<City> citiesListSpy =  spy(ArrayList.class);
        City newCity = new City("Burera", 34);
        citiesListSpy.add(new City("Kamonyi", 20));
        citiesListSpy.add(newCity);

        assertEquals("Kamonyi", citiesListSpy.get(0).getName());
        assertTrue(citiesListSpy.size()==2);
        verify(citiesListSpy).add(newCity);

        when(citiesListSpy.size()).thenReturn(6);
        citiesListSpy.add(new City("Nyabihu", 20));
        assertTrue(citiesListSpy.size()==6);
    }

    @Test
    public void testMocking(){
        ArrayList<City> citiesListMock =  mock(ArrayList.class);
        City newCity = new City("Musanze", 34);

        citiesListMock.add(new City("Nyabihu", 20));
        citiesListMock.add(new City("Burera", 34));
        assertEquals(0, citiesListMock.size());

        when(citiesListMock.size()).thenReturn(3);
        when(citiesListMock.get(0)).thenReturn(newCity);

        assertEquals("Musanze", citiesListMock.get(0).getName());
        assertEquals(null, citiesListMock.get(1));
    }

}
