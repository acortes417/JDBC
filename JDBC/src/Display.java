/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class Display {
    Scanner scan = new Scanner(System.in);
    Statement stmt = null;
    Connection conn = null;
    String displayFormat = "%-15s%-15s%-15s\n";
    
    Display(Statement state, Connection con)
    {
        stmt = state;
        conn = con;
        mainDis();
    }
    void mainDis()
    {
        int choice = 0;
        while (choice != 10){
            System.out.println("*******JDBC Project*******");
            System.out.println("1. Print All Writing Groups");
            System.out.println("2. Choose Writing Group data");
            System.out.println("3. Print All Publishers");
            System.out.println("4. Choose Publisher data");
            System.out.println("5. Print All Books");
            System.out.println("6. Choose Book data");
            System.out.println("7. Insert new Book");
            System.out.println("8. Insert new Publisher");
            System.out.println("9. Remove Book");
            System.out.println("10. end");

            choice = getIntRange(1,10);
            switch (choice) {
                case 1:
                    printWritingsGroups();
                    break;
                case 2:
                    choice = chooseWritingGroups();
                    break;
                case 3:
                    printPublishers();
                    break;
                case 4:
                    choice = choosePublisher();
                    break;
                case 5:
                    printBooks();
                    break;
                case 6:
                    choice = chooseBooks();
                    break;
                case 7:
                    insertBook();
                    break;
                case 8:
                    insertPublisher();
                    break;
                case 9:
                    removeBook();
                    break;
                default:
                    break;
            }        
            System.out.println("\n");
        }
    }
    void printWritingsGroups()
    {
        String sql = "SELECT GroupName FROM WritingGroups";
        try{
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                System.out.println("Writing Group Names: ");
                
                do{
                    String names = rs.getString("GroupName");
                    System.out.println(names);
                }while(rs.next());
            }
            else
                System.out.println("\nSorry, there are no Writing Groups in your database\n");
        }
        catch(SQLException err){
            System.out.println(err.getMessage());
        }
        System.out.println("\n");
        
    }
    int chooseWritingGroups()
    {
       System.out.print("Enter the Writing Group Name you wish to find: ");
       String name = "";
       scan = scan.useDelimiter("\n");
       name = scan.next();
       
       PreparedStatement getGroup = null;
       String sql = "SELECT * FROM WritingGroups WHERE LOWER(GroupName) LIKE LOWER(?)";
        try{
            getGroup = conn.prepareStatement(sql);
            getGroup.setString(1, name);
            ResultSet rs = getGroup.executeQuery();
            if(rs.next()){
                System.out.printf(displayFormat, "Group Name", "Head Writer", "Year Formed", "Subject");
                
                do{
                    String gName = rs.getString("GroupName");
                    String writer = rs.getString("HeadWriter");
                    String year = rs.getString("YearFormed");
                    String subject = rs.getString("Subject");

                    //Display values
                    System.out.printf(displayFormat, 
                            dispNull(gName), dispNull(writer), dispNull(year), dispNull(subject));
                }while(rs.next());
            }
            else
                System.out.println("Sorry, there are no Writing Groups in your database with the name: " + name);
        }
        catch(SQLException err){
            System.out.println(err.getMessage());
        }
        System.out.println("\n\n1. Back to main menu");
        System.out.println("2. Try again");
        System.out.println("3. Exit");
        
        int choice = getIntRange(1,3);
        
        System.out.println("\n");
        
        if(choice == 2)
            chooseWritingGroups();
        else if(choice == 3)
            return 10;
        else if(choice == 1)
            return 0;
        return 10;
   
    }
    String dispNull (String input) 
    {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    //checks if values are between low and high
    int getIntRange( int low, int high ) {
        Scanner in = new Scanner( System.in );
        int input = 0;
        boolean valid = false;
        while( !valid ) {
                if( in.hasNextInt() ) {
                        input = in.nextInt();
                        if( input <= high && input >= low ) {
                                valid = true;
                        } else {
                                System.out.println( "Invalid Range." );
                        }
                } else {
                        in.next(); //clear invalid string
                        System.out.println( "Invalid Input." );
                }
        }
        return input;
    }
    void printPublishers()
    {
        String sql = "SELECT PublisherName FROM Publishers";
        try{
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                System.out.println("Publisher Names: ");
                
                do{
                    String names = rs.getString("PublisherName");
                    System.out.println(names);
                }while(rs.next());
            }
            else
                System.out.println("Sorry, there are no Publishers in your database");
        }
        catch(SQLException err){
            System.out.println(err.getMessage());
        }
        System.out.println("\n");
    }
    int choosePublisher()
    {
       System.out.print("Enter the Publisher Name you wish to find: ");
       
       scan = scan.useDelimiter("\n");
       String name = scan.next();
       
       PreparedStatement getPub = null;
       String sql = "SELECT  * FROM Publishers WHERE LOWER(PublisherName) LIKE LOWER(?)";
        try{
            getPub = conn.prepareStatement(sql);
            getPub.setString(1, name);
            ResultSet rs = getPub.executeQuery();
            if(rs.next()){
                System.out.printf(displayFormat, "Publisher Name", "Publisher Address", "Publisher Phone", "Publisher Email");
                
                do{
                    String gName = rs.getString("PublisherName");
                    String writer = rs.getString("PublisherAddress");
                    String year = rs.getString("PublisherPhone");
                    String subject = rs.getString("PublisherEmail");

                    //Display values
                    System.out.printf(displayFormat, 
                            dispNull(gName), dispNull(writer), dispNull(year), dispNull(subject));
                }while(rs.next());
            }
            else
                System.out.println("Sorry, there are no Publishers with the name: " + name);
        }
        catch(SQLException err){
            System.out.println(err.getMessage());
        }
        System.out.println("\n\n1. Back to main menu");
        System.out.println("2. Try again");
        System.out.println("3. Exit");
        
        int choice = getIntRange(1,3);
        
        System.out.println("\n");
        if(choice == 3)
            return 10;
        else if(choice == 2)
            choosePublisher();
        else if(choice == 1)
            return 0;
        return 10;
    }
    void printBooks()
    {
        String sql = "SELECT BookTitle FROM Books";
        try{
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                System.out.println("Book Names: ");
                do{
                    String names = rs.getString("BookTitle");
                    System.out.println(names);
                }
                while(rs.next());
                
            }
            else
                System.out.println("Sorry, there are no Books in your database");
        }
        catch(SQLException err){
            System.out.println(err.getMessage());
        }
        System.out.println("\n");
    }
    int chooseBooks()
    {
        System.out.print("Enter the Book Title: ");
        scan = scan.useDelimiter("\n");
        String bookName = scan.nextLine();

       PreparedStatement getBook = null;
       displayFormat = "%-20s%-20s%-20s%-20s\n";
       String sql = "SELECT BookTitle,PublisherName, GroupName, YearPublished,NumberPages FROM Books Where LOWER(booktitle) LIKE LOWER(?)";
        try{
            getBook = conn.prepareStatement(sql);
            getBook.setString(1, bookName);
            ResultSet rs = getBook.executeQuery();
            if(rs.next()){
                System.out.printf(displayFormat, "Book Title", "Publisher Name","Writin Group","Number Of Pages");
                
                do{
                    String gName = rs.getString("PublisherName");
                    String writer = rs.getString("BookTitle");
                    String year = rs.getString("GroupName");
                    String pages = rs.getString("NumberPages");
                    //Display values
                    System.out.printf(displayFormat, 
                            dispNull(gName), dispNull(writer), dispNull(year),dispNull(pages));
                }while(rs.next());
            }
            else
                System.out.println("Sorry, there are no Books with the name: " + bookName);
        }
        catch(SQLException err){
            System.out.println(err.getMessage());
        }
        System.out.println("\n\n1. Back to main menu");
        System.out.println("2. Try again");
        System.out.println("3. Exit");
        
        int choice = getIntRange(1,3);
        
        System.out.println("\n");
        if(choice == 3)
            return 10;
        else if(choice == 2)
            chooseBooks();
        else if(choice == 1)
            return 0;
        
        return 10;
    }
    void insertBook()
    {
        
    }
    void insertPublisher()
    {
        
    }
    void removeBook()
    {
        
    }
}
