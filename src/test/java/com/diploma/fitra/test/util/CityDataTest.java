package com.diploma.fitra.test.util;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;

public class CityDataTest {

    private static final Long CITY_1_ID = 1L;
    private static final String CITY_1_TITLE_EN = "Waigani";

    private static final Long CITY_2_ID = 2L;
    private static final String CITY_2_TITLE_EN = "Madang";

    private static final Long CITY_3_ID = 3L;
    private static final String CITY_3_TITLE_EN = "Port Moresby";

    private static final Long CITY_4_ID = 4L;
    private static final String CITY_4_TITLE_EN = "Namatanai";

    private static final Country CITY_COUNTRY = CountryDataTest.getCountry1();

    public static City getCity1() {
        City city = new City();
        city.setId(CITY_1_ID);
        city.setTitleEn(CITY_1_TITLE_EN);
        city.setCountry(CITY_COUNTRY);
        return city;
    }

    public static City getCity2() {
        City city = new City();
        city.setId(CITY_2_ID);
        city.setTitleEn(CITY_2_TITLE_EN);
        city.setCountry(CITY_COUNTRY);
        return city;
    }

    public static City getCity3() {
        City city = new City();
        city.setId(CITY_3_ID);
        city.setTitleEn(CITY_3_TITLE_EN);
        city.setCountry(CITY_COUNTRY);
        return city;
    }

    public static City getCity4() {
        City city = new City();
        city.setId(CITY_4_ID);
        city.setTitleEn(CITY_4_TITLE_EN);
        city.setCountry(CITY_COUNTRY);
        return city;
    }

    public static CityDto getCityDto1() {
        CityDto cityDto = new CityDto();
        cityDto.setId(CITY_1_ID);
        cityDto.setTitle(CITY_1_TITLE_EN);
        return cityDto;
    }

    public static CityDto getCityDto2() {
        CityDto cityDto = new CityDto();
        cityDto.setId(CITY_2_ID);
        cityDto.setTitle(CITY_2_TITLE_EN);
        return cityDto;
    }

    public static CityDto getCityDto3() {
        CityDto cityDto = new CityDto();
        cityDto.setId(CITY_3_ID);
        cityDto.setTitle(CITY_3_TITLE_EN);
        return cityDto;
    }

    public static CityDto getCityDto4() {
        CityDto cityDto = new CityDto();
        cityDto.setId(CITY_4_ID);
        cityDto.setTitle(CITY_4_TITLE_EN);
        return cityDto;
    }
}
