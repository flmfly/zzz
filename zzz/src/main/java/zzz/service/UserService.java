package zzz.service;

import java.util.Date;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.model.Filters;

import zzz.repository.MongoRepository;

@Service
public class UserService {

	@Autowired
	private MongoRepository mongoRepository;

	public String createUser(String json) {
		return null;
	}

	public String deleteUser(String id) {
		return null;
	}

	public void updateUserById(String id, String json) {

	}

	public void findUserById(String id) {

	}

	public void createIfNotExist(String id) {
		Document user = this.mongoRepository.getMongoDatabase().getCollection(COLLECTION_NAME)
				.find(Filters.eq("account", id)).first();
		if (null == user) {
			user = new Document();
			user.put("account", id);
			user.put("firstLogin", new Date());
			this.mongoRepository.getMongoDatabase().getCollection(COLLECTION_NAME).insertOne(user);
		}
	}

	private static final String COLLECTION_NAME = "user";
}
