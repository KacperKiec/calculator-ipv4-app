package com.kk.ipv4calculator.calculatorapp.service;

import com.kk.ipv4calculator.calculatorapp.exception.InvalidAddressException;
import com.kk.ipv4calculator.calculatorapp.exception.InvalidMaskException;
import com.kk.ipv4calculator.calculatorapp.exception.InvalidParameterException;
import com.kk.ipv4calculator.calculatorapp.exception.InvalidSubnetsAmountException;
import com.kk.ipv4calculator.calculatorapp.model.Subnet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class implementing methods for operations on Ipv4 addresses
 * */
@Service
public class Converter implements ConvertInterface{

    final int[] netValues;
    final int[] hostsValues;

    public Converter() {
        netValues = new int[]{128, 64, 32, 16, 8, 4, 2, 1};

        hostsValues = new int[16];
        int index = 0;
        for(int i = 15; i >= 0; i--){
            hostsValues[index] = (int) Math.pow(2, i + 1) - 2;
            index++;
        }
    }

    @Override
    public Subnet getNetAddress(String address, String mask) {

        if (Integer.parseInt(mask) > 32) throw new InvalidMaskException("Given mask is invalid");

        byte[] hostAddress = convertAddressToBinary(address);
        byte[] maskAddress = convertMaskToBinary(mask);
        byte[] netAddress = new byte[32];

        for(int i = 0; i < hostAddress.length; i++){
            netAddress[i] = (byte) (hostAddress[i] & maskAddress[i]);
        }

        var finalNetAddress =  convertBinaryToAddress(netAddress);
        return new Subnet(finalNetAddress, mask);
    }

    @Override
    public Subnet getBroadcastAddress(String address, String mask) {

        if (Integer.parseInt(mask) > 32) throw new InvalidMaskException("Given mask is invalid");

        byte[] netAddress = convertAddressToBinary(getNetAddress(address, mask).address());
        byte[] maskAddress = convertMaskToBinary(mask);
        byte[] broadcastAddress = new byte[32];

        for(int i = 0; i < 32; i++){
            if(maskAddress[i] == 1) broadcastAddress[i] = (byte) (netAddress[i] & maskAddress[i]);
            else {
                broadcastAddress[i] = 1;
            }
        }

        var finalBroadcastAddress =  convertBinaryToAddress(broadcastAddress);
        return new Subnet(finalBroadcastAddress, mask);
    }

    @Override
    public List<Subnet> getSubnetsNets(String address, String mask, int numberOfSubnets) {

        if (numberOfSubnets > netValues[0] || numberOfSubnets < netValues[netValues.length - 1]) throw new InvalidParameterException("Given parameter is out of range.");

        if (!address.equals(getNetAddress(address, mask).address())) throw new InvalidAddressException("Given address is not the network address");

        int bitsOnSubnets = 0;
        int index;
        for(index = 0; index < netValues.length; index++){
            if(netValues[index] == numberOfSubnets){
                bitsOnSubnets = netValues.length - 1 - index;
                break;
            } else if (netValues[index] < numberOfSubnets) {
                bitsOnSubnets = netValues.length - index;
                index--;
                break;
            }
        }

        byte[] binaryAddress = convertAddressToBinary(address);
        List<Subnet> subnets = new ArrayList<>();
        int intMask = Integer.parseInt(mask);
        int newMaskLength = intMask + bitsOnSubnets;

        if(newMaskLength > 30) throw new InvalidSubnetsAmountException("It is not possible to designate a subnets");

        for(int i = 0; i < netValues[index]; i++){
            if(i != 0) {
                getSubnetAddress(intMask, newMaskLength, binaryAddress);
            }

            subnets.add(new Subnet(convertBinaryToAddress(binaryAddress), String.valueOf(newMaskLength)));
        }

        return subnets;
    }

    @Override
    public List<Subnet> getSubnetsHosts(String address, String mask, int numberOfHosts) {

        if (numberOfHosts > hostsValues[0] || numberOfHosts < hostsValues[hostsValues.length - 1]) throw new InvalidParameterException("Given parameter is out of range.");

        if (!address.equals(getNetAddress(address, mask).address())) throw new InvalidAddressException("Given address is not the network address");

        int bitsOnHosts = 0;
        for(int i = 0; i < hostsValues.length; i++){
            if(hostsValues[i] == numberOfHosts){
                bitsOnHosts = hostsValues.length - i;
                break;
            } else if (hostsValues[i] < numberOfHosts) {
                bitsOnHosts = hostsValues.length - i + 1;
                break;
            }
        }

        byte[] binaryAddress = convertAddressToBinary(address);
        List<Subnet> subnets = new ArrayList<>();
        int newMaskLength = 32 - bitsOnHosts;
        int intMask = Integer.parseInt(mask);
        int numberOfSubnets = newMaskLength - intMask;

        if(newMaskLength > 30 || newMaskLength < intMask) throw new InvalidSubnetsAmountException("It is not possible to designate a subnets");

        for(int i = 0; i < Math.pow(2, numberOfSubnets); i++){
            if(i != 0) {
                getSubnetAddress(intMask, newMaskLength, binaryAddress);
            }
            subnets.add(new Subnet(convertBinaryToAddress(binaryAddress), String.valueOf(newMaskLength)));
        }

        return subnets;
    }

    @Override
    public List<Subnet> getOptimalSubnets(String address, String mask, List<Integer> numberOfHosts) {

        if (!address.equals(getNetAddress(address, mask).address())) throw new InvalidAddressException("Given address is not the network address");

        numberOfHosts.sort(Comparator.reverseOrder());

        if (numberOfHosts.get(0) > hostsValues[0] || numberOfHosts.getLast() < hostsValues[hostsValues.length - 1]) throw new InvalidParameterException("Given parameter is out of range.");

        byte[] binaryAddress = convertAddressToBinary(address);
        int intMask = Integer.parseInt(mask);
        List<Subnet> subnets = new ArrayList<>();
        int bitsOnHosts = 0;
        int newMask;

        for(int hosts : numberOfHosts){

            for(int i = 0; i < hostsValues.length; i++){
                if(hostsValues[i] == hosts){
                    bitsOnHosts = hostsValues.length - i;
                    break;
                } else if (hostsValues[i] < hosts) {
                    bitsOnHosts = hostsValues.length - i + 1;
                    break;
                }
            }
            newMask = 32 - bitsOnHosts;

            if(newMask > 30 || newMask < intMask) throw new InvalidSubnetsAmountException("It is not possible to designate a subnets");

            subnets.add(new Subnet(convertBinaryToAddress(binaryAddress), String.valueOf(newMask)));
            getSubnetAddress(intMask, newMask, binaryAddress);
        }

        return subnets;
    }

    /**
     * Designates the address of the new subnet
     * @param mask supernets Ipv4 mask
     * @param binaryAddress supernets Ipv4 address
     * @param newMaskLength Ipv4 mask of the subnet
     * */
    private void getSubnetAddress(int mask, int newMaskLength, byte[] binaryAddress) {
        for (int j = newMaskLength - 1; j >= mask; j--) {
            if (binaryAddress[j] == 0) {
                binaryAddress[j] = 1;
                break;
            } else {
                binaryAddress[j] = 0;
            }
        }
    }

    /**
     * Converts Ipv4 address from decimal notation to binary notation
     * @param address Ipv4 address to convert
     * @return byte array containing Ipv4 address binary notated
     * */
    private byte[] convertAddressToBinary(String address){
        String[] octets = address.split("\\.");
        byte[] binaryAddress = new byte[32];

        int octetInt;
        int index = 0;

        for(String octet : octets){
            octetInt= Integer.parseInt(octet);

            for (int netValue : netValues) {
                if (octetInt - netValue >= 0) {
                    octetInt -= netValue;
                    binaryAddress[index] = 1;
                } else {
                    binaryAddress[index] = 0;
                }
                index++;
            }
        }

        return binaryAddress;
    }

    /**
     * Converts IPv4 address from binary notation to decimal notation
     * @param binaryAddress Ipv4 address to convert
     * @return String containing Ipv4 address decimal notated
     * */
    private String convertBinaryToAddress(byte[] binaryAddress){
        int[] octets = new int[4];
        int index = 0;

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 8; j++){
                if(binaryAddress[index] == 1) octets[i] += netValues[j];
                index++;
            }
        }

        return octets[0] + "." + octets[1] + "." + octets[2] + "." + octets[3];
    }

    /**
     * Converts IPv4 mask from slash notation to binary notation
     * @param mask Ipv4 mask to convert
     * @return byte array containing Ipv4 mask binary notated
     * */
    private byte[] convertMaskToBinary(String mask){
        byte[] binaryMask = new byte[32];
        int temp = Integer.parseInt(mask);

        for(int i = 0; i < 32; i++){
            if(i < temp){
                binaryMask[i] = 1;
            }
            else binaryMask[i] = 0;
        }
        return binaryMask;
    }
}
