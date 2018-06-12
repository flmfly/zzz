package zzz.repository;

import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

@Repository
public class MongoRepository {

	public MongoDatabase getMongoDatabase() {
		if (null == mongoClient)
			// 连接到 mongodb 服务
			mongoClient = new MongoClient("localhost", 27017);

		// 连接到数据库
		return mongoClient.getDatabase("mydb");
	}

	private MongoClient mongoClient;

}
