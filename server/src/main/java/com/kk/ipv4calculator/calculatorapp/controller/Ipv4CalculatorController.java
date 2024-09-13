package com.kk.ipv4calculator.calculatorapp.controller;

import com.kk.ipv4calculator.calculatorapp.model.Subnet;
import com.kk.ipv4calculator.calculatorapp.service.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class Ipv4CalculatorController {
    private final Converter converter;
    private static final Logger logger = LoggerFactory.getLogger(Ipv4CalculatorController.class);

    public Ipv4CalculatorController(Converter converter) {
        this.converter = converter;
    }

    @GetMapping(value = "/net-address")
    public ResponseEntity<Subnet> getNetAddress(@RequestParam String address, @RequestParam String mask){
        var result =  converter.getNetAddress(address, mask);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/broadcast-address")
    public ResponseEntity<Subnet> getBroadcastAddress(@RequestParam String address, @RequestParam String mask){
        var result =  converter.getBroadcastAddress(address, mask);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/subnets-net")
    public ResponseEntity<List<Subnet>> getSubnetsNets(@RequestParam String address, @RequestParam String mask, @RequestParam int numberOfSubnets){
        var result =  converter.getSubnetsNets(address, mask, numberOfSubnets);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/subnets-host")
    public ResponseEntity<List<Subnet>> getSubnetsHosts(@RequestParam String address, @RequestParam String mask, @RequestParam int numberOfHosts){
        var result =  converter.getSubnetsHosts(address, mask, numberOfHosts);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/subnets-optimal")
    public ResponseEntity<List<Subnet>> getSubnetsOptimal(@RequestParam String address, @RequestParam String mask, @RequestParam List<Integer> listOfHosts){
        var result =  converter.getOptimalSubnets(address, mask, listOfHosts);
        return ResponseEntity.ok(result);
    }
}
