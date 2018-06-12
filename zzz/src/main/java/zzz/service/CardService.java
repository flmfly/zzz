package zzz.service;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.client.model.Filters;

import zzz.dao.MongoDAO;

public class CardService {
	@Autowired
	private MongoDAO mongoDAO;

	@Autowired
	private UserService userService;

	public String createCard(String userId, String json) {
		Document card = Document.parse(json);
		this.mongoDAO.getCollection(COLLECTION_NAME).insertOne(card);
		this.userService.addCard(userId, this.mongoDAO.getReference(COLLECTION_NAME, card));
		return null;
	}

	public void deleteCard(String cardId) {
		Document update = new Document();
		update.put("trash", true);
		this.mongoDAO.update(COLLECTION_NAME, getIdFilter(cardId), update);
	}
	
	public void removeCard() {
		
	}

	public void updateCardById(String cardId, String json) {
		this.mongoDAO.update(COLLECTION_NAME, getIdFilter(cardId), Document.parse(json));
	}

	public String findCardById(String cardId) {
		return this.mongoDAO.get(COLLECTION_NAME, cardId);
	}

	private Bson getIdFilter(String id) {
		return Filters.eq("_id", new ObjectId(id));
	}

	private static final String COLLECTION_NAME = "card";
}
