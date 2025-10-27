public class room {

    private int HOTEL_ID;
    private int ID;
    private String name;
    private int floor;
    private int beds;
    private int bathrooms;
    private int bedrooms;
    private String type;
    private double price;
    private String description;
    private String status;




    public room(){

    }

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











    public void setHOTEL_ID(int HOTEL_ID) {
        this.HOTEL_ID = HOTEL_ID;
    }
    public int getHOTEL_ID() {
        return HOTEL_ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public int getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getBeds() {
        return beds;
    }
    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }
    public int getBathrooms() {
        return bathrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public double getPrice() {
        return this.price;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}

