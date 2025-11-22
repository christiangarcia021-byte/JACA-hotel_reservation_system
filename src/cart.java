import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * The cart class maintains the users selected hotel rooms as cartItems
 * Provides a list of items that can automatically updates the Tableview when changes happen in the cart
 * Makes sure the same room isn’t added more than once
 * Lets user add, remove, clear rooms, check if the cart is empty, and view the total price
 * @author Angel Cruz
 * @version 1.0     Date: 11/11/2025
 */
public class cart {
    /**
     * Holds all the items in the cart and automatically updates the display whenever something changes by the ObservableList
     * */
    private ObservableList<cartItem> items = FXCollections.observableArrayList();
    /**
     * Gets the observable list of cart items
     */
    public ObservableList<cartItem> getItems() { return items; }
    /**
     * Adds a room from a hotel if the same room id is not already in the cart, preventing duplicate items
     * @param r the room to add
     * @param h the hotel that owns the room
     * @return true if a new item was added; otherwise return false if inputs were null or a duplicate room id
     */
    public void add(room r, hotel h)
    {
        if (r == null || h == null) return;
        boolean exists = items.stream().anyMatch(ci -> ci.selectedRoom.getID() == r.getID());
        if (!exists) items.add(new cartItem(r, h));
    }
    /**
     * Removes a certain cart item in the cart
     * @param ci the cartItem to remove
     */
    public void remove(cartItem ci) {items.remove(ci);}
    /**
     * Clears all items in the cart
     */
    public void clear() { items.clear(); }
    /**
     * Checks if the cart currently is empty
     */
    public boolean isEmpty() { return items.isEmpty();}
    /**
     * Calculates the total price of all items currently in the cart
     */
    public double total() { return items.stream().mapToDouble(cartItem::price).sum(); }
}

