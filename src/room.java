

/**
The room class represents a hotel room with various attributes such as ID, name, floor, beds, bathrooms, bedrooms, type, price, description, and status.
It provides methods to set and get these attributes, as well as a method to print all the attributes of a room

 @author Andy Hernandez
 @version 1.0       Date: 10/22/2025
*/

public class room {


    /**
    Attributes of the room class
     */


    /**
     HOTEL_ID - an int representing the ID of the hotel the room belongs to. Refers to a hotel record in the databases HOTEL table.
    */
    private int HOTEL_ID;
    /**
        ID - an int representing the unique identifier for the room. Refers to a room record in the database's HOTEL_ROOMS table.
     */
    private int ID;
    /**
        NAME - a String representing the name of the room. Should refer to a physical room name/#
     */
    private String name;
    /**
     Floor - an int representing the floor number where the room is located.
     */
    private int floor;
    /**
        Beds - an int representing the number of beds in the room.
     */
    private int beds;
    /**
        Bathrooms - an int representing the number of bathrooms in the room.
     */
    private int bathrooms;
    /**
        Bedrooms - an int representing the number of bedrooms in the room.
     */
    private int bedrooms;
    /**
       Type - a String representing the type of room (e.g., single, double, suite, etc).
     */
    private String type;
    /**
     *   Price - a double representing the price per night for the room.
     */
    private double price;
    /**
         Description - a String providing a description of the room.
     */
    private String description;
    /**
        Status - a String representing the current status of the room (e.g., available, occupied, under maintenance, etc).
     */
    private String status;



    /**
     Constructs a new room object with default values for all attributes (null or 0)
     */
    public room(){

    }

    /**
        Prints the room information to the console. Prints all attributes of the room
     */
    public void printRoomInfo(){
        System.out.println("Room ID: " + ID);
        System.out.println("Room Name: " + name);
        System.out.println("Floor: " + floor);
        System.out.println("Beds: " + beds);
        System.out.println("Bathrooms: " + bathrooms);
        System.out.println("Bedrooms: " + bedrooms);
        System.out.println("Type: " + type);
        System.out.println("Price: " + price);
        System.out.println("Description: " + description);
        System.out.println("Status: " + status);
    }









    /**
        sets the HOTEL_ID attribute of the room
        @param HOTEL_ID - an int representing the hotel ID to set
     */
    public void setHOTEL_ID(int HOTEL_ID) {
        this.HOTEL_ID = HOTEL_ID;
    }
    /**
     *   gets the HOTEL_ID attribute of the room
        @return an int representing the hotel ID
     */
    public int getHOTEL_ID() {
        return HOTEL_ID;
    }
    /**
     *   sets the ID attribute of the room
        @param ID - an int representing the room ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }
    /**
     *  gets the ID attribute of the room
        @return an int representing the room ID
     */
    public int getID() {
        return ID;
    }
    /**
     *  sets the name attribute of the room
        @param name - a String representing the room name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     *  gets the name attribute of the room
        @return a String representing the room name
     */
    public String getName() {
        return name;
    }
    /**
     *  sets the floor attribute of the room
        @param floor - an int representing the floor number to set
     */
    public void setFloor(int floor) {
        this.floor = floor;
    }
    /**
     *  gets the floor attribute of the room
        @return an int representing the floor number
     */
    public int getFloor() {
        return floor;
    }
    /**
     *  sets the beds attribute of the room
        @param beds - an int representing the number of beds to set
     */
    public void setBeds(int beds) {
        this.beds = beds;
    }
    /**
     * gets the beds attribute of the room
        @return an int representing the number of beds
     */
    public int getBeds() {
        return beds;
    }
    /**
     * sets the bathrooms attribute of the room
        @param bathrooms - an int representing the number of bathrooms to set
     */
    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }
    /**
     * gets the bathrooms attribute of the room
        @return an int representing the number of bathrooms
     */
    public int getBathrooms() {
        return bathrooms;
    }
    /**
     * sets the bedrooms attribute of the room
        @param bedrooms - an int representing the number of bedrooms to set
     */
    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }
    /**
     * gets the bedrooms attribute of the room
        @return an int representing the number of bedrooms
     */
    public int getBedrooms() {
        return bedrooms;
    }
    /**
     * sets the description attribute of the room
        @param description - a String representing the room description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * gets the description attribute of the room
        @return a String representing the room description
     */
    public String getDescription() {
        return description;
    }
    /**
     * sets the type attribute of the room
        @param type - a String representing the room type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * gets the type attribute of the room
        @return a String representing the room type
     */
    public String getType() {
        return type;
    }
    /**
     * sets the price attribute of the room
        @param price - a double representing the room price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
    /**
     * gets the price attribute of the room
        @return a double representing the room price
     */
    public double getPrice() {
        return this.price;
    }
    /**
     * sets the status attribute of the room
        @param status - a String representing the room status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * gets the status attribute of the room
        @return a String representing the room status
     */
    public String getStatus() {
        return status;
    }




}


