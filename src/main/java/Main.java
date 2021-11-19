import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.management.Query;
import java.util.Arrays;
import java.util.Iterator;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class Main {
    public static void main(String[] args) {
        String wrongSource="";

        User user1 = new User(1, "Nikos");
        Picture pic1 = new Picture(1, "c://helloworld");
        user1.insertPicture(pic1);

        insertIntoDb(user1);

        User user2=new User(2,"Dimitris");
        Picture pic2= new Picture(2,"c://picture2source");
        user2.insertPicture(pic1);
        user2.insertPicture(pic2);

        insertIntoDb(user2);

        //update wrong source
        wrongSource= pic1.getSource();
        pic1.setSource("c://rightSource");
        updatePic(user2,wrongSource,pic1);

        //delete pic 2 from user2
        deletePic(user2,pic2);

    }

    public static void insertIntoDb(User user) {

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("UserInformation");
        MongoCollection<Document> collection = database.getCollection("user");

        Document picturesDoc = new Document().append("name", user.getName());
        picturesDoc.append("pictures", Arrays.asList());
        try {
            collection.insertOne(picturesDoc);
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
        for (int i=0;i<user.pictures.size();i++) {
            Bson filter = eq("name",user.getName() );
            Bson update = Updates.addToSet("pictures", new Document()
                    .append("source",user.pictures.get(i).getSource())
                    .append("id",i));
            UpdateOptions options = new UpdateOptions().upsert(true);
            collection.updateOne(filter, update, options);
            System.out.println("\nInsertion completed\n");
            printTable(user);

        }



    }
    public static void updatePic(User user , String wrongSource,Picture picture){

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("UserInformation");
        MongoCollection<Document> collection = database.getCollection("user");

        Bson filter = eq("name", user.getName());
        String set ="pictures."+String.valueOf(user.pictures.indexOf(picture))+".source";
        Bson update = set(set, picture.getSource());
        collection.updateOne(filter, update);
        System.out.println("\nUpdate completed\n");
        printTable(user);

    }
    public static void deletePic(User user,Picture picture){
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("UserInformation");
        MongoCollection<Document> collection = database.getCollection("user");

        Bson query = new Document().append("name", user.getName());
        //String set ="pictures."+String.valueOf(user.pictures.indexOf(picture));
        Bson fields = new Document().append("pictures", new Document().append( "source", picture.getSource()));
        Bson update = new Document("$pull",fields);

        collection.updateOne( query, update );
        System.out.println("\nDeletion completed\n");
        printTable(user);

    }

    public static void printTable(User user){
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("UserInformation");
        MongoCollection<Document> collection = database.getCollection("user");
        FindIterable<Document> iterDoc = collection.find();
        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

    }
}
