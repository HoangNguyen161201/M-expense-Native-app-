package greenwich.edu.vn.ExpenseManageApp.expense;

public class Expense {
    private String type, date, time, additionalComments, address;
    private int tripId, amount, id;

    public Expense(int id, String type, String date, String time, String additionalComments, int tripId, int amount, String address) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.time = time;
        this.additionalComments = additionalComments;
        this.tripId = tripId;
        this.amount = amount;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Expense "+ additionalComments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
