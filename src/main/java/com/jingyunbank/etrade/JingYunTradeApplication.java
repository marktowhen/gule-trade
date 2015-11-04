package com.jingyunbank.etrade;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Stack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JingYunTradeApplication 
{
    public static void main( String[] args ) throws IOException
    {
    	SpringApplication.run(JingYunTradeApplication.class, args);
    	//deletefiles();
//    	HashSet<Long> sets = new HashSet<Long>(); 
//    	long s = System.currentTimeMillis();
//    	for (int i = 0; i < 20000000; i++) {
//        	//sets.add(System.nanoTime());
//        	UUID uid = UUID.randomUUID();
//        	long f = uid.getLeastSignificantBits();
//        	long l = uid.getMostSignificantBits();
//        	long sd = f + l;
//        	System.out.println(sd>0?sd:-sd);
//        	sets.add(sd>0?sd:-sd);
//		}
//    	s = System.currentTimeMillis() - s;
//        System.out.println(s);
//        System.out.println(sets.size());
    }
    public static long gen(){
    	long l = System.currentTimeMillis();
    	long t = System.nanoTime();
    	String ll = String.valueOf(l);
    	String lt = String.valueOf(t);
    	StringBuilder b = new StringBuilder(ll);
    	b.append(lt.substring(10));
    	return Long.parseLong(b.toString());
    }
    public static void deletefiles() throws IOException {
		Path p = Paths.get("D:/Projects/angular-yeo");
    	Stack<File> stack = new Stack<File>();
    	File f = p.toFile();
    	addtostack(f, stack);
    	delete(stack);
	}

	private static void delete(Stack<File> stack) throws IOException {
		for (int i = 0; i < stack.size(); i++) {
			File file = stack.pop();
    		System.out.println(file.toString());
			Files.delete(file.toPath());
			System.out.println(file.exists());
		}
	}
    
    private static void addtostack (File f, Stack<File> stack){
    	stack.push(f);
    	File[] cfs = f.listFiles();
    	if(cfs == null) return;
    	for (int i = 0; i < cfs.length; i++) {
    		addtostack(cfs[i], stack);
		}
    }
}
