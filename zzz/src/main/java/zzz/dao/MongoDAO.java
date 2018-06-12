package zzz.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import zzz.repository.MongoRepository;

@Component
public class MongoDAO {

	private static final String _ID = "_id";
	@Autowired
	private MongoRepository mongoRepository;

	public Document getReference(String collectionName, Document target) {
		Document ref = new Document();
		ref.put("$db", this.mongoRepository.getMongoDatabase().getName());
		ref.put("$ref", collectionName);
		ref.put("$id", target.getObjectId(_ID));
		return ref;
	}

	public MongoCollection<Document> getCollection(String collectionName) {
		return this.mongoRepository.getMongoDatabase().getCollection(collectionName);
	}

	public void addToArray(String collectionName, Bson filter, Bson target) {
		this.mongoRepository.getMongoDatabase().getCollection(collectionName).updateOne(filter,
				new Document("$push", target));
	}

	public String insert(String collectionName, String json) {
		Document document = Document.parse(json);
		this.mongoRepository.getMongoDatabase().getCollection(collectionName).insertOne(document);
		return document.toJson();
	}

	public void update(String collectionName, Bson filter, Document target) {
		Document update = new Document();
		update.put("$set", target);
		this.mongoRepository.getMongoDatabase().getCollection(collectionName).updateOne(filter, update);
	}

	public void delete(String collectionName, String id) {
		this.mongoRepository.getMongoDatabase().getCollection(collectionName)
				.deleteOne(Filters.eq(_ID, new ObjectId(id)));
	}

	public String get(String collectionName, String id) {
		Document document = this.mongoRepository.getMongoDatabase().getCollection(collectionName)
				.find(Filters.eq(_ID, new ObjectId(id))).first();
		return null == document ? "{}" : document.toJson();
	}

	public List<String> select(String collectionName, String filter, int pageSize, int pageNumber) {
		MongoCollection<Document> collection = this.mongoRepository.getMongoDatabase().getCollection(collectionName);

		List<String> list = new ArrayList<>();
		if (collection == null) {
			return list;
		}
		FindIterable<Document> findIterable = collection.find(Document.parse(filter));

		if (pageSize >= 0) {
			if (pageNumber >= 1) {
				findIterable = findIterable.skip((pageNumber - 1) * pageSize);
			}
			findIterable = findIterable.limit(pageSize);
		}
		Iterator<Document> iterator = findIterable.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next().toJson());
		}
		return list;
	}

}
