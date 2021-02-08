package org.japper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JapperTest {

    public static class AddressDTO {
        private String street;
        private String number;
        private String city;
        private String state;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    private enum State {
        SP, RJ, MG, ES
    }

    public static class Address {
        private String street;
        private Long number;
        private String city;
        private State state;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public Long getNumber() {
            return number;
        }

        public void setNumber(Long number) {
            this.number = number;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }
    }

    @Test
    public void createsProperMapper() throws Exception {

        var mapper = new Japper<AddressDTO, Address>(Address.class)
            .map(Address::setNumber, s -> Long.parseLong(s.getNumber()))
            .map(Address::setCity, AddressDTO::getCity)
            .map(Address::setState, s -> switch (s.getState()) {
                case "RJ" -> State.RJ;
                case "MG" -> State.MG;
                case "ES" -> State.ES;
                default -> State.SP;
            })
            .map(Address::setStreet, AddressDTO::getStreet);

        var result = mapper.parse(new AddressDTO() {{
            setCity("Rio de Janeiro");
            setNumber("123");
            setState("RJ");
            setStreet("Smpl Street");
        }});

        assertEquals("Rio de Janeiro", result.getCity());
        assertEquals(123L, result.getNumber());
        assertEquals(State.RJ, result.getState());
        assertEquals("Smpl Street", result.getStreet());
    }
}