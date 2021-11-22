import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private ArrayList<Picture> pictures= new ArrayList<>();

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Picture> getPictures() {
        return pictures;
    }

    public void insertPicture(Picture picture){
        this.pictures.add(picture);
    }

}
