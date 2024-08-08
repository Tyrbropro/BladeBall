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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class DataBase {

    static String MONGO_URI;
    static String DATABASE_NAME;
    static String COLLECTION_NAME;

    public static void loadDatabaseConfig() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader("database_config.txt"))) {
            MONGO_URI = reader.readLine();
            DATABASE_NAME = reader.readLine();
            COLLECTION_NAME = reader.readLine();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load database config", e);
        }
    }

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
