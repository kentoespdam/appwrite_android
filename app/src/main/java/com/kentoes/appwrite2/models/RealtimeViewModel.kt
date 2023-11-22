package com.kentoes.appwrite2.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kentoes.appwrite2.datas.Product
import io.appwrite.Client
import io.appwrite.extensions.toJson
import io.appwrite.models.RealtimeResponseEvent
import io.appwrite.models.RealtimeSubscription
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Realtime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Objects

class RealtimeViewModel : ViewModel(), LifecycleObserver {
    private var client: Client? = null

    private val endPoint = "https://cloud.appwrite.io/v1"
    private val projectId = "655c5b876acadbecfa58"
    private val databaseId = "655c5bd088f24a128330"
    private val collectionId = "655c5bf2b2e5c0131d3c"

    private val realtime by lazy { Realtime(client!!) }
    private val account by lazy { Account(client!!) }
    private val db by lazy { Databases(client!!) }

    private val _productStream = MutableLiveData<Product>()
    val productStream: LiveData<Product> = _productStream

    private val _productDeleted = MutableLiveData<Product>()
    val productDeleted: LiveData<Product> = _productDeleted

    private var subscription: RealtimeSubscription? = null

    fun getProducts(context: Context) {
        buildClient(context)

        viewModelScope.launch {
            createSession()
//            realtime.subscribe("databases.$databaseId.collections.$collectionId") {
//                Log.e("events", it.events.toString())
//                Log.e("payload: ", it.payload.toString())
//            }
            subscription?.close()
            subscription = realtime.subscribe(
                "documents",
                payloadType = Product::class.java,
                callback = ::handleProductMessage
            )

//            val products = db.listDocuments(
//                databaseId = databaseId,
//                collectionId = collectionId
//            )
//            for (productData in products.documents) {
//                Log.d(
//                    "product", Product(
//                        productData.data["name"].toString(),
//                        productData.data["sku"].toString(),
//                        productData.data["description"].toString(),
//                        productData.data["price"].toString().toDouble(),
//                        productData.data["imageUrl"].toString()
//                    ).toString()
//                )
//                val prod = Product(
//                    productData.data["name"].toString(),
//                    productData.data["sku"].toString(),
//                    productData.data["description"].toString(),
//                    productData.data["price"].toString().toDouble(),
//                    productData.data["imageUrl"].toString()
//                )
//                _productStream.postValue(prod)
//            }
//            _productStream.postValue(products.documents.first())
        }
    }

    fun subscribeToProducts(context: Context) {
        buildClient(context)

        viewModelScope.launch {
            createSession()
            subscription = realtime.subscribe(
                "documents",
                payloadType = Product::class.java,
                callback = ::handleProductMessage
            )

            createDummyProducts()
        }
    }

    private suspend fun createSession() {
        if (Objects.isNull(account.getSession(sessionId = "current"))) {
            try {
                account.createAnonymousSession()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun buildClient(context: Context) {
        if (client == null) {
            client = Client(context)
                .setEndpoint(endPoint)
                .setProject(projectId)
                .setSelfSigned(status = true)
        }
    }

    private suspend fun createDummyProducts() {
        try {
            // For testing; insert 100 products while subscribed
            val url = "https://dummyimage.com/600x400/cde/fff"
            for (i in 1 until 100) {
                db.createDocument(
                    databaseId = databaseId,
                    collectionId = collectionId,
                    documentId = "sku-$i",
                    data = Product(
                        "iPhone $i",
                        "sku-$i",
                        "description of product $i",
                        i.toDouble(),
                        url
                    ).toJson()
                )
                delay(1000)
            }
        } catch (e: Exception) {
            Log.e("AppWrite", "Error: " + e.message)
        }
    }


    private fun handleProductMessage(message: RealtimeResponseEvent<Product>) {
        if (
            message.events.contains("databases.$databaseId.collections.$collectionId.documents.*.create") ||
            message.events.contains("databases.$databaseId.collections.$collectionId.documents.*.update")
        ) {
            Log.i("create/updated:", message.payload.sku)
            _productStream.postValue(message.payload!!)
        }
        if (message.events.contains("databases.$databaseId.collections.$collectionId.documents.*.delete")) {
            Log.i("event delete: ", message.payload.sku)
            _productDeleted.postValue(message.payload!!)
        }
    }

    fun unsubscribe() {
        subscription?.close()
    }

}