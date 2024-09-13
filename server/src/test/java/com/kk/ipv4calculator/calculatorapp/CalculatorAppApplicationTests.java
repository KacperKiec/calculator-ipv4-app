package com.kk.ipv4calculator.calculatorapp;

import com.kk.ipv4calculator.calculatorapp.controller.Ipv4CalculatorController;
import com.kk.ipv4calculator.calculatorapp.exception.InvalidAddressException;
import com.kk.ipv4calculator.calculatorapp.exception.InvalidMaskException;
import com.kk.ipv4calculator.calculatorapp.exception.InvalidParameterException;
import com.kk.ipv4calculator.calculatorapp.exception.InvalidSubnetsAmountException;
import com.kk.ipv4calculator.calculatorapp.model.Subnet;
import com.kk.ipv4calculator.calculatorapp.service.Converter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CalculatorAppApplicationTests {

    @Mock
    private Converter converter;

    @InjectMocks
    private Ipv4CalculatorController controller;

    @Test
    @DisplayName("should return network address")
    public void testGetNetworkAddress() {
        String address = "172.16.0.4";
        String mask = "24";
        Subnet subnetMock = new Subnet("172.16.0.0", "24");

        when(converter.getNetAddress(address, mask)).thenReturn(subnetMock);

        ResponseEntity<Subnet> response = controller.getNetAddress(address, mask);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("172.16.0.0", response.getBody().address());
    }

    @Test
    @DisplayName("should throw InvalidMaskException")
    public void testGetNetworkAddressWithInvalidMask() {
        String address = "172.16.0.4";
        String mask = "33";

        when(converter.getNetAddress(address, mask)).thenThrow(InvalidMaskException.class);

        assertThrows(InvalidMaskException.class, () -> controller.getNetAddress(address, mask));
    }

    @Test
    @DisplayName("should return broadcast address")
    public void testGetBroadcastAddress() {
        String address = "172.16.0.4";
        String mask = "16";
        Subnet subnetMock = new Subnet("172.16.255.255", "24");

        when(converter.getBroadcastAddress(address, mask)).thenReturn(subnetMock);

        ResponseEntity<Subnet> response = controller.getBroadcastAddress(address, mask);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("172.16.255.255", response.getBody().address());
    }

    @Test
    @DisplayName("should throw InvalidMaskException")
    public void testGetBroadcastAddressWithInvalidMask() {
        String address = "172.16.0.2";
        String mask = "33";

        when(converter.getBroadcastAddress(address, mask)).thenThrow(InvalidMaskException.class);

        assertThrows(InvalidMaskException.class, () -> controller.getBroadcastAddress(address, mask));
    }

    @Test
    @DisplayName("should return proper subnets divided by number of subnets")
    public void testGetSubnetsNets() {
        String address = "172.16.0.0";
        String mask = "24";
        int numberOfSubnets = 8;

        List<Subnet> mockSubnets = Arrays.asList(
                new Subnet("172.16.0.0", "26"),
                new Subnet("172.16.0.32", "26"),
                new Subnet("172.16.0.64", "26"),
                new Subnet("172.16.0.96", "26"),
                new Subnet("172.16.0.128", "26"),
                new Subnet("172.16.0.160", "26"),
                new Subnet("172.16.0.192", "26"),
                new Subnet("172.16.0.224", "26")
        );

        when(converter.getSubnetsNets(address, mask, numberOfSubnets)).thenReturn(mockSubnets);

        ResponseEntity<List<Subnet>> response = controller.getSubnetsNets(address, mask, numberOfSubnets);

        assertNotNull(response);
        assertEquals(8, response.getBody().size());
        assertEquals("26", response.getBody().get(0).mask());
    }

    @Test
    @DisplayName("should throw InvalidAddressException")
    public void testGetSubnetsNetsWithInvalidNetworkAddress() {
        String address = "172.16.0.4";
        String mask = "24";
        int numberOfSubnets = 8;

        when(converter.getSubnetsNets(address, mask, numberOfSubnets)).thenThrow(InvalidAddressException.class);

        assertThrows(InvalidAddressException.class, () -> controller.getSubnetsNets(address, mask, numberOfSubnets));
    }

    @Test
    @DisplayName("should throw InvalidMaskException")
    public void testGetSubnetsNetsWithInvalidMask() {
        String address = "172.16.0.0";
        String mask = "33";
        int numberOfSubnets = 8;

        when(converter.getSubnetsNets(address, mask, numberOfSubnets)).thenThrow(InvalidMaskException.class);

        assertThrows(InvalidMaskException.class, () -> controller.getSubnetsNets(address, mask, numberOfSubnets));
    }

    @Test
    @DisplayName("should throw InvalidSubnetsAmountException")
    public void testGetSubnetsNetsWithInvalidSubnetsAmount() {
        String address = "172.16.0.0";
        String mask = "24";
        int numberOfSubnets = 200;

        when(converter.getSubnetsNets(address, mask, numberOfSubnets)).thenThrow(InvalidSubnetsAmountException.class);

        assertThrows(InvalidSubnetsAmountException.class, () -> controller.getSubnetsNets(address, mask, numberOfSubnets));
    }

    @Test
    @DisplayName("should throw InvalidParameterException")
    public void testGetSubnetsNetsWithInvalidParameters() {
        String address = "172.16.0.0";
        String mask = "24";
        int numberOfSubnets = 500;

        when(converter.getSubnetsNets(address, mask, numberOfSubnets)).thenThrow(InvalidParameterException.class);

        assertThrows(InvalidParameterException.class, () -> controller.getSubnetsNets(address, mask, numberOfSubnets));
    }

    @Test
    @DisplayName("should return proper subnets divided by number of hosts")
    public void testGetSubnetsHosts() {
        String address = "172.16.0.0";
        String mask = "24";
        int numberOfHosts = 30;

        List<Subnet> mockSubnets = Arrays.asList(
                new Subnet("172.16.0.0", "26"),
                new Subnet("172.16.0.32", "26"),
                new Subnet("172.16.0.64", "26"),
                new Subnet("172.16.0.96", "26"),
                new Subnet("172.16.0.128", "26"),
                new Subnet("172.16.0.160", "26"),
                new Subnet("172.16.0.192", "26"),
                new Subnet("172.16.0.224", "26")
        );

        when(converter.getSubnetsHosts(address, mask, numberOfHosts)).thenReturn(mockSubnets);

        ResponseEntity<List<Subnet>> response = controller.getSubnetsHosts(address, mask, numberOfHosts);

        assertNotNull(response);
        assertEquals(8, response.getBody().size());
        assertEquals("26", response.getBody().get(0).mask());
    }

    @Test
    @DisplayName("should throw InvalidAddressException")
    public void testGetSubnetsHostsWithInvalidNetworkAddress() {
        String address = "172.16.0.4";
        String mask = "24";
        int numberOfHosts = 40;

        when(converter.getSubnetsHosts(address, mask, numberOfHosts)).thenThrow(InvalidAddressException.class);

        assertThrows(InvalidAddressException.class, () -> controller.getSubnetsHosts(address, mask, numberOfHosts));
    }

    @Test
    @DisplayName("should throw InvalidMaskException")
    public void testGetSubnetsHostsWithInvalidMask() {
        String address = "172.16.0.0";
        String mask = "33";
        int numberOfHosts = 50;

        when(converter.getSubnetsHosts(address, mask, numberOfHosts)).thenThrow(InvalidMaskException.class);

        assertThrows(InvalidMaskException.class, () -> controller.getSubnetsHosts(address, mask, numberOfHosts));
    }

    @Test
    @DisplayName("should throw InvalidSubnetsAmountException")
    public void testGetSubnetsHostsWithInvalidSubnetsAmount() {
        String address = "172.16.0.0";
        String mask = "24";
        int numberOfHosts = 2000;

        when(converter.getSubnetsHosts(address, mask, numberOfHosts)).thenThrow(InvalidSubnetsAmountException.class);

        assertThrows(InvalidSubnetsAmountException.class, () -> controller.getSubnetsHosts(address, mask, numberOfHosts));
    }

    @Test
    @DisplayName("should throw InvalidParameterException")
    public void testGetSubnetsHostsWithInvalidParameters() {
        String address = "172.16.0.0";
        String mask = "8";
        int numberOfHosts = 66666;

        when(converter.getSubnetsHosts(address, mask, numberOfHosts)).thenThrow(InvalidParameterException.class);

        assertThrows(InvalidParameterException.class, () -> controller.getSubnetsHosts(address, mask, numberOfHosts));
    }

    @Test
    @DisplayName("should return proper optimal subnets")
    public void testGetOptimalSubnets() {
        String address = "172.16.0.0";
        String mask = "24";
        List<Integer> hosts = Arrays.asList(12,30,2);

        List<Subnet> mockSubnets = Arrays.asList(
                new Subnet("172.16.0.0", "27"),
                new Subnet("172.16.0.32", "28"),
                new Subnet("172.16.0.48", "30")
        );

        when(converter.getOptimalSubnets(address, mask, hosts)).thenReturn(mockSubnets);

        ResponseEntity<List<Subnet>> response = controller.getSubnetsOptimal(address, mask, hosts);

        assertNotNull(response);
        assertEquals(3, response.getBody().size());
        assertEquals("27", response.getBody().get(0).mask());
    }

    @Test
    @DisplayName("should throw InvalidAddressException")
    public void testGetOptimalSubnetsWithInvalidNetworkAddress() {
        String address = "172.16.0.1";
        String mask = "24";
        List<Integer> hosts = Arrays.asList(12,30,2);

        when(converter.getOptimalSubnets(address, mask, hosts)).thenThrow(InvalidAddressException.class);

        assertThrows(InvalidAddressException.class, () -> controller.getSubnetsOptimal(address, mask, hosts));
    }

    @Test
    @DisplayName("should throw InvalidMaskException")
    public void testGetOptimalSubnetsWithInvalidMask() {
        String address = "172.16.0.0";
        String mask = "33";
        List<Integer> hosts = Arrays.asList(12,30,2);

        when(converter.getOptimalSubnets(address, mask, hosts)).thenThrow(InvalidMaskException.class);

        assertThrows(InvalidMaskException.class, () -> controller.getSubnetsOptimal(address, mask, hosts));
    }

    @Test
    @DisplayName("should throw InvalidSubnetsAmountException")
    public void testGetOptimalSubnetsWithInvalidSubnetsAmount() {
        String address = "172.16.0.0";
        String mask = "24";
        List<Integer> hosts = Arrays.asList(12,30,2000);

        when(converter.getOptimalSubnets(address, mask, hosts)).thenThrow(InvalidSubnetsAmountException.class);

        assertThrows(InvalidSubnetsAmountException.class, () -> controller.getSubnetsOptimal(address, mask, hosts));
    }

    @Test
    @DisplayName("should throw InvalidParameterException")
    public void testGetOptimalSubnetsWithInvalidParameters() {
        String address = "172.16.0.0";
        String mask = "8";
        List<Integer> hosts = Arrays.asList(12,30,66666);

        when(converter.getOptimalSubnets(address, mask, hosts)).thenThrow(InvalidParameterException.class);

        assertThrows(InvalidParameterException.class, () -> controller.getSubnetsOptimal(address, mask, hosts));
    }
}
