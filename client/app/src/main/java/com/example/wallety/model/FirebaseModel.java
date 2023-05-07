package com.example.wallety.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class FirebaseModel {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    FirebaseModel() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private Task<QuerySnapshot> getUserByEmail(String email) {
        return db.collection(User.COLLECTION)
                .whereEqualTo(User.EMAIL, email)
                .get();
    }

    public void fetchLoggedInUser(Model.Listener<User> callback) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String id = currentUser.getUid();
            db.collection(User.COLLECTION).document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            User user = null;
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                Map<String, Object> data = document.getData();
                                if (data != null) {
                                    data.put(User.EMAIL, currentUser.getEmail());
                                    data.put(User.PASSWORD, "");
                                    user = User.fromJson(data);
                                }
                            }
                            callback.onComplete(user);
                        }
                    });
        } else {
            callback.onComplete(null);
        }
    }

    public void handleUserCreation(User user, Model.Listener<Void> listener, Model.Listener<String> onFailure) {
        String email = user.getEmail();
        String phone = user.getPhone();

        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener((OnCompleteListener<SignInMethodQueryResult>) task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().getSignInMethods().isEmpty()) {
                            onFailure.onComplete("email");
                        } else {
                            db.collection(User.COLLECTION)
                                    .whereEqualTo(User.PHONE, phone)
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (!task.getResult().isEmpty()) {
                                                    onFailure.onComplete("phone");
                                                } else {
                                                    createUser(user, listener);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void createUser(User user, Model.Listener<Void> listener) {
        String email = user.getEmail();
        String password = user.getPassword();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = auth.getUid();
                            user.setId(id);
                            db.collection(User.COLLECTION).document(id).set(user.toJson())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d("TAG", "createUserWithEmail:success");
                                            listener.onComplete(null);
                                        }
                                    });
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    public void updateUser(User user, Model.Listener<Void> listener) {
        db.collection(User.COLLECTION).document(user.getId()).set(user.toJson())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete(null);
                    }
                });
    }

    public void loginUser(String email, String password, Model.Listener<Void> success, Model.Listener<Void> error) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.collection(User.COLLECTION)
                                    .document(auth.getUid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            User user = null;
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                Map<String, Object> data = document.getData();
                                                if (data != null) {
                                                    data.put(User.EMAIL, email);
                                                    data.put(User.PASSWORD, password);
                                                    user = User.fromJson(data);
                                                }
                                                Model.instance().setCurrentUser(user);
                                            }
                                            success.onComplete(null);
                                        }
                                    });
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            error.onComplete(null);
                        }
                    }
                });
    }

    public void getUsers(Model.Listener<HashMap<String, User>> callback) {
        db.collection(User.COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        HashMap<String, User> usersByIds = new HashMap<>();
                        if (task.isSuccessful()) {
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot json : jsonsList) {
                                User user = User.fromJson(json.getData());
                                usersByIds.put(user.getId(), user);
                            }
                            Log.d("TAG", "getUsers:success");
                        }
                        callback.onComplete(usersByIds);
                    }
                });
    }
}
