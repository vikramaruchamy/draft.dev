package handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.gson.Gson;

public class FireStoreHandler {

	private static Firestore getFireStoreObject() throws IOException {
		ClassLoader classLoader = FireStoreHandler.class.getClassLoader();
		
		InputStream inputStream = classLoader.getResourceAsStream("firestorecredentials.json");

		FirestoreOptions options = FirestoreOptions.newBuilder()
				.setCredentials(GoogleCredentials.fromStream(inputStream)).build();

		Firestore firestore = options.getService();
		
		return firestore;
	}

	public static void createDocument(String collectionName, String documentId, Object data) throws Exception {
		
		Firestore firestore = getFireStoreObject();
		
		ApiFuture<WriteResult> future = firestore.collection(collectionName).document(documentId).set(data);
		
		System.out.println("Document created: " + future.get().getUpdateTime());
		firestore.close();
	}

	public static void updateDocument(String collectionName, String documentId, Object data) throws Exception {
		
		Firestore firestore = getFireStoreObject();
		
		DocumentReference documentRef = firestore.collection(collectionName).document(documentId);
		
		ApiFuture<WriteResult> future = documentRef.set(data, SetOptions.merge());
		
		System.out.println("Document updated: " + future.get().getUpdateTime());
		firestore.close();
	}

	public static String getDocument(String collectionName, String documentId) throws Exception {
		
		Firestore firestore = getFireStoreObject();
		
		DocumentReference documentRef = firestore.collection(collectionName).document(documentId);
		
		ApiFuture<DocumentSnapshot> future = documentRef.get();
		
		DocumentSnapshot document = future.get();

		if (document.exists()) {
	        Map<String, Object> documentData = document.getData();
	        Gson gson = new Gson();
	        firestore.close();
	        return gson.toJson(documentData);
	    } else {
	        System.out.println("Document not found.");
	        firestore.close();
	        return null;
	    }
	}
	
	public static Map<String, Map<String, Object>> getAllDocuments(String collectionName) throws InterruptedException, ExecutionException,Exception {
	    Firestore firestore = getFireStoreObject();
	    CollectionReference collectionRef = firestore.collection(collectionName);
	    ApiFuture<QuerySnapshot> future = collectionRef.get();
	    QuerySnapshot querySnapshot = future.get();
	    
	    Map<String, Map<String, Object>> jsonDocuments = new HashMap<>();
	    //Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    
	    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
	        if (document.exists()) {
	        	
	            Map<String, Object> documentData = document.getData();
	            
	            jsonDocuments.put(document.getId(),documentData);
	        } else {
	            System.out.println("Document not found.");
	        }
	    }
	    
	    firestore.close();
	    return jsonDocuments;
	}
	
	public static void deleteDocument(String collectionName, String documentId) throws Exception {
		
		Firestore firestore = getFireStoreObject();
		
		DocumentReference documentRef = firestore.collection(collectionName).document(documentId);
		
		ApiFuture<WriteResult> future = documentRef.delete();
		
		System.out.println("Document deleted: " + future.get().getUpdateTime());
		firestore.close();
	}

}
