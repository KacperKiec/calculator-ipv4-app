package com.kk.ipv4calculator.calculatorapp.service;


import com.kk.ipv4calculator.calculatorapp.model.Subnet;

import java.util.List;

/**
 * Interface containing operations on Ipv4 addresses
 * */
interface ConvertInterface {

    /**
     * Designates the network address of the given host
     *
     * @param address Ipv4 host address
     * @param mask    Ipv4 mask in slash notation
     * @return Ipv4 network address
     */
    Subnet getNetAddress(String address, String mask);

    /**
     * Designates the broadcast address of the given host
     * @param address Ipv4 host address
     * @param mask Ipv4 mask in slash notation
     * @return Ipv4 broadcast address
     * */
    Subnet getBroadcastAddress(String address, String mask);

    /**
     * Divides the network into subnets depending on the number of subnets. Maximum number of subnets is 128.
     * @param address Ipv4 host address
     * @param mask Ipv4 mask in slash notation
     * @param numberOfSubnets number of subnets to obtain
     * @return List of subnets
     * @throws IllegalArgumentException invalid Ipv4 address
     * @throws IllegalStateException too many subnets
     * */
    List<Subnet> getSubnetsNets(String address, String mask, int numberOfSubnets)  throws IllegalArgumentException, IllegalStateException;

    /**
     * Divides the network into subnets depending on the number of hosts.
     * @param address Ipv4 host address
     * @param mask Ipv4 mask in slash notation
     * @param numberOfHosts number of hosts per subnet
     * @return List of subnets
     * @throws IllegalArgumentException invalid Ipv4 address
     * @throws IllegalStateException too many hosts
     * */
    List<Subnet> getSubnetsHosts(String address, String mask, int numberOfHosts)  throws IllegalArgumentException, IllegalStateException;

    /**
     * Divides network into subnets in optimal way
     * @param address Ipv4 host address
     * @param mask Ipv4 mask in slash notation
     * @param numberOfHosts list of hosts required in subnets
     * @return List of subnets
     * @throws IllegalArgumentException invalid Ipv4 address
     * @throws IllegalStateException too many hosts
     * */
    List<Subnet> getOptimalSubnets(String address, String mask, List<Integer> numberOfHosts)  throws IllegalArgumentException, IllegalStateException;
}
