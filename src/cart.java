import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//keeps an observable list so TableViews update automatically
// allows you to add, remove, and clear rooms
//checks if the cart is empty and calculates the total price


public class cart {
    private ObservableList<cartItem> items = FXCollections.observableArrayList(); // updates the UI when rooms are changed

    public ObservableList<cartItem> getItems() { return items; }

    public void add(room r, hotel h) // adds a room from a certain hotel to the cart, also prevents duplicates from room ID
    {
        if (r == null || h == null) return;
        boolean exists = items.stream().anyMatch(ci -> ci.selectedRoom.getID() == r.getID());
        if (!exists) items.add(new cartItem(r, h));
    }

    public void remove(cartItem ci) {items.remove(ci);}  //removes one row

    public void clear() { items.clear(); }

    public boolean isEmpty() { return items.isEmpty();} // empty check


    public double total() { return items.stream().mapToDouble(cartItem::price).sum(); } //sum of the total
}

