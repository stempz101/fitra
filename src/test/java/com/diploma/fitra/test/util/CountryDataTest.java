package com.diploma.fitra.test.util;

import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.model.Country;

public class CountryDataTest {

    private static final Long COUNTRY_1_ID = 1L;
    private static final String COUNTRY_1_TITLE_EN = "Papua New Guinea";
    private static final String COUNTRY_1_TITLE_UA = "Папуа-Нова Гвінея";

    private static final Long COUNTRY_2_ID = 2L;
    private static final String COUNTRY_2_TITLE_EN = "Cambodia";
    private static final String COUNTRY_2_TITLE_UA = "Камбоджа";

    private static final Long COUNTRY_3_ID = 3L;
    private static final String COUNTRY_3_TITLE_EN = "Kazakhstan";
    private static final String COUNTRY_3_TITLE_UA = "Казахстан";

    private static final Long COUNTRY_4_ID = 4L;
    private static final String COUNTRY_4_TITLE_EN = "Paraguay";
    private static final String COUNTRY_4_TITLE_UA = "Парагвай";

    public static Country getCountry1() {
        Country country = new Country();
        country.setId(COUNTRY_1_ID);
        country.setTitleEn(COUNTRY_1_TITLE_EN);
        country.setTitleUa(COUNTRY_1_TITLE_UA);
        return country;
    }

    public static Country getCountry2() {
        Country country = new Country();
        country.setId(COUNTRY_2_ID);
        country.setTitleEn(COUNTRY_2_TITLE_EN);
        country.setTitleUa(COUNTRY_2_TITLE_UA);
        return country;
    }

    public static Country getCountry3() {
        Country country = new Country();
        country.setId(COUNTRY_3_ID);
        country.setTitleEn(COUNTRY_3_TITLE_EN);
        country.setTitleUa(COUNTRY_3_TITLE_UA);
        return country;
    }

    public static Country getCountry4() {
        Country country = new Country();
        country.setId(COUNTRY_4_ID);
        country.setTitleEn(COUNTRY_4_TITLE_EN);
        country.setTitleUa(COUNTRY_4_TITLE_UA);
        return country;
    }

    public static CountryDto getCountryDto1() {
        CountryDto countryDto = new CountryDto();
        countryDto.setId(COUNTRY_1_ID);
        countryDto.setTitle(COUNTRY_1_TITLE_EN);
        return countryDto;
    }

    public static CountryDto getCountryDto2() {
        CountryDto countryDto = new CountryDto();
        countryDto.setId(COUNTRY_2_ID);
        countryDto.setTitle(COUNTRY_2_TITLE_EN);
        return countryDto;
    }

    public static CountryDto getCountryDto3() {
        CountryDto countryDto = new CountryDto();
        countryDto.setId(COUNTRY_3_ID);
        countryDto.setTitle(COUNTRY_3_TITLE_EN);
        return countryDto;
    }

    public static CountryDto getCountryDto4() {
        CountryDto countryDto = new CountryDto();
        countryDto.setId(COUNTRY_4_ID);
        countryDto.setTitle(COUNTRY_4_TITLE_EN);
        return countryDto;
    }
}
