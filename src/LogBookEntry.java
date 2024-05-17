import java.util.Date;

public class LogBookEntry {

    private Date dateLogged;
    private int roomNum;
    private int computerNum;
    private String fault;
    private String status;
    private String name;

    public LogBookEntry(Date dateLogged, int roomNum, int computerNum, String fault, String status, String name) {
        this.dateLogged = dateLogged;
        this.roomNum = roomNum;
        this.computerNum = computerNum;
        this.fault = fault;
        this.status = status;
        this.name = name;
    }

    public Date getDateLogged() {
        return dateLogged;
    }

    public void setDateLogged(Date dateLogged) {
        this.dateLogged = dateLogged;
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
