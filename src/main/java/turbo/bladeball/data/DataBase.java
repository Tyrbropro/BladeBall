package turbo.bladeball.data;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import turbo.bladeball.currency.kill.repository.KillRepositoryImpl;
import turbo.bladeball.currency.lose.repository.LoseRepositoryImpl;
import turbo.bladeball.currency.money.repository.MoneyRepositoryImpl;
import turbo.bladeball.currency.win.repository.WinRepositoryImpl;

import java.util.UUID;

public class DataBase {

    static final String MONGO_URI = "mongodb://127.0.0.1:27017";
    static final String DATABASE_NAME = "BladeBall";
    static final String COLLECTION_NAME = "Players";

    public static void saveToMongoDB(PlayerData playerData) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document doc = new Document("uuid", playerData.getUuid().toString())
                    .append("money", playerData.getMoneyRepository().getMoney())
                    .append("kills", playerData.getKillRepository().getKill())
                    .append("wins", playerData.getWinRepository().getWin())
                    .append("losses", playerData.getLoseRepository().getLose());

            Document query = new Document("uuid", playerData.getUuid().toString());
            collection.replaceOne(query, doc, new ReplaceOptions().upsert(true));
        }
    }

    public static PlayerData loadFromMongoDB(UUID uuid) {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document query = new Document("uuid", uuid.toString());
            Document result = collection.find(query).first();

            if (result != null) {
                MoneyRepositoryImpl moneyRepo = new MoneyRepositoryImpl();
                KillRepositoryImpl killRepo = new KillRepositoryImpl();
                WinRepositoryImpl winRepo = new WinRepositoryImpl();
                LoseRepositoryImpl loseRepo = new LoseRepositoryImpl();

                moneyRepo.setMoney(result.getInteger("money"));
                killRepo.setKill(result.getInteger("kills"));
                winRepo.setWin(result.getInteger("wins"));
                loseRepo.setLose(result.getInteger("losses"));

                return new PlayerData(uuid, moneyRepo, killRepo, winRepo, loseRepo);
            } else {
                return new PlayerData(uuid);
            }
        }
    }
}
