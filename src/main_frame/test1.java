package main_frame;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main_frame.demo;

class test1 {

	@Test
	void test() throws IOException {
		String testword="test how to use ";
		JFrame newframe = new JFrame("Text Editor");
		JTextArea textArea = new JTextArea();
	    newframe.add(textArea);
	    textArea.setText(testword);
		File file = new File("./test.txt");
		file.createNewFile();
		demo.saveFunction(file, textArea);
	    String pathname = "test.txt";
	    String reads="";
	    try (FileReader reader = new FileReader(pathname);
	         BufferedReader br = new BufferedReader(reader)
	        ) 
	    {
	            String line;
	            while ((line = br.readLine()) != null) {
	                reads=reads+line;
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    assertEquals(testword,reads);
	}
	@Test
	void testopen() throws IOException {
		File file = new File("./testopen.txt");
		file.createNewFile();
		Writer out =new FileWriter(file);//write some words
		String data="Open test";
		out.write(data);
		out.close();
		
		JFrame newframe = new JFrame("Text Editor");
		JTextArea textArea = new JTextArea(); 
	    newframe.add(textArea);
	    demo.open(file,textArea);
	    assertEquals(textArea.getText(),data+"\r\n");
	}
	
	@Test
	void testSearch() throws IOException {
		JFrame newframe = new JFrame("Text Editor");
		JTextArea textArea = new JTextArea(); 
	    newframe.add(textArea);
	    textArea.setText("which word to search");
	    demo.searchFunction(textArea,"word",false,false,true);
	    assertTrue(textArea.getCaretPosition()==10);
	}
}
