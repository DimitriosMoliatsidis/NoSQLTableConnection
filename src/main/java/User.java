import org.bson.types.ObjectId;

import java.util.ArrayList;

public class User {
    private Object id;
    private String name;
    private ArrayList<Picture> pictures= new ArrayList<>();

    public User(String name) {

        this.name = name;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
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
