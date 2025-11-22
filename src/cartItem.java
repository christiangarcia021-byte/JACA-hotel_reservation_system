/**
 * The cartItem class represents an item in a cart
 * A cartitem is connected to a specific room with the hotel it belongs too
 * Displays the items hotel name, room id, and its price
 * @author Angel Cruz
 * @version 1.0     Date: 11/11/2025
 */
public class cartItem {
    /**
     * The room selected for the cart item
     */
    public final room selectedRoom;
    /**
     * The hotel that owns the selected room
     */
    public final hotel selectedHotel;
    /**
     * Creates a new cartItem that links a room with its hotel
     * @param selectedRoom the room being added to the cart
     * @param selectedHotel the hotel that belongs to the room
     */
    public cartItem(room selectedRoom, hotel selectedHotel) {
        this.selectedRoom = selectedRoom;
        this.selectedHotel = selectedHotel;
    }
    /**
     * Builds simple display name
     * @return a string
     */
    public String displayName() {
        return selectedRoom.getName() + " " + selectedHotel.getName();
    }
    /**
     * Retrieves the price for the cart item from the selected room
     * @return the rooms price as a double
     */
    public double price() {
        return selectedRoom.getPrice();
    }
}
