package com.diploma.fitra.test.util;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.role.Role;

import java.time.LocalDate;

public class UserDataTest {

    private static final Long USER_1_ID = 1L;
    private static final String USER_1_FIRST_NAME = "Anton";
    private static final String USER_1_LAST_NAME = "Antonov";
    private static final String USER_1_EMAIL = "anton@gmail.com";
    private static final LocalDate USER_1_BIRTHDAY = LocalDate.of(1991, 12, 31);
    private static final City USER_1_CITY = CityDataTest.getCity1();
    private static final CityDto USER_1_CITY_DTO = CityDataTest.getCityDto1();

    private static final Long USER_2_ID = 2L;
    private static final String USER_2_FIRST_NAME = "Sergei";
    private static final String USER_2_LAST_NAME = "Sergeev";
    private static final String USER_2_EMAIL = "sergei@gmail.com";
    private static final LocalDate USER_2_BIRTHDAY = LocalDate.of(1991, 1, 1);
    private static final City USER_2_CITY = CityDataTest.getCity2();
    private static final CityDto USER_2_CITY_DTO = CityDataTest.getCityDto2();

    private static final Long USER_3_ID = 3L;
    private static final String USER_3_FIRST_NAME = "Stanislav";
    private static final String USER_3_LAST_NAME = "Stasov";
    private static final String USER_3_EMAIL = "stas@gmail.com";
    private static final LocalDate USER_3_BIRTHDAY = LocalDate.of(2001, 7, 15);
    private static final City USER_3_CITY = CityDataTest.getCity3();
    private static final CityDto USER_3_CITY_DTO = CityDataTest.getCityDto3();

    private static final Long USER_4_ID = 4L;
    private static final String USER_4_FIRST_NAME = "Aleksei";
    private static final String USER_4_LAST_NAME = "Alekseev";
    private static final String USER_4_EMAIL = "aleks@gmail.com";
    private static final LocalDate USER_4_BIRTHDAY = LocalDate.of(1997, 3, 18);
    private static final City USER_4_CITY = CityDataTest.getCity4();
    private static final CityDto USER_4_CITY_DTO = CityDataTest.getCityDto4();

    private static final String USER_PASSWORD = "qwerty123";
    private static final String USER_ABOUT = "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus, aspernatur atque consectetur distinctio dolorum enim error et fugit id incidunt ipsam neque nisi optio perferendis porro possimus provident quaerat qui ratione repudiandae similique tempore totam ut. Animi delectus distinctio harum ipsum laborum libero, minima mollitia nam, pariatur sint vero vitae.";
    private static final Country USER_COUNTRY = CountryDataTest.getCountry1();
    private static final CountryDto USER_COUNTRY_DTO = CountryDataTest.getCountryDto1();
    private static final boolean USER_IS_ADMIN = true;
    private static final boolean USER_IS_NOT_ADMIN = false;

    public static User getUser1() {
        User user = new User();
        user.setId(USER_1_ID);
        user.setFirstName(USER_1_FIRST_NAME);
        user.setLastName(USER_1_LAST_NAME);
        user.setEmail(USER_1_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setAbout(USER_ABOUT);
        user.setBirthday(USER_1_BIRTHDAY);
        user.setCountry(USER_COUNTRY);
        user.setCity(USER_1_CITY);
        user.setRole(Role.ADMIN);
        return user;
    }

    public static User getUser2() {
        User user = new User();
        user.setId(USER_2_ID);
        user.setFirstName(USER_2_FIRST_NAME);
        user.setLastName(USER_2_LAST_NAME);
        user.setEmail(USER_2_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setAbout(USER_ABOUT);
        user.setBirthday(USER_2_BIRTHDAY);
        user.setCountry(USER_COUNTRY);
        user.setCity(USER_2_CITY);
        user.setRole(Role.USER);
        return user;
    }

    public static User getUser3() {
        User user = new User();
        user.setId(USER_3_ID);
        user.setFirstName(USER_3_FIRST_NAME);
        user.setLastName(USER_3_LAST_NAME);
        user.setEmail(USER_3_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setAbout(USER_ABOUT);
        user.setBirthday(USER_3_BIRTHDAY);
        user.setCountry(USER_COUNTRY);
        user.setCity(USER_3_CITY);
        user.setRole(Role.USER);
        return user;
    }

    public static User getUser4() {
        User user = new User();
        user.setId(USER_4_ID);
        user.setFirstName(USER_4_FIRST_NAME);
        user.setLastName(USER_4_LAST_NAME);
        user.setEmail(USER_4_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setAbout(USER_ABOUT);
        user.setBirthday(USER_4_BIRTHDAY);
        user.setCountry(USER_COUNTRY);
        user.setCity(USER_4_CITY);
        user.setRole(Role.USER);
        return user;
    }

    public static UserDto getUserDto1() {
        UserDto userDto = new UserDto();
        userDto.setId(USER_1_ID);
        userDto.setFirstName(USER_1_FIRST_NAME);
        userDto.setLastName(USER_1_LAST_NAME);
        userDto.setAbout(USER_ABOUT);
        userDto.setCountry(USER_COUNTRY_DTO);
        userDto.setCity(USER_1_CITY_DTO);
        userDto.setIsAdmin(USER_IS_ADMIN);
        return userDto;
    }

    public static UserDto getUserDto2() {
        UserDto userDto = new UserDto();
        userDto.setId(USER_2_ID);
        userDto.setFirstName(USER_2_FIRST_NAME);
        userDto.setLastName(USER_2_LAST_NAME);
        userDto.setAbout(USER_ABOUT);
        userDto.setCountry(USER_COUNTRY_DTO);
        userDto.setCity(USER_2_CITY_DTO);
        return userDto;
    }

    public static UserDto getUserDto3() {
        UserDto userDto = new UserDto();
        userDto.setId(USER_3_ID);
        userDto.setFirstName(USER_3_FIRST_NAME);
        userDto.setLastName(USER_3_LAST_NAME);
        userDto.setAbout(USER_ABOUT);
        userDto.setCountry(USER_COUNTRY_DTO);
        userDto.setCity(USER_3_CITY_DTO);
        return userDto;
    }

    public static UserDto getUserDto4() {
        UserDto userDto = new UserDto();
        userDto.setId(USER_4_ID);
        userDto.setFirstName(USER_4_FIRST_NAME);
        userDto.setLastName(USER_4_LAST_NAME);
        userDto.setAbout(USER_ABOUT);
        userDto.setCountry(USER_COUNTRY_DTO);
        userDto.setCity(USER_4_CITY_DTO);
        return userDto;
    }
}
