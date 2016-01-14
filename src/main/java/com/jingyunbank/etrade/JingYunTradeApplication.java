package com.jingyunbank.etrade;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JingYunTradeApplication 
{
    public static void main( String[] args ) throws IOException
    {
    	SpringApplication.run(JingYunTradeApplication.class, args);
    }
}
