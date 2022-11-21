package greenwich.edu.vn.ExpenseManageApp.trip;

public class Trip {
    private String name, description, destination, date, picture;
    private int id, isRisk, memberCount, predictedAmount;

    public Trip(String name, String description, String destination, String date, int id, int isRisk, String picture, int memberCount, int predictedAmount) {
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.date = date;
        this.id = id;
        this.isRisk = isRisk;
        this.picture = picture;
        this.memberCount = memberCount;
        this.predictedAmount = predictedAmount;
    }

    @Override
    public String toString() {
        return "Trip " + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsRisk() {
        return isRisk;
    }

    public void setIsRisk(int isRisk) {
        this.isRisk = isRisk;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getPredictedAmount() {
        return predictedAmount;
    }

    public void setPredictedAmount(int predictedAmount) {
        this.predictedAmount = predictedAmount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
