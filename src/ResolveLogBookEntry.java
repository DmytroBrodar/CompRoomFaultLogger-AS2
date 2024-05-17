import java.util.Date;

public class ResolveLogBookEntry {

    private Date dateFixed;
    private int roomNum;
    private int computerNum;
    private String fault;
    private String status;
    private String name;

    public ResolveLogBookEntry(Date dateFixed, int roomNum, int computerNum, String fault, String status, String name) {
        this.dateFixed = dateFixed;
        this.roomNum = roomNum;
        this.computerNum = computerNum;
        this.fault = fault;
        this.status = status;
        this.name = name;
    }

    public Date getDateFixed() {
        return dateFixed;
    }

    public void setDateFixed(Date dateFixed) {
        this.dateFixed = dateFixed;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public int getComputerNum() {
        return computerNum;
    }

    public void setComputerNum(int computerNum) {
        this.computerNum = computerNum;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
