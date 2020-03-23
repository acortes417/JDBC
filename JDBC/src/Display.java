/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import java.util.ArrayList;
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
                    chooseWritingGroups();
                    break;
                case 3:
                    printPublishers();
                    break;
                case 4:
                    choosePublisher();
                    break;
                case 5:
                    printBooks();
                    break;
                case 6:
                    chooseBooks();
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
    void chooseWritingGroups()
    {
        displayFormat = "%-20s%-20s%-20s\n";
       System.out.print("Enter the Writing Group Name you wish to find: ");
       String name = scan.nextLine();
       
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
    void choosePublisher()
    {
       System.out.print("Enter the Publisher Name you wish to find: ");
       displayFormat = "%-20s%-45s%-20s%-15s\n";
       String name = scan.nextLine();
       
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
    void chooseBooks()
    {
        System.out.print("Enter the Book Title: ");
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
                             dispNull(writer), dispNull(gName),dispNull(year),dispNull(pages));
                }while(rs.next());
            }
            else
                System.out.println("Sorry, there are no Books with the name: " + bookName);
        }
        catch(SQLException err){
            System.out.println(err.getMessage());
        }
    }
    void insertBook()
    {        
        String stmt1 = "INSERT INTO Books(GroupName,BookTitle,PublisherName,YearPublished,NumberPages) VALUES(?, ?, ?, ?, ?)";
        String stmt3 = "SELECT PublisherName FROM Publishers";
        String sql = "SELECT GroupName FROM WritingGroups";
        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<String>();
        try{
            ResultSet rs = stmt.executeQuery(sql);
            int count = 1;
            if(rs.next()){
                System.out.println("Choose the Writing Group of your book:");
                
                do{
                    String names = rs.getString("GroupName");
                    list1.add(names);
                    System.out.println(count + ". " + names);
                    count++;
                }while(rs.next());
                int choice = getIntRange(1,count) - 1;
                
                rs = stmt.executeQuery(stmt3);
                count = 1;
                System.out.println("Choose the Publisher of your book:");
                if(rs.next()){
                    do{
                        String names = rs.getString("PublisherName");
                        list2.add(names);
                        System.out.println(count + ". " +names);
                        count++;
                    }while(rs.next());
                }else 
                    System.out.println("No poublishers in database");
                int choice2 = getIntRange(1,count) - 1;
                
                System.out.print("Next the Book Title: ");
                String bookTitle = scan.nextLine();
                System.out.print("Next the year it was published(DD/MM/YYY): ");
                String yearPublished = scan.nextLine();
                System.out.print("Finally, the number of pages: ");
                int numberPages = getIntRange(1,100000);

                
                PreparedStatement pstmt = conn.prepareStatement(stmt1);

                pstmt.setString(1, list1.get(choice));
                pstmt.setString(2, bookTitle);
                pstmt.setString(3, list2.get(choice2));
                pstmt.setString(4, yearPublished);
                pstmt.setInt(5, numberPages);
                int change = pstmt.executeUpdate();
                if(change > 0)
                    System.out.println("New Book Added");
                else
                    System.out.println("Sorry, couldnt add the book " + bookTitle);
            }
            else
                System.out.println("\nSorry, there are no Writing Groups in your database\n");
        } 
        catch (SQLException e) 
        {
            if(e.getMessage().contains("FK_1"))
            {
                System.out.println("Sorry, We need an existing writing group");
            }
            else if(e.getMessage().contains("FK_2"))
            {
                System.out.println("Sorry, We need an existing Publisher or insert a new one");
            }
            else if(e.getMessage().contains("date"))
                System.out.println("Please enter the date int the correct format (DD/MM/YYYY)");
            else if(e.getMessage().contains("duplicate"))
                System.out.println("Sorry, that book already exists");
            else
                System.out.println(e.getMessage());
        }
    }
    void insertPublisher()
    {
        System.out.println("Which publisher do you want to replace: ");
        
        
        String stmt1 = "INSERT INTO Publishers (PublisherName,PublisherAddress,PublisherPhone,PublisherEmail)values(?,?,?,?)";
        String stmt2 =  "UPDATE Books SET PublisherName = ? WHERE PublisherName = ?";
        String stmt3 = "SELECT PublisherName FROM publishers";
        ArrayList<String> list = new ArrayList<String>();
  	try{
            int count = 1;
            ResultSet rs = stmt.executeQuery(stmt3);
            if(rs.next()){
                do{
                    String names = rs.getString("PublisherName");
                    list.add(names);
                    System.out.println(count + ". " +names);
                    count++;
                }while(rs.next());
            }else 
                System.out.println("No poublishers in database");
            int choice = getIntRange(1,count) - 1;
            
            System.out.print("Enter the name of the new Publisher: ");
            String publisher = scan.nextLine();
            System.out.print("Enter Address: ");
            String address = scan.nextLine();
            System.out.print("Enter phone number: ");
            String number = scan.nextLine();
            System.out.print("Enter E-mail: ");
            String email = scan.nextLine();
            
            PreparedStatement pstmt = conn.prepareStatement(stmt1);

            pstmt.setString(1, publisher);
            pstmt.setString(2, address);
            pstmt.setString(3, number);
            pstmt.setString(4, email);
            count = pstmt.executeUpdate();
            if(count > 0){
                System.out.println("\nSuccessfully inserted publisher");
                
                PreparedStatement pstmt2 = conn.prepareStatement(stmt2);
                pstmt2.setString(1,publisher);
                pstmt2.setString(2,list.get(choice));
                count = pstmt2.executeUpdate();
                if(count > 0)
                    System.out.println("Successfully updated Books");
                else
                    System.out.println("\nSorry, we couldn't update books");
            }
            else
                System.out.println("\nSorry, we couldn't insert");
            
            
  	} 
        catch (SQLException e) 
        {
            if(e.getMessage().contains("duplicate"))
                System.out.println("Sorry, that book already exists");
  	}
    }
    void removeBook()
    {        
        String stmt1 = "DELETE FROM Books WHERE LOWER(BookTitle) LIKE LOWER(?) AND (LOWER(PublisherName) LIKE LOWER(?) OR LOWER(GroupName) LIKE LOWER(?))";
        String stmt2 = "SELECT PublisherName FROM Publishers";
        String stmt3 = "SELECT GroupName FROM WritingGroups";
        String stmt4 = "SELECT BookTitle FROM Books";
        
        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<String>();
        ArrayList<String> list3 = new ArrayList<String>();
        try{
            ResultSet rs = stmt.executeQuery(stmt4);
            int count = 1;
            if(rs.next()){
                System.out.println("Choose the Book you want to delete:");
                
                do{
                    String names = rs.getString("BookTitle");
                    list1.add(names);
                    System.out.println(count + ". " + names);
                    count++;
                }while(rs.next());
                int choice = getIntRange(1,count) - 1;
                
                rs = stmt.executeQuery(stmt2);
                count = 1;
                System.out.println("Choose the Publisher of your book:");
                if(rs.next()){
                    do{
                        String names = rs.getString("PublisherName");
                        list2.add(names);
                        System.out.println(count + ". " +names);
                        count++;
                    }while(rs.next());
                }else 
                    System.out.println("No poublishers in database");
                int choice2 = getIntRange(1,count) - 1;
                
                rs = stmt.executeQuery(stmt3);
                count = 1;
                System.out.println("Choose the Writing Group:");
                if(rs.next()){
                    do{
                        String names = rs.getString("GroupName");
                        list3.add(names);
                        System.out.println(count + ". " +names);
                        count++;
                    }while(rs.next());
                }else 
                    System.out.println("No Writing Group in database");
                int choice3 = getIntRange(1,count) - 1;
                PreparedStatement pstmt = conn.prepareStatement(stmt1);
            
            
            pstmt.setString(1, list1.get(choice));
            pstmt.setString(2, list2.get(choice2));
            pstmt.setString(3, list3.get(choice3));
            int num = pstmt.executeUpdate();
            if(num > 0)
                System.out.println("Successfully removed " + list1.get(choice));
            else
                System.out.println("Sorry, we couldn't find a book matching those perameters");
                
            }else
                System.out.println("Sorry, no Books in your datbase");
            
  	} 
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
  	}
    }
}
