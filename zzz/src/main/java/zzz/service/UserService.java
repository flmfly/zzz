package zzz.service;

import java.util.Date;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.model.Filters;

import zzz.dao.MongoDAO;

@Service
public class UserService {

	@Autowired
	private MongoDAO mongoDAO;

	public void trashCard(String userId, String cardId) {
		this.mongoDAO.update(COLLECTION_NAME,
				Filters.and(getUserIdFilter(userId),
						Filters.elemMatch("cards", Filters.eq("card._id", new ObjectId(cardId)))),
				new Document("cards.$.trash", true));
	}

	public void deleteCard(String userId, String cardId) {
		this.mongoDAO.update(COLLECTION_NAME,
				Filters.and(getUserIdFilter(userId),
						Filters.elemMatch("cards", Filters.eq("card._id", new ObjectId(cardId)))),
				new Document("cards.$.deleted", true));
	}

	public String createUser(String json) {
		return null;
	}

	public String deleteUser(String userId) {
		return null;
	}

	public void updateUserById(String userId, String json) {

	}

	public void findUserById(String userId) {

	}

	void addCard(String userId, Document cardReference) {
		Document element = new Document();
		element.put("card", cardReference);
		this.mongoDAO.addToArray(COLLECTION_NAME, getUserIdFilter(userId), new Document("cards", element));
	}

	public void createIfNotExist(String userId) {
		Document user = this.mongoDAO.getCollection(COLLECTION_NAME).find(getUserIdFilter(userId)).first();
		if (null == user) {
			user = new Document();
			user.put("account", userId);
			user.put("firstLogin", new Date());
			this.mongoDAO.getCollection(COLLECTION_NAME).insertOne(user);
		}
	}

	private Bson getUserIdFilter(String userId) {
		return Filters.eq("account", userId);
	}

	private static final String COLLECTION_NAME = "user";
}
