
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.lang.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;  
import javax.swing.JOptionPane;

public class FreeBird 
{
  public static void main(String[] args) throws Exception 
  {
	 JOptionPane.showMessageDialog(null, "Started Running..", "FreeBird", JOptionPane.INFORMATION_MESSAGE);
	    
    FileSystem fileSystem = FileSystems.getDefault();
    WatchService watchService = fileSystem.newWatchService();
    Path directory = Paths.get("C:\\FB");
    WatchEvent.Kind<?>[] events = { StandardWatchEventKinds.ENTRY_CREATE};//        StandardWatchEventKinds.ENTRY_DELETE,        StandardWatchEventKinds.ENTRY_MODIFY };
    directory.register(watchService, events);
    while (true) 
    {
      System.out.println("Waiting for a watch event");
      WatchKey watchKey = watchService.take();
      System.out.println("Path being watched: " + watchKey.watchable());
      if (watchKey.isValid() == false) 
      {
        return;
      }
      for (WatchEvent<?> event : watchKey.pollEvents()) 
      {
        System.out.println("Kind: " + event.kind());
        System.out.println("Context: " + event.context());
        String filename = event.context().toString();
        if( filename.contains(".xml"))
        {
        	XMLLoader(filename);
        }
        if( filename.contains("67109395_60"))
        {
        	CSVLoader(filename);
        }
        
        System.out.println("Count: " + event.count());
        System.out.println();
      }
      boolean valid = watchKey.reset();
      System.out.println(valid);

    }
  }
  
  
  public static void XMLLoader(String filename1)
  {

		String line= "";
		String line1= "";
		
		boolean a,b;
		String lineSep = System.getProperty("line.separator");
		
		try
	    {      
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila","root","root");        
		
	        	try (BufferedReader br = new BufferedReader(new FileReader("C:\\FB\\"+filename1)))
	        	{

					String sqlinsert = "INSERT INTO NEIGHBOR_DEFINED (SOURCERNCID,SOURCECELLID,TARGETRNCID,TARGETCELLID,INSERTDATE)VALUES (?,?,?,?,CURDATE())";
	    			PreparedStatement pstmt=con.prepareStatement(sqlinsert);
					
	    			System.out.println("Started...");
	        		
	        		while ((line = br.readLine()) != null ) 
          		{
	        			
	        			a = line.indexOf("MO className") >0;
          			b = line.indexOf("INTRAFREQNCELL=") > 0;
          		    if (a&&b)
          			{
          		    	
          		    	 //String str = "abcd=0; efgh=1";
          		    	 line = line.replaceAll("[^a-zA-Z0-9]", ",");
          		    	 //String replacedStr2 = line.replaceAll("">", "_");
          		    	 String[] lineVar1 = line.split(","); 
          		    	 //wr.write(line);
          		    	 //wr.write(lineSep);
          				//wr.write(lineVar1[19]+","+lineVar1[20]+","+lineVar1[21]+","+lineVar1[22]);
  						//wr.write(lineSep);
          		    	 
          		    	 
          		    	pstmt.setString(1, lineVar1[22]);
               			pstmt.setString(2, lineVar1[19]);
               			pstmt.setString(3, lineVar1[21]);
               			pstmt.setString(4, lineVar1[20]);
               			//String temp = "CURDATE()";
               			//pstmt.setString(5, temp );
               			pstmt.executeUpdate();
  						
          			}
          			
          		}
	        	System.out.println("Inserted to Neighbor_defined...");
	        	
	        	}
	        	catch (IOException e) 
				{
				    System.err.println(e);
				}
	        //	wr.close();
	        	con.close();  
	     }
		 catch (Exception e) 
		 {
			    System.err.println(e);
		}
	


	  
	  
	  
	  
  }
  
  public static void CSVLoader(String filename2)
  {
	  String line = "";
		//String line1 = "";
		
		try
		{  
			Class.forName("com.mysql.jdbc.Driver");  
		  	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila","root","root");  
		 	
		 	try (BufferedReader br = new BufferedReader(new FileReader("C:\\FB\\"+filename2)))
      	{
		 		System.out.println("CSV file loader started......");
		 		br.readLine();
		 		br.readLine();
		 		String sqlinsert = "INSERT INTO 67109395_60 (DATETIME,GRANULARITYPERIOD,SOURCERNCNAME,SOURCERNCID,SOURCECELLNAME,SOURCECELLID,TARGETRNCID,TARGETCELLID,COUNTER_67183491,COUNTER_67183492,COUNTER_67183493,COUNTER_67183494,COUNTER_67189909,COUNTER_67189910,COUNTER_67189911 )VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  			PreparedStatement pstmt=con.prepareStatement(sqlinsert);
      		while ((line = br.readLine()) != null ) 
      		{
      			String[] lineVar1 = line.split(",");
      			lineVar1[0]=lineVar1[0].replace("'", "");
      			lineVar1[0]=lineVar1[0]+":00";
      			pstmt.setString(1, lineVar1[0]);
      			pstmt.setInt(2, Integer.parseInt(lineVar1[1]));
      			String[] lineVar100 =lineVar1[2].split("_");
      			lineVar100[0] = lineVar100[0].replaceAll("[^a-zA-Z0-9]+","");
      			pstmt.setString(3, lineVar100[0]);
      			if(lineVar100[0].equals("KKRBL02"))
      			{
      				pstmt.setInt(4, 403);
      			}
      			String[] lineVar101 =lineVar1[2].split("=");
      			pstmt.setString(5, lineVar101[1]);
      			String[] lineVar102 =lineVar1[3].split("/");
      			String[] lineVar1020=lineVar102[0].split("=");
      			pstmt.setInt(6, Integer.parseInt(lineVar1020[1]));
      			String[] lineVar103 =lineVar1[3].split(":");
      			String[] lineVar104 =lineVar103[1].split("/");
      			pstmt.setInt(7, Integer.parseInt(lineVar104[0]));
      			String[] lineVar105 =lineVar1[3].split(":");
      			lineVar105[2]= lineVar105[2].replaceAll("[^0-9]", "");
      			pstmt.setInt(8, Integer.parseInt(lineVar105[2])); 
      			pstmt.setString(9, lineVar1[5]);
      			pstmt.setString(10, lineVar1[6]);
      			pstmt.setString(11, lineVar1[7]);
      			pstmt.setString(12, lineVar1[8]);
      			pstmt.setString(13, lineVar1[9]);
      			pstmt.setString(14, lineVar1[10]);
      			pstmt.setString(15, lineVar1[11]);
      			
      			pstmt.executeUpdate();
      		}
      		System.out.println("CSV finished inserting to database");
      		//ResultSet rs=pstmt.executeQuery("select * from complete_database");  
    		   	//while(rs.next())  
  		 		//System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+"  "+rs.getString(4)+"  "+rs.getString(5));  
  		
      	}
		 	catch (IOException e) 
			{
			    System.err.println(e);
			}
		 	con.close();  
		  
		}
		catch(Exception e)
		{ System.out.println(e);
		}  
		  
		 
		  


  }
  
}


